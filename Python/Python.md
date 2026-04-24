# Python安装

## pyenv 版本管理

[pyenv](https://github.com/pyenv/pyenv) 可以轻松切换多个版本的 Python。它简单、不干扰，遵循 UNIX 的单一用途工具传统，专注于一件事

安装
```bash
brew install pyenv
```
配置（zsh）
```bash
echo 'export PYENV_ROOT="$HOME/.pyenv"' >> ~/.zshrc
echo 'export PATH="$PYENV_ROOT/bin:$PATH"' >> ~/.zshrc
echo 'eval "$(pyenv init -)"' >> ~/.zshrc
source ~/.zshrc
```
安装 Python
```bash
pyenv install 3.12
```
验证
```bash
# 仅在某个项目目录下使用 3.12（推荐）
cd your-project
pyenv local 3.12
# 或设置全局默认版本
pyenv global 3.12
# 验证
python --version
```

常见命令：

| 命令 | 说明 | 常用示例 |
|------|------|---------|
| `--version` | 显示 pyenv 自身的版本号 | `pyenv --version` |
| `commands` | 列出所有可用命令 | `pyenv commands` |
| `exec` | 用指定 Python 版本运行某个可执行文件 | `pyenv exec pip list` |
| `global` | 设置或查看全局默认 Python 版本 | `pyenv global 3.12` |
| `help` | 查看某个子命令的帮助 | `pyenv help install` |
| `hooks` | 列出某命令的钩子脚本（高级用法） | `pyenv hooks install` |
| `init` | 输出初始化 shell 环境所需的配置 | `eval "$(pyenv init -)"` |
| `install` | 安装指定版本的 Python | `pyenv install 3.12` |
| `latest` | 显示某前缀下最新的已安装或可用版本 | `pyenv latest 3.12` |
| `local` | 设置当前目录专属的 Python 版本（写入 `.python-version`） | `pyenv local 3.12` |
| `prefix` | 显示某版本的安装路径前缀 | `pyenv prefix 3.12` |
| `rehash` | 安装新包/可执行文件后刷新 shims | `pyenv rehash` |
| `root` | 显示 pyenv 的根目录 | `pyenv root` |
| `shell` | 设置仅在当前 shell 会话生效的版本 | `pyenv shell 3.12` |
| `shims` | 列出所有已存在的 shim 文件 | `pyenv shims` |
| `uninstall` | 卸载某个已安装的 Python 版本 | `pyenv uninstall 3.12.13` |
| `version` | 显示当前生效的 Python 版本及其来源 | `pyenv version` |
| `version-file` | 显示当前版本是由哪个 `.python-version` 文件决定的 | `pyenv version-file` |
| `version-name` | 只显示当前版本号（不含来源信息） | `pyenv version-name` |
| `version-origin` | 解释当前版本是如何被设置的 | `pyenv version-origin` |
| `versions` | 列出所有已安装的 Python 版本 | `pyenv versions` |
| `whence` | 查找哪些 Python 版本包含某个可执行文件 | `pyenv whence pip` |
| `which` | 显示某可执行文件的完整路径 | `pyenv which python` |

**版本优先级**：`shell` > `local` > `global`，pyenv 按此顺序决定当前使用哪个版本。

## Linux源码安装

- [Using Python on Unix platforms](https://docs.python.org/3/using/unix.html)

## Anaconda

Anaconda是⼀个开源的Python和R语⾔发⾏版，主要⾯向数据科学和机器学习任务

在使用pip3 安装 langchain 时，报错：WARNING: Retrying (Retry(total=4, connect=None, read=None, redirect=None, status=None)) after connection broken by 'SSLError(SSLCertVerificationError(1, '[SSL: CERTIFICATE_VERIFY_FAILED] certificate verify failed: unable to get local issuer certificate (_ssl.c:1007)'))': /simple/langchain/

使用 --trusted-host 选项绕过证书
```
pip3 install langchain --trusted-host pypi.org --trusted-host files.pythonhosted.org
```

# Python 代码风格

[PEP 8 – Style Guide for Python Code](https://peps.python.org/pep-0008/)

# Python应用

- [Python 动手教程，包含 50+ Python 应用](https://github.com/qxresearch/qxresearch-event-1)


# 数据科学

[计算和推理思维：数据科学的基础](https://inferentialthinking.com/chapters/intro.html)

# 参考资料

- [Python Language](https://www.geeksforgeeks.org/python/python-programming-language-tutorial/)
- [Python Tutorial](https://www.pythontutorial.net/)
- [pythoncheatsheet](https://www.pythoncheatsheet.org/cheatsheet/string-formatting)
- [Python-dotenv](https://pypi.org/project/python-dotenv/)
- [DrissionPage-是一个基于 python 的网页自动化工具)
- [25 Python Projects for Beginners – Easy Ideas to Get Started Coding Python](https://www.freecodecamp.org/news/python-projects-for-beginners/)
- [Python-100天学习](https://github.com/jackfrued/Python-100-Days)
