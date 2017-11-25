yum -y install wget httpd-tools vim
yum -y install gcc gcc-c++ autoconf pcre pcre-devel make automake
cd /opt; mkdir app download log work backup

1.什么是 Nginx:一个开源且高性能,可靠的HTTP中间件,代理服务;

2.Nginx安装:
	2.1.Linux安装:(基于yum安装)
		(1).在 /etc/yum.repos.d/ 目录下新建文件 nginx.repo,在文件中输入:
			[nginx]
			name=nginx repo
			baseurl=http://nginx.org/packages/OS/OSRELEASE/$basearch/
			gpgcheck=0
			enabled=1
			==> OS - "rhel" or "centos"
			==> OSRELEASE - 对应的版本,如6,7
		(2).查看nginx相关安装包:
			yum list | grep nginx
		(3).安装:
			yum install nginx
		(4).nginx -v:查看版本好
			nginx -V:查看对应的编译参数
	2.2.
3.Nginx 参数使用:
	3.1.Nginx 安装目录:查看其安装目录 rpm -ql nginx
		(1)./etc/logrotate.d/nginx - nginx 日志轮转,用于 logrotate 服务的日志切割;
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
	3.2.Nginx 编译参数:nginx -V
			























