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
        chrono.setFormat("��ʱ����%s");  //ȱʡ�������ʱ����ʾΪMM:SS���������1Сʱ����ʾH:MM:SS�����������Ҫ����һЩ���֣�������setFormat��������XML�ļ�����android:format�����á�setFormat�е�һ��%s�Ǽ�ʱ����Ҳ����MM:SS/H:MM:SS���ڱ����У�������ǰ����ϡ���ʱ����������������
    /*  // ϵͳ�����tick��������ʱ��ʱ��ı仯�� tick��ϵͳ��ʱ��������ʱ���仯���ڴ�����ÿ��tick���ˣ����Ǽ�һ��log����ģ�����У����ʱ���Դ���1�롣һ�����������������ر�����ʱ�����Զ����м�ʱ��
        chrono.setOnChronometerTickListener(new OnChronometerTickListener() {
            private int count = 0; 
            @Override 
            public void onChronometerTick(Chronometer meter) { 
                Log.d("wei","-- " + (count ++));                  
            } 
        });*/ 
    } 
  //��Start button�������� 
   public void onMeterStart(View v){ 
        chrono.setBase(SystemClock.elapsedRealtime());  //setBase�����û�׼ʱ�䣬��ʱ��=��ǰʱ��-��׼ʱ�䣬��������Start��ʱ������Ϊ��׼ʱ�䣬����ʱ����0�뿪ʼ������
        chrono.start();  //��ʼ����
    }  
   //��Stop Button 
   public void onMeterStop(View v){ 
        chrono.stop(); //ֹͣ����
    }  
   //��Reset Button 
   public void onMeterReset(View v){ 
        chrono.setBase(SystemClock.elapsedRealtime());  //��ʱ��reset������ֻҪ����׼ʱ����Ϊ��ǰʱ�䣬�������Ϳ��Թ��㡣 
    } 
}