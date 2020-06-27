### [各种SQL在PIG中实现]([http://guoyunsky.iteye.com/blog/1317084](https://www.iteye.com/blog/1317084))

  我这里以Mysql 5.1.x为例,Pig的版本是0.8

  同时我将数据放在了两个文件,存放在/tmp/data_file_1和/tmp/data_file_2中.文件内容如下:

**tmp_file_1:**

```
zhangsan  23 1 
lisi  24 1 
wangmazi  30 1 
meinv  18 0 
dama  55 0 
```

**tmp_file_2:**

```
1  a 
23 bb 
50 ccc 
30 dddd 
66 eeeee
```

 

​    **1.从文件导入数据**

​     1)Mysql (Mysql需要先创建表).

```mysql
CREATE TABLE TMP_TABLE(USER VARCHAR(32),AGE INT,IS_MALE BOOLEAN);
CREATE TABLE TMP_TABLE_2(AGE INT,OPTIONS VARCHAR(50));  -- 用于Join
LOAD DATA LOCAL INFILE '/tmp/data_file_1'  INTO TABLE TMP_TABLE ;
LOAD DATA LOCAL INFILE '/tmp/data_file_2'  INTO TABLE TMP_TABLE_2;
```

​     2)Pig

```pig
tmp_table = LOAD '/tmp/data_file_1' USING PigStorage('\t') AS (user:chararray, age:int,is_male:int);

tmp_table_2= LOAD '/tmp/data_file_2' USING PigStorage('\t') AS (age:int,options:chararray);
```

**2.查询整张表**

​     1)Mysql

```mysql
      SELECT * FROM TMP_TABLE;
```

​     2)Pig

```
   DUMP tmp_table;
```

   **3. 查询前50行**

​     1)Mysql

```mysql
 SELECT * FROM TMP_TABLE LIMIT 50;
```

​     2)Pig

```
tmp_table_limit = LIMIT tmp_table 50;
DUMP tmp_table_limit; 
```

   **4.查询某些列**

​    1)Mysql

```mysql
  SELECT USER FROM TMP_TABLE;
```

​    2)Pig

```
	tmp_table_user = FOREACH tmp_table GENERATE user;
	DUMP tmp_table_user;
```

  **5. 给列取别名**

​    1)Mysql

```mysql
   SELECT USER AS USER_NAME,AGE AS USER_AGE FROM TMP_TABLE;
```

​    2)Pig

```
	tmp_table_column_alias = FOREACH tmp_table GENERATE user AS user_name,age AS user_age;

	DUMP tmp_table_column_alias; 
```

 

**6.排序**

​    1)Mysql

```mysql
  SELECT * FROM TMP_TABLE ORDER BY AGE;
```

​    2)Pig

```
	tmp_table_order = ORDER tmp_table BY age ASC;

	DUMP tmp_table_order;
```

 

   **7.条件查询**

​    1）Mysql

```mysql
  SELECT * FROM TMP_TABLE WHERE AGE>20;
```

​    2) Pig

```
	tmp_table_where = FILTER tmp_table by age > 20;

	DUMP tmp_table_where;
```

 

   **8.内连接Inner Join**

​    1)Mysql

```mysql
   SELECT * FROM TMP_TABLE A JOIN TMP_TABLE_2 B ON A.AGE=B.AGE;
```

​    2)Pig

```
	tmp_table_inner_join = JOIN tmp_table BY age,tmp_table_2 BY age;

	DUMP tmp_table_inner_join;
```

  **9.左连接Left  Join**

​    1)Mysql

```mysql
  SELECT * FROM TMP_TABLE A LEFT JOIN TMP_TABLE_2 B ON A.AGE=B.AGE;
```

​    2)Pig

```
	tmp_table_left_join = JOIN tmp_table BY age LEFT OUTER,tmp_table_2 BY age;

	DUMP tmp_table_left_join;
```

  **10.右连接Right Join**

​     1)Mysql

```mysql
  SELECT * FROM TMP_TABLE A RIGHT JOIN TMP_TABLE_2 B ON A.AGE=B.AGE;
```

​     2)Pig

```
	tmp_table_right_join = JOIN tmp_table BY age RIGHT OUTER,tmp_table_2 BY age;

	DUMP tmp_table_right_join;
```

  **11.全连接Full Join**

​     1)Mysql

```mysql
	SELECT * FROM TMP_TABLE A  JOIN TMP_TABLE_2 B ON A.AGE=B.AGE
	UNION SELECT * FROM TMP_TABLE A LEFT JOIN TMP_TABLE_2 B ON A.AGE=B.AGE
	UNION SELECT * FROM TMP_TABLE A RIGHT JOIN TMP_TABLE_2 B ON A.AGE=B.AGE;
```

​     2)Pig

```
	tmp_table_full_join = JOIN tmp_table BY age FULL OUTER,tmp_table_2 BY age;
	DUMP tmp_table_full_join;
```



**12.同时对多张表交叉查询**

​     1)Mysql

```
	SELECT * FROM TMP_TABLE,TMP_TABLE_2;
```

​     2)Pig

```
	tmp_table_cross = CROSS tmp_table,tmp_table_2;

	DUMP tmp_table_cross;
```

 

   **13.分组GROUP BY**

​     1)Mysql

```mysql
	SELECT * FROM TMP_TABLE GROUP BY IS_MALE;
```

​     2)Pig

```
	tmp_table_group = GROUP tmp_table BY is_male;
	
	DUMP tmp_table_group;
```

   **14.分组并统计**

​      1)Mysql

```mysql
	SELECT IS_MALE,COUNT(*) FROM TMP_TABLE GROUP BY IS_MALE;
```

​      2)Pig

```
	tmp_table_group_count = GROUP tmp_table BY is_male;

	tmp_table_group_count = FOREACH tmp_table_group_count GENERATE group,COUNT($1);

	DUMP tmp_table_group_count;
```

 

   **15.查询去重DISTINCT**

​      1)MYSQL

```mysql
	SELECT DISTINCT IS_MALE FROM TMP_TABLE;
```

​      2)Pig

```
	tmp_table_distinct = FOREACH tmp_table GENERATE is_male;
	
	tmp_table_distinct = DISTINCT tmp_table_distinct;

	DUMP  tmp_table_distinct;
```

