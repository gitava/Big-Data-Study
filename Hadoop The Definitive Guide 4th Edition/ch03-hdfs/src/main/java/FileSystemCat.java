
// cc FileSystemCat Displays files from a Hadoop filesystem on standard output by using the FileSystem directly
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

// vv FileSystemCat
public class FileSystemCat {

	public static void main(String[] args) throws Exception {
		String uri = args[0];
		Configuration conf = new Configuration();
		System.out.println("Current URI: " + uri);
		System.out.println("Current configuration: " + conf);

		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		
		InputStream in = null;
		
		try {
			in = fs.open(new Path(uri));
			//屏幕标准输出
			//4096 标准缓冲区大小
			IOUtils.copyBytes(in, System.out, 4096, false);
		} finally {
			IOUtils.closeStream(in);
		}
	}
}
// ^^ FileSystemCat
