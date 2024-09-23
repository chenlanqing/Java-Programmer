# 1、声调

声调 | 格式 | 效果
----|------|-----
一声|	`&emacr;`	| &emacr;
二声|`&eacute`;|	&eacute;
三声|	`&ecaron`;| &ecaron;
四声|	`&egrave;`| &egrave;
u音|	`&euml;`	| &euml;

# 2、文本居中

- `<p align="center"></p>`，github会生效
- `<center></center>`

# 3、Markdown生成PDF

使用Visual Studio Code将Markdown 生成PDF

## 3.1、工具

需要相关的Visual Studio Code插件：
- Markdown All in One : 支持预览等
- Markdown Preview Enhanced : 增强预览，支持生成PDF、Html等
- markdownlint : markdown语法及规范检查
- [prince](https://www.princexml.com/): 生成带目录的PDF

## 3.2、具体步骤

（1）Markdown Preview Enhanced 设置字体、大小等

打开Visual Studio Code，输入Ctrl+Shift+P（cmd+shift+p）打开命令面板，然后运行 Markdown Preview Enhanced: Customize Css，输入如下css代码，相关配置参考：[Markdown Preview Enhanced](https://shd101wyy.github.io/markdown-preview-enhanced/#/zh-cn/)
```less
/*   https://shd101wyy.github.io/markdown-preview-enhanced/#/customize-css */
.markdown-preview.markdown-preview {
  font-family: Microsoft YaHei;
  h1{
    text-align: center
  }
  table {
    max-width: 100%; /*处理prince表格被截断的问题*/
    td {
      word-wrap: break-word;
      word-break: break-all;
    }
  }
  /* prince配置 */
  &.prince {
    @page {
      @bottom-right {
        font-family: Microsoft YaHei;
        font-size: 12pt;
        content: counter(page) " of " counter(pages)
      }
      @bottom-left {
        font-family: Microsoft YaHei;
        font-size: 12pt;
        content: "©版权所有"
      }
    }
  }
}
```
（2）配置Prince，需要将Prince添加到环境变量中，再path路径后面处理，不同系统参考：[Installation Guide](https://www.princexml.com/doc/installing/)

（3）编写到Markdown文件后，右键选择：`MPE:打开侧边预览`；

（4）在侧边预览中，右键选择：Export -> PDF(Prince)，生成PDF后自动打开；

（5）如果需要生成目录
- 手工输入`[TOC]`
- Markdown Preview Enhanced生成：Markdown Preview Enhanced: Create Toc
- Markdown All in One生成

## 3.3、其他方法

- 使用[Pandoc](https://pandoc.org/)生成
- [Convert your markdown files to PDF with our online converter](https://www.markdowntopdf.com/)

# 参考资料

- [Cmd Markdown 公式指导手册](https://www.zybuluo.com/codeep/note/163962)
- [Markdown 公式写法](https://www.jianshu.com/p/e74eb43960a1)
- [Markdown-画图工具](https://mermaid-js.github.io/mermaid/#/)
- [Mermaid-流程图渲染工具](https://mermaid.js.org/)
- [使用Datexify来画出想要的符号](http://detexify.kirelabs.org/classify.html)
- [完整Latex符号列表](https://mirror.its.dal.ca/ctan/info/symbols/comprehensive/symbols-a4.pdf)
- [ASCII Doc](https://asciidoc.org/)
- [Slidev-Markdown2PPT](https://github.com/slidevjs/slidev)