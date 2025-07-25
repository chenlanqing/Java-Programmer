
# 一、容器与容器编排

## 1、容器技术

- Docker
- Cloud Foundry

## 2、编排技术

- K8S：是一个自动化、容器化的应用程序部署扩展和管理的开放源代码系统。Kubernetes通过Master节点进行容器的统一编排决策和管理（类似人体的大脑），通过Worker节点（或者直接叫做Node节点）进行容器引擎（如Docker Engine）的配合和容器Container的实际操作（类似人体的手，但不止一双，可以有很多只手），真正实现了从单一容器的管理，向微服务应用的容器化落地的飞跃，提供了包含计算资源管理、存储资源管理、网络服务发现、系统和应用监控、软件升级回滚、安全认证等等主题的完整解决方案

- Mesos + Marathon

- Docker Swarm

# 二、Docker

- [Docker入门到实战](https://yeasy.gitbook.io/docker_practice/image/dockerfile/arg)

## 1、docker介绍

### 1.1、docker为什么出现

一款产品从开发到上线，从操作系统，到运行环境，再到应用配置。作为开发+运维之间的协作我们需要关心很多东西，这也是很多互联网公司都不得不面对的问题，特别是各种版本的迭代之后，不同版本环境的兼容，对运维人员都是考验；

Docker镜像的设计，使得Docker得以打破过去「程序即应用」的观念。透过镜像(images)将作业系统核心除外，运作应用程式所需要的系统环境，由下而上打包，达到应用程式跨平台间的无缝接轨运作

### 1.2、docker理念

Docker是基于Go语言实现的云开源项目。

Docker的主要目标是“Build，Ship and Run Any App,Anywhere”，也就是通过对应用组件的封装、分发、部署、运行等生命周期的管理，使用户的APP（可以是一个WEB应用或数据库应用等等）及其运行环境能够做到“一次封装，到处运行”

将应用运行在 Docker 容器上面，而 Docker 容器在任何操作系统上都是一致的，这就实现了跨平台、跨服务器。只需要一次配置好环境，换到别的机子上就可以一键部署好，大大简化了操作

### 1.3、docker

解决了运行环境和配置问题软件容器，方便做持续集成并有助于整体发布的容器虚拟化技术。

- 镜像：Docker 镜像（Image）就是一个只读的模板。镜像可以用来创建 Docker 容器，一个镜像可以创建很多容器
- 容器：Docker 利用容器（Container）独立运行的一个或一组应用。容器是用镜像创建的运行实例。它可以被启动、开始、停止、删除。每个容器都是相互隔离的、保证安全的平台；

    容器的定义和镜像几乎一模一样，也是一堆层的统一视角，唯一区别在于容器的最上面那一层是可读可写的

- 仓库：仓库（Repository）是集中存放镜像文件的场所。仓库(Repository)和仓库注册服务器（Registry）是有区别的。仓库注册服务器上往往存放着多个仓库，每个仓库中又包含了多个镜像，每个镜像有不同的标签（tag）；

    仓库分为公开仓库（Public）和私有仓库（Private）两种形式。最大的公开仓库是[Docker Hub](https://hub.docker.com/)，存放了数量庞大的镜像供用户下载

Docker 本身是一个容器运行载体或称之为管理引擎。把应用程序和配置依赖打包好形成一个可交付的运行环境，这个打包好的运行环境就似乎 image镜像文件。只有通过这个镜像文件才能生成 Docker 容器。image 文件可以看作是容器的模板。Docker 根据 image 文件生成容器的实例。同一个 image 文件，可以生成多个同时运行的容器实例。
* image 文件生成的容器实例，本身也是一个文件，称为镜像文件。
* 一个容器运行一种服务，当我们需要的时候，就可以通过docker客户端创建一个对应的运行实例，也就是我们的容器
* 至于仓储，就是放了一堆镜像的地方，我们可以把镜像发布到仓储中，需要的时候从仓储中拉下来就可以了

![](image/Docker结构.png)

## 2、docker安装

### 2.1、安装

针对centos7安装如下：
- 安装依赖：`yum -y install gcc gcc-c++ yum-utils device-mapper-persistent-data lvm2`；
- 卸载老版本：`yum -y remove docker docker-common docker-selinux docker-engine`；
- 设置stable镜像仓库
	- 国外镜像仓库，比较慢：`yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo`
	- 阿里云镜像仓库：`yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo`
- 更新yum软件包索引：`yum makecache fast`
- 安装DOCKER CE：`yum -y install docker-ce`
- 启动docker：`systemctl start docker`；
- 测试
	- `docker version`
	- `docker run hello-world`
- 配置镜像加速
	- `mkdir -p /etc/docker`
	- `vim /etc/docker/daemon.json`
        - 网易云：`{"registry-mirrors": ["http://hub-mirror.c.163.com"] }`
        - 阿里云：`{"registry-mirrors": ["https://｛自已的编码｝.mirror.aliyuncs.com"]}`
	- `systemctl daemon-reload`
	- 重启docker：`systemctl restart docker`

拉取镜像时报错：Error response from daemon: Get "https://registry-1.docker.io/v2/": net/http: request canceled while waiting for connection (Client.Timeout exceeded while awaiting headers)，可以添加更多镜像加速：
```json
{
    "registry-mirrors": [
        "https://docker.registry.cyou",
        "https://docker-cf.registry.cyou",
        "https://dockercf.jsdelivr.fyi",
        "https://docker.jsdelivr.fyi",
        "https://dockertest.jsdelivr.fyi",
        "https://mirror.aliyuncs.com",
        "https://dockerproxy.com",
        "https://mirror.baidubce.com",
        "https://docker.m.daocloud.io",
        "https://docker.nju.edu.cn",
        "https://docker.mirrors.sjtug.sjtu.edu.cn",
        "https://docker.mirrors.ustc.edu.cn",
        "https://mirror.iscas.ac.cn",
        "https://docker.rainbond.cc"
    ]
}
```

### 2.2、运行原理

Docker是一个Client-Server结构的系统，Docker守护进程运行在主机上，然后通过Socket连接从客户端访问，守护进程从客户端接受命令并管理运行在主机上的容器。容器，是一个运行时环境，就是集装箱

docker为什么比vm快：
- docker有着比虚拟机更少的抽象层。由亍docker不需要Hypervisor实现硬件资源虚拟化,运行在docker容器上的程序直接使用的都是实际物理机的硬件资源。因此在CPU、内存利用率上docker将会在效率上有明显优势。

- docker利用的是宿主机的内核，而不需要Guest OS。因此，当新建一个容器时，docker不需要和虚拟机一样重新加载一个操作系统内核。仍而避免引寻、加载操作系统内核返个比较费时费资源的过程，当新建一个虚拟机时，虚拟机软件需要加载Guest OS，返个新建过程是分钟级别的。而docker由于直接利用宿主机的操作系统，则省略了返个过程，因此新建一个docker容器只需要几秒钟；

## 3、常用命令

### 3.1、帮助命令

docker --help

### 3.2、镜像命令

- `docker images`：列出本地主机上的镜像，
    ```
    REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
    hello-world         latest              fce289e99eb9        12 months ago       1.84kB
    ```
    - REPOSITORY：表示镜像的仓库源
    - TAG：镜像的标签 
    - IMAGE ID：镜像ID 
    - CREATED：镜像创建时间 
    - SIZE：镜像大小 

    同一仓库源可以有多个 TAG，代表这个仓库源的不同个版本，我们使用 `REPOSITORY:TAG` 来定义不同的镜像；如果你不指定一个镜像的版本标签，例如你只使用 ubuntu，docker 将默认使用 ubuntu:latest 镜像 

    命令的选项OPTIONS说明：
    - `-a` :列出本地所有的镜像（含中间映像层）
    - `-q` :只显示镜像ID。
    - `--digests` :显示镜像的摘要信息
    - `--no-trunc` :显示完整的镜像信息

- `docker search 某个XXX镜像名字 `
    - 网站：https://hub.docker.com
    - 命令：`docker search [OPTIONS]` 镜像名字
    - OPTIONS说明：
        - `--no-trunc` : 显示完整的镜像描述
        - `-s` : 列出收藏数不小于指定值的镜像。
        - `--automated` : 只列出 automated build类型的镜像；

- `docker pull 某个XXX镜像名字`：下载镜像
    
    docker pull 镜像名字[:TAG]，如果没有带上tag，默认是latest

- `docker rmi 某个XXX镜像名字ID`：删除镜像，默认是删除 latest
  - 删除单个：`docker rmi  -f 镜像ID`
  - 删除多个：`docker rmi -f 镜像名1:TAG 镜像名2:TAG `
  - 删除全部：`docker rmi -f $(docker images -qa)`

- `commit操作`：docker commit提交容器副本使之成为一个新的镜像

  `docker commit -m=“提交的描述信息” -a=“作者” 容器ID 要创建的目标镜像名:[标签名]`
  
  案例演示：从Hub上下载tomcat镜像到本地并成功运行，`docker run -it -p 8080:8080 tomcat`，故意删除上一步镜像生产tomcat容器的文档，也即当前的tomcat运行实例是一个没有文档内容的容器，以它为模板commit一个没有doc的tomcat新镜像`test/tomcat02`启动我们的新镜像并和原来的对比，启动`test/tomcat02`，它没有docs，新启动原来的tomcat，它有docs

  `docker commit -m "update index.html" mynginx mynginx:v1.0`
  
- `save操作`：保存镜像为指定的文件：`docker save -o mynginx.tar mynginx:v1.0`

- `load操作`：加载镜像：`docker load -i mynginx.tar`

**推送镜像到社区：**
- 登录：`docker login`，需要注册docker hub账号
- 命名：`docker tag`，示例：`docker tag mynginx:v1.0 test/mynginx:v1.0`
- 推送：`docker push`，示例：`docker push test/mynginx:v1.0`

### 3.3、容器命令

开启官方docker的教程：`docker run -dp 80:80 docker/getting-started`

#### 3.3.1、新建并启动容器

`docker run [OPTIONS] IMAGE_id [COMMAND] [ARG...]`

OPTIONS说明
- `--name="容器新名字"`: 为容器指定一个名称； 
- `-d`: 后台运行容器，并返回容器ID，也即启动守护式容器； 
- `-i`：以交互模式运行容器，通常与 `-t` 同时使用； 
- `-t`：为容器重新分配一个伪输入终端，通常与 `-i` 同时使用； 
- `-P`: 随机端口映射； 
- `-p`: 指定端口映射，有以下四种格式，容器内的端口可以重复，但是docker主机上映射的端口不能重复
    - ip:hostPort:containerPort 
    - ip:containerPort 
    - hostPort:containerPort 主机端口映射与容器端口
    - containerPort

比如启动rabbitmq容器：`docker run -d --name rabbitmq3.7.7 -p 5672:5672 -p 15672:15672 rabbitmq:3.7.7-management`

**启动交互式容器**

使用镜像centos:latest以交互模式启动一个容器，在容器内执行/bin/bash命令：`docker run -it centos /bin/bash`

**启动守护式容器**

使用镜像centos:latest以后台模式启动一个容器：`docker run -d 容器名`，但是问题是：然后`docker ps -a` 进行查看, 会发现容器已经退出；

**Docker容器后台运行,就必须有一个前台进程**，容器运行的命令如果不是那些 一直挂起的命令 （比如运行top，tail），就是会自动退出的；是docker的机制问题，比如你的web容器，我们以nginx为例，正常情况下，我们配置启动服务只需要启动响应的service即可。例如：`service nginx start`；但是这样做，nginx为后台进程模式运行，就导致docker前台没有运行的应用，这样的容器后台启动后，会立即自杀因为他觉得他没事可做了。

所以，最佳的解决方案是，将你要运行的程序以前台进程的形式运行：
`docker run -d centos /bin/sh -c "while true;do echo hello bluefish;sleep 2;done"`

#### 3.3.2、列出当前所有正在运行的容器

`docker ps [OPTIONS]`

OPTIONS说明：
- `-a` : 列出当前所有 正在运行 的容器 + 历史上运行过 的 
- `-l` : 显示最近创建的容器。 
- `-n` : 显示最近n个创建的容器。 
- `-q` : 静默模式，只显示容器编号。 
- `--no-trunc` :不截断输出。 

#### 3.3.3、退出容器

两种退出方式
- exit：容器停止退出
- ctrl+P+Q：容器不停止退出

####  3.3.4、启动、停止、重启

- 启动容器：`docker start 容器ID或者容器名`
- 重启容器：`docker restart 容器ID或者容器名`
- 停止容器：`docker stop 容器ID或者容器名`
- 强制停止容器：`docker kill 容器ID或者容器名`
- 停止所有容器：`docker stop $(docker ps -a -q)`

#### 3.3.5、删除容器

删除已停止的容器：docker rm 容器ID

一次性删除多个容器
- `docker rm -f $(docker ps -a -q)`
- `docker ps -a -q | xargs docker rm`

#### 3.3.6、查看容器日志

`docker logs -f -t --tail 容器ID`
* `-t` 是加入时间戳
* `-f` 跟随最新的日志打印
* `--tail` 数字 显示最后多少条

#### 3.3.7、查看容器

```bash
docker inspect <容器或镜像名称>
# 输出容器详细元数据
```
[`docker inspect`](https://docs.docker.com/reference/cli/docker/inspect/) 命令用于查看 Docker 容器、镜像、网络等的详细元数据，通常返回的是 JSON 格式的输出。如果只想获取某些特定的信息，可以结合 `-f`（`--format`）选项，使用 Go 模板语法来提取你需要的字段。
```bash
docker inspect -f '{{.字段路径}}' <容器或镜像名称>
```
`-f` 后面的模板语法让可以从 `docker inspect` 返回的 JSON 数据中提取指定的信息。通过 Go 模板，可以深入到 JSON 对象的各个嵌套字段。

**获取容器的 IP 地址**：可以使用如下命令：
```bash
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' <容器名称或ID>
```

**获取容器的状态（Running/Exited）**：如果需要获取容器的状态（是正在运行还是已停止），可以使用：
```bash
docker inspect -f '{{.State.Status}}' <容器名称或ID>
```

**获取容器的进程 ID (PID)**：
```bash
docker inspect -f '{{.State.Pid}}' <容器名称或ID>
```

**获取容器的创建时间**：
```bash
docker inspect -f '{{.Created}}' <容器名称或ID>
```

**获取容器的主机端口映射**：如果容器有端口映射，并且你想知道主机端口和容器端口的对应关系，可以使用：
```bash
docker inspect -f '{{.NetworkSettings.Ports}}' <容器名称或ID>
```

**获取容器的挂载卷**：
```bash
docker inspect -f '{{.Mounts}}' <容器名称或ID>
```

**获取容器的环境变量**：
```bash
docker inspect -f '{{range .Config.Env}}{{println .}}{{end}}' <容器名称或ID>
```

**获取容器的镜像名称**：
```bash
docker inspect -f '{{.Config.Image}}' <容器名称或ID>
```

**获取容器的所有挂载卷路径**：
```bash
docker inspect -f '{{.Mounts}}' <容器名称或ID>
```

#### 3.3.8、容器交互

进入正在运行的容器并以命令行交互
- `docker exec -it 容器ID bashShell`，比如：docker exec -it container_id /bin/bash，不进入容器，执行命令

    比如：`docker exec container_id java -version`，执行对应容器里的`java -version`命令

- 重新进入`docker attach 容器ID`

上述两个区别
- attach：直接进入容器启动命令的终端，不会启动新的进程
- exec：是在容器中打开新的终端，并且可以启动新的进程

**文件拷贝：**
- 从容器内拷贝文件到宿主机：`docker cp 容器ID:容器内路径 宿主机路径`；
- 从宿主机拷贝文件到容器内：`docker cp 宿主机文件 容器ID:容器内路径`；

#### 3.3.9、查看容器状态

`docker stats <容器>`
```
CONTAINER ID   NAME          CPU %     MEM USAGE / LIMIT     MEM %     NET I/O          BLOCK I/O        PIDS
7706de4ef754   redis-stack   0.17%     117.7MiB / 1.715GiB   6.70%     116kB / 54.8kB   188kB / 57.3kB   22
```
另外也可以使用：
```bash
# 显示容器状态，jq 用来格式化 json 输出
$ docker inspect tomcat -f '{{json .State}}' | jq
{
  "Status": "exited",
  "Running": false,
  "Paused": false,
  "Restarting": false,
  "OOMKilled": true,
  "Dead": false,
  "Pid": 0,
  "ExitCode": 137,
  "Error": "",
  ...
}
```

#### 3.3.10、命令总结

命令 | 描述 | 中文描述
-----|-----|-------
attach    |Attach to a running container                 | 当前 shell 下 attach 连接指定运行镜像 
build     |Build an image from a Dockerfile              | 通过 Dockerfile 定制镜像 
commit    |Create a new image from a container changes   | 提交当前容器为新的镜像 
cp        |Copy files/folders from the containers filesystem to the host path   |从容器中拷贝指定文件或者目录到宿主机中 
create    |Create a new container                        | 创建一个新的容器，同 run，但不启动容器 
diff      |Inspect changes on a container's filesystem   | 查看 docker 容器变化 
events    |Get real time events from the server          | 从 docker 服务获取容器实时事件 
exec      |Run a command in an existing container        | 在已存在的容器上运行命令 
export    |Stream the contents of a container as a tar archive   | 导出容器的内容流作为一个 tar 归档文件[对应 import ] 
history   |Show the history of an image                  | 展示一个镜像形成历史 
images    |List images                                   | 列出系统当前镜像 
import    |Create a new filesystem image from the contents of a tarball | 从tar包中的内容创建一个新的文件系统映像[对应export] 
info      |Display system-wide information               | 显示系统相关信息 
inspect   |Return low-level information on a container   | 查看容器详细信息 
kill      |Kill a running container                      | kill 指定 docker 容器 
load      |Load an image from a tar archive              | 从一个 tar 包中加载一个镜像[对应 save] 
login    | Register or Login to the docker registry server    | 注册或者登陆一个 docker 源服务器 
logout    |Log out from a Docker registry server          | 从当前 Docker registry 退出 
logs      |Fetch the logs of a container                 | 输出当前容器日志信息 
port      |Lookup the public-facing port which is NAT-ed to PRIVATE_PORT    | 查看映射端口对应的容器内部源端口 
pause     |Pause all processes within a container        | 暂停容器 
ps        |List containers                               | 列出容器列表 
pull      |Pull an image or a repository from the docker registry server   | 从docker镜像源服务器拉取指定镜像或者库镜像 
push      |Push an image or a repository to the docker registry server    | 推送指定镜像或者库镜像至docker源服务器 
restart   |Restart a running container                   | 重启运行的容器 
rm        |Remove one or more containers                 | 移除一个或者多个容器 
rmi       |Remove one or more images             | 移除一个或多个镜像[无容器使用该镜像才可删除，否则需删除相关容器才可继续或 -f 强制删除] 
run       |Run a command in a new container              | 创建一个新的容器并运行一个命令 
save      |Save an image to a tar archive                | 保存一个镜像为一个 tar 包[对应 load] 
search    |Search for an image on the Docker Hub         | 在 docker hub 中搜索镜像 
start     |Start a stopped containers                    | 启动容器 
stop      |Stop a running containers                     | 停止容器 
tag       |Tag an image into a repository                | 给源中镜像打标签 
top       |Lookup the running processes of a container   | 查看容器中运行的进程信息 
unpause   |Unpause a paused container                    | 取消暂停容器 
version   |Show the docker version information           | 查看 docker 版本号 
wait      |Block until a container stops, then print its exit code   | 截取容器停止时的退出状态值 

## 4、镜像

### 4.1、镜像是什么

镜像是一种轻量级、可执行的独立软件包，用来打包软件运行环境和基于运行环境开发的软件，它包含运行某个软件所需的所有内容，包括代码、运行时、库、环境变量和配置文件
- UnionFS（联合文件系统）

    Union文件系统（UnionFS）是一种分层、轻量级并且高性能的文件系统，它支持 对文件系统的修改作为一次提交来一层层的叠加， 同时可以将不同目录挂载到同一个虚拟文件系统下(unite several directories into a single virtual filesystem)。Union 文件系统是 Docker 镜像的基础。镜像可以通过分层来进行继承，基于基础镜像（没有父镜像），可以制作各种具体的应用镜像

    特性：一次同时加载多个文件系统，但从外面看起来，只能看到一个文件系统，联合加载会把各层文件系统叠加起来，这样最终的文件系统会包含所有底层的文件和目录 

- Docker镜像加载原理
    - bootfs(boot file system)主要包含bootloader和kernel, bootloader主要是引导加载kernel, Linux刚启动时会加载bootfs文件系统， 在Docker镜像的最底层是bootfs。 这一层与我们典型的Linux/Unix系统是一样的，包含boot加载器和内核。当boot加载完成之后整个内核就都在内存中了，此时内存的使用权已由bootfs转交给内核，此时系统也会卸载bootfs。 
    - rootfs (root file system) ，在bootfs之上 。包含的就是典型 Linux 系统中的 /dev, /proc, /bin, /etc 等标准目录和文件。rootfs就是各种不同的操作系统发行版，比如Ubuntu，Centos等等

- 分层的镜像：很多镜像都有多层镜像的；

- 为什么 Docker 镜像要采用这种分层结构呢：共享资源 
  
    比如：有多个镜像都从相同的 base 镜像构建而来，那么宿主机只需在磁盘上保存一份base镜像，同时内存中也只需加载一份 base 镜像，就可以为所有容器服务了。而且镜像的每一层都可以被共享

Docker镜像都是只读的当容器启动时，一个新的可写层被加载到镜像的顶部。 这一层通常被称作“容器层”，“容器层”之下的都叫“镜像层”。

## 5、容器数据卷

### 5.1、介绍

有点类似Redis里面的rdb和aof文件，主要是用来持久化

卷就是目录或文件，存在于一个或多个容器中，由docker挂载到容器，但不属于联合文件系统，因此能够绕过Union File System提供一些用于持续存储或共享数据的特性： 

卷的设计目的就是数据的持久化，完全独立于容器的生存周期，因此Docker不会在容器删除时删除其挂载的数据卷 

特点： 
- 数据卷可在容器之间共享或重用数据 
- 卷中的更改可以直接生效 
- 数据卷中的更改不会包含在镜像的更新中 
- 数据卷的生命周期一直持续到没有容器使用它为止 

### 5.2、挂载目录

#### 5.2.1、目录挂载

- 挂在目录：使用`-v /宿主机绝对路径目录:/容器内目录`，比如：`docker run -it -v /宿主机绝对路径目录:/容器内目录 镜像名`
- 查看数据卷是否挂载成功：`docker inspect 容器ID `：
    ```jsonc
    "Mounts": [
        {
            "Type": "bind",
            "Source": "/myvolumedata",
            "Destination": "/datavolume",
            "Mode": "",
            "RW": true,
            "Propagation": "rprivate"
        }
    ]
    ```
- 容器和宿主机之间数据共享
- 容器停止退出后，主机修改后数据是否同步，会主动同步宿主机的共享的内容；
- 只读：只允许主机单向写数据到容器内，`docker run -it -v /宿主机绝对路径目录:/容器内目录:ro 镜像名`，如果操作对应的目录下的数据会报错
    ``` 
    [root@83cbda361ad9 datavolume]## echo '' > container.txt
    bash: container.txt: Read-only file system
    ```

#### 5.2.2、DockerFile添加

- 根目录下新建mydocker文件夹并进入
- 可在Dockerfile中使用VOLUME指令来给镜像添加一个或多个数据卷：`VOLUME["/dataVolumeContainer","/dataVolumeContainer2","/dataVolumeContainer3"]`
- File构建：构建一个cokerfile，输入如下命令：
    ```
    ## volume test 
    FROM centos 
    VOLUME ["/dataVolumeContainer1","/dataVolumeContainer2"] 
    CMD echo "finished,--------success1" 
    CMD /bin/bash 
    ```
- build后生成一个新的镜像：`docker build -f /mydocker/mydockerfile -t test-centos .`，获得一个新镜像test-centos
- run容器：`docker run -it test-centos`
- 通过上述步骤，容器内的卷目录地址已经知道 对应的主机目录地址：通过`docker inspect containerid` 可以查看到对应的宿主机的目录地址
    ```json
    "Mounts": [
            {
                "Type": "volume",
                "Name": "898b4d22b54c65a896d1b9ca296edd065967ef434e72c197c2687e0a37e7cb13",
                "Source": "/var/lib/docker/volumes/898b4d22b54c65a896d1b9ca296edd065967ef434e72c197c2687e0a37e7cb13/_data",
                "Destination": "/dataVolumeContainer1",
                "Driver": "local",
                "Mode": "",
                "RW": true, ## 是否读写
                "Propagation": ""
            },
            {
                "Type": "volume",
                "Name": "75cfbeb7b137ada07e1298d7302eee4006d12f1d89d29fbd7006a588d6e7f04b",
                "Source": "/var/lib/docker/volumes/75cfbeb7b137ada07e1298d7302eee4006d12f1d89d29fbd7006a588d6e7f04b/_data",
                "Destination": "/dataVolumeContainer2",
                "Driver": "local",
                "Mode": "",
                "RW": true,
                "Propagation": ""
            }
        ]
    ```
- 主机对应默认地址

**注意：**Docker挂载主机目录Docker访问出现`cannot open directory .: Permission denied`，解决办法：在挂载目录后多加一个`--privileged=true`参数即可 

### 5.3、卷映射

`-v ngconf:/etc/nginx`，不以`/`开头：
```bash
docker run -d -p 99:80 \
-v /app/nghtml:/usr/share/nginx/html \
-v ngconf:/etc/nginx \
--name app03 nginx
```
数据卷的地址：`/var/lib/docker/volumes/<volume-name>`

### 5.4、数据卷容器

- 查看容器卷：`docker volume ls`
- 查看容器卷：`docker volume inspect <volume-name>`

命名的容器挂载数据卷，其它容器通过挂载这个(父容器)实现数据共享，挂载数据卷的容器，称之为数据卷容器

以上一步新建的镜像`test-centos`为模板并运行容器dc01/dc02/dc03，它们已经具有容器卷
- `/dataVolumeContainer1`
- `/dataVolumeContainer2`

**容器间传递共享(--volumes-from)**
- 先启动一个父容器dc01： 在dataVolumeContainer2新增内容
- dc02/dc03继承自dc01：--volumes-from，命令如下：
    ```
    docker run -it --name dc02 --volumes-from dc01 test-centos
    docker run -it --name dc03 --volumes-from dc01 test-centos
    ```
- 命令：dc02/dc03分别在dataVolumeContainer2各自新增内容
- 回到dc01可以看到02/03各自添加的都能共享了
- 删除dc01，dc02修改后dc03可否访问
- 删除dc02后dc03可否访问
- 新建dc04继承dc03后再删除dc03

**结论：容器之间配置信息的传递，数据卷的生命周期一直持续到没有容器使用它为止**

## 6、DockerFile

- [Dockerfile reference](https://docs.docker.com/reference/dockerfile/)

### 6.1、什么是dockerFile

Dockerfile是用来构建Docker镜像的构建文件，是由一系列命令和参数构成的脚本。构建的三个步骤：
- 编写Dockerfile文件
- docker build
- docker run

比如centos的dockerFile如下：
```
FROM scratch
ADD centos-7-x86_64-docker.tar.xz /

LABEL org.label-schema.schema-version="1.0" \
    org.label-schema.name="CentOS Base Image" \
    org.label-schema.vendor="CentOS" \
    org.label-schema.license="GPLv2" \
    org.label-schema.build-date="20191001"

CMD ["/bin/bash"]
```

基本内容如下：
- 每条保留字指令都必须为大写字母且后面要跟随至少一个参数
- 指令按照从上到下，顺序执行
- `#`表示注释
- 每条指令都会创建一个新的镜像层，并对镜像进行提交

### 6.2、构建过程

Docker执行Dockerfile的大致流程：
- （1）docker从基础镜像运行一个容器
- （2）执行一条指令并对容器作出修改
- （3）执行类似docker commit的操作提交一个新的镜像层
- （4）docker再基于刚提交的镜像运行一个新容器
- （5）执行dockerfile中的下一条指令直到所有指令都执行完成

Dockerfile、Docker镜像与Docker容器：

从应用软件的角度来看，Dockerfile、Docker镜像与Docker容器分别代表软件的三个不同阶段， 
* Dockerfile是软件的原材料 
* Docker镜像是软件的交付品 
* Docker容器则可以认为是软件的运行态。 

Dockerfile面向开发，Docker镜像成为交付标准，Docker容器则涉及部署与运维，三者缺一不可，合力充当Docker体系的基石。 
- Dockerfile，需要定义一个Dockerfile，Dockerfile定义了进程需要的一切东西。Dockerfile涉及的内容包括执行代码或者是文件、环境变量、依赖包、运行时环境、动态链接库、操作系统的发行版、服务进程和内核进程(当应用进程需要和系统服务和内核进程打交道，这时需要考虑如何设计namespace的权限控制)等等; 
- Docker镜像，在用Dockerfile定义一个文件之后，docker build时会产生一个Docker镜像，当运行 Docker镜像时，会真正开始提供服务; 
- Docker容器，容器是直接提供服务的。

### 6.3、DockerFile体系结构(保留字指令)

- FROM：基础镜像，当前新镜像是基于哪个镜像的

- MAINTAINER：镜像维护者的姓名和邮箱地址

- RUN：容器构建时需要运行的命令

- EXPOSE：当前容器对外暴露出的端口

- WORKDIR：指定在创建容器后，终端默认登陆的进来工作目录，一个落脚点

- ENV：用来在构建镜像过程中设置环境变量

- ADD：将宿主机目录下的文件拷贝进镜像且ADD命令会自动处理URL和解压tar压缩包

- COPY：类似ADD，拷贝文件和目录到镜像中。 将从构建上下文目录中 `<源路径> 的文件/目录复制到新的一层的镜像内的 <目标路径>` 位置

- VOLUME：容器数据卷，用于数据保存和持久化工作

- CMD：指定一个容器启动时要运行的命令，Dockerfile 中可以有多个 CMD 指令，但只有最后一个生效，CMD 会被 docker run 之后的参数替换

- ENTRYPOINT ：指定一个容器启动时要运行的命令，ENTRYPOINT 的目的和 CMD 一样，都是在指定容器启动程序及参数

- ONBUILD：当构建一个被继承的Dockerfile时运行命令，父镜像在被子继承后父镜像的onbuild被触发

### 6.4、如何编写DockerFile

Base镜像(scratch)：Docker Hub 中 99% 的镜像都是通过在 base 镜像中安装和配置需要的软件构建出来的

#### 6.4.1、编写自定义镜像centos

**（1）自定义镜像的要求：**
- 登陆后的默认路径 
- vim编辑器 
- 查看网络配置ifconfig支持 

**（2）编写dockerFile**
```
FROM centos
MAINTAINER bluefish<bluefish@126.com> 

ENV MYPATH /usr/local 
WORKDIR $MYPATH 

RUN yum -y install vim 
RUN yum -y install net-tools 

EXPOSE 80 

CMD echo $MYPATH 
CMD echo "success--------------ok" 
CMD /bin/bash 
```

**（3）构建镜像**

`docker build -t 新镜像名字:TAG .`

**（4）运行新的进行**

`docker run -it 新镜像名字:TAG `

**列出镜像的变更历史**

`docker history 镜像名`

#### 6.4.2、CMD/ENTRYPOINT 镜像案例

都是指定一个容器启动时要运行的命令
- CMD：Dockerfile 中可以有多个 CMD 指令，但只有最后一个生效，CMD 会被 docker run 之后的参数替换

- ENTRYPOINT：docker run 之后的参数会被当做参数传递给 ENTRYPOINT，之后形成新的命令组合

#### 6.4.3、自定义镜像tomcat9

**准备工作：**
- 创建tomcat9目录，比如：`mkdir -p /dockerfile/tomcat`
- 将jdk-8u171-linux-x64.tar.gz、apache-tomcat-9.0.8.tar.gz两个包移动到该目录下；
- 创建文件DockerFile，文件内容如下：
    ```bash
    FROM         centos
    MAINTAINER    bluefish<bluefish@126.com> 
    #把宿主机当前上下文的c.txt拷贝到容器/usr/local/路径下 
    COPY c.txt /usr/local/cincontainer.txt 
    #把java与tomcat添加到容器中 
    ADD jdk-8u171-linux-x64.tar.gz /usr/local/ 
    ADD apache-tomcat-9.0.8.tar.gz /usr/local/ 
    #安装vim编辑器 
    RUN yum -y install vim 
    #设置工作访问时候的WORKDIR路径，登录落脚点 
    ENV MYPATH /usr/local 
    WORKDIR $MYPATH 
    #配置java与tomcat环境变量 
    ENV JAVA_HOME /usr/local/jdk1.8.0_171 
    ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar 
    ENV CATALINA_HOME /usr/local/apache-tomcat-9.0.8 
    ENV CATALINA_BASE /usr/local/apache-tomcat-9.0.8 
    ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/lib:$CATALINA_HOME/bin 
    #容器运行时监听的端口 
    EXPOSE  8080 
    #启动时运行tomcat 
    ## ENTRYPOINT ["/usr/local/apache-tomcat-9.0.8/bin/startup.sh" ] 
    ## CMD ["/usr/local/apache-tomcat-9.0.8/bin/catalina.sh","run"] 
    CMD /usr/local/apache-tomcat-9.0.8/bin/startup.sh && tail -F /usr/local/apache-tomcat-9.0.8/bin/logs/catalina.out 
    ```
- 运行： `docker run -d -p 9080:8080 --name myt9 -v /root/mydockerfile/tomcat9/test:/usr/local/apache-tomcat-9.0.8/webapps/test -v /root/mydockerfile/tomcat9/tomcat9logs/:/usr/local/apache-tomcat-9.0.8/logs --privileged=true mytomcat9 `

## 7、docker网络通信

### 7.1、外部访问docker

通过 `-P` 或 `-p` 参数来指定端口映射，当使用 `-P` 标记时，Docker 会随机映射一个端口到内部容器开放的网络端口；
```bash
$ docker run -d -P nginx:alpine
$ docker container ls -l
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                   NAMES
fae320d08268        nginx:alpine        "/docker-entrypoint.…"   24 seconds ago      Up 20 seconds       0.0.0.0:32768->80/tcp   bold_mcnulty
```
使用 `docker container ls` 可以看到，本地主机的 32768 被映射到了容器的 80 端口。此时访问本机的 32768 端口即可访问容器内 NGINX 默认页面

`-p` 则可以指定要映射的端口，并且，在一个指定端口上只可以绑定一个容器。支持的格式有：
```bash
ip:hostPort:containerPort | ip::containerPort | hostPort:containerPort
```
- 映射所有接口地址：`hostPort:containerPort`，比如：`$ docker run -d -p 80:80 nginx:alpine`，此时默认会绑定本地所有接口上的所有地址；
- 映射到指定地址的指定端口：使用`ip:hostPort:containerPort`格式指定映射使用一个特定地址，比如 localhost 地址 127.0.0.1，`$ docker run -d -p 127.0.0.1:80:80 nginx:alpine`
- 映射到指定地址的任意端口：使用 `ip::containerPort` 绑定 localhost 的任意端口到容器的 80 端口，本地主机会自动分配一个端口。`$ docker run -d -p 127.0.0.1::80 nginx:alpine`，也可以使用UDP：`docker run -d -p 127.0.0.1:80:80/udp nginx:alpine`

查看端口映射：
```bash
$ docker port fa 80
0.0.0.0:32768
```
其中`fa`表示容器的名称

### 7.2、容器互联

使用 `--link` 参数来使容器互联，但是推荐做法是：将容器加入自定义的 Docker 网络来连接多个容器，创建自定义网络，实现主机名作为稳定域名访问（因为默认的 docker0 网络不支持主机名访问）

**新建网络：**
```bash
docker network create -d bridge --subnet=172.19.0.0/24 my-net
```
`-d` 参数指定 Docker 网络类型，有 `bridge`、`overlay`

**连接容器**
```bash
$ docker run -it --rm --name busybox1 --network my-net busybox sh
$ docker run -it --rm --name busybox2 --network my-net busybox sh
```
更建议使用 docker compose 来完成多个容器互联；

容器之间访问，使用的不是映射出去的端口，而是使用被映射的端口，比如下面：
```bash
docker run -d -p 88:80 --name app01 nginx
```
`-p 88:80`，其中`88`端口为docker主机暴露的端口，`80`是容器内部端口，也就是容器之间访问应该使用 80 端口


## 8、docker安装软件

- rabbitmq：`docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:mamagement`
- redis：`docker run -d --name redis -p 6379:6379 redis redis-server`
- mysql：`docker run -d -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=123456 mysql`

## 9、docker compose

- [Install Compose standalone](https://docs.docker.com/compose/install/standalone/)
- [docker compose 文件](https://docs.docker.com/compose/compose-file/)

### 9.1、安装

下载并安装：`curl -SL https://github.com/docker/compose/releases/download/v2.27.0/docker compose-linux-x86_64 -o /usr/local/bin/docker compose`

如果下载安装比较慢，可以先下载 [docker compose](https://github.com/docker/compose/releases/download/v2.27.0/docker compose-linux-x86_64)，然后执行如下命令：
```bash
root# mv docker compose-Linux-x86_64 /usr/local/bin/docker compose
root# chmod +x /usr/local/bin/docker compose
```
### 9.2、相关命令

```bash
#启动，加上 -d 是后台启动
docker compose up
#停止，但不会删除
docker compose stop  
# 停止并删除
docker compose down  
# 查看日志
docker compose logs -f
docker compose scale x2=3  扩容
# 删除镜像
docker compose down --rmi all
```

### 9.3、yaml语法

顶级元素：
- name：名字
- services：服务
- networks：网络
- volumes：卷
- configs：配置
- secrets：密钥
```yaml
name: myblog
services:
  mysql:
    container_name: mysql
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=123456
      - MYSQL_DATABASE=wordpress
    volumes:
      - mysql-data:/var/lib/mysql
      - /app/myconf:/etc/mysql/conf.d
    restart: always
    networks:
      - blog
  wordpress:
    image: wordpress
    ports:
      - "8080:80"
    environment:
      WORDPRESS_DB_HOST: mysql
      WORDPRESS_DB_USER: root
      WORDPRESS_DB_PASSWORD: 123456
      WORDPRESS_DB_NAME: wordpress
    volumes:
      - wordpress:/var/www/html
    restart: always
    networks:
      - blog
    depends_on:
      - mysql
volumes:
  mysql-data:
  wordpress:

networks:
  blog:
```

## 10、Portainer

- [Portainer-A docker and kubernets management tool](https://www.portainer.io/)

portainer是一款容器管理可视化界面，不想在虚拟中使用命令管理容器的小伙伴，可以选择安装portainer对容器进行管理，查看日志、启动、停止容器等非常方便。

### 1、安装

拉取镜像：
```bash
docker pull portainer/portainer:1.23.2
```
在单机上通过 docker 运行：
```bash
docker run -d -p 9000:9000 --name=portainer --restart=unless-stopped -v /var/run/docker.sock:/var/run/docker.sock -v portainer_db:/data portainer/portainer

-- 中文版本
docker pull 6053537/portainer-ce    #拉取镜像
docker run -d --restart=always --name="portainer" -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock 6053537/portainer-ce
```
docker run 参数说明:
- `-restart=unless-stopped` #在容器退出时总是重启容器，但是不考虑在Docker守护进程启动时就已经停止了的容器
- `-v /var/run/docker.sock:/var/run/docker.sock` #容器中的进程可以通过它与Docker守护进程进行通信
- `-d` #后台模式
- `--name portainer` #容器命名为portainer
- `--net=host` #启用主机网络

端口说明：9000 浏览器访问，8000 agent接入端口

### 2、重置密码

（1）首先查看portainer容器的挂载信息：
```bash
docker inspect d7e042925563
# 找到如下信息：
"HostConfig": {
    "Binds": [
        "/data/portainer/data:/data",
    ],
```
（2）停止该容器

（3）使用上面的挂载信息重置密码：
```bash
[root@root env]# docker run --rm -v /data/portainer/data:/data portainer/helper-reset-password
Unable to find image 'portainer/helper-reset-password:latest' locally
latest: Pulling from portainer/helper-reset-password
79916c70cb9e: Pull complete 
93e26fa95550: Pull complete 
Digest: sha256:735a809b1bfe14b5fae340d4b350bae97c2016371c47fb6e34d71a45e4512f79
Status: Downloaded newer image for portainer/helper-reset-password:latest
2023/10/13 02:09:35 Password succesfully updated for user: admin
2023/10/13 02:09:35 Use the following password to login: :}RP1ABjl02!caDn)Tw>(~C8/Q34Jv65
```
`:}RP1ABjl02!caDn)Tw>(~C8/Q34Jv65` 即为重置的密码

## 11、构建Docker私有仓库

常见的Docker私有仓库：
- [Harbor](https://goharbor.io/)：作为一个企业级的Docker Registry服务，Harbor提供了安全、可信赖的镜像存储和管理功能。它支持RBAC权限控制、镜像复制、镜像签名、漏洞扫描等功能。
- Docker Trusted Registry (DTR)：由Docker官方推出的企业级Docker私有仓库服务，与Docker Engine紧密集成，支持高度的安全性和可靠性。
- Portus：一个开源的Docker镜像管理和认证服务，提供用户管理、团队管理、镜像审核等功能，与Docker Registry兼容。
- Nexus Repository Manager：虽然主要是用于构建和管理Java组件，但也可以用作Docker私有仓库。它具有强大的存储管理和权限控制功能。
- GitLab Container Registry：GitLab集成了容器注册表功能，允许您存储、管理和分发Docker镜像。这是GitLab自带的功能，无需额外部署。
- AWS Elastic Container Registry (ECR)：如果使用AWS云服务，可以考虑使用AWS ECR作为私有仓库。它与AWS的其他服务集成紧密，对AWS用户来说是一个方便的选择。


# 参考资料

- [Docker cli command](https://docs.docker.com/reference/cli/docker/)
- [OrbStack 是运行 Docker 容器和 Linux 的快速、轻便且简单的方法](https://orbstack.dev/)
- [Docker 入门实战](https://github.com/yeasy/docker_practice)