### [eclipse更新maven项目JRE被更改版本的问题](https://blog.csdn.net/weixin_34015860/article/details/89756865?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase)

在pom.xml文件中增加下面内容

```xml
<build>  
    <plugins>  
        <!-- define the project compile level -->  
        <plugin>  
          <groupId>org.apache.maven.plugins</groupId>  
          <artifactId>maven-compiler-plugin</artifactId>  
            <version>2.3.2</version>  
            <configuration>  
               <source>1.8</source>  
              <target>1.8</target>  
            </configuration>  
        </plugin>  
    </plugins>  
</build>  
```

* 其中的1.8是本机安装的JDK的版本，请与你自己安装的JDK版本相对应。



然后再更新maven项目就可以了。【选择 maven -> Update Project】

---

## Working Example

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.gitava.xxx</groupId>
	<artifactId>xxx-module</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<build>
		<plugins>
			<!-- define the project compile level -->
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>2.3.2</version>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>
```

