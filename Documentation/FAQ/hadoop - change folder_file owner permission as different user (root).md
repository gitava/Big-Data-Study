run change as root hadoop user.

```
[vagrant@hdp-node-01 ~]$ hdfs dfs -chown vagrant:supergroup /tmp
chown: changing ownership of '/tmp': Permission denied. user=hive is not the owner of inode=/tmp

# tell hadoop it's root
[vagrant@hdp-node-01 ~]$ export HADOOP_USER_NAME=root


[vagrant@hdp-node-01 ~]$ hdfs dfs -chown vagrant:supergroup /tmp
[vagrant@hdp-node-01 ~]$ hadoop hdfs -ls /
Error: Could not find or load main class hdfs
[vagrant@hdp-node-01 ~]$ hdfs dfs -ls /
Found 6 items
drwxr-xr-x   - fllbeaver supergroup          0 2020-06-24 03:13 /input
drwxr-xr-x   - fllbeaver supergroup          0 2020-06-24 03:33 /output
drwxr-xr-x   - fllbeaver supergroup          0 2020-06-24 02:28 /temp
drwx------   - vagrant   supergroup          0 2020-06-28 06:09 /tmp
drwxr-xr-x   - vagrant   supergroup          0 2020-06-28 06:40 /user
drwxr-xr-x   - root      supergroup          0 2020-05-27 12:31 /wordcount
[vagrant@hdp-node-01 ~]$ hdfs dfs -chmod 755 /tmp
[vagrant@hdp-node-01 ~]$ hdfs dfs -ls /
Found 6 items
drwxr-xr-x   - fllbeaver supergroup          0 2020-06-24 03:13 /input
drwxr-xr-x   - fllbeaver supergroup          0 2020-06-24 03:33 /output
drwxr-xr-x   - fllbeaver supergroup          0 2020-06-24 02:28 /temp
drwxr-xr-x   - vagrant   supergroup          0 2020-06-28 06:09 /tmp
drwxr-xr-x   - vagrant   supergroup          0 2020-06-28 06:40 /user
drwxr-xr-x   - root      supergroup          0 2020-05-27 12:31 /wordcount
```