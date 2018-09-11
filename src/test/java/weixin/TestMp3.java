package weixin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.JsonParser;


public class TestMp3 {
    public static void main(String[] args){
        byte[] byteArr = readFile("C:\\Users\\Jacky\\Desktop\\20180622001.mp3");
        byte[] tmp = new byte[24];
        byte[] resArr = new byte[byteArr.length-24];

        for(int i=0;i<byteArr.length;i++){
            if(i<tmp.length){
                tmp[i] = byteArr[i];
            }else{
                resArr[i-tmp.length] = byteArr[i];
            }
        }


        int size = 0;
        List<String> list = getData(tmp);
        System.out.println("list size: " + list);
        //前24字节的内容
        for(int i=0;i<list.size();i++){
            //0:合法文件标志   1:样本总数 2:数据包总长度  3:数据包的个数 4:数据包的毫秒数 5:1秒钟的样本数
            if(i==0){
                System.out.println(list.get(i));
            }else{
                int num = Integer.parseInt(list.get(i).replaceAll("^0[x|X]", ""), 16);
                System.out.println(num);
                if(i==2){
                    size = num;//解析出大小
                }
            }
        }

        System.out.println("size: " + size);
        int begin = size;//找到09的位置
        int index = 0;
        for(int i=begin;i<resArr.length;i=i+2){//遍历 每次取2个字节
            tmp = new byte[2];
            tmp[0] = resArr[i];
            tmp[1] = resArr[i+1];
            if (i == begin){
                System.out.println("tmp 0 :" + tmp[0] + ", tmp 1: " + tmp[1]);
            }
            int num = Integer.parseInt(bytesToHexString(tmp).replaceAll("^0[x|X]", ""), 16);//转换为数组大小即每个数据包的大小
            //System.out.println("num: " + num);
            if (i == begin){
                System.out.println("num: " + num);
            }
            byte[] resultByte = new byte[num];//最终要发送的每个包的字节大小
            int jBegin = 0;
            for(int j=index;j<num;j++){//循环获得每个数据包
                resultByte[jBegin] = resArr[j];
                jBegin++;
            }
            index = num;
        }


        System.out.print(resArr[size]);
    }
    public static List<String> getData(byte[] byteArr){
        List<String> result = new ArrayList<String>();
        byte[] tmp = new byte[4];
        int index = 0;
        for(int i=0;i<byteArr.length;i++){
            tmp[index] = byteArr[i];
            if((index+1)%4==0){
                result.add(bytesToHexString(tmp));

                index = -1;
                tmp = new byte[4];
            }

            index++;
        }

        return result;
    }
    //byte转16进制
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    //读取文件
    public static byte[] readFile(String path){
        File file = new File(path);
        FileInputStream input = null;
        try{
            input = new FileInputStream(file);
            byte[] buf =new byte[input.available()];
            input.read(buf);
            return buf;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(input != null){
                try {
                    input.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
