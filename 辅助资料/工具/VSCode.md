# 一、C++开发环境

## 1、安装vscode插件

- Code Runner
- C/C++
- C/C++ Extension Pack
- C/C++ Themes
- CMake
- CMake Tools

## 2、Windows环境

- [Windows配置C++环境](https://code.visualstudio.com/docs/cpp/config-mingw#_prerequisites)

### 2.1、安装MSYS2

[MSYS2](https://www.msys2.org/) 是一个用于 Windows 的软件分发和构建平台，它提供了 MinGW 工具链，可以用于编译和调试 C/C++ 程序

下载包后，按照提示安装，默认是安装在C盘：`C:\msys64`，双击`msys2.exe`打开 MSYS2 终端，安装MinGW-w64 工具链，输入如下命令
```bash
pacman -S --needed base-devel mingw-w64-ucrt-x86_64-toolchain
```
按 Enter 键接受工具链组中的默认包数，然后确认，等待安装完成；

验证安装完成，在 MSYS2 终端中，运行以下命令验证安装是否成功：
```bash
gcc --version
g++ --version
gdb --version
```
如果显示版本信息，说明安装成功

### 2.2、添加环境变量

假设安装目录为：`C:\msys64`

添加 `C:\msys64\ucrt64\bin` 到环境变量

在 Windows 命令行中运行 gcc --version，验证是否可以在命令行中直接调用 GCC

### 2.3、集成MSYS2到VSCode

#### （1）配置编译器路径

```json
{
    "C_Cpp.default.compilerPath": "C:/msys64/ucrt64/bin/g++.exe"
}
```

#### （2）创建 tasks.json 文件：

在 VSCode 中打开一个 C/C++ 项目文件夹。按 Ctrl+Shift+P，输入 Tasks: Configure Default Build Task，选择 Create tasks.json file from template。
选择 Others，生成 tasks.json 文件。修改 tasks.json 文件，配置编译任务：
```json
{
    "version": "2.0.0",
    "tasks": [
		{
			"label": "build",
			"type": "shell",
			"command": "g++",
			"args": [
				"-g",
				"${file}",
				"-o",
				"${fileDirname}/${fileBasenameNoExtension}.exe"
			],
			"group": {
				"kind": "build",
				"isDefault": true
			},
			"detail": "Generated task by VSCode"
		}
	]
}
```

#### （3）创建 launch.json 文件：

按 Ctrl+Shift+D，打开调试面板。点击 create a launch.json file，选择 C++ (GDB/LLDB)。生成 launch.json 文件，修改配置如下：
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "name": "C++ Launch",
            "type": "cppdbg",
            "request": "launch",
            "program": "${fileDirname}/${fileBasenameNoExtension}.exe",
            "args": [],
            "stopAtEntry": false,
            "cwd": "${workspaceFolder}",
            "environment": [],
            "externalConsole": true,
            "MIMode": "gdb",
            "miDebuggerPath": "C:/msys64/ucrt64/bin/gdb.exe",
            "setupCommands": [
                {
                    "description": "Enable pretty-printing for gdb",
                    "text": "-enable-pretty-printing",
                    "ignoreFailures": true
                },
                {
                    "description": "将反汇编风格设置为 Intel",
                    "text": "-gdb-set disassembly-flavor intel",
                    "ignoreFailures": true
                }
            ],
            "preLaunchTask": "build"
        }
    ]
}
```

# 参考资料

- [官方文档](https://code.visualstudio.com/docs)
