package com.imooc.hadoop;


import org.apache.hadoop.util.Progressable;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

/**
 *
 * HADOOP HDFS JAVA API
 * */
public class HDFSAPP {
    public static final String HDFS_PATH="hdfs://hadoop000:8020";

    FileSystem fileSystem = null;
    Configuration configuration = null;

    @Before
    public void setUp() throws Exception{
        System.out.println("HDFSApp.setUp");
        configuration = new Configuration();
        fileSystem = FileSystem.get(URI.create(HDFS_PATH),configuration,"hadoop");
    }

/**
 * 创建HDFS目录
 * */
    @Test
    public void mkdir() throws Exception{
        System.out.println("HDFSApp.mkdir");
        fileSystem.mkdirs(new Path("/hdfsapi/test"));
    }

    /**
     * 写文件
     * */
    @Test
    public void create() throws Exception{
        FSDataOutputStream output = fileSystem.create(new Path("/hdfsapi/test/a.txt"));
        output.write("hello,world".getBytes());
        output.flush();
        output.close();
    }

    /**
     * 查看文件内容
     * */
    @Test
    public void cat() throws Exception{
        FSDataInputStream input = fileSystem.open(new Path("/hdfsapi/test/a.txt"));
        IOUtils.copyBytes(input,System.out,1024);
        System.out.println();
        input.close();
    }
    /**
     * 重命名
     * */
    @Test
    public void rename() throws Exception{
        Path oldPath = new Path("/hdfsapi/test/a.txt");
        Path newPath = new Path("/hdfsapi/test/b.txt");
        fileSystem.rename(oldPath,newPath);
    }
    /**
     * 上传文件到hadoop
     * */
    @Test
    public void copyFromLocalFile() throws Exception{
        Path localFile = new Path("/Users/renyiting/Desktop/opt");
        Path hadoopFile=new Path("/hdfsapi/test/");
        fileSystem.copyFromLocalFile(localFile,hadoopFile);
    }

    @Test
    public void copyFromLocalFileWithProgress() throws Exception{
        InputStream in =new BufferedInputStream(
                new FileInputStream(new File("/Users/renyiting/Desktop/opt/I94.pdf"))
        );
        FSDataOutputStream output = fileSystem.create(new Path("/hdfsapi/test/optt.pdf"), new Progressable() {
            @Override
            public void progress() {
                System.out.println('.');//带进度提醒
            }
        });
        IOUtils.copyBytes(in,output,4096);
    }

    /**
     * 下载HDFS文件
     * */

    @Test
    public void DownloadFromHDFS() throws Exception{
        Path localPath = new Path("/Users/renyiting/Desktop/opt/I94.pdf");
        Path hdfsPath =  new Path("/hdfsapi/test/optt.pdf");
        fileSystem.copyFromLocalFile(localPath,hdfsPath);
    }
    /**
     * 列出目录下的所有文件
     * */
    @Test
    public void listFiles() throws Exception{
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/hdfsapi/test/"));
        for(FileStatus fileStatus: fileStatuses){
            String isDir = fileStatus.isDirectory()?"文件夹":"文件";
            short replication = fileStatus.getReplication();
            long len = fileStatus.getLen();
            String path =fileStatus.getPath().toString();

            System.out.println(isDir+"\t"+replication+"\t"+len+"\t"+path);
        }

    }
    /**
     * 删除
     * */
    @Test
    public void delete() throws Exception{
        fileSystem.delete(new Path(""),true);
    }
    @After
    public void tearDown() throws Exception{
        fileSystem = null;
        configuration= null;
        System.out.println("HDFSApp.tearDown");

    }



}
