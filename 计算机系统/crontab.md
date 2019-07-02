
# 1、什么是

CRONTAB是一种用于设置周期性执行任务的工具

yum install -y cornie crontabs 

crontab -e  编辑crontab表单

crontab -l  查看crontab表单任务

systemctl restart crond  重启crond服务

    * * * * * mycommand

配置文件

/etc/crontab