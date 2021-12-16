# 一、源码导入idea

- （1）安装JDK、gradle 环境；
- （2）下载kafka源码；
- （3）执行kafka文件的中的：`./gradlew`
- （4）因为kafka需要生成对应的java类（org.apache.kafka.common.message包的类），需要打包命令：`./gradlew jar` 或者`./gradlew clean releaseTarGz`


https://mp.weixin.qq.com/s/UdzdvSymNeu_99JUTHMOuQ