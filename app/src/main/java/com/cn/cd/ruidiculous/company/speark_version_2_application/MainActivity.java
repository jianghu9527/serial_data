package com.cn.cd.ruidiculous.company.speark_version_2_application;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.azhon.serialport.ReceiveDataListener;
import com.azhon.serialport.SerialPortPlus;
import com.vvui.sdk.NativeVvui;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Base64;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public class MainActivity extends AppCompatActivity implements NativeVvui.Listener  {
    private static final String LOG_TAG = "--------Main-------";

    private static final int DATA_TYPE_PCM_DENOISED = 0;
    private static final int DATA_TYPE_PCM_ORIGINAL = 1;
    private static final String FILENAME_PCM_DENOISED = "/vvui/audio/mic_demo_vvui_denoised.pcm";
    private static final String FILENAME_PCM_ORIGINAL = "/vvui/audio/mic_demo_vvui_original.pcm";
    private static final String FILENAME_PCM_ORIGINALa = "/vvui/audio/a.wav";
    private static final String FILENAME_PCM_ORIGINALa2 = "/vvui/audio/b.wav";
    private static final String TAG = "------串口-------";
    private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        CH34XInitDevice();
        TextView tv = findViewById(R.id.tv_sample);
        tv.setText(NativeVvui.stringFromJNI());

        findViewById(R.id.getchuankou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAngleTest();
                Toast.makeText(MainActivity.this, "读取串口",
                        Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.saveMoney).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        findViewById(R.id.getMoney).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


    }

    public void startAngleTest(){


        byte[] to_send = "1".getBytes();
//        Log.d("---------------","-----开始转向----11----send---"+ ByteBufUtil.hexDump(to_send).toUpperCase());

        SerialPortPlus serialPortPlus = null;
        try {
            serialPortPlus = new SerialPortPlus("/dev/ttyXRM1", 115200);
            serialPortPlus.writeAndFlush(to_send);
            serialPortPlus.setReceiveDataListener(new ReceiveDataListener() {
                @Override
                public void receiveData(ByteBuf byteBuf) {
                    Log.d("------------","--------------开始转向--------1---"+byteBuf);
                    String cases = ByteBufUtil.hexDump(byteBuf).toUpperCase();
                    Log.d("------------","--------------开始转向------2-----"+cases);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 设置唤醒词
     * @param view
     */
    public void vvuiSetAwakeWord(View view) {
        NativeVvui.setAwakeWords("小睿小睿");//小明小明
    }

    /**
     * 设置主麦id(0–5)
     * @param view
     */
    public void vvuiSetMajorMicId(View view) {
        NativeVvui.setMajorMicId(3);
//        Log.d("","------------设置主麦id----setMajorMicId--"+id);
        Toast.makeText(MainActivity.this, "设置主麦id(0–5)",
                Toast.LENGTH_SHORT).show();


    }

String filePathvoid;
    /**
     * 播放声音
     * @param view
     */
    public void play(View view){

//        File mfile=  new File(Environment.getExternalStorageDirectory().getAbsolutePath()+FILENAME_PCM_ORIGINALa
//                ,Environment.getExternalStorageDirectory().getAbsolutePath()+FILENAME_PCM_ORIGINALa  );
//        if (!mfile.exists()) {
//            mfile.mkdirs();
//        }
//
//        if (!mfile.exists()) {
//            try {
//                mfile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("-----------------","---------播放声音-----111---"+filePathvoid);

                AudioRecorderUtil.getInstance().copyWaveFile(Environment.getExternalStorageDirectory().getAbsolutePath()+FILENAME_PCM_ORIGINAL
                        ,Environment.getExternalStorageDirectory().getAbsolutePath()+FILENAME_PCM_ORIGINALa);
                AudioRecorderUtil.getInstance().copyWaveFile(Environment.getExternalStorageDirectory().getAbsolutePath()+FILENAME_PCM_DENOISED
                        ,Environment.getExternalStorageDirectory().getAbsolutePath()+FILENAME_PCM_ORIGINALa2);
//        copyWaveFile(filePathvoid);
                Log.d("-----------------","---------播放声音-----55---"+filePathvoid);
            }
        }).start();
//        Log.d("-----------------","---------播放声音-----66---"+mfile.getAbsolutePath());
//        Log.d("-----------------","---------播放声音-----66---"+mfile.getAbsolutePath());
    }

    /**
     * 停止使用
     * @param view
     */
    public void vvuiDeinitialize(View view) {
        int  ids=     NativeVvui.deinitialize();
        Log.d("-----------","------------停止使用------"+ids);
    }
    /**
     * 获取主麦克风id
     * @param view
     */
    public void vvuiGetMajorMicId(View view) {
        int    MicId=NativeVvui.getMajorMicId();

        Log.d("---------------","-------------获取主麦克风id------"+MicId);
    }


    @Override
    public void onEvent(int modId, int msgId, int status, String msg) {
        Log.d(LOG_TAG, "-------------onEvent:===modId=" + modId + ", msgId=" + msgId + "," +
                " status=" + status + ", msg=" + msg);


        //常见status   0，30002，2，1     30002 设置主麦    0初始化     -2设置唤醒词
        if (modId == 0xff) {
            Log.d(LOG_TAG, "-----------测试----角度->>>>>-255--"+msg);
        }
        /**
         * 获取角度
         */
        Message  msge=new Message();
        msge.obj=msg;
        msge.what=1;
        mhandler.sendMessage(msge);
    }

    @Override
    public void onData(int type, byte[] data) {
        Log.d(LOG_TAG, "--------------onData: -------------" + type + "----, -----" + new String(data));

        if (type == DATA_TYPE_PCM_DENOISED) {
            writeFile(FILENAME_PCM_DENOISED, data);
        } else if (type == DATA_TYPE_PCM_ORIGINAL) {
            writeFile(FILENAME_PCM_ORIGINAL, data);
        }
    }

    /**
     * 初始化  麦克风
     * @param view
     */
    public void vvuiInitialize(View view) {
        NativeVvui.registerListener(this);
        NativeVvui.initialize(getAssets());

        Toast.makeText(MainActivity.this, "初始化麦克风",
                Toast.LENGTH_SHORT).show();

//        NativeVvui.registerListener(new NativeVvui.Listener() {
//            @Override
//            public void onEvent(int i, int i1, int i2, String s) {
//
//            }
//
//            @Override
//            public void onData(int i, byte[] bytes) {
//
//            }
//        });

    }


    public void vvuiStartRecordDenoised(View view) {
        NativeVvui.startRecordDenoised();
    }

    public void vvuiStopRecordDenoised(View view) {
        NativeVvui.stopRecordDenoised();
    }

    public void vvuiStartRecordOriginal(View view) {
        NativeVvui.startRecordOriginal();
    }

    public void vvuiStopRecordOriginal(View view) {
        NativeVvui.stopRecordOriginal();
    }


    Boolean  iseyee=true;
    /**
     * 熊猫眼睛
     * @param view
     */
    public void getchunakou(View view){
         Log.d("---------------","-------熊猫眼睛---22-----");

        if (iseyee){
            iseyee=false;
        }else {
            iseyee=true;
        }
        padom(iseyee);
    }

    public void PandaEarsData(View view){
        getPandaEarsData();
    }
    /**
     * 测试byte
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bytetest(View view){
        Log.d("-----------","-----测试byte-----");

        byte []  cesi=new byte[9];

        cesi[0]= (byte)181;//b5   181 帧头
        cesi[1]= (byte)91;  //5b  91   帧头
        cesi[2]= (byte)3;  //03  3    有效位数
        cesi[3]= (byte)1;  //01  1;  方向
        cesi[4]= (byte)35;  //01  1;   角度
        cesi[5]= (byte)1;  //01  1;   转速
        cesi[6]= (byte)1;  //01  1;   效验
        cesi[7]= (byte)13;  //0D 13;   帧尾
        cesi[8]= (byte)10;  //0A  10;  帧尾


        Log.d("-----------","------------测试byte--4--角度--"+35);
        Log.d("-----------","------------测试byte--5----"+cesi.length);
        Log.d("-----------","------------测试byte--6----"+Integer.toHexString(181).getBytes()[0]);
        Log.d("-----------","------------测试byte--7----"+new String(Integer.toHexString(181).getBytes()));
        Log.d("-----------","------------测试byte--8----"+SerialPortUtil.toInt(cesi));
        Log.d("-----------","------------测试byte--9----"+SerialPortUtil.byte2Int(cesi[0])  );

//        startAngle(cesi);
//        SerialPort(cesi);


    }


    /**
     * 转动方向
     * @param aglent  角度
     * @param    方向
     */
    public void DirectionAglent(AngleBean aglent){
        byte []  cesi=new byte[9];

        cesi[0]= (byte)181;//b5   181 帧头
        cesi[1]= (byte)91;  //5b  91   帧头
        cesi[2]= (byte)3;  //03  3    有效位数
        cesi[3]= (byte)1;  //01  1;  方向
        cesi[4]= (byte)aglent.Angle;  //01  1;   角度
        cesi[5]= (byte)1;  //01  1;   转速
        cesi[6]= (byte)1;  //01  1;   效验
        cesi[7]= (byte)13;  //0D 13;   帧尾
        cesi[8]= (byte)10;  //0A  10;  帧尾


        Log.d("-----------","------------测试byte--4--角度--"+35);
        Log.d("-----------","------------测试byte--5----"+cesi.length);
        Log.d("-----------","------------测试byte--6----"+Integer.toHexString(181).getBytes()[0]);
        Log.d("-----------","------------测试byte--7----"+new String(Integer.toHexString(181).getBytes()));
        Log.d("-----------","------------测试byte--8----"+SerialPortUtil.toInt(cesi));
        Log.d("-----------","------------测试byte--9----"+SerialPortUtil.byte2Int(cesi[0])  );

        startAngle(cesi);


    }

    /**
     * ch34x
     * @param view
     */
    public void ch34x(View view){
        startActivity(new Intent(this, MainCH34XActivity.class));
    }
    public void ttymethor(View view){
        SerialPort();
    }



    private void writeFile(String filename, byte[] data) {
        filename = Environment.getExternalStorageDirectory().getPath() + filename;

        File file = new File(filename);
        File parent = file.getParentFile();

        Log.d("","-------------writeFile------1-----"+filename.toString());
        Log.d("","-------------writeFile------2-----"+parent.getAbsolutePath().toString());
        filePathvoid=file.getAbsolutePath();
        DataOutputStream dos = null;
        try {
            assert parent != null;
            if (!parent.exists()) {
                parent.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            dos = new DataOutputStream(new FileOutputStream(file, true));
            dos.write(data);
        } catch (IOException e) {
            Log.e(LOG_TAG, "-----------write file " + filename + " exception", e);
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "--------------close file " + filename + " exception", e);
            }
        }
    }




    Handler  mhandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                 String  mtext= (String) msg.obj;

                    if (!TextUtils.isEmpty(mtext)&&mtext.contains("唤醒角度为")){
                        String  [] mspliter= mtext.split(",");
                        String Angle=      (String) mspliter[1].subSequence(7, mspliter[1].length());

                        new TextToSpeechUtil(MainActivity.this).speechStart("角度:"+Angle);
                        Log.d("-------","-------获取--计算前---"+Angle);
                        Log.d("---------","-----"+SerialPortUtil.getAngleBean(Integer.valueOf(Angle)));

//                        DirectionAglent(SerialPortUtil.getAngleBean(Integer.valueOf(Angle)));

                    }


                    break;
            }
        }
    };



    /**
     *开始转向
     *
     * 一般情况下串口的名称全部在dev下面，如果你没有外插串口卡的话默认是dev下的ttyS*,一般ttyS0对应com1，ttyS1对应com2，当然也不一定是必然的；
     *
     */
    public void startAngle(byte[]  value){

        Log.d("---------------","-----测试byte--------send---"+ ByteBufUtil.hexDump(value).toUpperCase());

        byte[] to_send = value;
        int retval = BaseAlication.driver.WriteData(to_send, to_send.length);//写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度

        Log.d("---------------","--------写入---返回实际发--2--"+retval);
        if (retval < 0)
            Toast.makeText(MainActivity.this, "写失败!",
                    Toast.LENGTH_SHORT).show();
    }


    private Handler handler;
    public int baudRate = 115200;//波特率

    public byte stopBit = 1;//停止位
    public byte dataBit= 8;//数据位
    public byte parity  = 0;//奇偶校验位
    public byte flowControl = 0;//停止位
    /**
     * 初始化 CH34X
     */
    public void CH34XInitDevice(){

        BaseAlication.driver = new CH34xUARTDriver(
                (UsbManager) getSystemService(Context.USB_SERVICE), this,
                ACTION_USB_PERMISSION);

        if (!BaseAlication.driver.UsbFeatureSupported())// 判断系统是否支持USB HOST
        {
            Log.d("--------------","----------写入-------您的手机不支持USB HOST，请更换其他手机再试！");
        }
        BaseAlication.isOpen_CH34X = false;

        handler = new Handler() {
            public void handleMessage(Message msg) {

//                ByteBufUtil.hexDump(value).toUpperCase()
                String  data =(String) msg.obj;
                String Angledatas=    data.substring(12, 14);
                int Angles= SerialPortUtil.toInt(Angledatas.getBytes());

                Log.d("---------------","-------写入------转头返回数据---0----"+data);
                Log.d("---------------","-------写入------转头返回数据----1----"+Angledatas);
//                SerialPortUtil.subBytes();
            }
        };

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initCh34x();
            }
        },2000);
    }

    private class readThread extends Thread {

        public void run() {

            byte[] buffer = new byte[64];
            while (true) {
                Message msg = Message.obtain();
                if (!BaseAlication.isOpen_CH34X) {
                    break;
                }
                int length = BaseAlication.driver.ReadData(buffer, 64);
                if (length > 0) {
                    String recv =new SerialPortUtil(MainActivity.this).toHexString(buffer, length);
                    Log.d("---------------","--------写入---返回实际发--1--"+recv);
                    msg.obj = recv;
                    handler.sendMessage(msg);
                }
            }
        }
    }


   void  initCh34x(){

       boolean isConnect=  BaseAlication.driver.isConnected();
       Log.d("----------------","------------是否连接---CH34X------"+isConnect);
       if(!isConnect){

           if (!BaseAlication.isOpen_CH34X) {
               if (BaseAlication.driver!=null&&!BaseAlication.driver.ResumeUsbList())// ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
               {
                   Toast.makeText(MainActivity.this, "打开设备失败!",
                           Toast.LENGTH_SHORT).show();
                   BaseAlication.driver.CloseDevice();
                   Log.d("----------------","----------------打开设备失败------1----");
               } else {
                   if (!BaseAlication.driver.UartInit()) {  //对串口设备进行初始化操作
                       Toast.makeText(MainActivity.this, "设备初始化失败!",
                               Toast.LENGTH_SHORT).show();
                       Toast.makeText(MainActivity.this, "打开" +
                                       "设备失败!",
                               Toast.LENGTH_SHORT).show();
                       Log.d("----------------","----------------设备初始化失败----2------");
                       return;
                   }
                   Toast.makeText(MainActivity.this, "打开设备成功!",
                           Toast.LENGTH_SHORT).show();
                   BaseAlication.isOpen_CH34X = true;
                   new MainActivity.readThread().start();//开启读线程读取串口接收的数据
                   Log.d("----------------","----------------打开设备成功----------");
               }
           } else {
               BaseAlication.driver.CloseDevice();
               BaseAlication.isOpen_CH34X = false;
               Log.d("----------------","----------------打开设备成功----关闭------");
           }

           if (BaseAlication.driver!=null&&BaseAlication.driver.isConnected()){
               if (BaseAlication.driver.SetConfig(baudRate, dataBit, stopBit, parity,//配置串口波特率，函数说明可参照编程手册
                       flowControl)) {
                   Toast.makeText(MainActivity.this, "串口设置成功!",
                           Toast.LENGTH_SHORT).show();
                   Log.d("----------------","----------------串口设置成功---1----");
               } else {
                   Toast.makeText(MainActivity.this, "串口设置失败!",
                           Toast.LENGTH_SHORT).show();
                   Log.d("----------------","----------------串口设置失败---1----");
               }
           }else{
               Log.d("----------------","----------------打开失败---999----");
           }
       }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseAlication.isOpen_CH34X=false;
    }


    /**
     * 熊猫
     * @param
     */
    public void padom(Boolean  iseye){
        String  eye="";
        if(iseye){
            eye="OPEN";
        }else{
            eye="CLOSE";
        }
        Log.d("----------------","--------熊猫--->>>>>>>----"+eye);
        try {
            //    /dev/ttyXRM0    /dev/ttyFIQ0
            SerialPortPlus mserialPortPlus=new SerialPortPlus("/dev/ttyUSB0",115200);
//            mserialPortPlus.writeAndFlush(eye.getBytes());

            mserialPortPlus.setReceiveDataListener(new ReceiveDataListener() {
                @Override
                public void receiveData(ByteBuf byteBuf) {
                    String dats=      ByteBufUtil.hexDump(byteBuf).toUpperCase();
                    Log.d("--------------------","--------熊猫-----<<<<<<-----"+dats);

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void  getPandaEarsData(){
        try {
            //    /dev/ttyXRM0    /dev/ttyFIQ0
            SerialPortPlus mserialPortPlus=new SerialPortPlus("/dev/ttyUSB0",115200);
//            mserialPortPlus.writeAndFlush(eye.getBytes());

            mserialPortPlus.setReceiveDataListener(new ReceiveDataListener() {
                @Override
                public void receiveData(ByteBuf byteBuf) {
                    String dats=      ByteBufUtil.hexDump(byteBuf).toUpperCase();
                    Log.d("--------------------","--------熊猫---尾巴数据--<<<<<<-----"+dats);

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 串口数据
     */
    public void SerialPort(){
        byte []  cesi=new byte[9];

        cesi[0]= (byte)181;//b5   181 帧头
        cesi[1]= (byte)91;  //5b  91   帧头
        cesi[2]= (byte)3;  //03  3    有效位数
        cesi[3]= (byte)1;  //01  1;  方向
        cesi[4]= (byte)35;  //01  1;   角度
        cesi[5]= (byte)1;  //01  1;   转速
        cesi[6]= (byte)1;  //01  1;   效验
        cesi[7]= (byte)13;  //0D 13;   帧尾
        cesi[8]= (byte)10;  //0A  10;  帧尾
        try {
//            Log.d("-----------","------------串口数据--8----"+SerialPortUtil.toInt(cesi));
            Log.d("---------------","-----串口数据----->>>>>>------"+ ByteBufUtil.hexDump(cesi).toUpperCase());
            SerialPortPlus mserialPortPlus=new SerialPortPlus("/dev/ttyS4",115200);
            mserialPortPlus.writeAndFlush(cesi);

            mserialPortPlus.setReceiveDataListener(new ReceiveDataListener() {
                @Override
                public void receiveData(ByteBuf byteBuf) {
                    Log.d("--------------------","-------串口数据---<<<<receiveData>>>>----");
                    String dats =  ByteBufUtil.hexDump(byteBuf).toUpperCase();
                    Log.d("--------------------","-------串口数据---<<<<<<<----"+dats);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}