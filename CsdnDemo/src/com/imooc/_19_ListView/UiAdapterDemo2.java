package com.imooc._19_ListView;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;

import com.imooc.toast.R;

public class UiAdapterDemo2 extends ListActivity{
    private int idCol; 
    private int nameCol; 
    private int timesContacted; 
    private Cursor cur;
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.ui_add_listview);     //使用我们自定义的layout 
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),
                ContactsContract.Contacts.CONTENT_URI, 
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        cur = cursorLoader.loadInBackground(); 
        //获取Cusor数据源中各项关键信息的列index，用于后面的信息检索 
        idCol = cur.getColumnIndex(ContactsContract.Contacts._ID); 
        nameCol = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME); 
        timesContacted = cur.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED);

        String[] cols = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
        int[] views = new int[]{android.R.id.text1};  
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_multiple_choice,  //这是子View的layout，打开源代码文件，我们看到这是id为android.R.id.text1（这就是int[] views所填的值）的CheckedTextView，CheckedTextView是TextView的子类，样子是一个TextView和一个CheckBox
                cur, cols, views,CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        this.setListAdapter(adapter); 

        ListView lv = getListView(); 
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);  //设置listview模式，使用多选模式。缺省是CHOICE_MODE_NONE，此外还有CHOICE_MODE_SINGLE（相应的，我们在adapter中设置sub view可以用android.R.layout.simple_list_item_single_choice。 
    } 
    
    public void submitFunc(View v){ 
        ListView lv = getListView(); 
        SparseBooleanArray positionChecked = lv.getCheckedItemPositions(); // 来自android.widget.AbsListView的函数，用于存放多选择模式listview的选择情况 
        for(int i = 0 ; i < lv.getCount() ; i ++){ 
            Log.d("listAddControl","position [" + i +"] " + positionChecked.get(i));
        } 
        
        for(int i = 0 ; i < lv.getCount(); i++){    //找出item的对应的数据源，显示更为详细的信息
            if(positionChecked.get(i)){ 
                cur.moveToPosition(i);  //在这里也可以使用AdapterView的 Object getItemAtPosition(int position)，在Content Provider，中返回的是CursorWrapper，返回信息格式与具体的数据源相关。本例也可以使用CursorWrapper cw = (CursorWrapper) lv.getItemAtPosition(i);
                Log.d("listAddControl","[" + i  +"]: _id=" + cur.getLong(idCol) + "   " + cur.getString(nameCol)
                        + "   Contact times:" + cur.getInt(timesContacted)); 
            } 
        } 
    } 

}