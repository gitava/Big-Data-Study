This file contains origin defintion as remote

**Path:** 
	`${repo}/.git/config`

```
[core]
	repositoryformatversion = 0
	filemode = true
	logallrefupdates = true
	precomposeunicode = true
[remote "origin"]
	url = https://github.com/gitava/Big-Data-Study.git
	fetch = +refs/heads/*:refs/remotes/origin/*
[branch "master"]
	remote = origin
	merge = refs/heads/master
	rebase = false
```

