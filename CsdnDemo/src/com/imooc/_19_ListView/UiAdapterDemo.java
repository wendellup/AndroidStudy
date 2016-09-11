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
        ListView lv = getListView();  //��ȡListView�ؼ� 
        lv.setOnItemClickListener(this);  //��΢����仯
        Log.d("UiAdapterDemo","Listview is " + lv.toString());
    } 

    //������������еĽ��ͼ�����������ĺ��� 
    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
        // parent��������ListView��������֤��listview��ͬһ���� 
        Log.d("onItemClick","parent : " + parent.toString()); 
        // position����list��λ�ã�����view����ţ���0���Ǽ��㡣rowId������Դ����ţ�����������Դ��ʽ�йأ��������cursor������_ID���������array������array[rowId]�� 
        Log.d("onItemClick","position = " + position + "   Row id is " + rowId); 

        //�ڲ�νṹ�ϣ�һ����ListView �C>��View��Layout����>��View�еľ���ؼ����������Ľṹ�ܼ�ΪListView �C> ��View��TextView�������ǿ���ͨ�������ַ�ʽ����ȡ����ؼ�����Ϣ
        //��ʽ1��ListView �C> ��View��TextView�� view������view��������android.R.layout.simple_list_item_1������һ���򵥵�TextView��������view����TextView��
        Log.d("onItemClick","view : " + view.toString()); 
        Log.d("onItemClick","name : " + ((TextView)view).getText()); 
        //��ʽ2�� ListView �C>��View��Layout����>��View�еľ���ؼ�Ҳ���԰�ApdaterView�Ľṹ������view��Ϊ��һ��������ͨ��findViewById�ҵ�������Ӧ�Ŀؼ������ַ�ʽ�Ľ����һ����
        TextView tv = (TextView) view.findViewById(android.R.id.text1); 
        Log.d("onItemClick","tv : " + tv.toString()); 
        Log.d("onItemClick","name : " + tv.getText()); 

        //��������򿪸���ϵ�ˣ���content Provider������£�rowId����_ID���������ArrayAdapter����rowId�����������λ�ã���position��rowId��һ����
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, rowId);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri); 
        startActivity(intent); 
    } 
}