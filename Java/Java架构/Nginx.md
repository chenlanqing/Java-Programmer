
* 启动Nginx：systemctl start nginx.service
* 检查nginx配置文件： nginx -t -c /etc/nginx/nginx.conf

# 一、Nginx

## 1、什么是 Nginx

一个开源且高性能，可靠的HTTP中间件、代理服务；

为什么选择Nginx

- Io[多路复用](http://www.cnblogs.com/fanzhidongyzby/p/4098546.html)epoll多路复用
- 轻量级：功能模块少，代码模块化；
- CPU亲和（affinity）：是一种把CPU核心和Nginx工作进程绑定方式，把每个worker进程固定在一个cpu上执行，减少切换CPU的cache miss，活动更好的性能；
- sendfile

## 2、Nginx安装

### 2.1、yum安装

- 安装一些依赖：
	* yum -y install wget httpd-tools vim
	* yum -y install gcc gcc-c++ autoconf pcre pcre-devel make automake

- 初始化：
	* cd /opt； mkdir app download log work backup

Linux安装：（基于yum安装）

- 在 /etc/yum.repos.d/ 目录下新建文件 nginx.repo，在文件中输入：
```
[nginx]
name=nginx repo
baseurl=http://nginx.org/packages/OS/OSRELEASE/$basearch/
gpgcheck=0
enabled=1
==> OS - "rhel" or "centos"
==> OSRELEASE - 对应的版本，如6，7
```
- 查看nginx相关安装包：

	yum list | grep nginx

- 安装：

	yum install nginx

- nginx -v：查看版本好

	nginx -V：查看对应的编译参数

### 2.2、tar包安装

- 安装依赖环境：
	- 安装gcc环境：`yum install gcc-c++`
	- 安装PCRE库，用于解析正则表达式：`yum install -y pcre pcre-devel`
	- zlib压缩和解压缩依赖：`yum install -y zlib zlib-devel`
	- SSL 安全的加密的套接字协议层，用于HTTP安全传输，也就是https：`yum install -y openssl openssl-devel`

- 下载tar包
- 解压，需要注意，解压后得到的是源码，源码需要编译后才能安装：`tar -zxvf nginx-1.16.1.tar.gz` 
- 编译之前，先创建nginx临时目录，如果不创建，在启动nginx的过程中会报错：`mkdir /var/temp/nginx -p`
- 在nginx目录，输入如下命令进行配置，目的是为了创建makefile文件：
	```
	./configure --prefix=/usr/local/nginx --pid-path=/var/run/nginx/nginx.pid --lock-path=/var/lock/nginx.lock --error-log-path=/var/log/nginx/error.log --http-log-path=/var/log/nginx/access.log --with-http_gzip_static_module --http-client-body-temp-path=/var/temp/nginx/client --http-proxy-temp-path=/var/temp/nginx/proxy --http-fastcgi-temp-path=/var/temp/nginx/fastcgi --http-uwsgi-temp-path=/var/temp/nginx/uwsgi --http-scgi-temp-path=/var/temp/nginx/scgi
	```
	|命令	 |解释 |
	| ------|------|
	|–prefix	|指定nginx安装目录|
	|–pid-path	|指向nginx的pid|
	|–lock-path	|锁定安装文件，防止被恶意篡改或误操作|
	|–error-log	|错误日志|
	|–http-log-path	|http日志|
	|–with-http_gzip_static_module	|启用gzip模块，在线实时压缩输出数据流|
	|–http-client-body-temp-path	|设定客户端请求的临时目录|
	|–http-proxy-temp-path	|设定http代理临时目录|
	|–http-fastcgi-temp-path	|设定fastcgi临时目录|
	|–http-uwsgi-temp-path	|设定uwsgi临时目录|
	|–http-scgi-temp-path	|设定scgi临时目录|
- make编译：`make`
- 安装：`make install`
- 进入`/usr/local/nginx/sbin`目录启动nginx：`./nginx`
- 停止：`./nginx -s stop`
- 重新加载：`./nginx -s reload`

## 3、Nginx命令

查看其安装目录 rpm -ql nginx
```
(1)./etc/logrotate.d/nginx - nginx 日志轮转,用于 logrotate 服务的日志切割；
(2)./etc/nginx
	/etc/nginx/nginx.conf - 主配置文件
	/etc/nginx/conf.d
	/etc/nginx/conf.d/default.conf - 默认配置文件
(3)./etc/nginx/fastcgi_params
	/etc/nginx/scgi_params
	/etc/nginx/uwsgi_params
	cgi配置文件
(4)./etc/nginx/koi-utf
	/etc/nginx/koi-win
	/etc/nginx/win-utf
	编码转换映射文件
(5)./etc/nginx/mime.types-设置http协议的content-type与扩展名对应关系；
(6)./usr/lib/systemd/system/nginx-debug.service
	/usr/lib/systemd/system/nginx.service
	/etc/sysconfig/nginx
	/etc/sysconfig/nginx-debug
	用于配置出系统守护进程管理器管理方式
(7)./usr/lib64/nginx/modules
	/etc/nginx/modules
	模块目录
	......
```	

**常用nginx命令：**
```
nginx -s stop       快速关闭Nginx，可能不保存相关信息，并迅速终止web服务。
nginx -s quit       平稳关闭Nginx，保存相关信息，有安排的结束web服务。
nginx -s reload     因改变了Nginx相关配置，需要重新加载配置而重载。
nginx -s reopen     重新打开日志文件。
nginx -c filename   为 Nginx 指定一个配置文件，来代替缺省的。
nginx -t            不运行，而仅仅测试配置文件。nginx 将检查配置文件的语法的正确性，并尝试打开配置文件中所引用到的文件。
nginx -v            显示 nginx 的版本。
nginx -V            显示 nginx 的版本，编译器版本和配置参数
```

## 4、nginx.conf配置文件详解

- 设置worker进程的用户，指的linux中的用户，会涉及到nginx操作目录或文件的一些权限，默认为nobody：
	```
	user root;
	```
- worker进程工作数设置，一般来说CPU有几个，就设置几个，或者设置为N-1也行
	```
	worker_processes 1;
	```
- nginx 日志级别`debug | info | notice | warn | error | crit | alert | emerg`，错误级别从左到右越来越大

- 设置nginx进程 pid
	```
	pid        logs/nginx.pid;
	```
- 设置工作模式
	```
	events {
		# 默认使用epoll
		use epoll;
		# 每个worker允许连接的客户端最大连接数
		worker_connections  10240;
	}
	```
- http 是指令块，针对http网络传输的一些指令配置
	```
	http {
	}
	```
- include 引入外部配置，提高可读性，避免单个配置文件过大
	```
	include       mime.types;
	```
- 设定日志格式，main为定义的格式名称，如此 access_log 就可以直接使用这个变量了
	|参数名	|参数意义|
	|------|------|
	|$remote_addr	|客户端ip|
	|$remote_user	|远程客户端用户名，一般为：’-’|
	|$time_local	|时间和时区|
	|$request	|请求的url以及method|
	|$status	|响应状态码|
	|$body_bytes_send	|响应客户端内容字节数|
	|$http_referer|	记录用户从哪个链接跳转过来的|
	|$http_user_agent	|用户所使用的代理，一般来时都是浏览器|
	|$http_x_forwarded_for	|通过代理服务器来记录客户端的ip|
	
- `sendfile`使用高效文件传输，提升传输性能。启用后才能使用tcp_nopush，是指当数据表累积一定大小后才发送，提高了效率。
	```
	sendfile        on;
	tcp_nopush      on;
	```
- keepalive_timeout设置客户端与服务端请求的超时时间，保证客户端多次请求的时候不会重复建立新的连接，节约资源损耗。
	```
	#keepalive_timeout  0;
	keepalive_timeout  65;
	```
- gzip启用压缩，`html/js/css`压缩后传输会更快
	```
	gzip on;
	```
- `server`可以在`http`指令块中设置多个虚拟主机
	- listen 监听端口
	- server_name localhost、ip、域名
	- location 请求路由映射，匹配拦截
	- root 请求位置
	- index 首页设置
	- alias 别名
	```
	server {
			listen       88;
			server_name  localhost;
			location / {
				root   html;
				index  index.html index.htm;
			}
	}
	```

	**root 与 alias：**

	- root 路径完全匹配访问，配置的时候为：
		```
		location /demo {
			root /home
		}
		```
		用户访问的时候请求为：`url:port/demo/files/img/face.png`

	- alias 可以为你的路径做一个别名，对用户透明配置的时候为：
		```
		location /hello {
			root /home/demo
		}
		```
		用户访问的时候请求为：`url:port/hello/files/img/face.png`，如此相当于为目录demo做一个自定义的别名。

	**location 的匹配规则：**
	- 空格：默认匹配，普通匹配
		```
		location / {
			root /home;
		}
		```
	- `=`：精确匹配
		```
		location = /imooc/img/face1.png {
			root /home;
		}
		```
	- `~*`：匹配正则表达式，不区分大小写
		```
		#符合图片的显示
		location ~ \.(GIF|jpg|png|jpeg) {
			root /home;
		}
		```
	- `~`：匹配正则表达式，区分大小写
		```
		#GIF必须大写才能匹配到
		location ~ \.(GIF|jpg|png|jpeg) {
			root /home;
		}
		```
	- `^~`：以某个字符路径开头
		```
		location ^~ /imooc/img {
			root /home;
		}
		```

**全部配置如下：**
```conf
#运行用户
#user somebody;

#启动进程,通常设置成和cpu的数量相等
worker_processes  1;

#全局错误日志
error_log  /usr/local/nginx/logs/error.log;
error_log  /usr/local/nginx/logs/notice.log  notice;
error_log  /usr/local/nginx/logs/info.log  info;

#PID文件，记录当前启动的nginx的进程ID
pid        /usr/local/nginx/logs/nginx.pid;

#工作模式及连接数上限
events {
    worker_connections 1024;    #单个后台worker process进程的最大并发链接数
}

#设定http服务器，利用它的反向代理功能提供负载均衡支持
http {
    #设定mime类型(邮件支持类型),类型由mime.types文件定义
    include       /usr/local/nginx/conf/mime.types;
    default_type  application/octet-stream;

    #设定日志
    log_format  main  '[$remote_addr] - [$remote_user] [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log    /usr/local/nginx/logs/access.log main;
    rewrite_log     on;

    #sendfile 指令指定 nginx 是否调用 sendfile 函数（zero copy 方式）来输出文件，对于普通应用，
    #必须设为 on,如果用来进行下载等应用磁盘IO重负载应用，可设置为 off，以平衡磁盘与网络I/O处理速度，降低系统的uptime.
    sendfile        on;
    #tcp_nopush     on;

    #连接超时时间
    keepalive_timeout  120;
    tcp_nodelay        on;

    #gzip压缩开关
    #gzip  on;

    #设定实际的服务器列表
    upstream zp_server1{
        server 127.0.0.1:8089;
    }

    #HTTP服务器
    server {
        #监听80端口，80端口是知名端口号，用于HTTP协议
        listen       80;

        #定义使用www.xx.com访问
        server_name  www.helloworld.com;

        #首页
        index index.html

        #指向webapp的目录
        root /root/demo/webapp;

        #编码格式
        charset utf-8;

        #代理配置参数
        proxy_connect_timeout 180;
        proxy_send_timeout 180;
        proxy_read_timeout 180;
        proxy_set_header Host $host;
        proxy_set_header X-Forwarder-For $remote_addr;

        #反向代理的路径（和upstream绑定），location 后面设置映射的路径
        location / {
            proxy_pass http://zp_server1;
        }

        #静态文件，nginx自己处理
        location ~ ^/(images|javascript|js|css|flash|media|static)/ {
            root /root/demo/webapp/views;
            #过期30天，静态文件不怎么更新，过期可以设大一点，如果频繁更新，则可以设置得小一点。
            expires 30d;
        }

        #设定查看Nginx状态的地址
        location /NginxStatus {
            stub_status           on;
            access_log            on;
            auth_basic            "NginxStatus";
            auth_basic_user_file  conf/htpasswd;
        }

        #禁止访问 .htxxx 文件
        location ~ /\.ht {
            deny all;
        }

        #错误处理页面（可选择性配置）
        #error_page   404              /404.html;
        #error_page   500 502 503 504  /50x.html;
        #location = /50x.html {
        #    root   html;
        #}
    }
}
```

## 5、HTTP反向代理

```conf
#运行用户
#user somebody;

#启动进程,通常设置成和cpu的数量相等
worker_processes  1;

#全局错误日志
error_log  /usr/local/nginx/logs/error.log;
error_log  /usr/local/nginx/logs/notice.log  notice;
error_log  /usr/local/nginx/logs/info.log  info;

#PID文件，记录当前启动的nginx的进程ID
pid        /var/logs/nginx/nginx.pid;

#工作模式及连接数上限
events {
    worker_connections 1024;    #单个后台worker process进程的最大并发链接数
}

#设定http服务器，利用它的反向代理功能提供负载均衡支持
http {
    #设定mime类型(邮件支持类型),类型由mime.types文件定义
    include       /usr/local/nginx/conf/mime.types;
    default_type  application/octet-stream;

    #设定日志
    log_format  main  '[$remote_addr] - [$remote_user] [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log    /usr/local/nginx/logs/access.log main;
    rewrite_log     on;

    #sendfile 指令指定 nginx 是否调用 sendfile 函数（zero copy 方式）来输出文件，对于普通应用，
    #必须设为 on,如果用来进行下载等应用磁盘IO重负载应用，可设置为 off，以平衡磁盘与网络I/O处理速度，降低系统的uptime.
    sendfile        on;
    #tcp_nopush     on;

    #连接超时时间
    keepalive_timeout  120;
    tcp_nodelay        on;

    #gzip压缩开关
    #gzip  on;

    #设定实际的服务器列表
    upstream zp_server1{
        server 127.0.0.1:8089;
    }

    #HTTP服务器
    server {
        #监听80端口，80端口是知名端口号，用于HTTP协议
        listen       80;
        #定义使用www.xx.com访问
        server_name  www.helloworld.com;
        #首页
        index index.html
        #指向webapp的目录
        root /root/demo/;
        #编码格式
        charset utf-8;

        #代理配置参数
        proxy_connect_timeout 180;
        proxy_send_timeout 180;
        proxy_read_timeout 180;
        proxy_set_header Host $host;
        proxy_set_header X-Forwarder-For $remote_addr;

        #反向代理的路径（和upstream绑定），location 后面设置映射的路径
        location / {
            proxy_pass http://zp_server1;
        }

        #静态文件，nginx自己处理
        location ~ ^/(images|javascript|js|css|flash|media|static)/ {
            root /root/demo/views;
            #过期30天，静态文件不怎么更新，过期可以设大一点，如果频繁更新，则可以设置得小一点。
            expires 30d;
        }

        #设定查看Nginx状态的地址
        location /NginxStatus {
            stub_status           on;
            access_log            on;
            auth_basic            "NginxStatus";
            auth_basic_user_file  conf/htpasswd;
        }

        #禁止访问 .htxxx 文件
        location ~ /\.ht {
            deny all;
        }

        #错误处理页面（可选择性配置）
        #error_page   404              /404.html;
        #error_page   500 502 503 504  /50x.html;
        #location = /50x.html {
        #    root   html;
        #}
    }
}
```

## 6、负载均衡配置

假设这样一个应用场景：将应用部署在 `192.168.1.11:80`、`192.168.1.12:80`、`192.168.1.13:80` 三台 linux 环境的服务器上。网站域名叫 `www.helloworld.com`，公网 IP 为 `192.168.1.11`。在公网 IP 所在的服务器上部署 nginx，对所有请求做负载均衡处理

```conf
http {
     #设定mime类型,类型由mime.type文件定义
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;
    #设定日志格式
    access_log    /var/log/nginx/access.log;

    #设定负载均衡的服务器列表
    upstream load_balance_server {
        #weigth参数表示权值，权值越高被分配到的几率越大
        server 192.168.1.11:80   weight=5;
        server 192.168.1.12:80   weight=1;
        server 192.168.1.13:80   weight=6;
    }

   #HTTP服务器
   server {
        #侦听80端口
        listen       80;

        #定义使用www.xx.com访问
        server_name  www.helloworld.com;

        #对所有请求进行负载均衡请求
        location / {
            root        /root;                 #定义服务器的默认网站根目录位置
            index       index.html index.htm;  #定义首页索引文件的名称
            proxy_pass  http://load_balance_server ;#请求转向load_balance_server 定义的服务器列表

            #以下是一些反向代理的配置(可选择性配置)
            #proxy_redirect off;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            #后端的Web服务器可以通过X-Forwarded-For获取用户真实IP
            proxy_set_header X-Forwarded-For $remote_addr;
            proxy_connect_timeout 90;          #nginx跟后端服务器连接超时时间(代理连接超时)
            proxy_send_timeout 90;             #后端服务器数据回传时间(代理发送超时)
            proxy_read_timeout 90;             #连接成功后，后端服务器响应时间(代理接收超时)
            proxy_buffer_size 4k;              #设置代理服务器（nginx）保存用户头信息的缓冲区大小
            proxy_buffers 4 32k;               #proxy_buffers缓冲区，网页平均在32k以下的话，这样设置
            proxy_busy_buffers_size 64k;       #高负荷下缓冲大小（proxy_buffers*2）
            proxy_temp_file_write_size 64k;    #设定缓存文件夹大小，大于这个值，将从upstream服务器传

            client_max_body_size 10m;          #允许客户端请求的最大单文件字节数
            client_body_buffer_size 128k;      #缓冲区代理缓冲用户端请求的最大字节数
        }
    }
}
```

### 3.3、Nginx解决跨域

```conf
server {
	listen       90;
	server_name  localhost;
	#允许跨域请求的域，*代表所有
	add_header 'Access-Control-Allow-Origin' *;
	#允许带上cookie请求
	add_header 'Access-Control-Allow-Credentials' 'true';
	#允许请求的方法，比如 GET/POST/PUT/DELETE
	add_header 'Access-Control-Allow-Methods' *;
	#允许请求的header
	add_header 'Access-Control-Allow-Headers' *;
	location / {
		root   /root/software/demo;
		index  index.html;
	}
	location = /50x.html {
		root   html;
	}
}
```

### 3.4、静态资源防盗链

```conf
server {
	listen	90;
	server_name localhost;
	#对源站点验证
	valid_referers *.chenlanqing.com; 
	#非法引入会进入下方判断
	if ($invalid_referer) {
		return 404;
	}
	....
}
```


# 三、Nginx深入

## 1、Nginx的进程模型

- master进程
- process进程

## 2、Nginx的请求抢占机制


## 3、Nginx的事件处理



## 4、Nginx 模块化设计

**4.1、[sub_status](http://nginx.org/en/docs/http/ngx_http_stub_status_module.html)**
```
(1).模块名称：ngx_http_stub_status_module
(2).用途：显示nginx客户端基本状态信息；
(3).安装模块：--with-http_stub_status_module
(4).配置语法：
	Syntax：	stub_status；
	Default：—
	Context：server, location
(5).示例：
	location /basic_status {
		stub_status；
	}
	页面访问： 127.0.0.1/mystatus,浏览器显示如下内容
	Active connections： 1 
	server accepts handled requests
		2 2 2 
	Reading： 0 Writing： 1 Waiting： 0 
```

**4.2、random_index：**

```
(1).模块名称：ngx_http_random_index_module
(2).用途：在对应目录中随机选择一个主页,默认是关闭的,需要安装
(3).安装模块：--with-http_random_index_module
(4).配置语法：
	Syntax：	random_index on | off；
	Default：	
	random_index off；
	Context：	location
(5).示例：
	location / {
		random_index on；
	}
```

**4.3、sub_module：**
```
(1).模块名称：ngx_http_sub_module
(2).用途：http内容替换
(3).安装模块：--with-http_sub_module
(4).配置语法：
	A.sub_filter：替换内容
		Syntax：	sub_filter string replacement；
		Default：	—
		Context：	http, server, location
	B.sub_filter_last_modified：
		Syntax：	sub_filter_last_modified on | off；
		Default： sub_filter_last_modified off；
		Context：http, server, location
	C.sub_filter_once：是否全部替换
		Syntax：	sub_filter_once on | off；
		Default：	
		sub_filter_once on；
		Context：	http, server, location
	D.sub_filter_types：过滤文件类型
		Syntax：	sub_filter_types mime-type ...；
		Default：	
		sub_filter_types text/html；
		Context：	http, server, location
(5).配置示例：
	location / {
		sub_filter '<a href="http://127.0.0.1：8080/'  '<a href="https：//$host/'；
		sub_filter '<img src="http://127.0.0.1：8080/' '<imgsrc="https：//$host/'；
		sub_filter_once on；
	}
```
**4.4、请求限制**

- **4.4.1、连接频率限制：**
	```
	(1).模块名称：ngx_http_limit_conn_module
	(2).用途：限制并发连接次数
	(3).安装模块：
	(4).配置语法：
		A.limit_conn_zone：key表示限制的条件,比如ip,为其开辟size空间
			Syntax：	limit_conn_zone key zone=name：size；
			Default：	—
			Context：	http
		B.limit_conn： 这里zone是上述limit_conn_zone中name值,number表示并发的限制
			Syntax：	limit_conn zone number；
			Default：	—
			Context：	http, server, location
	(5).配置示例：
		limit_conn_zone $binary_remote_addr zone=perip：10m；
		limit_conn_zone $server_name zone=perserver：10m；
		server {
			...
			limit_conn perip 10；
			limit_conn perserver 100；
		}
	```
- **4.4.2、请求限制：**
	```
	(1).模块名称：ngx_http_limit_req_module
	(2).用途：限制客户端请求次数,或者速度等
	(3).安装模块：
	(4).配置语法：
		A.limit_req_zone：
			Syntax：	limit_req_zone key zone=name：size rate=rate；
			Default：	—
			Context：	http
		B.limit_req： 
			Syntax：	limit_req zone=name [burst=number] [nodelay]；
			Default：	—
			Context：	http, server, location
	(5).配置示例：
		limit_req_zone $binary_remote_addr zone=perip：10m rate=1r/s；
		limit_req_zone $server_name zone=perserver：10m rate=10r/s；
		server {
			...
			limit_req zone=perip burst=5 nodelay；
			limit_req zone=perserver burst=10；
		}
	```
- **4.4.3、整个配置示例：修改 /nginx/conf.d/default.conf 配置文件**
	```
	limit_conn_zone $binary_remote_addr zone=conn_zone：1m；
	limit_req_zone $binary_remote_addr zone=req_zone：1m rate=1r/s；
	server {
		listen       80；
		server_name  localhost；

		#charset koi8-r；
		#access_log  /var/log/nginx/host.access.log  main；

		location / {
			root   /usr/share/nginx/html；
			#limit_conn conn_zone 1；
			#limit_req zone=req_zone burst=3 nodelay；
			#limit_req zone=req_zone burst=3；
			#limit_req zon e=req_zone；
			index  index.html index.htm；
		}
		...
	}
	```

# 二、静态资源web服务

## 1、文件读取
```
(1).配置语法：
	Syntax：	sendfile on | off；
	Default： sendfile off；
	Context： http, server, location, if in location
	引用： --with-file-aio 异步文件读取
```
## 2、tcp_nopush
```
Syntax：	tcp_nopush on | off；
Default： tcp_nopush off；
Context： http, server, location
注意： sendfile 开启的情况下,提高网络包的传输效率
```

## 3、tcp_nodelay
```
Syntax：	tcp_nodelay on | off；
Default： tcp_nodelay on；
Context： http, server, location
在keeplive连接下,提高网络包的传输实时性
```

# 参考文档

* [Nginx官方文档](http://nginx.org/en/docs/)
* [Nginx极简教程](https://mp.weixin.qq.com/s/vHkxYfpuiAteMNSrpNWdsw)






















