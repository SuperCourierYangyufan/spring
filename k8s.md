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
2. pod:最小的部署单元,一组容器的集合,一个pod中的容器是共享网络的,生命周期短暂的,重启服务就更换了
3. controller,主要就是创建pod
    - 取保预期pod副本的数量 
    - 部署有状态(特点环境部署),无状态应用(无特殊环境,任意node可以迁移)
    - 一次性任务,定时任务                
4. server:定义一组pod的访问规则

### kubectl(酷不ctl)语法
1. 基本 kubectl 操作 类型 名字 可选参数
    - 操作 create,get,describe,delete
    - 类型 指定资源类型  
    - 名字 资源名称
    - 可选参数 可用-s指定server的地址和端口
2. kubectl --help
3. kubectl create deployment nginx --image=nginx
4. kubectl export deployment nginx --port=80 --type=NodePort
5. kubectl get pod,svc
6. kubectl get node 

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