package com.imooc.hadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * WordCount by using mapreduce
 * */
public class WordCount2App {
    /**
     * map read file
     */
    public static class MyMapper extends Mapper<LongWritable,Text,Text,LongWritable>{
        LongWritable one = new LongWritable(1);

        @Override
        protected void map(LongWritable Key,Text value,Context context) throws IOException, InterruptedException {
            String line = context.toString();
            String[] words = line.split(" ");
            for (String word: words){
                context.write(new Text(word),one);
            }
        }

    }

    public static class MyReducer extends Reducer<Text, LongWritable,Text,LongWritable>{
        @Override
        protected void reduce(Text Key,Iterable<LongWritable> values,Context context)throws IOException,InterruptedException{
            long sum =0;
            for(LongWritable value: values){
                sum += value.get();
            }
            context.write(Key,new LongWritable(sum));
        }


    }
    public void main(String[] args) throws Exception{
        // Configuration
        Configuration configuration = new Configuration();

        Path outputPath = new Path(args[1]);
        //clean exist output file
        FileSystem fileSystem = FileSystem.get(configuration);
        if(fileSystem.exists(outputPath)){
            fileSystem.delete(outputPath, true);
        }
        // create Job
        Job job = Job.getInstance(configuration,"WordCount");
        //set Job class
        job.setJarByClass(WordCountApp.class);

        //set Path
        FileInputFormat.setInputPaths(job,new Path(args[0]));

        // map
        job.setMapperClass(MyMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //Reduce
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //set output path
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

    }

}
