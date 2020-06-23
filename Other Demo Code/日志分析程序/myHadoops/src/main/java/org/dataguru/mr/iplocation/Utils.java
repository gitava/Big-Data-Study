package org.dataguru.mr.iplocation;

import java.io.UnsupportedEncodingException;

/*
 * 用来解读数据库： /myHadoops/src/main/resources/qqwry.dat
 * 
 * 纯真IP地址数据库qqwry.dat解析
 * ip地址数据库，在现在互联网时代非常有用，比如大型网站的用户安全保护系统，就常常会根据ip反查的信息，甄别账号的一些不安全登录行为，比如跨区域登录问题等。ip其实关联了一些有信息，比如区域，所在运营商，一些收录全的，甚至包括具体经纬度，像百度的IP定位api就比较全。
 * 下面来介绍一下“ 纯真IP地址数据库qqwry”的格式以及解析
 * 以下是“ 纯真IP地址数据库qqwry”官网对其的介绍。
 * 纯真版IP地址数据库是当前网络上最权威、地址最精确、IP记录以及网吧数据最多的IP地址数据库。收集了包括中国电信、中国移动、中国联通、铁通、长城宽带等各 ISP 的最新准确 IP 地址数据。通过大家的共同努力打造一个没有未知数据，没有错误数据的QQ IP。IP数据库每5天更新一次，请大家定期更新最新的IP数据库！
 * [https://www.jianshu.com/p/01d3c19738c2]
 */
public class Utils {
    /**
     * 从ip的字符串形式得到字节数组形式
     * @param ip 字符串形式的ip
     * @return 字节数组形式的ip
     */
    public static byte[] getIpByteArrayFromString(String ip) {
        byte[] ret = new byte[4];
        java.util.StringTokenizer st = new java.util.StringTokenizer(ip, ".");
        try {
            ret[0] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[1] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[2] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
            ret[3] = (byte)(Integer.parseInt(st.nextToken()) & 0xFF);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }
    
    public static void main(String args[]){
         byte[] a=getIpByteArrayFromString(args[0]);
          for(int i=0;i< a.length;i++)
                System.out.println(a[i]);
          System.out.println(getIpStringFromBytes(a)); 
    }
    /**
     * 对原始字符串进行编码转换，如果失败，返回原始的字符串
     * @param s 原始字符串
     * @param srcEncoding 源编码方式
     * @param destEncoding 目标编码方式
     * @return 转换编码后的字符串，失败返回原始字符串
     */
    public static String getString(String s, String srcEncoding, String destEncoding) {
        try {
            return new String(s.getBytes(srcEncoding), destEncoding);
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }
    
    /**
     * 根据某种编码方式将字节数组转换成字符串
     * @param b 字节数组
     * @param encoding 编码方式
     * @return 如果encoding不支持，返回一个缺省编码的字符串
     */
    public static String getString(byte[] b, String encoding) {
        try {
            return new String(b, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b);
        }
    }
    
    /**
     * 根据某种编码方式将字节数组转换成字符串
     * @param b 字节数组
     * @param offset 要转换的起始位置
     * @param len 要转换的长度
     * @param encoding 编码方式
     * @return 如果encoding不支持，返回一个缺省编码的字符串
     */
    public static String getString(byte[] b, int offset, int len, String encoding) {
        try {
            return new String(b, offset, len, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b, offset, len);
        }
    }
    
    /**
     * @param ip ip的字节数组形式
     * @return 字符串形式的ip
     */
    public static String getIpStringFromBytes(byte[] ip) {
        StringBuffer sb = new StringBuffer();
        sb.append(ip[0] & 0xFF);
        sb.append('.');       
        sb.append(ip[1] & 0xFF);
        sb.append('.');       
        sb.append(ip[2] & 0xFF);
        sb.append('.');       
        sb.append(ip[3] & 0xFF);
        return sb.toString();
    }
}