package com.example.jession_ding.newsapplication.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by segno on 2016/8/9.
 */
//MD5 hash 算法，又被称为 消息摘要算法--》任意长度的输入，都会生成一个16字节的hash值
//而且不同的输入，得到的hash值不一样

//1.多次MD5加密
//2.通过加盐算法

public class MD5Utils {

    public static String getMD5(String pwd) {

        String md5hashValue = "";

        //直接调用底层的MD5算法实现类
        try {
            //1.多次MD5加密
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            StringBuffer sb = new StringBuffer();

            //计算MD5数值
            byte[] digestdate = messageDigest.digest(pwd.getBytes());
            for (byte b : digestdate) {
                //把MD5字节数组转换成一个字符串
                int i = b & 0x000000FF;
                //FF
                //FF FF FF FF

                //2.通过加盐算法

                String s = Integer.toHexString(i);
                //整个字符串，写到一个StringBuffer里面去

                if (s.length() == 1) {
                    sb.append("0");//如果是一位的，则加0

                }
                //System.out.println(s);

                sb.append(s);
            }
            md5hashValue = sb.toString();

            //System.out.println(md5hashValue);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return md5hashValue;

    }
}
