package com.cn.cd.ruidiculous.company.speark_version_2_application;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.azhon.serialport.ReceiveDataListener;
import com.azhon.serialport.SerialPortPlus;
import com.vvui.sdk.NativeVvui;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Base64;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public class MainActivity extends AppCompatActivity implements NativeVvui.Listener  {
    private static final String LOG_TAG = "--------Main-------";

    private static final int DATA_TYPE_PCM_DENOISED = 0;
    private static final int DATA_TYPE_PCM_ORIGINAL = 1;
    private static final String FILENAME_PCM_DENOISED = "/vvui/audio/mic_demo_vvui_denoised.pcm";
    private static final String FILENAME_PCM_ORIGINAL = "/vvui/audio/mic_demo_vvui_original.pcm";
    private static final String TAG = "------串口-------";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call sto a native method
        TextView tv = findViewById(R.id.tv_sample);
        tv.setText(NativeVvui.stringFromJNI());

    }

    public void vvuiInitialize(View view) {
        NativeVvui.registerListener(this);
        NativeVvui.initialize(getAssets());
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
    /**
     * 测试byte
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bytetest(View view){
        Log.d("-----------","-----测试byte-----");
//        byte [] mtest=   Common.Companion.getKill_callpolice_open();
        byte [] mtest=   Common.Companion.getKill_callpolice_open();
//        Log.d("","------------测试byte--1----"+mtest);
//        Log.d("","------------测试byte----2--"+ Arrays.toString(mtest));

         byte[]  mbyte  =SerialPortUtil.toLH(135);
        Log.d("-----------","------------测试byte--1----"+mbyte);
        Log.d("-----------","------------测试byte--2----"+SerialPortUtil.toInt(mbyte));
        Log.d("-----------","------------测试byte--3----"+SerialPortUtil.toInt(mtest));
        Log.d("-----------","------------测试byte--4----"+mtest.length);
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

        Log.d("-----------","------------测试byte--5----"+cesi.length);
        Log.d("-----------","------------测试byte--6----"+Integer.toHexString(181).getBytes()[0]);
        Log.d("-----------","------------测试byte--7----"+new String(Integer.toHexString(181).getBytes()));
        Log.d("-----------","------------测试byte--8----"+SerialPortUtil.toInt(cesi));
        Log.d("-----------","------------测试byte--9----"+SerialPortUtil.byte2Int(cesi[0])  );

        startAngle(cesi);

    }

    public void vvuiDeinitialize(View view) {
   int  ids=     NativeVvui.deinitialize();
        Log.d("-----------","------------停止使用------"+ids);
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


    /**
     * 设置主麦id(0–5)
     * @param view
     */
    public void vvuiSetMajorMicId(View view) {
         NativeVvui.setMajorMicId(3);
//        Log.d("","------------设置主麦id----setMajorMicId--"+id);
    }

    /**
     * 获取主麦克风id
     * @param view
     */
    public void vvuiGetMajorMicId(View view) {
        NativeVvui.getMajorMicId();

//     int id=   NativeVvui.getMajorMicId();
//        Log.d("","------------获取主麦克风id------"+id);
    }

    public void vvuiSetAwakeWord(View view) {
        NativeVvui.setAwakeWords("小明小明");
    }

    @Override
    public void onEvent(int modId, int msgId, int status, String msg) {
        Log.d(LOG_TAG, "-------------onEvent:===modId=" + modId + ", msgId=" + msgId + "," +
                " status=" + status + ", msg=" + msg);

        if (modId == 0xff) {
            Log.d(LOG_TAG, "-----------测试--------"+msg);

        }
        getModidAngle(msg);
    }

    @Override
    public void onData(int type, byte[] data) {
        Log.d(LOG_TAG, "--------------onData: " + type + ", " + data.length);

        if (type == DATA_TYPE_PCM_DENOISED) {
            writeFile(FILENAME_PCM_DENOISED, data);
        } else if (type == DATA_TYPE_PCM_ORIGINAL) {
            writeFile(FILENAME_PCM_ORIGINAL, data);
        }
    }

    private void writeFile(String filename, byte[] data) {
        filename = Environment.getExternalStorageDirectory().getPath() + filename;

        File file = new File(filename);
        File parent = file.getParentFile();

        Log.d("","-------------writeFile-----------"+parent.getAbsolutePath());
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

    /**
     * 获取角度
     */
    public void getModidAngle(String text){

        Message  msg=new Message();
        msg.obj=text;
        msg.what=1;
        mhandler.sendMessage(msg);
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
                        Log.d("-------","----------角度---获取-----"+Angle);

     Log.d("---------","-----角度------"+SerialPortUtil.getAngleBean(Integer.valueOf(Angle)));
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
        try {
            //    /dev/ttyXRM0   /dev/ttyXRM1   /dev/ttyFIQ0   CH34x
            //  /dev/ttyS0 0-4     /dev/ttyUSB0 0-3  /dev/ttySCA0  0-2

//            path="/dev/ttyXRM0"
//            path="/dev/ttyXRM1"
//            path="/dev/ttyS4"
//            path="/dev/ttyUSB1"
//            path="/dev/ttyUSB0"
//            /dev/ttyUSB10  //叶帅

            SerialPortPlus mserialPortPlus=new SerialPortPlus("/system/bin/su/CH34x",115200);
            mserialPortPlus.writeAndFlush(value);

            mserialPortPlus.setReceiveDataListener(new ReceiveDataListener() {
                @Override
                public void receiveData(ByteBuf byteBuf) {

              String dats=      ByteBufUtil.hexDump(byteBuf).toUpperCase();
              Log.d("--------------------","---------测试byte---------开始转向----返回数据------"+dats);
              Log.d("--------------------","---------测试byte---------开始转向--------"+dats);


                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Log.d("----------------","--------熊猫---1-----"+eye);
        try {
            //    /dev/ttyXRM0    /dev/ttyFIQ0
            SerialPortPlus mserialPortPlus=new SerialPortPlus("/dev/ttyUSB0",115200);
//            mserialPortPlus.writeAndFlush(eye.getBytes());

            mserialPortPlus.setReceiveDataListener(new ReceiveDataListener() {
                @Override
                public void receiveData(ByteBuf byteBuf) {
                    String dats=      ByteBufUtil.hexDump(byteBuf).toUpperCase();
                    Log.d("--------------------","--------熊猫-----2-----"+dats);

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}