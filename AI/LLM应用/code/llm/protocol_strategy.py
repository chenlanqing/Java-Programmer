from abc import ABC, abstractmethod
from typing import Any
import json

class ProtocolStrategy(ABC):
    """协议适配层抽象接口"""

    @abstractmethod
    def build_request(self, messages: list[dict], tools: list[dict] = None,
                      system: str = None, **kwargs) -> dict:
        """将统一格式转换为目标协议格式"""
        pass

    @abstractmethod
    def parse_response(self, response: dict) -> dict:
        """将目标协议响应解析为统一格式"""
        pass

    @abstractmethod
    def parse_tool_call(self, response: dict) -> list[dict]:
        """提取工具调用信息"""
        pass

    @abstractmethod
    def parse_stream_chunk(self, chunk: str) -> dict:
        """解析流式响应块"""
        pass


class OpenAIProtocol(ProtocolStrategy):
    """OpenAI Chat Completions 协议"""

    def build_request(self, messages, tools=None, system=None, **kwargs):
        msgs = messages.copy()
        if system:
            msgs = [{"role": "system", "content": system}] + msgs
        body = {"model": kwargs.get("model", "gpt-4o"), "messages": msgs}
        if tools:
            body["tools"] = [{"type": "function", "function": t} for t in tools]
        return body

    def parse_response(self, response):
        return {
            "content": response["choices"][0]["message"]["content"],
            "role": response["choices"][0]["message"]["role"],
            "finish_reason": response["choices"][0]["finish_reason"]
        }

    def parse_tool_call(self, response):
        msg = response["choices"][0]["message"]
        if not msg.get("tool_calls"):
            return []
        return [
            {"id": tc["id"], "name": tc["function"]["name"],
             "args": json.loads(tc["function"]["arguments"])}
            for tc in msg["tool_calls"]
        ]

    def parse_stream_chunk(self, chunk):
        data = json.loads(chunk.removeprefix("data: ").strip())
        delta = data["choices"][0].get("delta", {})
        return {"content": delta.get("content", ""),
                "tool_call": delta.get("tool_calls", None)}


class AnthropicProtocol(ProtocolStrategy):
    """Anthropic Messages API 协议"""

    def build_request(self, messages, tools=None, system=None, **kwargs):
        body = {
            "model": kwargs.get("model", "claude-sonnet-4-20250514"),
            "messages": messages,
            "max_tokens": kwargs.get("max_tokens", 4096)
        }
        if system:
            body["system"] = system
        if tools:
            body["tools"] = [
                {"name": t["name"], "description": t["description"],
                 "input_schema": t["parameters"]} for t in tools
            ]
        return body

    def parse_response(self, response):
        content_blocks = response.get("content", [])
        text = "".join(b.get("text", "") for b in content_blocks if b["type"] == "text")
        return {"content": text, "role": "assistant",
                "finish_reason": response.get("stop_reason", "")}

    def parse_tool_call(self, response):
        return [
            {"id": b["id"], "name": b["name"],
             "args": b.get("input", {})}
            for b in response.get("content", []) if b["type"] == "tool_use"
        ]

    def parse_stream_chunk(self, chunk):
        # Anthropic SSE: event: content_block_delta
        if chunk.startswith("data: "):
            data = json.loads(chunk[6:])
            if data.get("type") == "content_block_delta":
                delta = data.get("delta", {})
                return {"content": delta.get("text", ""),
                        "tool_call": None}
        return {"content": "", "tool_call": None}


class ResponsesProtocol(ProtocolStrategy):
    """OpenAI Responses API (2025+) 协议"""

    def build_request(self, messages, tools=None, system=None, **kwargs):
        # Responses API 使用 input 字段
        body = {
            "model": kwargs.get("model", "gpt-4.1"),
            "input": messages,  # 直接使用 messages 数组
        }
        if system:
            body["instructions"] = system
        if tools:
            body["tools"] = tools  # 支持内置工具类型
        return body

    def parse_response(self, response):
        return {
            "content": response.get("output_text", ""),
            "role": "assistant",
            "finish_reason": response.get("status", "completed")
        }

    def parse_tool_call(self, response):
        calls = []
        for item in response.get("output", []):
            if item.get("type") == "function_call":
                calls.append({
                    "id": item.get("call_id"),
                    "name": item.get("name"),
                    "args": json.loads(item.get("arguments", "{}"))
                })
        return calls

    def parse_stream_chunk(self, chunk):
        data = json.loads(chunk.removeprefix("data: ").strip())
        etype = data.get("type", "")
        if etype == "response.output_text.delta":
            return {"content": data.get("delta", ""), "tool_call": None}
        return {"content": "", "tool_call": None}


class OllamaProtocol(ProtocolStrategy):
    """Ollama 本地模型协议"""

    def build_request(self, messages, tools=None, system=None, **kwargs):
        body = {
            "model": kwargs.get("model", "llama3.2"),
            "messages": messages,
            "stream": kwargs.get("stream", False),
            "format": "json" if kwargs.get("json_mode") else None
        }
        if system and not any(m["role"] == "system" for m in messages):
            body["messages"] = [{"role": "system", "content": system}] + messages
        return body

    def parse_response(self, response):
        return {
            "content": response.get("message", {}).get("content", ""),
            "role": "assistant",
            "finish_reason": "stop"
        }

    def parse_tool_call(self, response):
        # Ollama 通过 OpenAI 兼容模式支持工具调用
        return []

    def parse_stream_chunk(self, chunk):
        data = json.loads(chunk)
        return {"content": data.get("message", {}).get("content", ""),
                "tool_call": None}


# === 协议注册中心 ===
PROTOCOLS = {
    "openai": OpenAIProtocol(),
    "anthropic": AnthropicProtocol(),
    "responses": ResponsesProtocol(),
    "ollama": OllamaProtocol(),
}

def get_protocol(name: str) -> ProtocolStrategy:
    """根据协议名获取适配器，切换模型零成本"""
    if name not in PROTOCOLS:
        raise ValueError(f"Unknown protocol: {name}. Supported: {list(PROTOCOLS.keys())}")
    return PROTOCOLS[name]

# === 使用示例：一行切换模型 ===
protocol = get_protocol("anthropic")  # 切到 Claude
request = protocol.build_request(
    messages=[{"role": "user", "content": "你好"}],
    system="你是一个助手",
    model="claude-sonnet-4-20250514"
)
# 切到 OpenAI 只需改一行
protocol = get_protocol("openai")
request = protocol.build_request(
    messages=[{"role": "user", "content": "你好"}],
    system="你是一个助手",
    model="gpt-4.1"
)