# 1、概述

## 1.1、定义

DSL的全称是Domain Specific Language，即领域特定语言，一种为特定领域所设计的编程语言。可以简单的理解为可以通过设计DSL来设计一套语法，用来描述某些领域的一系列相关行为。

这种特定于某个领域是相对于通用语言而言的，通用语言可以横跨各个领域，我们熟悉的大多数程序设计语言都是通用语言；

比如正则表达式，就是一直DSL，一种用于文本处理这个特定领域的 DSL；配置文件是一种比正则表达式更简单的DSL，比如Nginx，无论你是用它单独做 Web 服务器也好，做反向代理也罢，抑或是做负载均衡，只要通过 Ngnix 的配置文件，你都能实现；

常见的DSL由两种：外部 DSL 和内部 DSL；外部 DSL 和内部 DSL 的区别就在于，DSL 采用的是不是宿主语言（Host Language）。可以这么去理解，假设模型主要是用 Java 写的，如果 DSL 用的就是 Java 语言，它就是内部 DSL；如果 DSL 用的不是 Java，比如，你自己设计了一种语法，那它就是外部 DSL

## 1.2、适用场景

简单来说当用户不是系统的开发者，但又需要定制逻辑的时候，就需要一门DSL。比如：风控规则引擎系统，需要业务运营定制一些列复杂的风控逻辑。再比如权益营销系统，需要运营指定一系列营销工具的用户参与逻辑、发奖逻辑等等。

## 1.3、如何设计DSL

通过编程原理，我们知道高级语言转机器指令需要经过一系列的过程，根据与机器的相关性大体分为前、后两端：
- 前端：与源语言有关而与机器无关，即仅与编译的源代码相关，将源代码转换为中间代码。包括：
    - 词法分析(Token)：将一些文本序列进行识别，识别出一个一个Token，并确认其词类型。
    - 语法分析(Parser)：将Token流进行分析组合形成语句，如果语法分析通过，就可以得到一颗以树状的形式表现编程语言语法结构的抽象语法树AST。
    - 语义分析：是对结构上正确的源程序进行上下文有关性质的审查，比如类型审查等。
- 后端：与机器有关，即将中间代码适用于（不同类型的）机器上。包括：
    - 字节码。
    - 目标代码

# 2、ANTLR：语法解析器

- [antlr-抢到语法解析器](https://www.antlr.org/)
- [ANTLR语法](https://github.com/antlr/grammars-v4)
- [ANTLR-QuickStart](https://www.chungkwong.cc/antlr.html)