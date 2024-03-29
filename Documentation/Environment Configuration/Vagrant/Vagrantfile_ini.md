This is the one to be used for the first time provision.

Usage:

* copy the following info into a Vagrant file.
* run vagrant command as described orginally.

```shell
# -*- mode: ruby -*-
# vi: set ft=ruby :

# Version: Vagrantfile_ini

#
#this is for building master
#
#author:lhy
#time:2018.8.25
#
#revised for hadoop study 
#with 1 master and 3 slaves
#time:2020.5.27
$clusters_script = <<-SCRIPT
#!/bin/bash

# at /home/vagrant
#---hosts---
# To work with eclipse-hadoop-plugin,
# needs to comment-out 127.0.0.1 part on hdp-node-01
# will edit this file later to add it.
cat >> /etc/hosts <<EOF

192.168.33.101  hdp-node-01
192.168.33.102  hdp-node-02
192.168.33.103  hdp-node-03
192.168.33.104  hdp-node-04

EOF
#---env---
cat >> /root/.bashrc <<EOF
export HADOOP_HOME=/home/vagrant/hadoop
export JAVA_HOME=/home/vagrant/jdk
#add Java bin to environment
export PATH=$PATH/:/home/vagrant/hadoop/bin/:$JAVA_HOME/bin
EOF
source /root/.bashrc

cat >> .bashrc <<EOF
export HADOOP_HOME=/home/vagrant/hadoop
export JAVA_HOME=/home/vagrant/jdk
#add Java bin to environment
export PATH=$PATH:/home/vagrant/hadoop/bin/:$JAVA_HOME/bin
EOF
source .bashrc


#---hadoop---
tar -zxf hadoop-2.9.2.tar.gz
mv hadoop-2.9.2 hadoop
mv env/*  hadoop/etc/hadoop/
rm  hadoop-2.9.2.tar.gz
# add classpath to work with hadoop-env.sh on 20200622
mkdir /home/vagrant/myclass

#---jdk---
tar -xf jdk-7u80-linux-x64.tar
mv jdk1.7.0_80 jdk
rm jdk-7u80-linux-x64.tar


#---ssh---
mv /home/vagrant/sshd_config /etc/ssh/sshd_config
systemctl restart sshd.service

SCRIPT


Vagrant.configure("2") do |config|

		(1..4).each do |i|
		config.vm.define "hdp#{i}" do |node|
	
		# 设置虚拟机的Box
		node.vm.box = "centos/7"
		# 设置虚拟机的主机名
		node.vm.hostname="hdp-node-0#{i}"
		# 设置虚拟机的IP
		node.vm.network "private_network", ip: "192.168.33.#{100+i}"

		# 设置主机与虚拟机的共享目录
		#node.vm.synced_folder "~/Desktop/share", "/home/vagrant/share"
		# 复制相应的依赖文件
		config.vm.provision "file", source: "./jdk-7u80-linux-x64.tar", destination: "/home/vagrant/jdk-7u80-linux-x64.tar"
		config.vm.provision "file", source: "./hadoop-2.9.2.tar.gz", destination: "/home/vagrant/hadoop-2.9.2.tar.gz"
		config.vm.provision "file", source: "./sshd/sshd_config", destination: "/home/vagrant/sshd_config"
		config.vm.provision "file", source: "./conf", destination: "/home/vagrant/env"
		config.vm.provision "file", source: "./test", destination: "/home/vagrant/test"

		# VirtaulBox相关配置
		node.vm.provider "virtualbox" do |v|
			# 设置虚拟机的名称
			v.name = "hdp#{i}"
			# 设置虚拟机的内存大小  
			v.memory = 1024
			# 设置虚拟机的CPU个数
			v.cpus = 1
		end
		node.vm.provision "shell", inline: $clusters_script # 使用shell脚本进行软件安装和配置
		end
	end
end

```

