# 一、Anaconda


## conda命令简单实用说明

创建新环境：
```bash
conda create --name myenv
```
创建一个名为myenv的新环境。


激活环境：
```bash
conda activate myenv
```
激活名为myenv的环境。

停用环境：
```bash
conda deactivate
```
停用当前活跃的Conda环境。


列出所有环境：
```bash
conda env list
```
或
```bash
conda info --envs
```
显示所有已创建的Conda环境。


删除环境：
```bash
conda remove --name myenv --all
```
删除名为myenv的环境。


克隆环境：
```bash
conda create --name newenv --clone myenv
```
克隆名为myenv的环境到newenv。

安装包：
```bash
conda install package-name
```
安装指定的包。

更新包：
```bash
conda update package-name
```
更新指定的包到最新版本。

删除包：
```bash
conda remove package-name
```
删除指定的包。

列出已安装包：
```bash
conda list
```
列出当前环境中已安装的所有包。

搜索包：
```bash
conda search package-name
```
搜索可用的包。

导出环境：
```bash
conda env export > environment.yml
```
将当前环境导出为environment.yml文件。

从文件创建环境：
```bash
conda env create -f environment.yml
```
从environment.yml文件创建环境。