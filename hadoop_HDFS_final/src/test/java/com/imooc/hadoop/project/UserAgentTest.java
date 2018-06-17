package com.imooc.hadoop.project;
import org.apache.commons.lang.StringUtils;
import com.kumkee.userAgent.UserAgent;
import com.kumkee.userAgent.UserAgentParser;
import org.junit.Test;
import java.lang.String;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * UserAgent测试类
 * */
public class UserAgentTest {

    @Test
    public void readFile() throws IOException {
        String path = "/Users/renyiting/Desktop/hadoop_HDFS_final/src/test/java/com/imooc/100_access.log";

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(path)))
        );
        UserAgentParser userAgentParser = new UserAgentParser();

        String line ="";

        Map<String,Integer> browserMap = new HashMap<String,Integer>();



        while(line != null){
            line = reader.readLine(); //一次读入一行数据
            //获取字符串里面指定位置
            if (StringUtils.isNotBlank(line)){
                String source= line.substring(getCharacterPosition(line,"\"",7))+1;
                UserAgent agent = userAgentParser.parse(source);
                String browser = agent.getBrowser();
                String engine = agent.getEngine();
                String engineVersion = agent.getEngineVersion();
                String Os = agent.getOs();
                String platform = agent.getPlatform();
                boolean isMobile = agent.isMobile();


                if (browserMap.get(browser) !=null){
                    browserMap.put(browser,browserMap.get(browser)+1);
                }else{
                    browserMap.put(browser,1);
                }

                System.out.println(browser + " , " + engine + " , " + engineVersion + " , " + Os + " , " + platform + " , " + isMobile);




            }
        }

        for(Map.Entry<String, Integer> entry: browserMap.entrySet()){
            System.out.println(entry.getKey()+": "+entry.getValue());
        }

    }

    /**
     * 测试
     * */
    @Test
    public void testgetCharacterPosition(){
        String value="120.198.231.150 - - [10/Nov/2016:00:01:02 +0800] \"POST /api3/userinfo HTTP/1.1\" 200 153 \"www.imooc.com\" \"-\" cid=0&timestamp=1478707262376&uid=2303417&secrect=4bb99edb808fc2cb0d111d417e3c708e&uuid=9c4663025df901f22799bb97ff3b0c09&token=09271531b6074fdc3d69b62b5e4e7073 \"mukewang/5.0.1 (Android 6.0; HUAWEI BTV-W09 Build/HUAWEIBEETHOVEN-W09),Network WIFI\" \"-\" 10.100.136.64:80 200 0.079 0.079";
        int index = getCharacterPosition(value,"\"",7);
        System.out.println(index);
    }

    /**
     * 获得指定字符串中指定标志的字符串出现的位置
     * */
    private int getCharacterPosition(String value,String operator,int index){
        Matcher slashMatcher = Pattern.compile(operator).matcher(value);
        int mIdx = 0;
        while(slashMatcher.find()){
            mIdx++;

            if(mIdx ==index){
                break;
            }
        }

        return slashMatcher.start();
    }
    /**
     *单元测试：UserAgentParser工具类的使用
     * */
    @Test
    public void testUserAgentParse() {


        // public static void main(String[] args){

        String source = "183.162.52.7 - - [10/Nov/2016:00:01:02 +0800] \"POST /api3/getadv HTTP/1.1\" 200 813 \"www.imooc.com\" \"-\" cid=0&timestamp=14787\n" +
                "07261865&uid=2871142&marking=androidbanner&secrect=a6e8e14701ffe9f6063934780d9e2e6d&token=f51e97d1cb1a9caac669ea8acc162b96 \"\n" +
                "mukewang/5.0.0 (Android 5.1.1; Xiaomi Redmi 3 Build/LMY47V),Network 2G/3G\" \"-\" 10.100.134.244:80 200 0.027 0.027\n";
        //8888888888888
        UserAgentParser userAgentParser = new UserAgentParser();
        UserAgent agent = userAgentParser.parse(source);
        String browser = agent.getBrowser();
        String engine = agent.getEngine();
        String engineVersion = agent.getEngineVersion();
        String Os = agent.getOs();
        String platform = agent.getPlatform();
        boolean isMobile = agent.isMobile();

        System.out.println(browser + " , " + engine + " , " + engineVersion + " , " + Os + " , " + platform + " , " + isMobile);
        //  }
    }

}
