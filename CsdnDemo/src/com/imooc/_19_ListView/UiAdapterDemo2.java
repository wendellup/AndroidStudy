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
        setContentView(R.layout.ui_add_listview);     //ʹ�������Զ����layout 
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),
                ContactsContract.Contacts.CONTENT_URI, 
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        cur = cursorLoader.loadInBackground(); 
        //��ȡCusor����Դ�и���ؼ���Ϣ����index�����ں������Ϣ���� 
        idCol = cur.getColumnIndex(ContactsContract.Contacts._ID); 
        nameCol = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME); 
        timesContacted = cur.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED);

        String[] cols = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
        int[] views = new int[]{android.R.id.text1};  
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_multiple_choice,  //������View��layout����Դ�����ļ������ǿ�������idΪandroid.R.id.text1�������int[] views�����ֵ����CheckedTextView��CheckedTextView��TextView�����࣬������һ��TextView��һ��CheckBox
                cur, cols, views,CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        this.setListAdapter(adapter); 

        ListView lv = getListView(); 
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);  //����listviewģʽ��ʹ�ö�ѡģʽ��ȱʡ��CHOICE_MODE_NONE�����⻹��CHOICE_MODE_SINGLE����Ӧ�ģ�������adapter������sub view������android.R.layout.simple_list_item_single_choice�� 
    } 
    
    public void submitFunc(View v){ 
        ListView lv = getListView(); 
        SparseBooleanArray positionChecked = lv.getCheckedItemPositions(); // ����android.widget.AbsListView�ĺ��������ڴ�Ŷ�ѡ��ģʽlistview��ѡ����� 
        for(int i = 0 ; i < lv.getCount() ; i ++){ 
            Log.d("listAddControl","position [" + i +"] " + positionChecked.get(i));
        } 
        
        for(int i = 0 ; i < lv.getCount(); i++){    //�ҳ�item�Ķ�Ӧ������Դ����ʾ��Ϊ��ϸ����Ϣ
            if(positionChecked.get(i)){ 
                cur.moveToPosition(i);  //������Ҳ����ʹ��AdapterView�� Object getItemAtPosition(int position)����Content Provider���з��ص���CursorWrapper��������Ϣ��ʽ����������Դ��ء�����Ҳ����ʹ��CursorWrapper cw = (CursorWrapper) lv.getItemAtPosition(i);
                Log.d("listAddControl","[" + i  +"]: _id=" + cur.getLong(idCol) + "   " + cur.getString(nameCol)
                        + "   Contact times:" + cur.getInt(timesContacted)); 
            } 
        } 
    } 

}