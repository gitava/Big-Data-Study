

```bash
s='57752 \"http://f.dataguru.cn/forum-58-1.html\" \"Mozilla/5.0 (iPhone; CPU iPhone OS 5_0_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9A406\"'

# need to remove end of line before appending the line
while read line ;do
   echo ${line%} $s
done < access_short.log >access_out.log
```

