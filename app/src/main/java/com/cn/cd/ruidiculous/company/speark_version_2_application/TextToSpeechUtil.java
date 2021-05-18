package com.cn.cd.ruidiculous.company.speark_version_2_application;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;


import java.util.Locale;

/**
 * 本地语音初始化
 * 语音
 */
public class TextToSpeechUtil {

    private TextToSpeech textToSpeech = null;
   public TextToSpeechUtil(Context context){
       textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
           @Override
           public void onInit(int status) {
               //		初始化成功

               try {
                   if (status == TextToSpeech.SUCCESS) {
                       int result = textToSpeech.setLanguage(Locale.CHINA);
//                       LogUtil.d("-----------------result--初始化语音--"+result);
                       if (result == TextToSpeech.LANG_MISSING_DATA
                               || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                           Toast.makeText(BaseApplication.Companion.getContext(), "数据丢失或不支持", Toast.LENGTH_SHORT).show();
                       }
                   } else {
                       //		初始化失败
//                       Toast.makeText(BaseApplication.Companion.getContext(), "不支持朗读功能", Toast.LENGTH_SHORT).show();
                   }
               } catch (Exception e) {
//                   LogUtil.d("-------------初始化语音----result--异常--"+status);
                   e.printStackTrace();
               }
           }
       });
       // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
       textToSpeech.setPitch(1.0f);
       // 设置语速
       textToSpeech.setSpeechRate(1.5f);
   }

    boolean currentTemp=true;
    Handler mHandlerDelayed = new Handler(Looper.getMainLooper());

    public void speechStart(String text) {
//        LogUtil.d("------------语音-----text-----"+text);

        if (currentTemp){
            currentTemp=false;
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int  mspaeck =  textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null); //QUEUE_FLUSH
//                    LogUtil.d("------------语音-----2-----"+mspaeck);
                }
            },100);
        }

        mHandlerDelayed.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentTemp=true;
            }
        },2000);

    }


}
