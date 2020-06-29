#### Initial setup

In the future, after clone this folder from git, then run vagrant from this folder directly.

if something not working, then try to make links manually.

```shell
ln -s 'xxx/Big-Data-Study/Documentation/Environment\ Configuration/Vagrant' ~/vagrant
```

setup environment variables to run vagrant from anywhere of the computer

```shell
# put this under .profile of current user on Mac
export VAGRANT_CWD=~/vagrant
```

then next time,  vagrant can be used this way:

```shell
cd ~/vagrant
vagrant up
```

#### provision

In case, some temporary scripts need to be run on VM.

1. put contents into ./scripts folder
2. modify a bit of Vagrantfile
3. Run 

-  启动时自动执行，缺省地，任务只执行一次，第二次启动就不会自动运行了。
- 如果需要每次都自动运行，需要为provision指定run:"always"属性
- 启动时运行，在启动命令加 --provision 参数,适用于 vagrant up 和 vagrant reload
- vm启动状态时，执行 vagrant provision 命令
- 只执行shell类型的任务。可以如下操作：vagrant provision --provision-with shell



#### .vagrant 

the folder .vagrant under this folder is critical to run after all machines have been provisioned.

any location change of this location will cause a warning and change as below. But everything is working properly.

```shell
vagrant ssh hdp1

==> hdp1: This machine used to live in /Users/xxx/Documents/JavaStudy/envsetup/vagrant_project but it's now at /Users/xxx/Documents/JavaStudy/envsetup/git/Big-Data-Study/Documentation/Environment Configuration/Vagrant.
==> hdp1: Depending on your current provider you may need to change the name of
==> hdp1: the machine to run it as a different machine.
```



#### Virtual Machine Locations

* ~/VirtualBox VMs