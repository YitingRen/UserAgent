package com.project;

import org.apache.commons.lang.StringUtils;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.kumkee.userAgent.*;
import com.kumkee.userAgent.UserAgent;

public class UserAgentTest {
    static Map<String,Integer> map = new HashMap<String, Integer>();
    public  static void readfile() throws IOException{
        String path = "/Users/renyiting/Desktop/UserAgent/src/main/java/com/data/100_access.log";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
        String line =  "" , str ="";

        UserAgentParser userAgentParser = new UserAgentParser();
        UserAgent userAgent = new UserAgent();
        int start = 0, end = 0;
        while (line != null){
            line =bufferedReader.readLine();
            if (StringUtils.isNotBlank(line)) {
                start=getInfo(line,"\"",7);
                end=getInfo(line,"\"",8);
                str = line.substring(start+1,end);
                userAgent = userAgentParser.parse(str);
                if (map.get(userAgent.getBrowser())!=null){
                    map.put(userAgent.getBrowser(),map.get(userAgent.getBrowser())+1);
                }else{
                    map.put(userAgent.getBrowser(),1);
                }
            }
        }
        for(Map.Entry<String, Integer> entry :map.entrySet()){
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }
    public static int getInfo(String line, String operator, int index){
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


    public static void main(String[] args) throws IOException {
        readfile();
        map = null;
    }
}
