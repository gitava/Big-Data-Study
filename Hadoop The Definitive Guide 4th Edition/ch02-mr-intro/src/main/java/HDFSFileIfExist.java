import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSFileIfExist {
	public static void main(String[] args) {
		try{
		
			//加载配置项
			Configuration conf = new Configuration();
			conf.set("fs.defaultFS", "hdfs://localhost:9000");
			conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
			
			//创建文件系统实例
			FileSystem fs = FileSystem.get(conf);
			
			//创建文件实例
			String fileName = "文件或目录名";
			Path file = new Path(fileName);
			
			//判断文件是否存在
			if(fs.exists(file)){
				System.out.println(fileName + "存在");
			}else{
				System.out.println(fileName + "不存在");
			}
			
			//关闭文件系统
			fs.close();
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
//
//————————————————
//版权声明：本文为CSDN博主「stepondust」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//原文链接：https://blog.csdn.net/qq_44009891/article/details/104358705