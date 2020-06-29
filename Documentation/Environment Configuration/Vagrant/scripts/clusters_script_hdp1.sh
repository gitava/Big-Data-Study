#!/bin/bash

# hdp1 special scripts
# at /home/vagrant

#---hosts---
# To work with eclipse-hadoop-plugin,
# needs to comment-out 127.0.0.1 part on hdp-node-01
# will edit this file later to add it.
cat > /etc/hosts <<EOF
#127.0.0.1	hdp-node-01	hdp-node-01
#127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

192.168.33.101  hdp-node-01
192.168.33.102  hdp-node-02
192.168.33.103  hdp-node-03
192.168.33.104  hdp-node-04