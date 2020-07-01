  将 .DS_Store 加入到 .gitignore
>  2.`echo .DS_Store >> ~/.gitignore`
>  更新项目
>  3.`git add --all`
>  4.`git commit -m '.DS_Store banished!'`


.gitignore file 

if the contents like 

./.DS_Store : then it only ignored one file

.DS_Store : this will ingore all occurrances of this file in the repo.

----
https://www.jianshu.com/p/fdaa8be7f6c3