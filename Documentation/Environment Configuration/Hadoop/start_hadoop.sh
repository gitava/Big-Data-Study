# [vagrant@hdp-node-01 ~]$ chmod +755 start_hadoop.sh
# [vagrant@hdp-node-01 ~]$ chmod +755 stop_hadoop.sh
# [vagrant@hdp-node-01 ~]$ ls -l *sh
# -rwxrwxr-x. 1 vagrant vagrant 141 Jun 24 01:13 start_hadoop.sh
# -rwxrwxr-x. 1 vagrant vagrant 137 Jun 24 01:13 stop_hadoop.sh

sudo $HADOOP_HOME/sbin/start-dfs.sh
sudo $HADOOP_HOME/sbin/start-yarn.sh
sudo $HADOOP_HOME/sbin/mr-jobhistory-daemon.sh start historyserver

echo "Show running java processes by jps under root ..."
sudo $JAVA_HOME/bin/jps