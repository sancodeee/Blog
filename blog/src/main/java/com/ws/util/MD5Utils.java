package com.ws.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5Utils {
//    MD5加密方法
    public static String code(String str){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[]byteDigest = md.digest();
            int i;
            StringBuilder builder = new StringBuilder("");
            for (int offset = 0;offset < byteDigest.length;offset++){
                i = byteDigest[offset];
                if(i < 0) {
                    i += 256;
                }
                if (i<16) {
                    builder.append("0");
                }
                builder.append(Integer.toHexString(i));
            }
//            32位加密
            return builder.toString();
//            16位加密
//            return buf.toString().substring(8,24);

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){
        log.info("密码："+code("111111"));
    }


}
