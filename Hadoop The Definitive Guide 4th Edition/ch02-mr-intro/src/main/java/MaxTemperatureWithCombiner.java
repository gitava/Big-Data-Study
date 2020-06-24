/*
 * How to run under Eclipse with the following arguments under "Run As"
 * hdfs://192.168.33.101:9000/input/A51256-00451-2020 hdfs://192.168.33.101:9000/output
 */

// cc MaxTemperatureWithCombiner Application to find the maximum temperature, 
// using a combiner function for efficiency

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

// vv MaxTemperatureWithCombiner
public class MaxTemperatureWithCombiner {

	public static void main(String[] args) throws Exception {
		
		//Path is like hdfs://192.168.33.101:9000/output
		if (args.length != 2) {
			System.err.println("Usage: MaxTemperatureWithCombiner <input path> " + "<output path>");
			System.exit(-1);
		}

//		Job job = new Job();
		//Job is deprecated in Hadoop 2.9.2, changed it to getInsance instead.
		Job job= Job.getInstance();
		job.setJarByClass(MaxTemperatureWithCombiner.class);
		job.setJobName("Max temperature");

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(MaxTemperatureMapper.class);
		/* [ */job.setCombinerClass(MaxTemperatureReducer.class)/* ] */;
		job.setReducerClass(MaxTemperatureReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
// ^^ MaxTemperatureWithCombiner
