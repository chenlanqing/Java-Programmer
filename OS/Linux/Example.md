## 一.ls命令
    1.列出/home/chenlanqing文件夹下的所有文件和目录的详细资料:
    ls -l -R 
    
    2.列出当前目录中所有以“t”开头的目录的详细内容
    ls -l t*
    
    3.只列出文件下的子目录
    ls -F /opt/soft |grep /$
    ls -l /opt/soft | grep "^d"
    
    4.列出目前工作目录下所有名称是s 开头的档案，愈新的排愈后面
    ls -ltr s*
    
    5.列出目前工作目录下所有档案及目录;目录于名称后加"/", 可执行档于名称后加"*" 
    ls -AF
    
    6.计算当前目录下的文件数和目录数
    ls -l * |grep "^-"|wc -l ---文件个数  
    ls -l * |grep "^d"|wc -l    ---目录个数
    
    7.在ls中列出文件的绝对路径
    ls | sed "s:^:`pwd`/:"
    
    8.列出当前目录下的所有文件（包括隐藏文件）的绝对路径， 对目录不做递归
    find $PWD -maxdepth 1 | xargs ls -ld
    
    递归列出当前目录下的所有文件（包括隐藏文件）的绝对路径
    find $PWD | xargs ls -ld
    
    9.指定文件时间输出格式:
    ls -tl --time-style=full-iso
    
     ls -ctl --time-style=long-iso
     
    10.显示彩色目录列表
    	打开/etc/bashrc, 加入如下一行:
        alias ls="ls --color"



















