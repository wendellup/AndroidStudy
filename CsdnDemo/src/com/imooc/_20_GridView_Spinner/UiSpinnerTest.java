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
        //本例采用ArrayAdapter，这里的第三个参数是设置普通模式的显示，即一个简单的TextView，虽然这里可视的只是其中一个item，但整个概念和listview的一样。
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets, android.R.layout.simple_spinner_item);
        //通过setDropDownViewResource设置pop-up list的子view显示layout，我们采用了不同的layout，分别为有不同的显示效果，见上图所示。需要注意的是Android版本不同，会对layout的实际布局有不同，在API Level 17（Android 4.2.2）中，simple_spinner_dropdown_item中去掉checkMark。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spin.setAdapter(adapter); 
        //选择变化，触发OnItemSelectedListener
        spin.setOnItemSelectedListener(this); 
    } 

    @Override 
    public void onItemSelected(AdapterView<?> arg0, View view, int pos, long index) { 
        Log.d("Spinner",view.toString()); 
        Log.d("Spinner","" + ((TextView)view).getText()); 
        //本例中普通模式的子view是TextView，而pop-up list的CheckedTextView，在onItemSelected中，给出的view是普通模式下的子view，这点需要特别注意。     
        Log.d("Spinner","" + pos + " " + index); 
    } 

    @Override 
    public void onNothingSelected(AdapterView<?> arg0) {  
        Log.d("Spinner","Nothing Selected"); 
    }  

}