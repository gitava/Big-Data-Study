



```shll
# Mount NFS from Mac
#
#
# [vagrant@hdp-node-01 ~]$ cat /etc/fstab

#
# /etc/fstab
# Created by anaconda on Thu Apr 30 22:04:55 2020
#
# Accessible filesystems, by reference, are maintained under '/dev/disk'
# See man pages fstab(5), findfs(8), mount(8) and/or blkid(8) for more info
#
UUID=1c419d6c-5064-4a2b-953c-05b2c67edb15 /                       xfs     defaults        0 0
/swapfile none swap defaults 0 0
192.168.33.1:/Users/xxx/nfs-share /mnt/nfs-data nfs nolock,nfsvers=3,vers=3 0 0
```

