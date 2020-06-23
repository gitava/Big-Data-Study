## Hadoop 启动和停止 （ref Book）

To start the HDFS, YARN, and MapReduce daemons, type:

```bash
sudo $HADOOP_HOME/sbin/start-dfs.sh
sudo $HADOOP_HOME/sbin/start-yarn.sh
sudo $HADOOP_HOME/sbin/mr-jobhistory-daemon.sh start historyserver
```

Stopping the daemons is done as follows:

```bash
sudo $HADOOP_HOME/sbin/mr-jobhistory-daemon.sh stop historyserver
sudo $HADOOP_HOME/sbin/stop-yarn.sh
sudo $HADOOP_HOME/sbin/stop-dfs.sh
```

##### NOTE

If you have placed configuration files outside the default conf directory, either export the HADOOP_CONF_DIR environment variable before running the scripts, or start the daemons with the --config option, which takes an absolute path to the configuration directory:

```bash
% start-dfs.sh --config path-to-config-directory
% start-yarn.sh --config path-to-config-directory
% mr-jobhistory-daemon.sh --config path-to-config-directory start historyserver
```

Example：

```bash
[vagrant@hdp-node-01 ~]$ sudo $HADOOP_HOME/sbin/start-dfs.sh
Starting namenodes on [hdp-node-01]
hdp-node-01: starting namenode, logging to /home/vagrant/hadoop/logs/hadoop-root-namenode-hdp-node-01.out
hdp-node-01: starting datanode, logging to /home/vagrant/hadoop/logs/hadoop-root-datanode-hdp-node-01.out
hdp-node-04: starting datanode, logging to /home/vagrant/hadoop/logs/hadoop-root-datanode-hdp-node-04.out
...
Starting secondary namenodes [hdp-node-01]
hdp-node-01: starting secondarynamenode, logging to /home/vagrant/hadoop/logs/hadoop-root-secondarynamenode-hdp-node-01.out

[vagrant@hdp-node-01 ~]$ sudo $HADOOP_HOME/sbin/start-yarn.sh
starting yarn daemons
starting resourcemanager, logging to /home/vagrant/hadoop/logs/yarn-root-resourcemanager-hdp-node-01.out
hdp-node-02: starting nodemanager, logging to /home/vagrant/hadoop/logs/yarn-root-nodemanager-hdp-node-02.out
hdp-node-04: starting nodemanager, logging to /home/vagrant/hadoop/logs/yarn-root-nodemanager-hdp-node-04.out
...
[vagrant@hdp-node-01 ~]$ sudo $HADOOP_HOME/sbin/mr-jobhistory-daemon.sh start historyserver
starting historyserver, logging to /home/vagrant/hadoop/logs/mapred-root-historyserver-hdp-node-01.out
```

