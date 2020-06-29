#!/bin/bash

# this is a onetime-off script 
# contents can be put here whenever needed and run as 
# * 启动时自动执行，缺省地，任务只执行一次，第二次启动就不会自动运行了。
# 	如果需要每次都自动运行，需要为provision指定run:"always"属性
# * 启动时运行，在启动命令加 --provision 参数,适用于 vagrant up 和 vagrant reload
# * vm启动状态时，执行 vagrant provision 命令
# * 只执行shell类型的任务。可以如下操作：vagrant provision --provision-with shell

echo "Love you!"