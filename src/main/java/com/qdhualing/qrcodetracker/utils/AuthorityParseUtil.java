package com.qdhualing.qrcodetracker.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 马鹏昊
 * @desc
 * @date 2018/2/26
 */
public class AuthorityParseUtil {

    /**
     * @param authorityStr 所属部门拥有的权限集合字符串（现在改成是一个用户拥有多个权限，这个方法不用了）
     * @param extraAuth    额外添加的权限
     * @return
     * @desc 将数据库中查出来的权限字符串解析成module2中的对应功能名集合
     */
    public static List<Integer> parseToFunctionList(String authorityStr, int extraAuth) {
        List<Integer> result = new ArrayList<Integer>();
    	if(authorityStr==null) {
    		return result;
    	}
        String[] strs = authorityStr.split(",");
        for (String s : strs) {
            result.add(Integer.parseInt(s));
        }
        if (extraAuth > 0 && !result.contains(extraAuth))
            result.add(extraAuth);
        return result;
    }

    /**
     * @param authorityStr 用户拥有的权限集合字符串
     * @return
     */
    public static List<Integer> parseToFunctionList(String authorityStr) {
        List<Integer> result = new ArrayList<Integer>();
    	if(authorityStr==null) {
    		return result;
    	}
        String[] strs = authorityStr.split(",");
        for (String s : strs) {
            if("bz".equals(s)||"ld".equals(s)||"zjy".equals(s)||"zjld".equals(s))//这里是不同身份的字符串标识（班长、领导、质检员、质检领导等），不能与Integer兼容，不能往集合里加
                continue;
            result.add(Integer.parseInt(s));
        }
        return result;
    }

}
