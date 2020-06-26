# 安装配置pig -- 0.17.0

* 下载解压安装包 （https://mirrors.bfsu.edu.cn/apache/pig/pig-0.17.0/）
* 修改hosts文件 （/etc/hosts）
* 设置PATH，增加指向hadoop/bin（~/.bash_profile）
* 设置PIG_CLASSPATH环境变量（~/.bash_profile）
* 启动pig

### 复制Pig安装文件到虚拟机

```bash
scp pig-0.17.0.tar.gz vagrant@192.168.33.101:/home/vagrant/
```

### 解压目录

```bash
[vagrant@hdp-node-01 ~]$ tar -zxvf pig-0.17.0.tar.gz
...
[vagrant@hdp-node-01 ~]$ ls -l
...
drwxr-xr-x. 16 vagrant vagrant      4096 Jun  2  2017 pig-0.17.0
-rwxrwxr-x.  1 vagrant vagrant 230606579 Jun 26 09:14 pig-0.17.0.tar.gz
...
[vagrant@hdp-node-01 ~]$ rm pig-0.17.0.tar.gz

# Convinient for the future upgrade
[vagrant@hdp-node-01 ~]$ ln -s pig-0.17.0 pig
[vagrant@hdp-node-01 ~]$ ls -l

lrwxrwxrwx.  1 vagrant vagrant      10 Jun 26 09:16 pig -> pig-0.17.0
drwxr-xr-x. 16 vagrant vagrant    4096 Jun  2  2017 pig-0.17.0

```

### 修改hosts文件

```bash
[vagrant@hdp-node-01 ~]$ cat /etc/hosts

#127.0.0.1	hdp-node-01	hdp-node-01
#127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

192.168.33.101  hdp-node-01
192.168.33.102  hdp-node-02
192.168.33.103  hdp-node-03
192.168.33.104  hdp-node-04
```

## 设置PATH，增加指向hadoop/bin

```bash
# Add the following line to  (~/.bash_profile)
PATH=$PATH:$HOME/.local/bin:$HOME/bin:$HOME/pig/bin
```

Check if JAVA_HOME has been set yet.

### 设置PIG_CLASSPATH环境变量

设置完成后重新登录使环境变量生效 (~/.bash_profile)

```bash
# Add the following line to  (~/.bash_profile)
export PIG_CLASSPATH=/home/vagrant/hadoop/etc/hadoop
export PIG_USER_CLASSPATH_FIRST=/home/vagrant/pig/lib
```

make the change effective

```bash
[vagrant@hdp-node-01 ~]$ source .bash_profile
```

On CentOS, env can be used to review all environment variables.

### 启动grunt shell

```bash
[vagrant@hdp-node-01 ~]$ pig
```

### Issues and fixes (#5)

copy the following files from /home/vagrant/pig/lib to /home/vagrant/hadoop/share/hadoop/common

* joda-time-2.9.3.jar
* jline-2.11.jar
* antlr-runtime-3.4.jar

```bash
[vagrant@hdp-node-01 common]$ sudo chown 501:joda-time-2.9.3.jar
[vagrant@hdp-node-01 common]$ sudo chown 501:dialout jline-2.11.jar
[vagrant@hdp-node-01 common]$ sudo chown 501:dialout antlr-runtime-3.4.jar
```

```bash
[vagrant@hdp-node-01 common]$ pwd
/home/vagrant/hadoop/share/hadoop/common
[vagrant@hdp-node-01 common]$ ls -l
total 7556
-rw-r--r--. 1 501 dialout  164368 Jun 26 11:37 antlr-runtime-3.4.jar
-rw-r--r--. 1 501 dialout 2619447 Nov 13  2018 hadoop-common-2.9.2-tests.jar
-rw-r--r--. 1 501 dialout 3906902 Nov 13  2018 hadoop-common-2.9.2.jar
-rw-r--r--. 1 501 dialout  188721 Nov 13  2018 hadoop-nfs-2.9.2.jar
drwxr-xr-x. 2 501 dialout    4096 Nov 13  2018 jdiff
-rw-r--r--. 1 501 dialout  208781 Jun 26 11:36 jline-2.11.jar
-rw-r--r--. 1 501 dialout  627814 Jun 26 11:36 joda-time-2.9.3.jar
drwxr-xr-x. 2 501 dialout    4096 Nov 13  2018 lib
drwxr-xr-x. 2 501 dialout      89 Nov 13  2018 sources
drwxr-xr-x. 2 501 dialout      27 Nov 13  2018 templates
[vagrant@hdp-node-01 common]$
```



### 进入grunt shell -- Pig工作模式

```
pig -x local
```



```bash
[vagrant@hdp-node-01 ~]$ pig -x local
20/06/26 11:49:26 INFO pig.ExecTypeProvider: Trying ExecType : LOCAL
20/06/26 11:49:26 INFO pig.ExecTypeProvider: Picked LOCAL as the ExecType
2020-06-26 11:49:26,607 [main] INFO  org.apache.pig.Main - Apache Pig version 0.17.0 (r1797386) compiled Jun 02 2017, 15:41:58
2020-06-26 11:49:26,607 [main] INFO  org.apache.pig.Main - Logging error messages to: /home/vagrant/pig_1593172166606.log
2020-06-26 11:49:26,646 [main] INFO  org.apache.pig.impl.util.Utils - Default bootup file /home/vagrant/.pigbootup not found
2020-06-26 11:49:26,786 [main] INFO  org.apache.hadoop.conf.Configuration.deprecation - mapred.job.tracker is deprecated. Instead, use mapreduce.jobtracker.address
2020-06-26 11:49:26,788 [main] INFO  org.apache.pig.backend.hadoop.executionengine.HExecutionEngine - Connecting to hadoop file system at: file:///
2020-06-26 11:49:27,014 [main] INFO  org.apache.hadoop.conf.Configuration.deprecation - io.bytes.per.checksum is deprecated. Instead, use dfs.bytes-per-checksum
2020-06-26 11:49:27,032 [main] INFO  org.apache.pig.PigServer - Pig Script ID for the session: PIG-default-fc0958d7-347b-485e-9523-bde028295f58
2020-06-26 11:49:27,032 [main] WARN  org.apache.pig.PigServer - ATS is disabled since yarn.timeline-service.enabled set to false
grunt>
```

### 进入grunt shell -- 3pig的map-reduce模式

1. 启动hadoop

```bash
sudo $HADOOP_HOME/sbin/start-dfs.sh
sudo $HADOOP_HOME/sbin/start-yarn.sh
sudo $HADOOP_HOME/sbin/mr-jobhistory-daemon.sh start historyserver
```

2. Pig命令

```
pig
```

```bash
[vagrant@hdp-node-01 ~]$ pig
20/06/26 11:58:40 INFO pig.ExecTypeProvider: Trying ExecType : LOCAL
20/06/26 11:58:40 INFO pig.ExecTypeProvider: Trying ExecType : MAPREDUCE
20/06/26 11:58:40 INFO pig.ExecTypeProvider: Picked MAPREDUCE as the ExecType
2020-06-26 11:58:40,433 [main] INFO  org.apache.pig.Main - Apache Pig version 0.17.0 (r1797386) compiled Jun 02 2017, 15:41:58
2020-06-26 11:58:40,433 [main] INFO  org.apache.pig.Main - Logging error messages to: /home/vagrant/pig_1593172720417.log
2020-06-26 11:58:40,481 [main] INFO  org.apache.pig.impl.util.Utils - Default bootup file /home/vagrant/.pigbootup not found
2020-06-26 11:58:41,021 [main] INFO  org.apache.hadoop.conf.Configuration.deprecation - mapred.job.tracker is deprecated. Instead, use mapreduce.jobtracker.address
2020-06-26 11:58:41,021 [main] INFO  org.apache.pig.backend.hadoop.executionengine.HExecutionEngine - Connecting to hadoop file system at: hdfs://hdp-node-01:9000
2020-06-26 11:58:41,855 [main] INFO  org.apache.pig.PigServer - Pig Script ID for the session: PIG-default-185d39de-f1d5-46c0-81c2-ca71e3c9bc3d
2020-06-26 11:58:41,855 [main] WARN  org.apache.pig.PigServer - ATS is disabled since yarn.timeline-service.enabled set to false
grunt>
```

### 简单测试

```bash
grunt> ls /
hdfs://hdp-node-01:9000/input	<dir>
hdfs://hdp-node-01:9000/output	<dir>
hdfs://hdp-node-01:9000/temp	<dir>
hdfs://hdp-node-01:9000/tmp	<dir>
hdfs://hdp-node-01:9000/user	<dir>
hdfs://hdp-node-01:9000/wordcount	<dir>
grunt> ls /wordcount
hdfs://hdp-node-01:9000/wordcount/input	<dir>
hdfs://hdp-node-01:9000/wordcount/output	<dir>
grunt> ls /wordcount/input
hdfs://hdp-node-01:9000/wordcount/input/somewords.txt<r 3>	60
grunt> cat /wordcount/input/somewords.txt
liu 1
wang 2
sun 3
zhao 4
li 5
wu 6
mu 7
guo 8
han 9
lin 10

#system command by sh 
grunt> sh ls /home
vagrant
grunt> quit
2020-06-26 12:00:33,009 [main] INFO  org.apache.pig.Main - Pig script completed in 1 minute, 52 seconds and 816 milliseconds (112816 ms)
```

### 安装结束！！