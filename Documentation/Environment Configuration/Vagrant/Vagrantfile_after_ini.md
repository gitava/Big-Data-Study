This is the version used after intial run of vagrant. It's the one used to run the environment most of the time.

```ruby
# -*- mode: ruby -*-
# vi: set ft=ruby :

# Version: Vagrantfile_after_ini

#
#this is for building master
#
#original author:lhy
#time:2018.8.25


Vagrant.configure("2") do |config|
	# change to 3 servers from 4 to save machine resources. 
	(1..3).each do |i|
		#vv For each server - start 
		# node as the variable name of VM
		config.vm.define "hdp#{i}" do |node|
		
			# 设置虚拟机的Box
			node.vm.box = "centos/7"
			# 设置虚拟机的主机名
			node.vm.hostname="hdp-node-0#{i}"
			# 设置虚拟机的IP
			node.vm.network "private_network", ip: "192.168.33.#{100+i}"
			# Special configuraiton for machine hdp-node-01
			if i ==1
				puts "==============================================="
				puts "|Special configuration of machine hdp-node-0#{i}|"
				puts "==============================================="
				# 使用shell脚本进行软件安装和配置 - used only for initial setup
				#node.vm.provision "shell", path: "./scripts/clusters_script"
				#node.vm.provision "shell", path: "./scripts/clusters_script_hdp1"

				# onetime-off scripts
 				node.vm.provision "shell", path: "./scripts/script_onetimeoff_hdp1.sh"

 				#transfer files
 				#node.vm.provision "file", source: "./test", destination: "/home/vagrant/test"
			else
				# Special configuraiton for machine hdp-node-02,03,...
				puts "==============================================="
				puts "|Special configuration of machine hdp-node-0#{i}|"
				puts "==============================================="
				# 使用shell脚本进行软件安装和配置 - used only for initial setup
				#node.vm.provision "shell", path: "./scripts/clusters_script" 
			end

			# 设置主机与虚拟机的共享目录 - this needs " VirtualBox Guest Additions"
			# don't use this by default now.
			# node.vm.synced_folder "./", "/home/vagrant/share"
			# 复制相应的依赖文件

			#packages are disabled for the run after initial setup
			#config.vm.provision "file", source: "./jdk-7u80-linux-x64.tar", destination: "/home/vagrant/jdk-7u80-linux-x64.tar"
			#config.vm.provision "file", source: "./hadoop-2.9.2.tar.gz", destination: "/home/vagrant/hadoop-2.9.2.tar.gz"
			#config.vm.provision "file", source: "./sshd/sshd_config", destination: "/home/vagrant/sshd_config"
			#config.vm.provision "file", source: "./conf", destination: "/home/vagrant/env"
			#config.vm.provision "file", source: "./test", destination: "/home/vagrant/test"

			# VirtaulBox相关配置
			node.vm.provider "virtualbox" do |v|
				# 设置虚拟机的名称
				v.name = "hdp#{i}"

				# 设置虚拟机的内存大小  
				if i==1
					v.memory = 1280
				else
					v.memory = 1024
				end
				# 设置虚拟机的CPU个数
				v.cpus = 1
			end
			#node.vm.provision "shell", path: "./scripts/clusters_script" # 使用shell脚本进行软件安装和配置
		end
		#^^ For each server - end
	end
end
```

