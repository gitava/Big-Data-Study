### Hive - .hiverc - avoid mapreduce

```sql
-- Furthermore, Hive will attempt to run other operations in local mode if the 'hive.exec.mode.local.auto' property is set to true:
-- Otherwise, Hive uses MapReduce to run all other queries.
-- Trust us, you want to add set hive.exec.mode.local.auto=true; to your $HOME/.hiverc file.
set hive.exec.mode.local.auto=true;
```

