

# 1.安装相关命令
    (1).安装ifconfig命令:
        yum install net-tools
    (2).安装rz和sz命令
        yum install lrzsz -y
# 2.安装redis
    (1).下载安装包:
        wget http://download.redis.io/releases/redis-4.0.6.tar.gz
    (2).将下载的文件移动到目录:/usr/local
    (3).解压缩包:
        tar -zxvf redis-4.0.6.tar.gz
    (4).安装gcc依赖
        yum install gcc
    (5).进入到redis目录
        cd redis-4.0.6
    (6).编译安装
        make MALLOC=libc　
        将/usr/local/redis-4.0.6/src目录下的文件加到/usr/local/bin目录
        cd src && make install
    (7).测试是否安装成功,进入的 src目录,执行命令:
        ./redis-server
    (8).将redis以后台启动
        * 修改配置文件:redis.conf
            daemonize yes
        * 指定redis.conf文件启动
            ./redis-server /usr/local/redis-4.0.6/redis.conf
        * 关闭该进程
            ps -aux | grep redis查看redis进程
            使用kill命令杀死进程
            kill -9 pid
    (9).设置redis开机自启动
        * 在/etc目录下新建redis目录
        * 将/usr/local/redis-4.0.6/redis.conf 文件复制一份到/etc/redis目录下，并命名为6379.conf
        * 将redis的启动脚本复制一份放到/etc/init.d目录下
            cp /usr/local/redis-4.0.6/utils/redis_init_script /etc/init.d/redisd
        * 切换到/etc/init.d目录下,然后执行自启命令
            chkconfig redisd on
            看结果是redisd不支持chkconfig



# 参考文章
* [Redis安装](https://www.cnblogs.com/zuidongfeng/p/8032505.html)
