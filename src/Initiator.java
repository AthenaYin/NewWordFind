
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

/**
 * Created with IntelliJ IDEA.
 * User: athena
 * Date: 11/11/13
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */

public class Initiator {
    String line;
    JSONObject jObj = new JSONObject();
    JSONParser parser = new JSONParser();
    int SentenceCount = 0, total = 0;
    //parser = new JSONParser();
    HashSet<String> seeds = new HashSet<String>();

    /**
     * 获取短语中的模板
     * @param str
     * @return
     */
    public String getPattern(String str){
        String[] words = str.split(" ");
        String tmp = words[1].concat(words[words.length - 1].split("/")[0]);
        tmp = tmp.replace("/d", "_");
        tmp = tmp.replace("/a", "_");
        return tmp;
    }

    /**
     * 获取短语中的词语
     * @param str
     * @return
     */
    public String getWord(String str){
        String[] words = str.split(" ");
        String tmp = "";
        for(int i = 2;i < words.length - 1;i++)
            tmp = tmp.concat(words[i].split("/")[0]);
        return tmp;
    }

    public void init() throws Exception {

        seeds.add("坑爹");
        seeds.add("给力");
        seeds.add("有爱");
        seeds.add("蛋疼");

        BufferedWriter addMapPhit = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("addMapPhit.txt"), "UTF-8"));
        BufferedWriter addMapWhit = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("addMapWhit.txt"), "UTF-8"));
        BufferedWriter addMapHitboth = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("addMapHitboth.txt"), "UTF-8"));
        BufferedWriter addMapPc = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("addMapPc.txt"), "UTF-8"));
        BufferedWriter addInfoWleft = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("addInfoWleft.txt"), "UTF-8"));
        File weibo_data = new File("/home/athena/workspace/Newword/weibo");
        for (File year : weibo_data.listFiles()) {
            for (File month : year.listFiles()) {
                for (File date : month.listFiles()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            new FileInputStream(date), "UTF-8"));
                    while((line = in.readLine()) != null){
                        try{
                            jObj = (JSONObject) parser.parse(line);
                            line = (String) jObj.get("text");
                        }
                        catch(Exception e)
                        {
                             continue;
                        }
                      //  System.out.println(year.toString() + month.toString() + date.toString() + line);
                        //用正则表达式匹配出目标中文短语
                        String str="\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b";
                        Pattern p = Pattern.compile(
                                " [\u4E00-\u9FA5]{1,4}/[da] ([\u4E00-\u9FA5|A-Za-z]/[^(duye)]" +
                                        "{1,2} ){2,3}[\u4E00-\u9FA5"+str+"]{1,2}/[uyew]");
                        Matcher m = p.matcher(line);
                        while(m.find()){
                            total++;
                            String tmp = m.group();
                     //       System.out.println("******************************************************************");
                            //对特定模板出现次数累加1
                    //        addMap(getPattern(tmp),phit);
                            addMapPhit.write(getPattern(tmp) + "\n");
                    //        System.out.println(getPattern(tmp)); //
                            //对特定词语出现次数累加1
                    //        addMap(getWord(tmp),whit);
                            addMapWhit.write(getWord(tmp) + "\n");
                    //        System.out.println(getWord(tmp));       //
                            //对特定模板、词语同时出现次数累加1
                    //        addMap(getPattern(tmp).replace("_", getWord(tmp)),hitboth);
                            addMapHitboth.write(getPattern(tmp).replace("_", getWord(tmp)) + "\n");
                            //对匹配种子词的模板出现次数累加1
                    //        System.out.println("******************************************************************");
                            if(seeds.contains(getWord(tmp))){
                          //      addMap(getPattern(tmp),pc);
                                addMapPc.write(getPattern(tmp));
                            }
                            //统计出现在该词语左侧的词，为了计算左信息熵
                        //    addInfo(getWord(tmp),getPattern(tmp).split("_")[0],wleft);
                            addInfoWleft.write(getWord(tmp) + " " + getPattern(tmp).split("_")[0] + "\n");
                        }
                        if(SentenceCount++ % 500000 == 0)
                            System.out.println(SentenceCount);

                    }
           //         in.close();System.out.println("miao?");
                }
            }
        }

        BufferedWriter Counter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("Counter.txt"), "UTF-8"));
        Counter.write(SentenceCount);
        Counter.write("\n");
        Counter.write(total);
        addInfoWleft.close();
        addMapHitboth.close();
        addMapPc.close();
        addMapPhit.close();
        addMapWhit.close();
    }

    public static void main(String arg[]) throws Exception {
        Initiator initiator = new Initiator();
        initiator.init();
    }
}
