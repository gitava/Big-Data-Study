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


export HIVE_HOME=/home/vagrant/hive
export PATH=$PATH:$HIVE_HOME/bin
export CLASSPATH=$CLASSPATH:$HIVE_HOME/lib