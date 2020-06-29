



```
# create new file
[vagrant@hdp-node-01 tmp]$ cat > hosts <<EOF
> aa
> bb
> cc
> EOF

[vagrant@hdp-node-01 tmp]$ cat hosts
aa
bb
cc

# append contents to the file
[vagrant@hdp-node-01 tmp]$ cat >>hosts <<eof
> kk
> dd
> ss
> eof
[vagrant@hdp-node-01 tmp]$ cat hosts
aa
bb
cc
kk
dd
ss
```

