package org.dataguru.hadoop;

import java.io.IOException;

import java.util.Iterator;
import java.util.StringTokenizer;

 

//import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
//import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;



 

public class WordCount {

    public static class TokenizerMapper extends MapReduceBase implements Mapper<Object,Text,Text,IntWritable>{

        private final static IntWritable one=new IntWritable(1);

        private Text word =new Text();
        
        public void map(Object key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                output.collect(word, one);
            }

        }
    }

    
    public static class IntSumReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        
        public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            int sum = 0;
            while (values.hasNext()) {
                sum += values.next().get();
            }
            result.set(sum);
            output.collect(key, result);
        }
    }

    

 

    public static void main(String[] args) throws Exception {

    	String input = "hdfs://192.168.0.110:9000/input/results.txt";
    			
    	String output = "hdfs://192.168.0.110:9000/outputs1";
        
    	JobConf conf = new JobConf(WordCount.class);

    	conf.setJobName("WordCount");
    	
    	conf.setOutputKeyClass(Text.class);
        
    	conf.setOutputValueClass(IntWritable.class);
        
    	conf.setMapperClass(TokenizerMapper.class);

        conf.setCombinerClass(IntSumReducer.class);

        conf.setReducerClass(IntSumReducer.class);

        conf.setInputFormat(TextInputFormat.class);
        
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(input));//Â·¾¶1
        
        FileOutputFormat.setOutputPath(conf, new Path(output));//Êä³öÂ·¾¶

        JobClient.runJob(conf);

        System.exit(0);

    }

}
