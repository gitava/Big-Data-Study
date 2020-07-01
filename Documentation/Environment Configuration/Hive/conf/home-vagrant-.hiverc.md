Hive will attempt to run other operations in local mode if the 'hive.exec.mode.local.auto' property is set to true:

Otherwise, Hive uses MapReduce to run all other queries.

 Trust us, you want to add set hive.exec.mode.local.auto=true; to your $HOME/.hiverc file.

This file can't accept comments like starting with # ...

```bash
set hive.exec.mode.local.auto=true;
```

