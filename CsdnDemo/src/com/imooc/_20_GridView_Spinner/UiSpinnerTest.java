package com.imooc._20_GridView_Spinner;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.imooc.toast.R;

public class UiSpinnerTest extends Activity implements OnItemSelectedListener{ 

    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.ui_spinner); 
        Spinner spin = (Spinner)findViewById(R.id.ui_spinner); 
        //��������ArrayAdapter������ĵ�����������������ͨģʽ����ʾ����һ���򵥵�TextView����Ȼ������ӵ�ֻ������һ��item�������������listview��һ����
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets, android.R.layout.simple_spinner_item);
        //ͨ��setDropDownViewResource����pop-up list����view��ʾlayout�����ǲ����˲�ͬ��layout���ֱ�Ϊ�в�ͬ����ʾЧ��������ͼ��ʾ����Ҫע�����Android�汾��ͬ�����layout��ʵ�ʲ����в�ͬ����API Level 17��Android 4.2.2���У�simple_spinner_dropdown_item��ȥ��checkMark��
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spin.setAdapter(adapter); 
        //ѡ��仯������OnItemSelectedListener
        spin.setOnItemSelectedListener(this); 
    } 

    @Override 
    public void onItemSelected(AdapterView<?> arg0, View view, int pos, long index) { 
        Log.d("Spinner",view.toString()); 
        Log.d("Spinner","" + ((TextView)view).getText()); 
        //��������ͨģʽ����view��TextView����pop-up list��CheckedTextView����onItemSelected�У�������view����ͨģʽ�µ���view�������Ҫ�ر�ע�⡣     
        Log.d("Spinner","" + pos + " " + index); 
    } 

    @Override 
    public void onNothingSelected(AdapterView<?> arg0) {  
        Log.d("Spinner","Nothing Selected"); 
    }  

}