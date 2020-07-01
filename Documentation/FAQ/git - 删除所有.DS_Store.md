## 删除 .DS_Store

如果你的项目中还没有自动生成的 .DS_Store 文件，那么直接将 .DS_Store 加入到 .gitignore 文件就可以了。如果你的项目中已经存在 .DS_Store 文件，那就需要先从项目中将其删除，再将它加入到 .gitignore。如下：

> 删除项目中的所有.DS_Store。这会跳过不在项目中的 .DS_Store
>  1.`find . -name .DS_Store -print0 | xargs -0 git rm -f --ignore-unmatch`
>  将 .DS_Store 加入到 .gitignore
>  2.`echo .DS_Store >> ~/.gitignore`
>  更新项目
>  3.`git add --all`
>  4.`git commit -m '.DS_Store banished!'`



作者：iOSReverse
链接：https://www.jianshu.com/p/fdaa8be7f6c3
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。