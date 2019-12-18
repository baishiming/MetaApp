package com.baishiming.meta.metaapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bsm
 * @name MetaApp
 * @class name：com.baishiming.meta.metaapp
 * @class describe
 * @time 2019/12/17 18:32
 */
public class MyStringUtils {


    public static void main(String[] arg){

        String str = "<em>北京城市风光</em>摄影图__建筑摄影_建筑园林_摄";
        subReplace(str);
    }


    public static String subReplace(String str){
        Pattern p = Pattern.compile("<em.*?/em>");
        Matcher m = p.matcher(str);
        if (m.find()) {
            String group = m.group();
            group = group.replace("<em>","");
            group = group.replace("</em>","");
            return group;
        }
        return "";
    }


}
