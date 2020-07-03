- ## 环境配置 -- 配置免密码ssh

  

  - 在实施安装之前的另一准备工作是配置ssh，生成密钥，使到各节点之间可以使用ssh免密码连接

  - 本步骤很关键
  - 要注意密钥文件的**权限字**
  - 问题：只配namenode<->datanode的免密码是否就可以？

  ### 生成ssh密钥对

  注意要以vagrant用户登录，在vagrant用户的主目录下进行操作！

  每个节点作相同操作

  ```shell
  [vagrant@hdp-node-01 ~/.ssh]$ssh-keygen -t rsa -f ~/.ssh/id_rsa
  
  Generating public/private rsa key pair.
  Enter passphrase (empty for no passphrase):
  Enter same passphrase again:
  Your identification has been saved in /home/vagrant/.ssh/id_rsa.
  Your public key has been saved in /home/vagrant/.ssh/id_rsa.pub.
  The key fingerprint is:
  SHA256:8QWaP5Sm5mHLxsjMI+ZYpyYILcIMXfuiE3D06Vmjd20 vagrant@hdp-node-01
  The key's randomart image is:
  ...
  
  [vagrant@hdp-node-01 ~/.ssh]$ls -la
  total 20
  drwx------.  2 vagrant vagrant   80 Jul  2 08:38 .
  drwx------. 17 vagrant vagrant 4096 Jul  2 04:58 ..
  -rw-------.  1 vagrant vagrant  389 Jun 24 07:18 authorized_keys
  -rw-------.  1 vagrant vagrant 1679 Jul  2 08:38 id_rsa
  -rw-r--r--.  1 vagrant vagrant  401 Jul  2 08:38 id_rsa.pub
  -rw-r--r--.  1 vagrant vagrant  564 Jul  2 06:17 known_hosts
  [vagrant@hdp-node-01 ~/.ssh]$ll
  total 16
  -rw-------. 1 vagrant vagrant  389 Jun 24 07:18 authorized_keys
  -rw-------. 1 vagrant vagrant 1679 Jul  2 08:38 id_rsa
  -rw-r--r--. 1 vagrant vagrant  401 Jul  2 08:38 id_rsa.pub
  -rw-r--r--. 1 vagrant vagrant  564 Jul  2 06:17 known_hosts
  [vagrant@hdp-node-01 ~/.ssh]$
  ```

  密钥文件内容：私钥 -  id_rsa

  密钥文件内容：公钥 -  id_rsa.pub

  ## 分发ssh公钥

  把各个节点的**authorized_keys（权限644）**的内容互相拷贝加入到对方的**此文件**中，然后就可以免密码彼此ssh连入。

  适当复制hostname部分为ip，这样可以同时对hostname和ip的ssh免密。

  ```shell
  cd /home/vagrant/.ssh
  chmod 644 authorized_keys
  
  #introduced nfs mount file system to 3 servers
  cat id_rsa.pub >> /mnt/nfs-data/authorized_keys
  
  cat /mnt/nfs-data/authorized_keys >> authorized_keys
  
  ```

  