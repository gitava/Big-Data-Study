# Hive 远程模式的安装

#### 下载压Hive 

使用 release 2.3.7 （最新版）

```shell
wget https://downloads.apache.org/hive/hive-2.3.7/apache-hive-2.3.7-bin.tar.gz
```

#### ￼复制Pig安装文件到虚拟机

```bash
scp apache-hive-2.3.7-bin.tar.gz vagrant@192.168.33.101:/home/vagrant/
```

#### 解压目录

```bash
[vagrant@hdp-node-01 ~]$ tar -zxvf apache-hive-2.3.7-bin.tar.gz
...
[vagrant@hdp-node-01 ~]$ ls -ld apache-hive-2.3.7-bin*
drwxrwxr-x. 10 vagrant vagrant       184 Jun 27 11:10 apache-hive-2.3.7-bin
-rwxrwxr-x.  1 vagrant vagrant 232734895 Jun 27 11:09 apache-hive-2.3.7-bin.tar.gz

[vagrant@hdp-node-01 ~]$ rm apache-hive-2.3.7-bin.tar.gz

# Convinient for the future upgrade
[vagrant@hdp-node-01 ~]$ ln -s apache-hive-2.3.7-bin hive
[vagrant@hdp-node-01 ~]$ ls -ld *hive*
drwxrwxr-x. 10 vagrant vagrant 184 Jun 27 11:10 apache-hive-2.3.7-bin
lrwxrwxrwx.  1 vagrant vagrant  21 Jun 27 11:11 hive -> apache-hive-2.3.7-bin
```

#### 修改hosts文件

```bash
[vagrant@hdp-node-01 ~]$ cat /etc/hosts

#127.0.0.1	hdp-node-01	hdp-node-01
#127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

192.168.33.101  hdp-node-01
192.168.33.102  hdp-node-02
192.168.33.103  hdp-node-03
192.168.33.104  hdp-node-04
```

#### 设置环境变量

```bash
# .bash_profile

# Get the aliases and functions
if [ -f ~/.bashrc ]; then
        . ~/.bashrc
fi

# User specific environment and startup programs
# add pig bin folder to path for pig installation
PATH=$PATH:$HOME/.local/bin:$HOME/bin:$HOME/pig/bin

# Add PIG_CLASSPATH for pig installation
export PIG_CLASSPATH=/home/vagrant/hadoop/etc/hadoop
export PIG_USER_CLASSPATH_FIRST=/home/vagrant/pig/lib

# Configuration for hive installation
export HIVE_HOME=/home/vagrant/hive
export PATH=$PATH:$HIVE_HOME/bin
export CLASSPATH=$CLASSPATH:$HIVE_HOME/bind
```

使配置生效

```shell
source .bash_profile
```

#### 配置文件

```shell
[vagrant@hdp-node-01 hive]$ cd conf/
[vagrant@hdp-node-01 conf]$ pwd
/home/vagrant/hive/conf

[vagrant@hdp-node-01 conf]$ ls
beeline-log4j2.properties.template  hive-exec-log4j2.properties.template  llap-cli-log4j2.properties.template
hive-default.xml.template           hive-log4j2.properties.template       llap-daemon-log4j2.properties.template
hive-env.sh.template                ivysettings.xml                       parquet-logging.properties

[vagrant@hdp-node-01 conf]$ ls -ltr
total 288
-rw-r--r--. 1 vagrant vagrant   1596 Dec 16  2016 beeline-log4j2.properties.template
-rw-r--r--. 1 vagrant vagrant   2662 Mar  8  2017 parquet-logging.properties
-rw-r--r--. 1 vagrant vagrant   2060 Mar  8  2017 ivysettings.xml
-rw-r--r--. 1 vagrant vagrant   2274 Mar  8  2017 hive-exec-log4j2.properties.template
-rw-r--r--. 1 vagrant vagrant   2365 May  2  2017 hive-env.sh.template
-rw-r--r--. 1 vagrant vagrant   2925 Mar  9 16:09 hive-log4j2.properties.template
-rw-r--r--. 1 vagrant vagrant   7041 Mar  9 16:09 llap-daemon-log4j2.properties.template
-rw-r--r--. 1 vagrant vagrant   2719 Mar  9 16:09 llap-cli-log4j2.properties.template
-rw-r--r--. 1 vagrant vagrant 257573 Apr  7 19:42 hive-default.xml.template
```

#### 修改配置文件hive/conf/hive-env.sh 

```shell
cp hive-env.sh.template hive-env.sh
cp hive-log4j2.properties.template hive-log4j2.properties
cp hive-exec-log4j2.properties.template hive-exec-log4j2.properties


[vagrant@hdp-node-01 conf]$ ls -ltr|tail -4
-rw-r--r--. 1 vagrant vagrant   2387 Jun 27 11:51 hive-env.sh
-rw-r--r--. 1 vagrant vagrant 257573 Jun 27 11:54 hive-site.xml
-rw-r--r--. 1 vagrant vagrant   2925 Jun 27 12:03 hive-log4j2.properties
-rw-r--r--. 1 vagrant vagrant   2274 Jun 27 12:04 hive-exec-log4j2.properties
[vagrant@hdp-node-01 conf]$
```

```shell
[vagrant@hdp-node-01 conf]$ cat hive-env.sh
....
# Set HADOOP_HOME to point to a specific hadoop install directory
HADOOP_HOME=/home/vagrant/hadoop

# Hive Configuration Directory can be controlled by:
export HIVE_CONF_DIR=/home/vagrant/hive/conf
...
```

#### 修改hive/conf/hive-default.xml

```shell
 cp hive-default.xml.template hive-default.xml
```

修改对应的值

```xml
<!--jdbc连接方式-->
<name>javax.jdo.option.ConnectionDriverName</name>
<value>com.mysql.jdbc.Driver</value>

<!--mysql连接配置-->
<name>javax.jdo.option.ConnectionURL</name><value>jdbc:mysql://192.168.33.102:3306/hive?createDatabaseIfNotExist=true</value>

<!--mysql数据库的用户名-->
<name>javax.jdo.option.ConnectionUserName</name>
<value>hive</value>

<!--用户对应的密码-->
<name>javax.jdo.option.ConnectionPassword</name>
<value>!mySQL123!</value>
```



### Issues and fixes (#5)

##### Exception in thread "main" java.lang.NoClassDefFoundError: org/joda/time/ReadableInstant

Find a better fix which doesn't need the jar file to be moved.

Change the configuration file: /home/vagrant/hadoop/etc/hadoop/hadoop-env.sh

```shell
# Add customized classpath in order to run own java code
#export HADOOP_CLASSPATH=/home/vagrant/myclass

# Add to solve pig/hive class lib issues.
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:/home/vagrant/myclass
```



# 安装MySQL

##### 下载安装mysql

Enable the MySQL 8.0 repository with the following command: ([referece](https://linuxize.com/post/install-mysql-on-centos-7/))

```shell
sudo yum localinstall https://dev.mysql.com/get/mysql80-community-release-el7-1.noarch.rpm
```

Install MySQL 8.0 package with yum:

```shell
sudo yum install mysql-community-server
```

##### 设置默认字符和引擎

##### 启动mysql

```shell
[vagrant@hdp-node-02 /]$ sudo service mysqld start
Redirecting to /bin/systemctl start mysqld.service
```

##### **查看Mysql服务状态**

```shell
service mysqld status
```

```shell
[vagrant@hdp-node-02 /]$ service mysqld status
Redirecting to /bin/systemctl status mysqld.service
● mysqld.service - MySQL Server
   Loaded: loaded (/usr/lib/systemd/system/mysqld.service; enabled; vendor preset: disabled)
   Active: active (running) since Sun 2020-06-28 01:45:22 UTC; 5min ago
     Docs: man:mysqld(8)
           http://dev.mysql.com/doc/refman/en/using-systemd.html
  Process: 3251 ExecStartPre=/usr/bin/mysqld_pre_systemd (code=exited, status=0/SUCCESS)
 Main PID: 3328 (mysqld)
   Status: "Server is operational"
   CGroup: /system.slice/mysqld.service
           └─3328 /usr/sbin/mysqld
```

**查看root用户临时密码**

```bash
grep "A temporary password" /var/log/mysqld.log
```

```
[vagrant@hdp-node-02 /]$ sudo grep "A temporary password" /var/log/mysqld.log
2020-06-28T01:45:19.652661Z 6 [Note] [MY-010454] [Server] A temporary password is generated for root@localhost: oscX,Pa?e9f3
```

**配置Mysql安全策略**

```bash
mysql_secure_installation
```



```shell
[vagrant@hdp-node-02 /]$ mysql_secure_installation

Securing the MySQL server deployment.

Enter password for user root:
Error: Access denied for user 'root'@'localhost' (using password: YES)
[vagrant@hdp-node-02 /]$ mysql_secure_installation

Securing the MySQL server deployment.

Enter password for user root:
Error: Access denied for user 'root'@'localhost' (using password: YES)
[vagrant@hdp-node-02 /]$ mysql_secure_installation

Securing the MySQL server deployment.

#设置新的（Mysql中的）root用户密码（需由大写、小写、数字、符号四种混合组成）
Enter password for user root:

The existing password for the user account root has expired. Please set a new password.

New password:

Re-enter new password:
The 'validate_password' component is installed on the server.
The subsequent steps will run with the existing configuration
of the component.
Using existing password for root.

Estimated strength of the password: 100
Change the password for root ? ((Press y|Y for Yes, any other key for No) : y

New password:

Re-enter new password:

#系统自动检测root用户的密码强度，如分数过低可以输入【y】进行更改密码，否则输入【n】跳过。
Estimated strength of the password: 100
Do you wish to continue with the password provided?(Press y|Y for Yes, any other key for No) : y

#选择是否删除匿名用户。建议【y】
By default, a MySQL installation has an anonymous user,
allowing anyone to log into MySQL without having to have
a user account created for them. This is intended only for
testing, and to make the installation go a bit smoother.
You should remove them before moving into a production
environment.

Remove anonymous users? (Press y|Y for Yes, any other key for No) : y
Success.

#选择是否禁止root用户远程登录。建议【y】可根据下文添加另一远程用户
Normally, root should only be allowed to connect from
'localhost'. This ensures that someone cannot guess at
the root password from the network.

Disallow root login remotely? (Press y|Y for Yes, any other key for No) : y
Success.

#选择是否删除测试数据库。建议【y】（keep it for testing in the VM)
By default, MySQL comes with a database named 'test' that
anyone can access. This is also intended only for testing,
and should be removed before moving into a production
environment.


Remove test database and access to it? (Press y|Y for Yes, any other key for No) : n

 ... skipping.
 
#选择是否刷新privilege表，即是否执行flush privileges命令。建议【y】
Reloading the privilege tables will ensure that all changes
made so far will take effect immediately.

Reload privilege tables now? (Press y|Y for Yes, any other key for No) : y
Success.

All done!
```

# **配置远程访问**

##### 准备工作

change .bash_profile to add mysql (/bin )to path

```
[vagrant@hdp-node-02 ~]$ cat .bash_profile
# .bash_profile

# Get the aliases and functions
if [ -f ~/.bashrc ]; then
	. ~/.bashrc
fi

# User specific environment and startup programs

PATH=$PATH:$HOME/.local/bin:$HOME/bin:/bin:/usr/bin

export PATH
```

if can't find mysql, then you need to install mysql-client

```
[vagrant@hdp-node-02 bin]$ sudo yum reinstall mysql
Failed to set locale, defaulting to C
Loaded plugins: fastestmirror
Loading mirror speeds from cached hostfile
 * base: mirrors.aliyun.com
 * extras: mirrors.aliyun.com
 * updates: mirrors.aliyun.com
Resolving Dependencies
--> Running transaction check
---> Package mysql-community-client.x86_64 0:8.0.20-1.el7 will be reinstalled
--> Finished Dependency Resolution

Dependencies Resolved

=============================================================================================
 Package                      Arch         Version             Repository               Size
=============================================================================================
Reinstalling:
 mysql-community-client       x86_64       8.0.20-1.el7        mysql80-community        47 M

Transaction Summary
=============================================================================================
Reinstall  1 Package

Total download size: 47 M
Installed size: 225 M
Is this ok [y/d/N]: y
Downloading packages:
mysql-community-client-8.0.20-1.el7.x86_64.rpm                        |  47 MB  00:00:29
Running transaction check
Running transaction test
Transaction test succeeded
Running transaction
  Installing : mysql-community-client-8.0.20-1.el7.x86_64                                1/1
  Verifying  : mysql-community-client-8.0.20-1.el7.x86_64                                1/1

Installed:
  mysql-community-client.x86_64 0:8.0.20-1.el7

Complete!
[vagrant@hdp-node-02 bin]$ rpm -ql mysql-community-client-8.0.20-1.el7.x86_64
/usr/bin/mysql
/usr/bin/mysql_config_editor
/usr/bin/mysqladmin
/usr/bin/mysqlbinlog
/usr/bin/mysqlcheck
/usr/bin/mysqldump
/usr/bin/mysqlimport
/usr/bin/mysqlpump
/usr/bin/mysqlshow
/usr/bin/mysqlslap
/usr/share/doc/mysql-community-client-8.0.20
/usr/share/doc/mysql-community-client-8.0.20/LICENSE
/usr/share/doc/mysql-community-client-8.0.20/README
/usr/share/man/man1/mysql.1.gz
/usr/share/man/man1/mysql_config_editor.1.gz
/usr/share/man/man1/mysqladmin.1.gz
/usr/share/man/man1/mysqlbinlog.1.gz
/usr/share/man/man1/mysqlcheck.1.gz
/usr/share/man/man1/mysqldump.1.gz
/usr/share/man/man1/mysqlimport.1.gz
/usr/share/man/man1/mysqlpump.1.gz
/usr/share/man/man1/mysqlshow.1.gz
/usr/share/man/man1/mysqlslap.1.gz
```



##### **1）登录mysql控制台**

```
mysql -uroot -p
```



**2）创建新的远程用户**

```mysql
CREATE USER '用户名'@'%' IDENTIFIED BY '密码';
```

```mysql
CREATE USER 'hive'@'%' IDENTIFIED BY '!mySQL123!';
```

**3）授权给远程用户**

```mysql
GRANT ALL ON *.* TO '[用户名]'@'%'; # ALL表示授予所有权限、*.*表示所有数据库中的所有表、%表示任意IP可以远程连接
```

```mysql
-- for hive installation
GRANT ALL PRIVILEGES ON *.* TO 'hive'@'%' with grant option;
```

```mysql
-- avoid remote login error
mysql> ALTER USER 'hive'@'%' IDENTIFIED WITH mysql_native_password BY '!mySQL123!';
Query OK, 0 rows affected (0.05 sec)


mysql> -- for hive installation
mysql> GRANT ALL PRIVILEGES ON *.* TO 'hive'@'%' with grant option;
Query OK, 0 rows affected (0.01 sec)

-- 刷新
mysql> flush privileges;
Query OK, 0 rows affected (0.01 sec)
```



#### 用hive账号远程登录MySQL并建立hive专用的元数据库

install mysql-client

```shell
 ✘ fllbeaver@localhost  ~/Documents/JavaStudy/envsetup/vagrant_project  vagrant ssh hdp1
Last login: Sun Jun 28 01:13:14 2020 from 10.0.2.2
-bash: warning: setlocale: LC_CTYPE: cannot change locale (UTF-8): No such file or directory
[vagrant@hdp-node-01 ~]$ sudo yum install mysql
Failed to set locale, defaulting to C
Loaded plugins: fastestmirror
Loading mirror speeds from cached hostfile
 * base: mirrors.huaweicloud.com
 * extras: mirrors.huaweicloud.com
 * updates: mirrors.huaweicloud.com
Resolving Dependencies
--> Running transaction check
---> Package mariadb.x86_64 1:5.5.65-1.el7 will be installed
--> Processing Dependency: perl(Sys::Hostname) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: perl(IPC::Open3) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: perl(Getopt::Long) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: perl(File::Temp) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: perl(Fcntl) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: perl(Exporter) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: /usr/bin/perl for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Running transaction check
---> Package perl.x86_64 4:5.16.3-295.el7 will be installed
--> Processing Dependency: perl-libs = 4:5.16.3-295.el7 for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Socket) >= 1.3 for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Scalar::Util) >= 1.10 for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl-macros for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl-libs for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(threads::shared) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(threads) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(constant) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Time::Local) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Time::HiRes) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Storable) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Socket) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Scalar::Util) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Pod::Simple::XHTML) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Pod::Simple::Search) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Filter::Util::Call) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(File::Spec::Unix) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(File::Spec::Functions) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(File::Spec) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(File::Path) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Cwd) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Carp) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: libperl.so()(64bit) for package: 4:perl-5.16.3-295.el7.x86_64
---> Package perl-Exporter.noarch 0:5.68-3.el7 will be installed
---> Package perl-File-Temp.noarch 0:0.23.01-3.el7 will be installed
---> Package perl-Getopt-Long.noarch 0:2.40-3.el7 will be installed
--> Processing Dependency: perl(Pod::Usage) >= 1.14 for package: perl-Getopt-Long-2.40-3.el7.noarch
--> Processing Dependency: perl(Text::ParseWords) for package: perl-Getopt-Long-2.40-3.el7.noarch
--> Running transaction check
---> Package perl-Carp.noarch 0:1.26-244.el7 will be installed
---> Package perl-File-Path.noarch 0:2.09-2.el7 will be installed
---> Package perl-Filter.x86_64 0:1.49-3.el7 will be installed
---> Package perl-PathTools.x86_64 0:3.40-5.el7 will be installed
---> Package perl-Pod-Simple.noarch 1:3.28-4.el7 will be installed
--> Processing Dependency: perl(Pod::Escapes) >= 1.04 for package: 1:perl-Pod-Simple-3.28-4.el7.noarch
--> Processing Dependency: perl(Encode) for package: 1:perl-Pod-Simple-3.28-4.el7.noarch
---> Package perl-Pod-Usage.noarch 0:1.63-3.el7 will be installed
--> Processing Dependency: perl(Pod::Text) >= 3.15 for package: perl-Pod-Usage-1.63-3.el7.noarch
--> Processing Dependency: perl-Pod-Perldoc for package: perl-Pod-Usage-1.63-3.el7.noarch
---> Package perl-Scalar-List-Utils.x86_64 0:1.27-248.el7 will be installed
---> Package perl-Socket.x86_64 0:2.010-5.el7 will be installed
---> Package perl-Storable.x86_64 0:2.45-3.el7 will be installed
---> Package perl-Text-ParseWords.noarch 0:3.29-4.el7 will be installed
---> Package perl-Time-HiRes.x86_64 4:1.9725-3.el7 will be installed
---> Package perl-Time-Local.noarch 0:1.2300-2.el7 will be installed
---> Package perl-constant.noarch 0:1.27-2.el7 will be installed
---> Package perl-libs.x86_64 4:5.16.3-295.el7 will be installed
---> Package perl-macros.x86_64 4:5.16.3-295.el7 will be installed
---> Package perl-threads.x86_64 0:1.87-4.el7 will be installed
---> Package perl-threads-shared.x86_64 0:1.43-6.el7 will be installed
--> Running transaction check
---> Package perl-Encode.x86_64 0:2.51-7.el7 will be installed
---> Package perl-Pod-Escapes.noarch 1:1.04-295.el7 will be installed
---> Package perl-Pod-Perldoc.noarch 0:3.20-4.el7 will be installed
--> Processing Dependency: perl(parent) for package: perl-Pod-Perldoc-3.20-4.el7.noarch
--> Processing Dependency: perl(HTTP::Tiny) for package: perl-Pod-Perldoc-3.20-4.el7.noarch
---> Package perl-podlators.noarch 0:2.5.1-3.el7 will be installed
--> Running transaction check
---> Package perl-HTTP-Tiny.noarch 0:0.033-3.el7 will be installed
---> Package perl-parent.noarch 1:0.225-244.el7 will be installed
--> Finished Dependency Resolution

Dependencies Resolved

=============================================================================================
 Package                        Arch           Version                    Repository    Size
=============================================================================================
Installing:
 mariadb                        x86_64         1:5.5.65-1.el7             base         8.7 M
Installing for dependencies:
 perl                           x86_64         4:5.16.3-295.el7           base         8.0 M
 perl-Carp                      noarch         1.26-244.el7               base          19 k
 perl-Encode                    x86_64         2.51-7.el7                 base         1.5 M
 perl-Exporter                  noarch         5.68-3.el7                 base          28 k
 perl-File-Path                 noarch         2.09-2.el7                 base          26 k
 perl-File-Temp                 noarch         0.23.01-3.el7              base          56 k
 perl-Filter                    x86_64         1.49-3.el7                 base          76 k
 perl-Getopt-Long               noarch         2.40-3.el7                 base          56 k
 perl-HTTP-Tiny                 noarch         0.033-3.el7                base          38 k
 perl-PathTools                 x86_64         3.40-5.el7                 base          82 k
 perl-Pod-Escapes               noarch         1:1.04-295.el7             base          51 k
 perl-Pod-Perldoc               noarch         3.20-4.el7                 base          87 k
 perl-Pod-Simple                noarch         1:3.28-4.el7               base         216 k
 perl-Pod-Usage                 noarch         1.63-3.el7                 base          27 k
 perl-Scalar-List-Utils         x86_64         1.27-248.el7               base          36 k
 perl-Socket                    x86_64         2.010-5.el7                base          49 k
 perl-Storable                  x86_64         2.45-3.el7                 base          77 k
 perl-Text-ParseWords           noarch         3.29-4.el7                 base          14 k
 perl-Time-HiRes                x86_64         4:1.9725-3.el7             base          45 k
 perl-Time-Local                noarch         1.2300-2.el7               base          24 k
 perl-constant                  noarch         1.27-2.el7                 base          19 k
 perl-libs                      x86_64         4:5.16.3-295.el7           base         689 k
 perl-macros                    x86_64         4:5.16.3-295.el7           base          44 k
 perl-parent                    noarch         1:0.225-244.el7            base          12 k
 perl-podlators                 noarch         2.5.1-3.el7                base         112 k
 perl-threads                   x86_64         1.87-4.el7                 base          49 k
 perl-threads-shared            x86_64         1.43-6.el7                 base          39 k

Transaction Summary
=============================================================================================
Install  1 Package (+27 Dependent packages)

Total download size: 20 M
Installed size: 85 M
Is this ok [y/d/N]:
Exiting on user command
Your transaction was saved, rerun it with:
 yum load-transaction /tmp/yum_save_tx.2020-06-28.02-44.FecZme.yumtx
[vagrant@hdp-node-01 ~]$ sudo yum install mysql
Failed to set locale, defaulting to C
Loaded plugins: fastestmirror
Loading mirror speeds from cached hostfile
 * base: mirrors.huaweicloud.com
 * extras: mirrors.huaweicloud.com
 * updates: mirrors.huaweicloud.com
Resolving Dependencies
--> Running transaction check
---> Package mariadb.x86_64 1:5.5.65-1.el7 will be installed
--> Processing Dependency: perl(Sys::Hostname) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: perl(IPC::Open3) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: perl(Getopt::Long) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: perl(File::Temp) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: perl(Fcntl) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: perl(Exporter) for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Processing Dependency: /usr/bin/perl for package: 1:mariadb-5.5.65-1.el7.x86_64
--> Running transaction check
---> Package perl.x86_64 4:5.16.3-295.el7 will be installed
--> Processing Dependency: perl-libs = 4:5.16.3-295.el7 for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Socket) >= 1.3 for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Scalar::Util) >= 1.10 for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl-macros for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl-libs for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(threads::shared) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(threads) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(constant) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Time::Local) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Time::HiRes) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Storable) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Socket) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Scalar::Util) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Pod::Simple::XHTML) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Pod::Simple::Search) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Filter::Util::Call) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(File::Spec::Unix) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(File::Spec::Functions) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(File::Spec) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(File::Path) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Cwd) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: perl(Carp) for package: 4:perl-5.16.3-295.el7.x86_64
--> Processing Dependency: libperl.so()(64bit) for package: 4:perl-5.16.3-295.el7.x86_64
---> Package perl-Exporter.noarch 0:5.68-3.el7 will be installed
---> Package perl-File-Temp.noarch 0:0.23.01-3.el7 will be installed
---> Package perl-Getopt-Long.noarch 0:2.40-3.el7 will be installed
--> Processing Dependency: perl(Pod::Usage) >= 1.14 for package: perl-Getopt-Long-2.40-3.el7.noarch
--> Processing Dependency: perl(Text::ParseWords) for package: perl-Getopt-Long-2.40-3.el7.noarch
--> Running transaction check
---> Package perl-Carp.noarch 0:1.26-244.el7 will be installed
---> Package perl-File-Path.noarch 0:2.09-2.el7 will be installed
---> Package perl-Filter.x86_64 0:1.49-3.el7 will be installed
---> Package perl-PathTools.x86_64 0:3.40-5.el7 will be installed
---> Package perl-Pod-Simple.noarch 1:3.28-4.el7 will be installed
--> Processing Dependency: perl(Pod::Escapes) >= 1.04 for package: 1:perl-Pod-Simple-3.28-4.el7.noarch
--> Processing Dependency: perl(Encode) for package: 1:perl-Pod-Simple-3.28-4.el7.noarch
---> Package perl-Pod-Usage.noarch 0:1.63-3.el7 will be installed
--> Processing Dependency: perl(Pod::Text) >= 3.15 for package: perl-Pod-Usage-1.63-3.el7.noarch
--> Processing Dependency: perl-Pod-Perldoc for package: perl-Pod-Usage-1.63-3.el7.noarch
---> Package perl-Scalar-List-Utils.x86_64 0:1.27-248.el7 will be installed
---> Package perl-Socket.x86_64 0:2.010-5.el7 will be installed
---> Package perl-Storable.x86_64 0:2.45-3.el7 will be installed
---> Package perl-Text-ParseWords.noarch 0:3.29-4.el7 will be installed
---> Package perl-Time-HiRes.x86_64 4:1.9725-3.el7 will be installed
---> Package perl-Time-Local.noarch 0:1.2300-2.el7 will be installed
---> Package perl-constant.noarch 0:1.27-2.el7 will be installed
---> Package perl-libs.x86_64 4:5.16.3-295.el7 will be installed
---> Package perl-macros.x86_64 4:5.16.3-295.el7 will be installed
---> Package perl-threads.x86_64 0:1.87-4.el7 will be installed
---> Package perl-threads-shared.x86_64 0:1.43-6.el7 will be installed
--> Running transaction check
---> Package perl-Encode.x86_64 0:2.51-7.el7 will be installed
---> Package perl-Pod-Escapes.noarch 1:1.04-295.el7 will be installed
---> Package perl-Pod-Perldoc.noarch 0:3.20-4.el7 will be installed
--> Processing Dependency: perl(parent) for package: perl-Pod-Perldoc-3.20-4.el7.noarch
--> Processing Dependency: perl(HTTP::Tiny) for package: perl-Pod-Perldoc-3.20-4.el7.noarch
---> Package perl-podlators.noarch 0:2.5.1-3.el7 will be installed
--> Running transaction check
---> Package perl-HTTP-Tiny.noarch 0:0.033-3.el7 will be installed
---> Package perl-parent.noarch 1:0.225-244.el7 will be installed
--> Finished Dependency Resolution

Dependencies Resolved

=============================================================================================
 Package                        Arch           Version                    Repository    Size
=============================================================================================
Installing:
 mariadb                        x86_64         1:5.5.65-1.el7             base         8.7 M
Installing for dependencies:
 perl                           x86_64         4:5.16.3-295.el7           base         8.0 M
 perl-Carp                      noarch         1.26-244.el7               base          19 k
 perl-Encode                    x86_64         2.51-7.el7                 base         1.5 M
 perl-Exporter                  noarch         5.68-3.el7                 base          28 k
 perl-File-Path                 noarch         2.09-2.el7                 base          26 k
 perl-File-Temp                 noarch         0.23.01-3.el7              base          56 k
 perl-Filter                    x86_64         1.49-3.el7                 base          76 k
 perl-Getopt-Long               noarch         2.40-3.el7                 base          56 k
 perl-HTTP-Tiny                 noarch         0.033-3.el7                base          38 k
 perl-PathTools                 x86_64         3.40-5.el7                 base          82 k
 perl-Pod-Escapes               noarch         1:1.04-295.el7             base          51 k
 perl-Pod-Perldoc               noarch         3.20-4.el7                 base          87 k
 perl-Pod-Simple                noarch         1:3.28-4.el7               base         216 k
 perl-Pod-Usage                 noarch         1.63-3.el7                 base          27 k
 perl-Scalar-List-Utils         x86_64         1.27-248.el7               base          36 k
 perl-Socket                    x86_64         2.010-5.el7                base          49 k
 perl-Storable                  x86_64         2.45-3.el7                 base          77 k
 perl-Text-ParseWords           noarch         3.29-4.el7                 base          14 k
 perl-Time-HiRes                x86_64         4:1.9725-3.el7             base          45 k
 perl-Time-Local                noarch         1.2300-2.el7               base          24 k
 perl-constant                  noarch         1.27-2.el7                 base          19 k
 perl-libs                      x86_64         4:5.16.3-295.el7           base         689 k
 perl-macros                    x86_64         4:5.16.3-295.el7           base          44 k
 perl-parent                    noarch         1:0.225-244.el7            base          12 k
 perl-podlators                 noarch         2.5.1-3.el7                base         112 k
 perl-threads                   x86_64         1.87-4.el7                 base          49 k
 perl-threads-shared            x86_64         1.43-6.el7                 base          39 k

Transaction Summary
=============================================================================================
Install  1 Package (+27 Dependent packages)

Total download size: 20 M
Installed size: 85 M
Is this ok [y/d/N]: y
Downloading packages:
(1/28): mariadb-5.5.65-1.el7.x86_64.rpm                               | 8.7 MB  00:00:02
(2/28): perl-5.16.3-295.el7.x86_64.rpm                                | 8.0 MB  00:00:02
(3/28): perl-Carp-1.26-244.el7.noarch.rpm                             |  19 kB  00:00:00
(4/28): perl-Exporter-5.68-3.el7.noarch.rpm                           |  28 kB  00:00:00
(5/28): perl-Encode-2.51-7.el7.x86_64.rpm                             | 1.5 MB  00:00:00
(6/28): perl-File-Temp-0.23.01-3.el7.noarch.rpm                       |  56 kB  00:00:00
(7/28): perl-File-Path-2.09-2.el7.noarch.rpm                          |  26 kB  00:00:00
(8/28): perl-Getopt-Long-2.40-3.el7.noarch.rpm                        |  56 kB  00:00:00
(9/28): perl-HTTP-Tiny-0.033-3.el7.noarch.rpm                         |  38 kB  00:00:00
(10/28): perl-Filter-1.49-3.el7.x86_64.rpm                            |  76 kB  00:00:00
(11/28): perl-Pod-Escapes-1.04-295.el7.noarch.rpm                     |  51 kB  00:00:00
(12/28): perl-Pod-Perldoc-3.20-4.el7.noarch.rpm                       |  87 kB  00:00:00
(13/28): perl-PathTools-3.40-5.el7.x86_64.rpm                         |  82 kB  00:00:00
(14/28): perl-Pod-Simple-3.28-4.el7.noarch.rpm                        | 216 kB  00:00:00
(15/28): perl-Scalar-List-Utils-1.27-248.el7.x86_64.rpm               |  36 kB  00:00:00
(16/28): perl-Pod-Usage-1.63-3.el7.noarch.rpm                         |  27 kB  00:00:00
(17/28): perl-Socket-2.010-5.el7.x86_64.rpm                           |  49 kB  00:00:00
(18/28): perl-Storable-2.45-3.el7.x86_64.rpm                          |  77 kB  00:00:00
(19/28): perl-Time-HiRes-1.9725-3.el7.x86_64.rpm                      |  45 kB  00:00:00
(20/28): perl-Text-ParseWords-3.29-4.el7.noarch.rpm                   |  14 kB  00:00:00
(21/28): perl-Time-Local-1.2300-2.el7.noarch.rpm                      |  24 kB  00:00:00
(22/28): perl-constant-1.27-2.el7.noarch.rpm                          |  19 kB  00:00:00
(23/28): perl-libs-5.16.3-295.el7.x86_64.rpm                          | 689 kB  00:00:00
(24/28): perl-macros-5.16.3-295.el7.x86_64.rpm                        |  44 kB  00:00:00
(25/28): perl-podlators-2.5.1-3.el7.noarch.rpm                        | 112 kB  00:00:00
(26/28): perl-parent-0.225-244.el7.noarch.rpm                         |  12 kB  00:00:00
(27/28): perl-threads-1.87-4.el7.x86_64.rpm                           |  49 kB  00:00:00
(28/28): perl-threads-shared-1.43-6.el7.x86_64.rpm                    |  39 kB  00:00:00
---------------------------------------------------------------------------------------------
Total                                                        5.1 MB/s |  20 MB  00:00:03
Running transaction check
Running transaction test
Transaction test succeeded
Running transaction
  Installing : 1:perl-parent-0.225-244.el7.noarch                                       1/28
  Installing : perl-HTTP-Tiny-0.033-3.el7.noarch                                        2/28
  Installing : perl-podlators-2.5.1-3.el7.noarch                                        3/28
  Installing : perl-Pod-Perldoc-3.20-4.el7.noarch                                       4/28
  Installing : 1:perl-Pod-Escapes-1.04-295.el7.noarch                                   5/28
  Installing : perl-Text-ParseWords-3.29-4.el7.noarch                                   6/28
  Installing : perl-Encode-2.51-7.el7.x86_64                                            7/28
  Installing : perl-Pod-Usage-1.63-3.el7.noarch                                         8/28
  Installing : 4:perl-libs-5.16.3-295.el7.x86_64                                        9/28
  Installing : 4:perl-macros-5.16.3-295.el7.x86_64                                     10/28
  Installing : perl-Storable-2.45-3.el7.x86_64                                         11/28
  Installing : perl-Exporter-5.68-3.el7.noarch                                         12/28
  Installing : perl-constant-1.27-2.el7.noarch                                         13/28
  Installing : perl-Socket-2.010-5.el7.x86_64                                          14/28
  Installing : perl-Time-Local-1.2300-2.el7.noarch                                     15/28
  Installing : perl-Carp-1.26-244.el7.noarch                                           16/28
  Installing : 4:perl-Time-HiRes-1.9725-3.el7.x86_64                                   17/28
  Installing : perl-PathTools-3.40-5.el7.x86_64                                        18/28
  Installing : perl-Scalar-List-Utils-1.27-248.el7.x86_64                              19/28
  Installing : perl-File-Temp-0.23.01-3.el7.noarch                                     20/28
  Installing : perl-File-Path-2.09-2.el7.noarch                                        21/28
  Installing : perl-threads-shared-1.43-6.el7.x86_64                                   22/28
  Installing : perl-threads-1.87-4.el7.x86_64                                          23/28
  Installing : perl-Filter-1.49-3.el7.x86_64                                           24/28
  Installing : 1:perl-Pod-Simple-3.28-4.el7.noarch                                     25/28
  Installing : perl-Getopt-Long-2.40-3.el7.noarch                                      26/28
  Installing : 4:perl-5.16.3-295.el7.x86_64                                            27/28
  Installing : 1:mariadb-5.5.65-1.el7.x86_64                                           28/28
  Verifying  : perl-HTTP-Tiny-0.033-3.el7.noarch                                        1/28
  Verifying  : perl-threads-shared-1.43-6.el7.x86_64                                    2/28
  Verifying  : perl-Storable-2.45-3.el7.x86_64                                          3/28
  Verifying  : 1:perl-Pod-Escapes-1.04-295.el7.noarch                                   4/28
  Verifying  : perl-Exporter-5.68-3.el7.noarch                                          5/28
  Verifying  : perl-constant-1.27-2.el7.noarch                                          6/28
  Verifying  : perl-PathTools-3.40-5.el7.x86_64                                         7/28
  Verifying  : perl-Socket-2.010-5.el7.x86_64                                           8/28
  Verifying  : 1:mariadb-5.5.65-1.el7.x86_64                                            9/28
  Verifying  : 1:perl-parent-0.225-244.el7.noarch                                      10/28
  Verifying  : 4:perl-libs-5.16.3-295.el7.x86_64                                       11/28
  Verifying  : perl-File-Temp-0.23.01-3.el7.noarch                                     12/28
  Verifying  : 1:perl-Pod-Simple-3.28-4.el7.noarch                                     13/28
  Verifying  : perl-Time-Local-1.2300-2.el7.noarch                                     14/28
  Verifying  : 4:perl-macros-5.16.3-295.el7.x86_64                                     15/28
  Verifying  : 4:perl-5.16.3-295.el7.x86_64                                            16/28
  Verifying  : perl-Carp-1.26-244.el7.noarch                                           17/28
  Verifying  : 4:perl-Time-HiRes-1.9725-3.el7.x86_64                                   18/28
  Verifying  : perl-Scalar-List-Utils-1.27-248.el7.x86_64                              19/28
  Verifying  : perl-Pod-Usage-1.63-3.el7.noarch                                        20/28
  Verifying  : perl-Encode-2.51-7.el7.x86_64                                           21/28
  Verifying  : perl-Pod-Perldoc-3.20-4.el7.noarch                                      22/28
  Verifying  : perl-podlators-2.5.1-3.el7.noarch                                       23/28
  Verifying  : perl-File-Path-2.09-2.el7.noarch                                        24/28
  Verifying  : perl-threads-1.87-4.el7.x86_64                                          25/28
  Verifying  : perl-Filter-1.49-3.el7.x86_64                                           26/28
  Verifying  : perl-Getopt-Long-2.40-3.el7.noarch                                      27/28
  Verifying  : perl-Text-ParseWords-3.29-4.el7.noarch                                  28/28

Installed:
  mariadb.x86_64 1:5.5.65-1.el7

Dependency Installed:
  perl.x86_64 4:5.16.3-295.el7                    perl-Carp.noarch 0:1.26-244.el7
  perl-Encode.x86_64 0:2.51-7.el7                 perl-Exporter.noarch 0:5.68-3.el7
  perl-File-Path.noarch 0:2.09-2.el7              perl-File-Temp.noarch 0:0.23.01-3.el7
  perl-Filter.x86_64 0:1.49-3.el7                 perl-Getopt-Long.noarch 0:2.40-3.el7
  perl-HTTP-Tiny.noarch 0:0.033-3.el7             perl-PathTools.x86_64 0:3.40-5.el7
  perl-Pod-Escapes.noarch 1:1.04-295.el7          perl-Pod-Perldoc.noarch 0:3.20-4.el7
  perl-Pod-Simple.noarch 1:3.28-4.el7             perl-Pod-Usage.noarch 0:1.63-3.el7
  perl-Scalar-List-Utils.x86_64 0:1.27-248.el7    perl-Socket.x86_64 0:2.010-5.el7
  perl-Storable.x86_64 0:2.45-3.el7               perl-Text-ParseWords.noarch 0:3.29-4.el7
  perl-Time-HiRes.x86_64 4:1.9725-3.el7           perl-Time-Local.noarch 0:1.2300-2.el7
  perl-constant.noarch 0:1.27-2.el7               perl-libs.x86_64 4:5.16.3-295.el7
  perl-macros.x86_64 4:5.16.3-295.el7             perl-parent.noarch 1:0.225-244.el7
  perl-podlators.noarch 0:2.5.1-3.el7             perl-threads.x86_64 0:1.87-4.el7
  perl-threads-shared.x86_64 0:1.43-6.el7

Complete!
[vagrant@hdp-node-01 ~]$ cd /usr/bin
```



```
mysql -h 192.168.33.102 -uhive -p
```

```mysql
MySQL [(none)]> create database hive;
Query OK, 1 row affected (0.01 sec)

MySQL [(none)]> show databases;
+--------------------+
| Database           |
+--------------------+
| hive               |
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
5 rows in set (0.06 sec)

MySQL [(none)]> use hive
Database changed
MySQL [hive]> show tables;
Empty set (0.00 sec)
```



#### 安装JDBC包 [ref: #11]

Version: 5.1.49

https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.49/mysql-connector-java-5.1.49.jar

```shell
[vagrant@hdp-node-01 ~]$ mv mysql-connector-java-* MySQLJDBC/
[vagrant@hdp-node-01 ~]$ cd /home/vagrant/hive/lib
[vagrant@hdp-node-01 lib]$ ln -s /home/vagrant/MySQLJDBC/mysql-connector-java-5.1.49.jar mysql-connector-java.jar
[vagrant@hdp-node-01 lib]$ ll mysql-*
lrwxrwxrwx. 1 vagrant vagrant   55 Jun 28 06:04 mysql-connector-java.jar -> /home/vagrant/MySQLJDBC/mysql-connector-java-5.1.49.jar
-rw-r--r--. 1 vagrant vagrant 7954 Feb 13  2017 mysql-metadata-storage-0.9.2.jar
```

#### 启动hive前的准备

* 启动MySQL

```shell
vagrant@hdp-node-02 ~]$ systemctl list-unit-files|grep -i mysql
#mysql service 默认启动
mysqld.service                                enabled
mysqld@.service                               disabled
```

* 启动hadoop 集群

```shell
sudo $HADOOP_HOME/sbin/start-dfs.sh
sudo $HADOOP_HOME/sbin/start-yarn.sh
sudo $HADOOP_HOME/sbin/mr-jobhistory-daemon.sh start historyserver
```

## [初始化元数据库](https://www.cnblogs.com/koiiok/p/12984788.html)

```sh
schematool -dbType mysql -initSchema
```

```sh
[vagrant@hdp-node-01 ~]$ schematool -dbType mysql -initSchema
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/home/vagrant/apache-hive-2.3.7-bin/lib/log4j-slf4j-impl-2.6.2.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/home/vagrant/hadoop/share/hadoop/common/lib/slf4j-log4j12-1.7.25.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]
Metastore connection URL:	 jdbc:mysql://192.168.33.102:3306/hive?createDatabaseIfNotExist=true
Metastore Connection Driver :	 com.mysql.jdbc.Driver
Metastore connection User:	 hive
Sun Jun 28 06:07:53 UTC 2020 WARN: Establishing SSL connection without server's identity verification is not recommended. According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.
Starting metastore schema initialization to 2.3.0
Initialization script hive-schema-2.3.0.mysql.sql
Sun Jun 28 06:07:54 UTC 2020 WARN: Establishing SSL connection without server's identity verification is not recommended. According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.
Initialization script completed
Sun Jun 28 06:07:56 UTC 2020 WARN: Establishing SSL connection without server's identity verification is not recommended. According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.
schemaTool completed
```



#### 启动metastore

```shell
hive --service metastore & #程序在后台进行
```



#### 启动hiveserver???

```
hive --service hiveserver & #程序在后台进行
```



#### Update hive-site.xml

modify hive-site.xml

replace all occurrences of ${system:java.io.tmpdir} with /home/vagrant/hive/iotmp and also create this folder manually.

#### 启动hive











--------

### References

* https://blog.csdn.net/danykk/article/details/80137223
* 