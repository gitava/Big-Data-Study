HDP1 server

```bash
[vagrant@hdp-node-01 ~]$cat .bashrc
# .bashrc

# Source global definitions
if [ -f /etc/bashrc ]; then
    . /etc/bashrc
fi

# Uncomment the following line if you don't like systemctl's auto-paging feature:
# export SYSTEMD_PAGER=

# change prompt
export PS1='[\u@\H \w]\$'

# User specific aliases and functions
export HADOOP_HOME=/home/vagrant/hadoop
export JAVA_HOME=/home/vagrant/jdk
export HIVE_HOME=/home/vagrant/hive
# HBase configuration
export HBASE_HOME=/home/vagrant/hbase


#export PATH=/usr/local/sbin:/sbin:/bin:/usr/sbin:/usr/bin:/root/bin/:/home/vagrant/hadoop/bin/:/home/vagrant/hadoop/bin/:$PATH

PATH=/home/vagrant/hadoop/bin/:$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$HOME/pig/bin:$HIVE_HOME/bin:$HBASE_HOME/bin:$PATH

# Add PIG_CLASSPATH for pig installation
export PIG_CLASSPATH=/home/vagrant/hadoop/etc/hadoop
export PIG_USER_CLASSPATH_FIRST=/home/vagrant/pig/lib
export CLASSPATH=$JAVA_HOME/lib:$JAVA_HOME/jre/lib:/home/vagrant/hadoop/lib:/home/vagrant/hadoop/share/hadoop:$HIVE_HOME/lib:$CLASSPATH
```