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