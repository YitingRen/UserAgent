package com.imooc.hadoop.mapreduce;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;

/**
 * WordCount by using mapreduce
 * */
public class WordCountApp {
    /**
     * map read file
     */

    public static class Mymapper extends Mapper<LongWritable,Text,Text,LongWritable>{
        LongWritable one = new LongWritable(1);
        @Override
        protected void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] words = line.split(" ");

            for (String word: words){
                context.write(new Text(word),one);
            }
        }
    }
    /**
     * together
     * */
    public static class MyReducer extends Reducer<Text, LongWritable, Text, LongWritable>{
        @Override
        protected void reduce(Text key,Iterable<LongWritable> values, Context context) throws IOException, InterruptedException{
        long sum = 0;
        for (LongWritable value:values){
            sum += value.get();
        }

        context.write(key, new LongWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception{
        //building configuration
        Configuration configuration = new Configuration();

        //Create Job

        Job job = Job.getInstance(configuration,"wordCount");

        // set class for job
        job.setJarByClass(WordCountApp.class);

        // set path
        FileInputFormat.setInputPaths(job , new Path(args[0]));

        //set parameters for map
        job.setMapperClass(Mymapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //ser job assign output path
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.out.println(job.waitForCompletion(true)?0:1);

    }

}
