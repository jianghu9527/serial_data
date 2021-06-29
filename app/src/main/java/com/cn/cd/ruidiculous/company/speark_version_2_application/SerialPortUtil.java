package com.cn.cd.ruidiculous.company.speark_version_2_application;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 窗口数据处理
 */
public class SerialPortUtil {


    private static SerialPortUtil mDbController;
    private Context mcontext;

    /**
     * 获取单例
     */
    public static SerialPortUtil getInstance(Context context) {
        if (mDbController == null) {
            synchronized (SerialPortUtil.class) {
                if (mDbController == null) {
                    mDbController = new SerialPortUtil(context);
                }
            }
        }
        return mDbController;
    }

    public SerialPortUtil(Context context) {
        this.mcontext = context;
    }



    public static AngleBean getAngleBean(int mAngle){
        AngleBean  bean=new AngleBean();
        if (mAngle>270){
            bean.Direction=true;
            bean.Angle=Math.abs(270);
        }else{

            int  Angles= 135-mAngle;
            if(Angles<0) {  //正true   负false 方向
                bean.Direction=true;
                bean.Angle=Math.abs(Angles);
            }else if(Angles  > 0) {
                bean.Direction=false;
                bean.Angle= Angles;
            }else if(Angles==0){
                bean.Direction=true;
                bean.Angle=135;
            }
        }
        return bean;
    }

    /**
     * byte转int,对于b值大于127时必须用。
     *
     * @param b
     * @return
     */
    public static   int byte2Int(byte b) {
        return b & 0xFF;
    }
    /**
     * https://www.cnblogs.com/fnlingnzb-learner/p/13434669.html
     * @param b
     * @return
     */
    public static int toInt(byte[] b){
        int res = 0;
        for(int i=0;i<b.length;i++){
            res += (b[i] & 0xff) << (i*8);
        }
        return res;
    }
    /**
     * https://www.cnblogs.com/fnlingnzb-learner/p/13434669.html
     * @param n
     * @return
     */
    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }


    /**
     * 将byte[]数组转化为String类型
     * @param arg
     *            需要转换的byte[]数组
     * @param length
     *            需要转换的数组长度
     * @return 转换后的String队形
     */
    public String toHexString(byte[] arg, int length) {
        String result = new String();
        if (arg != null) {
            for (int i = 0; i < length; i++) {
                result = result
                        + (Integer.toHexString(
                        arg[i] < 0 ? arg[i] + 256 : arg[i]).length() == 1 ? "0"
                        + Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                        : arg[i])
                        : Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                        : arg[i])) + " ";
            }
            return result;
        }
        return "";
    }

    /**
     * 将String转化为byte[]数组
     * @param arg
     *            需要转换的String对象
     * @return 转换后的byte[]数组
     */
    public byte[] toByteArray(String arg) {
        Log.d("----------------------","----------写入--------toByteArray-------"+arg);
        if (arg != null) {
            /* 1.先去除String中的' '，然后将String转换为char数组 */
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != ' ') {
                    NewArray[length] = array[i];
                    length++;
                }
            }
            /* 将char数组中的值转成一个实际的十进制数组 */
            int EvenLength = (length % 2 == 0) ? length : length + 1;
            if (EvenLength != 0) {
                int[] data = new int[EvenLength];
                data[EvenLength - 1] = 0;
                for (int i = 0; i < length; i++) {
                    if (NewArray[i] >= '0' && NewArray[i] <= '9') {
                        data[i] = NewArray[i] - '0';
                    } else if (NewArray[i] >= 'a' && NewArray[i] <= 'f') {
                        data[i] = NewArray[i] - 'a' + 10;
                    } else if (NewArray[i] >= 'A' && NewArray[i] <= 'F') {
                        data[i] = NewArray[i] - 'A' + 10;
                    }
                }
                /* 将 每个char的值每两个组成一个16进制数据 */
                byte[] byteArray = new byte[EvenLength / 2];
                for (int i = 0; i < EvenLength / 2; i++) {
                    byteArray[i] = (byte) (data[i * 2] * 16 + data[i * 2 + 1]);
                }
                return byteArray;
            }
        }
        return new byte[] {};
    }


    public static int byteArrayToInt(byte[] bytes) {
        int value=0;
        for(int i = 0; i < 4; i++) {
            int shift= (3-i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }


    /**
     * 截取byte[]数组中的某一部分
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }
    /**
     * 将byte[]数组转化为String类型
     * @param arg
     *            需要转换的byte[]数组
     * @param length
     *            需要转换的数组长度
     * @return 转换后的String队形
     *    将byte[]数组转化为String类型
     */
    private String toHexStringS(byte[] arg, int length) {
        String result = new String();
        if (arg != null) {
            for (int i = 0; i < length; i++) {
                if (i==length-1){
                    result = result
                            + (Integer.toHexString(
                            arg[i] < 0 ? arg[i] + 256 : arg[i]).length() == 1 ? "0"
                            + Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                            : arg[i])
                            : Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                            : arg[i])) + "";
                }else {
                    result = result
                            + (Integer.toHexString(
                            arg[i] < 0 ? arg[i] + 256 : arg[i]).length() == 1 ? "0"
                            + Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                            : arg[i])
                            : Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                            : arg[i])) + " ";
                }
            }
            return result;
        }
        return "";
    }




}
