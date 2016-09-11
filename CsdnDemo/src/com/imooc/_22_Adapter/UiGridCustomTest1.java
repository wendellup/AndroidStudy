package com.imooc._22_Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.imooc.toast.R;

public class UiGridCustomTest1 extends Activity{ 
    /* 和系统提供的Adapter方式在调用上没有什么不同，只是我们将在MyAdapter中直接获取数据，和直接指定如何呈现，不需要传递数据源等信息。*/
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.ui_gridview); 
        GridView gv = (GridView)findViewById(R.id.ui_grid); 
        MyAdapter adapter = new MyAdapter(this); 
        gv.setAdapter(adapter); 
    } 
}