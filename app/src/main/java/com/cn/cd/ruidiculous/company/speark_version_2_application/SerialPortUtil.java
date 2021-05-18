package com.cn.cd.ruidiculous.company.speark_version_2_application;


import android.content.Context;
import android.content.Intent;

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
        int  Angles= 180-mAngle;

        AngleBean  bean=new AngleBean();
        if(Angles<0) {  //正true   负false 方向
            bean.Direction=true;
            bean.Angle=Math.abs(Angles);
        }else if(Angles  > 0) {
            bean.Direction=false;
            bean.Angle= Angles;
        }else if(Angles==0){
            bean.Direction=true;
            bean.Angle=0;
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
}
