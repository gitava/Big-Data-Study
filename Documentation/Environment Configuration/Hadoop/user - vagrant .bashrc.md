HDP1 server

```bash
# .bashrc

# Source global definitions
if [ -f /etc/bashrc ]; then
    . /etc/bashrc
fi

# Uncomment the following line if you don't like systemctl's auto-paging feature:
# export SYSTEMD_PAGER=

# User specific aliases and functions
export HADOOP_HOME=/home/vagrant/hadoop
export JAVA_HOME=/home/vagrant/jdk
export HIVE_HOME=/home/vagrant/hive

#export PATH=/usr/local/sbin:/sbin:/bin:/usr/sbin:/usr/bin:/root/bin/:/home/vagrant/hadoop/bin/:/home/vagrant/hadoop/bin/:$PATH

PATH=/home/vagrant/hadoop/bin/:$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$HOME/pig/bin:$HIVE_HOME/bin:$PATH

# add the following one to run jps under openjdk
# alias jps='/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.252.b09-2.el7_8.x86_64/bin/jps'

# Add PIG_CLASSPATH for pig installation
export PIG_CLASSPATH=/home/vagrant/hadoop/etc/hadoop
export PIG_USER_CLASSPATH_FIRST=/home/vagrant/pig/lib
export CLASSPATH=$JAVA_HOME/lib:$JAVA_HOME/jre/lib:$HIVE_HOME/lib:$CLASSPATH
```