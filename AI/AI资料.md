# 机器学习

Mahout:Apache Mahout 是 Apache Software Foundation（ASF ）旗下的一个开源项目，提供一些可扩展的机器学习领域经典算法的实现 ，旨在帮助开发人员更加方便快捷地创建智能应用程序

- [Kaggle-机器学习数据集](https://www.kaggle.com/)
- [深度学习-逐行解读代码](https://github.com/labmlai/annotated_deep_learning_paper_implementations)
- [machine learning](https://www.bbbdata.com/ml)

## 决策树

- [Decision tree-Wikipedia](https://en.wikipedia.org/wiki/Decision_tree)

# AI

- [AI笔记](http://www.huaxiaozhuan.com/)
- [Spring AI](https://spring.io/projects/spring-ai)
- [Spring AI Tutorial](https://howtodoinjava.com/series/spring-ai-tutorial/)
- [如果真的学习人工智能，应该做什么](https://mp.weixin.qq.com/s/wgHJnJzY7sjEFJcA9eCFfg)
- [AI Painting](https://github.com/hua1995116/awesome-ai-painting)
- [Deep learning AI](https://learn.deeplearning.ai/)

Google Gemini
- [笔记类AI应用-NotebookLM](https://notebooklm.google/)
- [Learn About 是 Google 基于 AI 研发的一个互动式学习网站](https://learning.google.com/experiments/learn-about?src=signup) 


# API Key

- [Free ChatGPT API Key，免费ChatGPT API](https://github.com/chatanywhere/GPT_API_free)
- [聚合优秀的AI大模型](https://www.apiyi.com/)
- https://bewildcard.com/
- [硅基流动](https://siliconflow.cn/)

# Deepseek

- [Deepseek-API集成工具](https://github.com/deepseek-ai/awesome-deepseek-integration)
- [如何训练Deepseek](https://juejin.cn/post/7473309339294695460)
- [微调Deepseek](https://juejin.cn/post/7473309339294695460)
 
 # AI 应用

- [AI构建各种工具](https://bolt.new/)
- [设计AI](https://readdy.ai/)
- [想法转变为现实](https://lovable.dev/?via=typescript)
- [The AI Toolkit for TypeScript](https://sdk.vercel.ai/)
- [AI工具集](https://ai-bot.cn/)
- [ideas转为实现的AI工具](https://replit.com/)
- [AI 大模型自动化流程](https://yuju-ai.com/)
- [具有本机AI功能的公平代码工作流程自动化平台](https://github.com/n8n-io/n8n)
- [AI聊天工具](https://poe.com/)
- [机器人应用](https://github.com/TyCoding/langchat)
- [AI开源工具](https://qyxznlkmwx.feishu.cn/wiki/BwWIwsCOuiMWGmkUzNHcKLvPnPh)
- [Chat V0](https://v0.dev/chat)
- [AI 源码解读工具](https://deepwiki.com/)
- [Exa-一个 API 用于搜索和抓取 Web，将其转换为应用的结构化数据](https://dashboard.exa.ai/playground)

# AI 工作平台

- [Langfuse 是一个开源LLM 工程平台，可帮助团队协作调试、分析和迭代LLM 应用程序](https://github.com/langfuse/langfuse)
- [anything-llm：这款全能的桌面和 Docker AI 应用程序内置了 RAG、AI 代理、无代码代理构建器、MCP 兼容性等功能](https://github.com/Mintplex-Labs/anything-llm)
- [Langflow 是用于构建和部署 AI 驱动的代理和工作流的强大工具](https://github.com/langflow-ai/langflow)

# AI 商业

- 可灵 AI：比如 AI 换衣
- 即梦 AI、火山引擎：[图片换装](https://www.volcengine.com/docs/85128/1462743)、[单图音频驱动](https://www.volcengine.com/docs/85128/1433887)、[图形融合](https://www.volcengine.com/docs/6791/1337909)

# AI 编辑器

- windsurf：https://windsurf.com/editor
- cursor
- tare
- [LMStudio](https://lmstudio.ai/)
- 美团 AI 编程: https://nocode.cn/#/

# RAGFlow

### Mac 部署的问题

#### icu依赖问题

报错信息
```bash
Package icu-i18n was not found in the pkg-config search path.
Perhaps you should add the directory containing `icu-i18n.pc'
```
解决方案：

**1. 安装依赖项**
- **(1) 安装 `pkg-config`**
`pyicu` 构建需要 `pkg-config` 工具来定位 ICU 库：
```bash
brew install pkg-config
```

- **(2) 安装 ICU 库**
`pyicu` 依赖于 ICU（International Components for Unicode）的 C++ 库：
```bash
brew install icu4c
# 从源码编译
brew install --build-from-source icu4c
```
添加到环境变量

If you need to have icu4c first in your PATH, run:
```bash
echo 'export PATH="/usr/local/opt/icu4c/bin:$PATH"' >> ~/.zshrc
echo 'export PATH="/usr/local/opt/icu4c/sbin:$PATH"' >> ~/.zshrc
```

For compilers to find icu4c you may need to set:
```bash
export LDFLAGS="-L/usr/local/opt/icu4c/lib"
export CPPFLAGS="-I/usr/local/opt/icu4c/include"
```
For pkg-config to find icu4c you may need to set:
```bash
export PKG_CONFIG_PATH="/usr/local/opt/icu4c/lib/pkgconfig"
```

#### torch 版本问题

```
ERROR: Could not find a version that satisfies the requirement torch<3.0.0,>=2.5.0 (from versions: 2.0.0, 2.0.1, 2.1.0, 2.1.1, 2.1.2, 2.2.0, 2.2.1, 2.2.2)
ERROR: No matching distribution found for torch<3.0.0,>=2.5.0
```
将 torch 的版本降低：
```
torch>=2.0.0,<3.0.0
```


# Java与AI

- LangChain4j
- Springai
- [Java+LangChain](https://mp.weixin.qq.com/s/n_89sQ_1XxnUvP4HKzgxhA)
- https://github.com/mymagicpower/AIAS
- https://gitee.com/dromara/wgai
- https://gitee.com/langchat/langchat
- [Java-AI入门资料](https://t.zsxq.com/vHopZ)
- [Spring AI Alibaba](https://java2ai.com/)


# AI与网关

## 面临的挑战

长连接、高延时、大带宽

**长链接**
- 大量使用 WebSocket和SSE等长连接协议
- 网关配置更新时需要保持连接稳定
- 必须确保业务连续性不受影响

**高延时**
- LLM推理响应时间远高于传统应用
- 容易受到慢速请求和并发攻击的影响
- 面临着攻击成本低但防御成本高的安全挑战

**大带宽**
- LLM上下文传输需要大量带宽
- 高延时场景下带宽消耗倍增
- 需要高效的流式处理能力
- 必须做好内存管理以防止系统崩溃

## [Higress](https://higress.cn/)

Higress 是一款云原生 API 网关，内核基于 Istio 和 Envoy

# AI与前端

- [OpenWebUI](https://github.com/open-webui/open-webui)
- LobeChat

# 参考资料

- [ChatGPT账号](https://chatgpt123.com/)
- [秘塔AI搜索](https://metaso.cn/)
