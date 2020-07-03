https://community.cloudera.com/t5/Support-Questions/HBase-Region-in-Transition/td-p/26703



```
[vagrant@hdp-node-01 ~/hbase]$sudo ./bin/hbase hbck -repair
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
2020-07-02 12:43:54,774 WARN  [main] util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
2020-07-02 12:43:57,530 INFO  [main] zookeeper.RecoverableZooKeeper: Process identifier=hbase Fsck connecting to ZooKeeper ensemble=hdp-node-01:2181,hdp-node-02:2181,hdp-node-03:2181
HBaseFsck command line options: -repair
2020-07-02 12:43:58,062 INFO  [main] util.HBaseFsck: Launching hbck
2020-07-02 12:43:58,455 INFO  [main] zookeeper.RecoverableZooKeeper: Process identifier=hconnection-0x6fd1046d connecting to ZooKeeper ensemble=hdp-node-01:2181,hdp-node-02:2181,hdp-node-03:2181
```

