# [vagrant@hdp-node-01 ~]$ chmod +755 start_hadoop.sh
# [vagrant@hdp-node-01 ~]$ chmod +755 stop_hadoop.sh
# [vagrant@hdp-node-01 ~]$ ls -l *sh
# -rwxrwxr-x. 1 vagrant vagrant 141 Jun 24 01:13 start_hadoop.sh
# -rwxrwxr-x. 1 vagrant vagrant 137 Jun 24 01:13 stop_hadoop.sh


sudo $HADOOP_HOME/sbin/mr-jobhistory-daemon.sh stop historyserver
sudo $HADOOP_HOME/sbin/stop-yarn.sh
sudo $HADOOP_HOME/sbin/stop-dfs.sh