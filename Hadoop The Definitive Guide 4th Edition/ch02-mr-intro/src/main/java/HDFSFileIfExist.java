import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * Usage HDFSFileIfExist [path]
 * 
 * - relative_path:from hdfs://192.168.33.101:9000/
 * 
 * Modified by: fllbeaver
 *
 */
public class HDFSFileIfExist {

	//Set a default path
	private static String path_base = "hdfs://192.168.33.101:9000";
	private static final String HDFS_IMPL="org.apache.hadoop.hdfs.DistributedFileSystem";

	public static String getPathBase() {
		return path_base;
	}

	public static void setPathBase(String pathBase) {
		path_base = pathBase;
	}

	private static String parsePathBase(String path) {
		int start = path.indexOf("//");
		int end = path.indexOf("/", start + 2);

//		System.out.println("start: " + start);
//		System.out.println("end: " + end);
//		System.out.println(path);

		return path.substring(0, end);
	}

	public static void main(String[] args) {

		String fileName = null;

		if (args.length != 0) {
			// Check if it's a absolute path provied or relative path only
			if (args[0].substring(0, 4).toLowerCase().equals(new String("hdfs"))) {
				fileName = args[0];
				System.out.println("Absolute path is provided.");

				// To get the BASE part of the path
				setPathBase(parsePathBase(args[0]));
				System.out.println("PATH_BASE: " + path_base);

			} else {
				fileName = path_base + "/" + args[0];
				System.out.println("Relative path is provided.");

			}

		} else {
			System.out.println("No path is provided.");
			fileName = path_base + "/" + "output";
		}

		try {
			// 加载配置项
			Configuration conf = new Configuration();
			conf.set("fs.defaultFS", getPathBase());
			conf.set("fs.hdfs.impl", HDFS_IMPL);

			// 创建文件系统实例
			FileSystem fs = FileSystem.get(conf);

			// 创建文件实例
//			String fileName = "文件或目录名";
			Path file = new Path(fileName);
//			Path file = new Path("hdfs://192.168.33.101:9000/output/part-r-00000");
//			System.out.println("file: " + file);

			// 判断文件是否存在
			if (fs.exists(file)) {
				System.out.println(fileName + " 存在");
			} else {
				System.out.println(fileName + " 不存在");
			}

			// 关闭文件系统
			fs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
//Based on:
//————————————————
//版权声明：本文为CSDN博主「stepondust」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//原文链接：https://blog.csdn.net/qq_44009891/article/details/104358705