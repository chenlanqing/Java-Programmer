def priority_based_prune(messages, token_budget):
    """
    按优先级裁剪消息列表，使总 token 不超过预算。
    messages: [{role, content, type, priority, tokens}]
    token_budget: 目标 token 上限
    """
    total = sum(m["tokens"] for m in messages)
    if total <= token_budget:
        return messages  # 不需要裁剪

    # P4: 先丢弃最低优先级的消息
    for m in messages:
        if m["priority"] == 4:
            m["_action"] = "drop"
            total -= m["tokens"]
            if total <= token_budget:
                return finalize(messages)

    # P3: 摘要压缩推理过程
    for m in messages:
        if m["priority"] == 3 and m["_action"] != "drop":
            summary_tokens = summarize(m["content"])
            m["_action"] = "summarize"
            m["_new_tokens"] = summary_tokens
            total = total - m["tokens"] + summary_tokens
            if total <= token_budget:
                return finalize(messages)

    # P2: 提取工具返回值关键字段
    for m in messages:
        if m["priority"] == 2 and m["_action"] != "drop":
            extracted = extract_key_fields(m["content"])
            m["_action"] = "extract"
            m["_new_tokens"] = len(extracted) // 4
            total = total - m["tokens"] + m["_new_tokens"]
            if total <= token_budget:
                return finalize(messages)

    # P1: 压缩工具 Schema 描述（保留结构，精简 description）
    for m in messages:
        if m["priority"] == 1 and m["_action"] != "drop":
            compressed = compress_schema(m["content"])
            m["_action"] = "compress_schema"
            m["_new_tokens"] = len(compressed) // 4
            total = total - m["tokens"] + m["_new_tokens"]

    # P0 永不裁剪
    return finalize(messages)


def finalize(messages):
    result = []
    for m in messages:
        if m.get("_action") == "drop":
            continue
        elif m.get("_action") == "summarize":
            result.append({**m, "content": summarize(m["content"])})
        elif m.get("_action") == "extract":
            result.append({**m, "content": extract_key_fields(m["content"])})
        elif m.get("_action") == "compress_schema":
            result.append({**m, "content": compress_schema(m["content"])})
        else:
            result.append(m)
    return result