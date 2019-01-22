/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 *
 * @author tangyue
 * @version $Id: StrUtil.java, v 0.1 2018-05-14 14:17 tangyue Exp $$
 */
public final class StrUtil {

    private StrUtil(){

    }

    public static boolean isNotBlank(String str){

        return StringUtils.isNotBlank(str);
    }

    public static boolean isBlank(String str){

        return StringUtils.isBlank(str);
    }

    public static boolean isNumber(String str){

        return StringUtils.isNumeric(str);
    }

    public static boolean isNotNumber(String str){

        return !isNumber(str);
    }

    /**
     *
     * @param list
     * @param split
     * @return
     */
    public static String join(List<?> list, String split){

       return StringUtils.join(list, split);
    }

    public static String encodeBase64(String str){
        if (isBlank(str)){
            return null;
        }
        return Base64.encodeBase64String(str.getBytes());
    }

    public static String decideBase64(String str){
        if (isBlank(str)){
            return null;
        }
        return new String(Base64.decodeBase64(str));
    }

}
