### vagrant学习笔记 - provision

从字面上来看，provision是准备，实现的功能是在原生镜像的基础上，进行一些附加的操作，以改变虚拟机的环境，比如安装应用，发布程序等。

1 helloword

在vagrant的 Vagrant.configure(2) do |config| 节点内，加入如下代码：

  config.vm.provision "shell", inline: "echo hello provisio."
1
还有一种格式：

  config.vm.provision "shell" do |s|
    s.inline = "echo hello provision."
  end

测试一下：

如果vm已经启动，直接运行

vagrant provision

就可以看到控制台显示的信息了。或者：

vagrant reload --provision

重启vm，并自动执行provision操作。

Tips: 运行后可能会提示：default: stdin: is not a tty 错误，不影响执行效果，想要去除，在配置文件增加一行即可。

config.ssh.shell = "bash -c 'BASH_ENV=/etc/profile exec bash'"

什么是provision任务

provision任务是预先设置的一些操作指令，格式：

config.vm.provision 命令字 json格式参数

config.vm.provion 命令字 do |s|
    s.参数名 = 参数值
end

每一个 config.vm.provision 命令字 代码段，我们称之为一个provisioner。

根据任务操作的对象，provisioner可以分为：

Shell
File
Ansible
CFEngine
Chef
Docker
Puppet
Salt
根据vagrantfile的层次，分为：

configure级：它定义在 Vagrant.configure("2") 的下一层次，形如： config.vm.provision ...
vm级：它定义在 config.vm.define "web" do |web| 的下一层次，web.vm.provision ...
执行的顺序是先执行configure级任务，再执行vm级任务，即便configure级任务在vm定义的下面才定义。例如：

Vagrant.configure("2") do |config|
  config.vm.provision "shell", inline: "echo foo"

  config.vm.define "web" do |web|
    web.vm.provision "shell", inline: "echo bar"
  end

  config.vm.provision "shell", inline: "echo baz"
end

输出结果：

==> default: "foo"
==> default: "baz"
==> default: "bar"

2 如何执行provision任务

尝试了helloword，我们来了解一下provision任务是怎么运行的。

启动时自动执行，缺省地，任务只执行一次，第二次启动就不会自动运行了。如果需要每次都自动运行，需要为provision指定run:"always"属性
启动时运行，在启动命令加 --provision 参数,适用于 vagrant up 和 vagrant reload
vm启动状态时，执行 vagrant provision 命令。
在编写provision任务时，可能同时存在几种类型的任务，但执行时可能只执行一种，如，我只执行shell类型的任务。可以如下操作：

vagrant provision --provision-with shell
1
3 shell

3.1 基本使用

3.1.1 数据类型

类型名	范例
string	“arg1”
hash	{key1:value1,key2:value2}
array	[“arg1”,”arg2”]
boolean	true,false
3.1.2 参数：

必选：inline 或者 path

可选：

参数名	类型	说明
args	string or array	传递给shell或path的参数
env	hash	传递给脚本的环境变量
binary	boolean	是否替换windows的行结束符，这个参数名有点奇怪
privileged	boolean	是否提权运行，如sudo执行，缺省为true
upload_path	boolean	上传到vm中的路径，缺省是/tmp/vagrant-shell
keep_color	boolean	设置是否脚本自身控制颜色，缺省为false，表示使用绿色和红色来显示输出到stdout和stderr的消息
name	string	给当前执行的脚本命名，与provisioner名称无关
powershell_args	string	windows相关，略
powershell_elevated_interactive	boolean	windows相关，略
在有些情况下，vagrant会帮你处理引号，但建议，都匹配好双引号，可读性也好一些。

3.1.3 使用规则

3.1.3.1 单行脚本

helloword只是一个开始，对于inline模式，命令只能在写在一行中。一个以上的命令，可以写在同一行，用分号分隔，这属于shell编程的范畴，在这里不多解释。

单行脚本使用的基本格式：

config.vm.provision "shell", inline: "echo foo"
1
shell命令的参数还可以写入do ... end代码块中，如下：

config.vm.provision "shell" do |s|
  s.inline = "echo hello provision."
end
1
2
3
其他，如后面提到的path参数也是一样的。

一个shell代码段，在1.7.0+版本，这个provisioner是可以命名的，如：

config.vm.provision "myshell,type:"shell" do |s|
  s.inline = "echo hello provision."
end

Tips: provisioner命名块如果重名，会有覆盖问题。

一个shell节点内，如果连续写一条以上s.inline,则只有最后一条有效，前面的会被后面的覆盖掉。

3.1.3.2 内联脚本

如果要执行脚本较多，可以在Vagrantfile中指定内联脚本，在Vagrant.configure节点外面，写入命名内联脚本：

$script = <<SCRIPT
echo I am provisioning...
echo hello provision.
SCRIPT
1
2
3
4
然后，inline调用如下：

  config.vm.provision "shell", inline: $script
1
3.1.3.3 外部脚本

也可以把代码写入代码文件，并保存在一个shell里，进行调用：

  config.vm.provision "shell", path: "script.sh"
1
script.sh的内容：

echo hello provision.
1
效果是一样的。

Tips：path可以使用http/https来访问远程脚本，这个在部署时访问一个脚本仓库来做一些通用的操作，比较方便。如：

  config.vm.provision "shell", path: "https://example.com/provisioner.sh"
1
Tips: 脚本文件在host机器上，而脚本实际上是在vm里运行的，做个测试验证一下，在Vagrant.configure节点外面，写入命名内联脚本：

```
$script = <<SCRIPT
echo I am provisioning...
date > /etc/vagrant_provisioned_at
SCRIPT
```


1
2
3
4
检查结果， /etc/vagrant_provisioned_at 这个文件不在host主机里，而是在vm虚机里。

3.2 脚本参数

如果要执行的脚本需要参数，那么使用args属性进行传递：

```
  config.vm.provision "shell" do |s|
    s.inline = "echo $1"
    s.args   = "'hello, world!'"
  end
```


Tips: 这两args有两层引号，如果去掉一层，字符串中的逗号会让shell以为是两个参数，那么后面的world会被丢掉。 
你也可以使用方括号作为外层分隔符，而内层分隔符使用单引号或双引号都可以，只要前后匹配即可，如：

Tips: 这两args有两层引号，如果去掉一层，字符串中的逗号会让shell以为是两个参数，那么后面的world会被丢掉。 
你也可以使用方括号作为外层分隔符，而内层分隔符使用单引号或双引号都可以，只要前后匹配即可，如：

s.args   = ["hello, world!"]
1
Tips: 多个参数，你可以把args理解为一个json 数组，只要在inline里用 $x 进行引用即可。

2.3 环境变量

为命令行指定环境变量，env的格式为hash，是一个hash对象的列表，多个环境变量，多次配置env。 
如：

```
  config.vm.provision "shell" do |s|
    s.inline = "echo $1 $JAVA_HOME"
    s.env = {JAVA_HOME:"/opt/java"}
    s.args   = ["java_home is "]
  end
```


执行结果：

==> default: java_home is /opt/java

多个环境变量的例子：

  config.vm.provision "shell" do |s|
    s.inline = "echo $1 $JAVA_HOME $2 $PATH"
    s.env = {JAVA_HOME:"/opt/java",PATH:"$JAVA_HOME/bin:$PATH"}
    s.args   = ["java_home is ",";PATH ="]
  end

执行结果：

==> default: java_home is /opt/java ;PATH = /opt/java/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games
1
Tips: 环境变量可以引用已经存在的环境变量，如 PATH:"/opt/java/bin:$PATH",结果是在原有的PATH环境变量前面增加了一个路径。

Tips: env新增的环境变量，是顺序执行赋值操作的，实例中JAVA_HOME，系统中原来是没有的，如果JAVA_HOME和PATH这两个参数顺序换一下，把JAVA_HOME放在后面，PATH在拼接JAVA_HOME的时候取到的是系统原来的值，这里是null。

  config.vm.provision "shell" do |s|
    s.inline = "echo $1 $JAVA_HOME $2 $PATH"
    s.env = {PATH:"$JAVA_HOME/bin:$PATH",JAVA_HOME:"/opt/java"}
    s.args   = ["java_home is ",";PATH ="]
  end
1
2
3
4
5
结果是： 
==> default: java_home is /opt/java ;PATH = /bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games

这里出错了，本来应该是/opt/java/bin，结果变成了/bin。

同样，如果在系统的/etc/profile中加入：export JAVA_HOME=/usr/local/jdk

那么上面例子的执行结果将是：

==> default: java_home is /usr/local/jdk ;PATH = /usr/local/jdk/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games
1
Tips: provision里设置的环境变量，只对provision自身操作有效，vagrant ssh 登录vm，里边的变量值是不会变的。

4 文件操作

file 操作有两个参数：

source : 源文件或文件夹
destination : 目标文件或文件
  config.vm.provision "file", source: "./Vagrantfile", destination: "Vagrantfile"
1
将host主机的 “./Vagrantfile” 上传到 vm虚拟机的目标文件 “./Vagrantfile” 。

Tips: 文件是通过scp上传到vm的，使用的是缺省用户，可使用vagrant ssh-config 查看缺省用户的名称，一般为vagrant。所以，目的路径需要让默认用户拥有写权限。

5 扩展操作

vagrant可以集成其他服务器运维工具，来增强服务器管理能力。在使用这些技术之前，需要系统地学习这些技术。而每一套系统都有很多内容学习。本文只简单介绍，不做详细展开。

5.1 集群管理，自动化配置等系统

ansible,cfengine,Chef,puppet 
每一套系统都可以写本书了，所以这里不详细说明。

简单来说 Ansible 是一个极简化的应用和系统部署工具，类似 Puppet、Chef、SaltStack。由于默认使用 ssh 管理服务器（集群），配置文件采用 yaml 而不是某一种特定语言制定。 
cfengine是一个Linux的自动化配置系统。 
Chef 是一套Linux的配置管理系统。

5.2 Docker 面向容器的虚拟解决方案

Docker 是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的容器中，然后发布到任何流行的 Linux 机器上。docker不会虚拟guest os系统，几乎没有性能开销，最重要的是,他们不依赖于任何语言、框架包括系统。

Docker自身提供了很多优秀的工具和客户端，目前vagant支持的操作并不方便，建议直接使用docker的客户端工具学习和使用docker。

5.3 Salt

Salt 是一个强大的远程执行管理器，用于快速和高效的服务器管理。

更多，请参考：https://www.vagrantup.com/docs/provisioning/
————————————————
版权声明：本文为CSDN博主「54powerman」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/54powerman/java/article/details/50684844