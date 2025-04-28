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

# Deepseek

- [Deepseek-API集成工具](https://github.com/deepseek-ai/awesome-deepseek-integration)
- [如何训练Deepseek](https://juejin.cn/post/7473309339294695460)
- [微调Deepseek](https://juejin.cn/post/7473309339294695460)
 
 # AI应用

- [AI构建各种工具](https://bolt.new/)
- [设计AI](https://readdy.ai/)
- [想法转变为现实](https://lovable.dev/?via=typescript)
- [AI-WebUI构建](https://github.com/open-webui/open-webui)
- [The AI Toolkit for TypeScript](https://sdk.vercel.ai/)
- [Langflow 是用于构建和部署 AI 驱动的代理和工作流的强大工具](https://github.com/langflow-ai/langflow)
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

# AI 编辑器

- windsurf：https://windsurf.com/editor
- cursor
- tare
- [LMStudio](https://lmstudio.ai/)

## RAGFlow

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

# 参考资料

- [ChatGPT账号](https://chatgpt123.com/)
- [秘塔AI搜索](https://metaso.cn/)
