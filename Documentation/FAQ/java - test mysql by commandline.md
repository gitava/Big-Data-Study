
```java
import java.sql.*;
import java.util.*;
 
public class JDBCTest {
	public static void main(String[] args) throws SQLException, ClassNotFoundException{
		// Load Driver
		Class.forName("com.mysql.jdbc.Driver");
		
		// Build Connection
		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.33.102:3306/hive?createDatabaseIfNotExist=true", "hive", "!mySQL123!");
		
		// Create Statement
		Statement stat = conn.createStatement();
		
		// Execute Sql
		ResultSet rs = stat.executeQuery("select * from person");

		System.out.println(rs.toString());
		
	}
}

/*
export CLASSPATH=$CLASSPATH:/home/vagrant/hive/lib/mysql-connector-java.jar:/home/vagrant
#if the current folder is /home/vagrant,then this folder needs to be part of the CLASSPATH,
# otherwise, java won't be able to find the compiled class file.
cd /home/vagrant
javac -d . JDBCTest.java
java JDBCTest  -classpath $CLASSPATH 
*/
```