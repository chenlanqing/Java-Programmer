
语言模型的本质是输入一些内容给神经网络模型，并不断预测出下一个词。而为了方便计算，这些内容会被转化为带有语义特征的向量（一组数值），而这个转化是以子词（Token）为单位进行

主要技术架构是基于 [transformer](https://en.wikipedia.org/wiki/Transformer_(deep_learning_architecture))

大模型推理过程：加载分词算法 -> 加载模型参数 -> 推理生成答案

# 参考资料

- [最全面的 LLM 架构技术解析](https://magazine.sebastianraschka.com/p/the-big-llm-architecture-comparison)
- [从零开始理解LLM内部原理](https://mp.weixin.qq.com/s/6zKOgq7oYmUVQcEshNFUAA)
- [从零开始理解LLM内部原理-英文版](https://towardsdatascience.com/understanding-llms-from-scratch-using-middle-school-math-e602d27ec876/)