package com.imooc._18_Adapter_AdapterView;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;

public class UiAdapterDemo extends ListActivity{ //ListView��Ҫ����ϵͳ��ListActivity
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        //����ϵ����Ϣ����Cursor�У����Ǹ����ṹ��ÿ����ϵ���ж�����ϸ��    
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), 
                ContactsContract.Contacts.CONTENT_URI, 
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        Cursor c = cursorLoader.loadInBackground(); 

       //�ڱ����У�����ֻ�轫��ϵ���е�displayName����Ӧ��ʾ����view��TextView�ؼ��ϡ�from��to�ֱ��¼��Ӧ��ϵ��ע�⣬����������Ϳؼ���һһ��Ӧ��
        String[] from = {ContactsContract.Contacts.DISPLAY_NAME}; 
        int[] to = {android.R.id.text1}; 
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,  /* context */  
               android.R.layout.simple_list_item_1,   /* childLayout ID����view��layout��Id����������ѡ��ϵͳ�Ѿ�ʵ�ֵ�һ���򵥵�layout��������ֻһ��TextView��idΪandroid.R.id.text1����ͽ�����Ϊ����to������Ҫ�����ֵ */ 
               c, /* ����Դ�� cursor*/ 
               from,  /* ����Դ����ѡȡ�������������ϵ���кܶ����������ֻѡ��displayName*/
               to, /* ��Щ����������Ӧ�Ŀؼ�*/
               CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);  /* flag����reference����Ŀǰû���Ƽ��ô�ֵ ����֪ͨ����ʱ���������ݷ����仯��������onContentChanged() */
        this.setListAdapter(adapter);/*��Adapter������ListActivity��ListView*/
    }
}