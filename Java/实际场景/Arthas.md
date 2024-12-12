## 1、Arthas介绍

## 2、Arthas使用

### 2.1、arthas排查死循环

### 2.2、获取controller 所有信息：

`watch org.springframework.web.servlet.DispatcherServlet doService '#httpRequest=params[0],#allHeaders={},#forEnumeration = :[#this.hasMoreElements()? (#headerName=#this.nextElement(),#allHeaders.add(#headerName+"="+#httpRequest.getHeader(#headerName)),#forEnumeration(#this)):null],#forEnumeration(#httpRequest.getHeaderNames()),#allHeaders'  -n 5  -x 3`

## 3、Arthas原理

# 参考资料

- [Arthas官方文档](https://arthas.aliyun.com/doc/)
- [Arthas原理](https://juejin.cn/post/6904280021632974856)
- [Arthas用户案例](https://github.com/alibaba/arthas/issues?q=label%3Auser-case)
- [Arthas命令使用场景](https://juejin.cn/post/7291931708920512527)
- [Arthas Idea Plugin](https://www.yuque.com/arthas-idea-plugin/help/pe6i45)
- [Arthas pulgin source code](https://github.com/WangJi92/arthas-idea-plugin/blob/master/README.md)