<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.开发环境的准备:](#%E4%B8%80%E5%BC%80%E5%8F%91%E7%8E%AF%E5%A2%83%E7%9A%84%E5%87%86%E5%A4%87)
- [二.微信开发模式:](#%E4%BA%8C%E5%BE%AE%E4%BF%A1%E5%BC%80%E5%8F%91%E6%A8%A1%E5%BC%8F)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一.开发环境的准备:
    1.微信公众号:
    2.外围映射工具(开发调试)
    与微信对接的URL要具备以下条件
    	(1).在公网上能够访问;
    	(2).端口只支持80端口
    	注:ngrok可以将内网地址映射到外网上,  http://www.tunnel.mobi/	
    	在CMD命令中先切换到ngrok所在的位置再进行如下操作
    	ngrok -config ngrok.cfg -subdomain example 8080
    	说明：
    	example-自己任意设置；
    	8080-tomcat的端口号
# 二.微信开发模式:
    开发模式与编辑模式是互斥的








































































































































































