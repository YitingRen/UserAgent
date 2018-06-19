# HDFS_Java
HDFS架构

1 Master(NameNode/NN)  带 N个Slaves(DataNode/DN)
HDFS/YARN/HBase

1个文件会被拆分成多个Block
blocksize：128M
130M ==> 2个Block： 128M 和 2M

NN：
1）负责客户端请求的响应
2）负责元数据（文件的名称、副本系数、Block存放的DN）的管理

DN：
1）存储用户的文件对应的数据块(Block)
2）要定期向NN发送心跳信息，汇报本身及其所有的block信息，健康状况

A typical deployment has a dedicated machine that runs only the NameNode software. 
Each of the other machines in the cluster runs one instance of the DataNode software.
The architecture does not preclude running multiple DataNodes on the same machine 
but in a real deployment that is rarely the case.

NameNode + N个DataNode
建议：NN和DN是部署在不同的节点上


replication factor：副本系数、副本因子

All blocks in a file except the last block are the same size







































Hadoop伪分布式安装步骤
1）jdk安装
	解压：tar -zxvf jdk-7u79-linux-x64.tar.gz -C ~/app
	添加到系统环境变量： ~/.bash_profile
		export JAVA_HOME=/home/hadoop/app/jdk1.7.0_79
		export PATH=$JAVA_HOME/bin:$PATH
	使得环境变量生效： source ~/.bash_profile
	验证java是否配置成功： java -v

2）安装ssh
	sudo yum install ssh
	ssh-keygen -t rsa
	cp ~/.ssh/id_rsa.pub ~/.ssh/authorized_keys	

3）下载并解压hadoop
	下载：直接去cdh网站下载
	解压：tar -zxvf hadoop-2.6.0-cdh5.7.0.tar.gz -C ~/app

4）hadoop配置文件的修改(hadoop_home/etc/hadoop)
	hadoop-env.sh
		export JAVA_HOME=/home/hadoop/app/jdk1.7.0_79

	core-site.xml
		<property>
	        <name>fs.defaultFS</name>
	        <value>hdfs://hadoop000:8020</value>
	    </property>

	    <property>
	        <name>hadoop.tmp.dir</name>
	        <value>/home/hadoop/app/tmp</value>
	    </property>

	hdfs-site.xml
		<property>
	        <name>dfs.replication</name>
	        <value>1</value>
	    </property>

	slaves    

5）启动hdfs
	格式化文件系统（仅第一次执行即可，不要重复执行）：hdfs/hadoop namenode -format
	启动hdfs: sbin/start-dfs.sh
	验证是否启动成功：
		jps
			DataNode
			SecondaryNameNode
			NameNode

		浏览器访问方式： http://hadoop000:50070

6）停止hdfs
	sbin/stop-dfs.sh 



Hadoop shell的基本使用
hdfs dfs
hadoop fs



Java API操作HDFS文件
文件	1	311585484	hdfs://hadoop000:8020/hadoop-2.6.0-cdh5.7.0.tar.gz
文件夹	0	0	hdfs://hadoop000:8020/hdfsapi
文件	1	49	hdfs://hadoop000:8020/hello.txt
文件	1	40762	hdfs://hadoop000:8020/install.log

问题：我们已经在hdfs-site.xml中设置了副本系数为1，为什么此时查询文件看到的3呢？
 如果你是通过hdfs shell的方式put的上去的那么，才采用默认的副本系数1
 如果我们是java api上传上去的，在本地我们并没有手工设置副本系数，所以否则采用的是hadoop自己的副本系数
 
 
 
 
 
 
 
 
 Hadoop1.x时：
MapReduce：Master/Slave架构，1个JobTracker带多个TaskTracker

JobTracker： 负责资源管理和作业调度
TaskTracker：
	定期向JT汇报本节点的健康状况、资源使用情况、作业执行情况；
	接收来自JT的命令：启动任务/杀死任务

YARN：不同计算框架可以共享同一个HDFS集群上的数据，享受整体的资源调度

XXX on YARN的好处：
	与其他计算框架共享集群资源，按资源需要分配，进而提高集群资源的利用率
XXX: Spark/MapReduce/Storm/Flink


YARN架构：
1）ResourceManager: RM
	整个集群同一时间提供服务的RM只有一个，负责集群资源的统一管理和调度
	处理客户端的请求： 提交一个作业、杀死一个作业
	监控我们的NM，一旦某个NM挂了，那么该NM上运行的任务需要告诉我们的AM来如何进行处理

2) NodeManager: NM
	整个集群中有多个，负责自己本身节点资源管理和使用
	定时向RM汇报本节点的资源使用情况
	接收并处理来自RM的各种命令：启动Container
	处理来自AM的命令
	单个节点的资源管理

3) ApplicationMaster: AM
	每个应用程序对应一个：MR、Spark，负责应用程序的管理
	为应用程序向RM申请资源（core、memory），分配给内部task
	需要与NM通信：启动/停止task，task是运行在container里面，AM也是运行在container里面

4) Container
	封装了CPU、Memory等资源的一个容器
	是一个任务运行环境的抽象

5) Client
	提交作业
	查询作业的运行进度
	杀死作业






YARN环境搭建
1）mapred-site.xml
<property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
</property>

2）yarn-site.xml
<property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
</property>

3) 启动YARN相关的进程
sbin/start-yarn.sh

4）验证
	jps
		ResourceManager
		NodeManager
	http://hadoop000:8088

5）停止YARN相关的进程
	sbin/stop-yarn.sh


提交mr作业到YARN上运行：
/home/hadoop/app/hadoop-2.6.0-cdh5.7.0/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.6.0-cdh5.7.0.jar

hadoop jar 

hadoop jar hadoop-mapreduce-examples-2.6.0-cdh5.7.0.jar pi 2 3 





















wordcount: 统计文件中每个单词出现的次数

需求：求wc
1) 文件内容小：shell
2）文件内容很大： TB GB   ???? 如何解决大数据量的统计分析

==> url TOPN <== wc的延伸 
工作中很多场景的开发都是wc的基础上进行改造的


借助于分布式计算框架来解决了： mapreduce

分而治之




(input) <k1, v1> -> map -> <k2, v2> -> combine -> <k2, v2> -> reduce -> <k3, v3> (output)

核心概念
Split：交由MapReduce作业来处理的数据块，是MapReduce中最小的计算单元
	HDFS：blocksize 是HDFS中最小的存储单元  128M
	默认情况下：他们两是一一对应的，当然我们也可以手工设置他们之间的关系（不建议）


InputFormat：
	将我们的输入数据进行分片(split):  InputSplit[] getSplits(JobConf job, int numSplits) throws IOException;
	TextInputFormat: 处理文本格式的数据

OutputFormat: 输出



















MapReduce1.x的架构
1）JobTracker: JT
	作业的管理者      管理的
	将作业分解成一堆的任务：Task（MapTask和ReduceTask）
	将任务分派给TaskTracker运行
	作业的监控、容错处理（task作业挂了，重启task的机制）
	在一定的时间间隔内，JT没有收到TT的心跳信息，TT可能是挂了，TT上运行的任务会被指派到其他TT上去执行

2）TaskTracker: TT
	任务的执行者      干活的
	在TT上执行我们的Task（MapTask和ReduceTask）
	会与JT进行交互：执行/启动/停止作业，发送心跳信息给JT

3）MapTask
	自己开发的map任务交由该Task出来
	解析每条记录的数据，交给自己的map方法处理
	将map的输出结果写到本地磁盘（有些作业只仅有map没有reduce==>HDFS）

4）ReduceTask
	将Map Task输出的数据进行读取
	按照数据进行分组传给我们自己编写的reduce方法处理
	输出结果写到HDFS





使用IDEA+Maven开发wc：
1）开发
2）编译：mvn clean package -DskipTests
3）上传到服务器：scp target/hadoop-train-1.0.jar hadoop@hadoop000:~/lib
4）运行
	hadoop jar /home/hadoop/lib/hadoop-train-1.0.jar com.imooc.hadoop.mapreduce.WordCountApp hdfs://hadoop000:8020/hello.txt hdfs://hadoop000:8020/output/wc

	相同的代码和脚本再次执行，会报错
	security.UserGroupInformation:
	PriviledgedActionException as:hadoop (auth:SIMPLE) cause:
	org.apache.hadoop.mapred.FileAlreadyExistsException: 
	Output directory hdfs://hadoop000:8020/output/wc already exists
	Exception in thread "main" org.apache.hadoop.mapred.FileAlreadyExistsException: 
	Output directory hdfs://hadoop000:8020/output/wc already exists

	在MR中，输出文件是不能事先存在的
	1）先手工通过shell的方式将输出文件夹先删除
		hadoop fs -rm -r /output/wc
	2) 在代码中完成自动删除功能: 推荐大家使用这种方式
	    Path outputPath = new Path(args[1]);
	    FileSystem fileSystem = FileSystem.get(configuration);
	    if(fileSystem.exists(outputPath)){
	        fileSystem.delete(outputPath, true);
	        System.out.println("output file exists, but is has deleted");
	    }



Combiner
hadoop jar /home/hadoop/lib/hadoop-train-1.0.jar com.imooc.hadoop.mapreduce.CombinerApp hdfs://hadoop000:8020/hello.txt hdfs://hadoop000:8020/output/wc

使用场景：
	求和、次数   + 
	平均数  X



Partitioner
hadoop jar /home/hadoop/lib/hadoop-train-1.0.jar com.imooc.hadoop.mapreduce.ParititonerApp hdfs://hadoop000:8020/partitioner hdfs://hadoop000:8020/output/partitioner











