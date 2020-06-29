In the future, after clone this folder from git, then run vagrant from this folder directly.



if something not working, then try to make links manually.

```shell
ln -s 'xxx/Big-Data-Study/Documentation/Environment\ Configuration/Vagrant' ~/vagrant
```



then next time,  vagrant can be used this way:

```shell
cd ~/vagrant
vagrant up
```



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