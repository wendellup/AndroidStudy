package com.imooc._22_Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.imooc.toast.R;

public class UiGridCustomTest1 extends Activity{ 
    /* ��ϵͳ�ṩ��Adapter��ʽ�ڵ�����û��ʲô��ͬ��ֻ�����ǽ���MyAdapter��ֱ�ӻ�ȡ���ݣ���ֱ��ָ����γ��֣�����Ҫ��������Դ����Ϣ��*/
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.ui_gridview); 
        GridView gv = (GridView)findViewById(R.id.ui_grid); 
        MyAdapter adapter = new MyAdapter(this); 
        gv.setAdapter(adapter); 
    } 
}