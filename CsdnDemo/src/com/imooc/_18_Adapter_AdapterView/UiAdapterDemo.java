package com.imooc._18_Adapter_AdapterView;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;

public class UiAdapterDemo extends ListActivity{ //ListView需要集成系统的ListActivity
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        //将联系人信息读到Cursor中，这是个表格结构，每个联系人有多项详细。    
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), 
                ContactsContract.Contacts.CONTENT_URI, 
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        Cursor c = cursorLoader.loadInBackground(); 

       //在本例中，我们只需将联系人中的displayName，对应显示到子view中TextView控件上。from和to分别记录对应关系。注意，这是数据项和控件的一一对应。
        String[] from = {ContactsContract.Contacts.DISPLAY_NAME}; 
        int[] to = {android.R.id.text1}; 
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,  /* context */  
               android.R.layout.simple_list_item_1,   /* childLayout ID：子view的layout的Id，这里我们选用系统已经实现的一个简单的layout，它里面只一个TextView，id为android.R.id.text1，这就解释了为何在to中我们要填入此值 */ 
               c, /* 数据源： cursor*/ 
               from,  /* 数据源中所选取数据项，本例中联系人有很多数据项，我们只选择displayName*/
               to, /* 这些数据项所对应的控件*/
               CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);  /* flag，从reference看，目前没有推荐用此值 ，当通知到来时（例如数据发生变化），触发onContentChanged() */
        this.setListAdapter(adapter);/*将Adapter适用于ListActivity的ListView*/
    }
}