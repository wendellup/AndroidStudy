package com.imooc._19_ListView;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class UiAdapterDemo extends ListActivity implements OnItemClickListener{
  
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
        ListView lv = getListView();  //获取ListView控件 
        lv.setOnItemClickListener(this);  //稍微做点变化
        Log.d("UiAdapterDemo","Listview is " + lv.toString());
    } 

    //请配合下面运行的结果图来看各参数的含义 
    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
        // parent本例就是ListView，可以验证和listview是同一对象 
        Log.d("onItemClick","parent : " + parent.toString()); 
        // position是在list的位置，即子view的序号，从0还是计算。rowId是数据源的序号，与具体的数据源方式有关，如果来自cursor，这是_ID，如果来自array，则是array[rowId]。 
        Log.d("onItemClick","position = " + position + "   Row id is " + rowId); 

        //在层次结构上，一般是ListView –>子View（Layout）—>子View中的具体控件，而本例的结构很简单为ListView –> 子View（TextView），我们可以通过这两种方式来获取具体控件的信息
        //方式1：ListView –> 子View（TextView） view就是子view，本例是android.R.layout.simple_list_item_1，就是一个简单的TextView，所以子view就是TextView。
        Log.d("onItemClick","view : " + view.toString()); 
        Log.d("onItemClick","name : " + ((TextView)view).getText()); 
        //方式2： ListView –>子View（Layout）—>子View中的具体控件也可以按ApdaterView的结构，将子view视为是一个容器，通过findViewById找到里面相应的控件，两种方式的结果是一样。
        TextView tv = (TextView) view.findViewById(android.R.id.text1); 
        Log.d("onItemClick","tv : " + tv.toString()); 
        Log.d("onItemClick","name : " + tv.getText()); 

        //点击触发打开该联系人，在content Provider的情况下，rowId就是_ID，但如果是ArrayAdapter，则rowId就是在数组的位置，即position和rowId会一样。
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, rowId);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri); 
        startActivity(intent); 
    } 
}