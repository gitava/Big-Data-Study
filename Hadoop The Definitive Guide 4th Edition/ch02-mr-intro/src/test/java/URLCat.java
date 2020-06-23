import java.io.InputStream;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

/**
 * on hadoop javac -classpath
 * ~/hadoop/share/hadoop/common/hadoop-common-2.9.2.jar ~/myclass/URLCat.java
 * 
 * 
 * 
 * @author fllbeaver
 *
 */
public class URLCat {
	static {
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}

	public static void main(String[] args) throws Exception {
		InputStream in = null;

		try {
			if (args.length == 0) {
				// if run on Eclipse w/o args
				System.out.println("Running without args " + args.length);
				in = new URL("hdfs://192.168.33.101:9000/wordcount/input/somewords.txt").openStream();
			} else {
				System.out.println("Running withargs " + args.length);
				in = new URL(args[0]).openStream();
			}
			IOUtils.copyBytes(in, System.out, 4096, false);
		} finally{
			IOUtils.closeStream(in);
		}
	}

}