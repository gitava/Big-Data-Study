# [XFS文件系统碎片整理](https://blog.csdn.net/scaleqiao/article/details/47122329)

网上有些帖子说XFS不用做碎片整理，其实是错误的。XFS用延迟写入等技术确实可以减少碎片的出现，但是如果服务器用了几年，并且文件操作比较频繁，还是会出现碎片的，应该整理。注意：在Debian中XFS相关命令在xfsprogs包中，而xfs_fsr命令是在xfsdump包中的。所以要用xfs_fsr整理碎片，记得安装xfsdump包。 其他Linux发行版本中，包含xfs相关命令的包也应该叫类似的名字，自己google一下。 

  xfsdump - Administrative utilities for the XFS filesystem 
  xfslibs-dev - XFS filesystem-specific static libraries and headers
  xfsprogs - Utilities for managing the XFS filesystem 

一、查看碎片 
  1、查看/dev/sdc1的碎片情况：
    \# xfs_db -c frag -r /dev/sdc1
    actual 93133, ideal 8251, fragmentation factor 91.14%
  这个应该整理一下碎片了 :)

  2、查看/dev/sdb1的碎片情况：
    \# xfs_db -c frag -r /dev/sdb1
    actual 905607, ideal 900507, fragmentation factor 0.56%
   这个不用做碎片整理。   

  3、另一种命令形式： 
    \# xfs_db -r /dev/sdd1
    xfs_db> frag
    actual 117578, ideal 116929, fragmentation factor 0.55%

  4、也可以通过xfs_bmap命令了解某个文件的情况： 
    \# xfs_bmap -v case19.dat
      case19.dat:
      EXT: FILE-OFFSET    BLOCK-RANGE       AG AG-OFFSET        TOTAL
      0: [0..9551]:     592061576..592071127   1 (103696496..103706047)  9552
      1: [9552..86039]:   599312816..599389303   1 (110947736..111024223)  76488
      2: [86040..170399]:  599655400..599739759   1 (111290320..111374679)  84360
      3: [170400..256799]: 599751632..599838031   1 (111386552..111472951)  86400
      4: [256800..340079]: 1185490752..1185574031  2 (208760592..208843871)  83280
      5: [340080..592703]: 1185577976..1185830599  2 (208847816..209100439) 252624

二、整理碎片 
   \# xfs_fsr /dev/sdc1

--------

```shell
[vagrant@hdp-node-01 ~]$ df -ah
Filesystem      Size  Used Avail Use% Mounted on
sysfs              0     0     0    - /sys
proc               0     0     0    - /proc
devtmpfs        489M     0  489M   0% /dev
securityfs         0     0     0    - /sys/kernel/security
tmpfs           496M     0  496M   0% /dev/shm
devpts             0     0     0    - /dev/pts
tmpfs           496M  6.7M  489M   2% /run
tmpfs           496M     0  496M   0% /sys/fs/cgroup
cgroup             0     0     0    - /sys/fs/cgroup/systemd
pstore             0     0     0    - /sys/fs/pstore
cgroup             0     0     0    - /sys/fs/cgroup/net_cls,net_prio
cgroup             0     0     0    - /sys/fs/cgroup/cpuset
cgroup             0     0     0    - /sys/fs/cgroup/devices
cgroup             0     0     0    - /sys/fs/cgroup/blkio
cgroup             0     0     0    - /sys/fs/cgroup/cpu,cpuacct
cgroup             0     0     0    - /sys/fs/cgroup/perf_event
cgroup             0     0     0    - /sys/fs/cgroup/freezer
cgroup             0     0     0    - /sys/fs/cgroup/pids
cgroup             0     0     0    - /sys/fs/cgroup/memory
cgroup             0     0     0    - /sys/fs/cgroup/hugetlb
configfs           0     0     0    - /sys/kernel/config
/dev/sda1        40G  5.6G   35G  14% /
selinuxfs          0     0     0    - /sys/fs/selinux
systemd-1          0     0     0    - /proc/sys/fs/binfmt_misc
debugfs            0     0     0    - /sys/kernel/debug
hugetlbfs          0     0     0    - /dev/hugepages
mqueue             0     0     0    - /dev/mqueue
sunrpc             0     0     0    - /var/lib/nfs/rpc_pipefs
tmpfs           100M     0  100M   0% /run/user/1000


[vagrant@hdp-node-01 ~]$ df -khT
Filesystem     Type      Size  Used Avail Use% Mounted on
devtmpfs       devtmpfs  489M     0  489M   0% /dev
tmpfs          tmpfs     496M     0  496M   0% /dev/shm
tmpfs          tmpfs     496M  6.7M  489M   2% /run
tmpfs          tmpfs     496M     0  496M   0% /sys/fs/cgroup
/dev/sda1      xfs        40G  5.6G   35G  14% /
tmpfs          tmpfs     100M     0  100M   0% /run/user/1000
[vagrant@hdp-node-01 ~]$ mount|grep "/dev"
devtmpfs on /dev type devtmpfs (rw,nosuid,seclabel,size=499864k,nr_inodes=124966,mode=755)
tmpfs on /dev/shm type tmpfs (rw,nosuid,nodev,seclabel)
devpts on /dev/pts type devpts (rw,nosuid,noexec,relatime,seclabel,gid=5,mode=620,ptmxmode=000)
cgroup on /sys/fs/cgroup/devices type cgroup (rw,nosuid,nodev,noexec,relatime,seclabel,devices)
/dev/sda1 on / type xfs (rw,relatime,seclabel,attr2,inode64,noquota)
hugetlbfs on /dev/hugepages type hugetlbfs (rw,relatime,seclabel)
mqueue on /dev/mqueue type mqueue (rw,relatime,seclabel)
[vagrant@hdp-node-01 ~]$ cat /etc/fstab

#
# /etc/fstab
# Created by anaconda on Thu Apr 30 22:04:55 2020
#
# Accessible filesystems, by reference, are maintained under '/dev/disk'
# See man pages fstab(5), findfs(8), mount(8) and/or blkid(8) for more info
#
UUID=1c419d6c-5064-4a2b-953c-05b2c67edb15 /                       xfs     defaults        0 0
/swapfile none swap defaults 0 0

[vagrant@hdp-node-01 ~]$ sudo xfs_db -c frag -r /dev/sda1
actual 61794, ideal 61326, fragmentation factor 0.76%
Note, this number is largely meaningless.
Files on this filesystem average 1.01 extents per file

[vagrant@hdp-node-01 ~]$ sudo xfs_db -r /dev/sda1
xfs_db> frag
actual 61795, ideal 61327, fragmentation factor 0.76%
Note, this number is largely meaningless.
Files on this filesystem average 1.01 extents per file
xfs_db> quit
[vagrant@hdp-node-01 ~]$ sudo xfs_fsr /dev/sda1
/ start inode=0
[vagrant@hdp-node-01 ~]$ sudo xfs_db -c frag -r /dev/sda1
actual 61795, ideal 61327, fragmentation factor 0.76%
Note, this number is largely meaningless.
Files on this filesystem average 1.01 extents per file
[vagrant@hdp-node-01 ~]$
```

