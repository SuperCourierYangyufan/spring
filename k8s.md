#K8s
### 基本概念
1. 主要组成部分两个大部分master,node
    - master
        * api-server:集群统一入口,个个组件的协调者(restful),交给etcd存储
        * scheduler:节点调度,选择一个node,进行应用部署
        * controller-manager:统一控制,处理器中常规的后台任务,一个资源对应一个控制器
        * etcd:用于保存集群相关数据
    - node
        * kubelet:master派到node的节点代表,管理本机容器的各种操作 
        * kube-proxy:提供网络代理,能实现负载均衡等操作
2. pod:最小的部署单元,一组容器的集合
    - pod除了一个自己的根容器(pause)，还有多个我们部署的容器,每个自定义容器创建后会加入pause
    - pod是一个多进程,每个docker是个单进程,运行多个应用程序
    - 共享存储,存储到Volumn(数据卷),实现持久化的存储  
    - 一个pod中的容器是共享网络的,更加容易互相调用
    - 生命周期短暂的,重启服务就更换了
3. controller,主要就是创建pod
    - 取保预期pod副本的数量 
    - 部署有状态(特点环境部署),无状态应用(无特殊环境,任意node可以迁移)
    - 一次性任务,定时任务                
4. server:定义一组pod的访问规则
    - 服务发现功能,防止Pod失联
    - 负载均衡

### kubectl(酷不ctl)语法
1. 基本 kubectl 操作 类型 名字 可选参数
    - 操作 create,get,describe,delete
    - 类型 指定资源类型  
    - 名字 资源名称
    - 可选参数 可用-s指定server的地址和端口
2. kubectl --help
3. kubectl create deployment nginx --image=nginx
4. kubectl expose deployment nginx --port=80 --type=NodePort --target-port=80 --name=mynginx 
5. kubectl get pod,svc -o wide
6. kubectl get node 
7. kubectl label node node1 env_role=dev(分组)
8. kubectl apply -f *.yaml
9. kubectl set image deployment mynginx nginx=nginx:1.15(升级版本)
10. kubectl rollout status deployment mynginx(查看版本升级是否成功)
11. kubectl rollout history deployment mynginx(查看历史版本)
12. kubectl rollout undo deployment mynginx(回滚上个版本) 
13. kubectl rollout undo deployment mynginx --to-revision=1(回滚指定版本)
14. kubectl scale deployment mynginx --replicas=10(创建10个副本,进行扩容)
15. kubectl get jobs
16. kubectl delete pod mynginx
17. kubectl delete deployment mynginx
18. kubectl logs mynginx
19. kubectl create -f secret.yaml
20. kubectl exec -it mynginx bash
21. kubectl create configmap myredis.conf --from-file=redis.properties
22. kubectl get cm(查看configmap)
23. kubectl describe cm myredis.conf
24. kubectl get ns(查看命名空间)
25. kubectl create ns myrole(创建命名空间)


### yml-字段清单文件(两空格)
1. 组成部分
    - 控制器定义
    - 被控制的对象
2. 字段说明
    - apiVersion API版本
    - kind 资源类型
    - metadata 资源元数据
    - spec 资源规格
    - replicas 副本数量
    - selector 标签选择器
    - template Pod模板
    - metadata Pod元数据
    - spec Pod规格
    - containers 容器配置
3. 快速编写
    - kubectl create deployment web --image=nginx -o yaml --dry-run > m1.yaml
    - kubectl get deploy nginx -o=yaml --export > m1.yaml

### Pod
1. 默认特性
    - 可以进行资源限制,限制最小,最大的cpu,内存
    - 镜像有三种拉取策略,镜像在宿主机不存在时拉取(默认)|每次拉取新的|不主动拉取
    - Pod重启策略:当容器终止退出后,总是重启容器(默认)|当容器异常退出时,才重启|不重启
2. 健康检查,支持两种
    - 存活检查,检查失败,杀死容器,根据重启策略来判断是否重启
    - 就绪检查,检查失败,从Pod中剔除容器
3. 创建流程
    - 通过apiserver创建pod,存储到etcd
    - scheduler作为定时任务,监听新的pod,新的pod,通过apiserver,知道etcd
    - 通过调度算法,把pod调度到某个node上
    - 通过kubelet通过apiserver读取etcd,通过docker创建容器,把结果返回给etcd
4. 可以通过env_role进行节点分组,区分开发,测试,生产节点,部署时只会到对应节点
5. 亲和度就是节点条件,包括硬亲和性(必须满足),软亲和性(包含满足),是调度的属性
6. 污点和污点容忍,是节点的属性,不是调度的属性。节点不做普通分配,让特定的pod,部署到特定节点
    - 污点值有三个:一定不被调度|尽量不被调度| 不会调度,且驱逐Node已有的Pod
    - 污点容忍:设置为一定不被调度,设置yaml可以污点容忍,就可以调度 

### Server&Controller
1. server类型
    - ClusterIp:集群内部使用 
    - NodePort:对外暴露的服务
    - LoadBalancer:也是对外暴露的服务,公有云
2. deployment为无状态控制器,StatefulSet为有状态控制器,DaemonSet为守护进程
    - StatefulSet需要设置yaml中clusterIp:None
    - StatefulSet会为每个Pod创建唯一的名称
    - DaemonSet会为每一个node都执行这个Pod,新加入的也会执行
3. 任务分为job(一次性),cronjob(定时任务)

### 配置
1. Secret(c 亏 ri 特),加载数据存到etcd中,让Pod容器以挂载Volume方式进行访问,如存凭证等等
    - 创建Kind:Secret的yml,里面包含你需要存的信息,创建命令
    - 可以以变量,或者以数据卷挂载到pod容器
2. configMap存储不加密数据,加载到etcd中,同样可以以量,或者以数据卷挂载到pod容器,常用配置文件  
3. 访问k8s集群需要三个步骤,然后进行访问时需要进过apiserver进行统一协调
    - 认证
        * 传输安全,对位不暴露8080,只能内部访问,对外使用6443
        * 常用方式:HTTPS基于ca证书|httpToken|http用户密码
    - 鉴权
        * 基于RBAC(基于角色)控制访问
    - 准入控制
        * 若果列表有请求内容通过,没有拒绝
4. Ingress类似于zuul吧

### 安装步骤
```
kubeadm是官方社区推出的一个用于快速部署kubernetes集群的工具。

这个工具能通过两条指令完成一个kubernetes集群的部署：


# 创建一个 Master 节点
$ kubeadm init

# 将一个 Node 节点加入到当前集群中
$ kubeadm join <Master节点的IP和端口 >
`

## 1. 安装要求

在开始之前，部署Kubernetes集群机器需要满足以下几个条件：

- 一台或多台机器，操作系统 CentOS7.x-86_x64
- 硬件配置：2GB或更多RAM，2个CPU或更多CPU，硬盘30GB或更多
- 可以访问外网，需要拉取镜像，如果服务器不能上网，需要提前下载镜像并导入节点
- 禁止swap分区

## 2. 准备环境

| 角色   | IP           |
| ------ | ------------ |
| master | 172.20.1.203 |
| node1  | 172.20.1.202 |


# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld

# 关闭selinux
sed -i 's/enforcing/disabled/' /etc/selinux/config  # 永久
setenforce 0  # 临时

# 关闭swap
swapoff -a  # 临时
sed -ri 's/.*swap.*/#&/' /etc/fstab    # 永久

# 根据规划设置主机名
hostnamectl set-hostname <hostname>

# 在master添加hosts
cat >> /etc/hosts << EOF
172.20.1.203 master
172.20.1.202 node1
EOF

# 将桥接的IPv4流量传递到iptables的链
cat > /etc/sysctl.d/k8s.conf << EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
sysctl --system  # 生效

# 时间同步
yum install ntpdate -y
ntpdate time.windows.com


## 3. 所有节点安装Docker/kubeadm/kubelet

Kubernetes默认CRI（容器运行时）为Docker，因此先安装Docker。

### 3.1 安装Docker


$ wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo
$ yum -y install docker-ce-18.06.1.ce-3.el7
$ systemctl enable docker && systemctl start docker
$ docker --version
Docker version 18.06.1-ce, build e68fc7a



$ cat > /etc/docker/daemon.json << EOF
{
  "registry-mirrors": ["https://b9pmyelo.mirror.aliyuncs.com"]
}
EOF

$ systemctl restart docker


### 3.2 添加阿里云YUM软件源


$ cat > /etc/yum.repos.d/kubernetes.repo << EOF
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF


### 3.3 安装kubeadm，kubelet和kubectl

由于版本更新频繁，这里指定版本号部署：


$ yum install -y kubelet-1.18.0 kubeadm-1.18.0 kubectl-1.18.0
$ systemctl enable kubelet


## 4. 部署Kubernetes Master

在172.20.1.203（Master）执行。,记得修改参数apiserver-advertise-address


$ kubeadm init  --apiserver-advertise-address=172.20.1.203  --image-repository registry.aliyuncs.com/google_containers --kubernetes-version v1.18.0  --service-cidr=10.96.0.0/12  --pod-network-cidr=10.244.0.0/16


由于默认拉取镜像地址k8s.gcr.io国内无法访问，这里指定阿里云镜像仓库地址。

使用kubectl工具：


mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
$ kubectl get nodes


## 5. 加入Kubernetes Node

在172.20.1.203（Node）执行。

向集群添加新节点，执行在kubeadm init输出的kubeadm join命令：记得换hash值，来自maser创建时给的


$ kubeadm join 172.20.1.203:6443 --token swcemm.8r0mxxpo5zgj4tp1  --discovery-token-ca-cert-hash sha256:35966e92e6233ed4e747c11dcb02d9dbf6ac9819dbf086b6e5016816d48d22e6


默认token有效期为24小时，当过期之后，该token就不可用了。这时就需要重新创建token，操作如下：


kubeadm token create --print-join-command


## 6. 部署CNI网络插件(maser)


wget https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml


默认镜像地址无法访问，sed命令修改为docker hub镜像仓库。


vim /etc/hosts
185.199.109.133 raw.githubusercontent.com
wq

kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml

kubectl get pods -n kube-system
NAME                          READY   STATUS    RESTARTS   AGE
kube-flannel-ds-amd64-2pc95   1/1     Running   0          72s


## 7. 测试kubernetes集群

在Kubernetes集群中创建一个pod，验证是否正常运行：

$ kubectl create deployment nginx --image=nginx
$ kubectl expose deployment nginx --port=80 --type=NodePort
$ kubectl get pod,svc

访问地址：http://NodeIP:Port  





```