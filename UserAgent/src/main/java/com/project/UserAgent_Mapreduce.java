package com.project;

import com.kumkee.userAgent.UserAgent;
import com.kumkee.userAgent.UserAgentParser;
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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgent_Mapreduce {

    static Map<String,Integer> map =new HashMap();
    private static class MyMapper extends Mapper<LongWritable,Text,Text,LongWritable>{
        LongWritable one= new LongWritable(1);

        private UserAgentParser userAgentParser;
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            userAgentParser = new UserAgentParser();
        }

        @Override
        protected void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException {
            //接收到的每一行数据：一行记录
            String line = value.toString();

            String source = line.substring(getInfo(line,"\"",7));
            UserAgent agent = userAgentParser.parse(source);
            String browser = agent.getBrowser();
            context.write(new Text(browser), one);


        }

        private static int getInfo(String line, String operator, int index){
            Pattern pattern = Pattern.compile(operator);
            Matcher matcher = pattern.matcher(line);
            int mIdx = 0;
            String substr = "";
            while (matcher.find()){
                mIdx++;
                if (mIdx == index){
                    break;
                }
            }
            return matcher.start();

        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            userAgentParser=null;
        }
    }
    private static class MyReudcer extends Reducer<Text,LongWritable,Text,LongWritable>{


        @Override
        protected void reduce(Text key,Iterable<LongWritable> values,Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable value:values){
                sum += value.get();
            }

            context.write(key,new LongWritable(sum));
        }

    }

    public static void main(String[] args) throws Exception{
        Configuration configuration = new Configuration();
        Path path = new Path(args[1]);
        FileSystem fileSystem = FileSystem.get(configuration);
        if(fileSystem.exists(path)){
            fileSystem.delete(path,true);
        }
        //创建Job
        Job job = Job.getInstance(configuration,"UserAgent_Mapreduce");

        //处理Job的处理类
        job.setJarByClass(UserAgent_Mapreduce.class);

        //set input path
        FileInputFormat.setInputPaths(job,new Path(args[0]));

        job.setMapperClass(UserAgent_Mapreduce.MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setReducerClass(UserAgent_Mapreduce.MyReudcer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //set output path
        FileOutputFormat.setOutputPath(job,new Path(args[1]));


    }
}












