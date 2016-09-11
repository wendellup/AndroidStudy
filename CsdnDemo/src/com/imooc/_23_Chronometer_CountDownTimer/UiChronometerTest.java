package com.imooc._23_Chronometer_CountDownTimer;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

import com.imooc.toast.R;

public class UiChronometerTest extends Activity{ 
    private Chronometer chrono; 
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.ui_chronometer);
        chrono = (Chronometer)findViewById(R.id.ui_meter); 
        chrono.setFormat("计时器：%s");  //缺省情况，计时器显示为MM:SS，如果超过1小时则显示H:MM:SS。如果我们需要增加一些文字，可以用setFormat，或者在XML文件中用android:format来设置。setFormat中第一个%s是计时器，也就是MM:SS/H:MM:SS。在本例中，我们在前面加上“计时器：”几个字样。
    /*  // 系统会根据tick来触发计时器时间的变化， tick是系统计时，触发计时器变化，在此我们每个tick到了，我们加一行log，在模拟器中，间隔时间略大于1秒。一般来讲，我们无需特别处理，计时器会自动进行计时。
        chrono.setOnChronometerTickListener(new OnChronometerTickListener() {
            private int count = 0; 
            @Override 
            public void onChronometerTick(Chronometer meter) { 
                Log.d("wei","-- " + (count ++));                  
            } 
        });*/ 
    } 
  //按Start button触发函数 
   public void onMeterStart(View v){ 
        chrono.setBase(SystemClock.elapsedRealtime());  //setBase是设置基准时间，计时器=当前时间-基准时间，本例将按Start的时间设置为基准时间，即计时器从0秒开始计数。
        chrono.start();  //开始计数
    }  
   //按Stop Button 
   public void onMeterStop(View v){ 
        chrono.stop(); //停止计数
    }  
   //按Reset Button 
   public void onMeterReset(View v){ 
        chrono.setBase(SystemClock.elapsedRealtime());  //计时器reset，我们只要将基准时间设为当前时间，计数器就可以归零。 
    } 
}