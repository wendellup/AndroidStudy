package com.imooc._23_Chronometer_CountDownTimer;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.GridView;

import com.imooc.toast.R;

public class UiGridViewTest extends Activity { // ע��û����ListView������extends
												// ListActivity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_gridview);
		GridView gv = (GridView) findViewById(R.id.ui_grid);
		// ��Ȼ����ϵ��Ϊ���ӣ���ȡ����Դ������adapter��֮ǰ��listview������ͬ�����Ƕ���view��������ѡ��ϵͳ�Դ��ļ򵥷�ʽ��
		CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),
				ContactsContract.Contacts.CONTENT_URI, null, null, null,
				ContactsContract.Contacts.DISPLAY_NAME + " ASC");
		Cursor cursor = cursorLoader.loadInBackground();
		String[] cols = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
		int[] views = new int[] { android.R.id.text1 };
		SimpleCursorAdapter simAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor, cols, views,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		// ΪGridView����adapter
		gv.setAdapter(simAdapter); // ȥ��reference����GridView��setAdapter(ListAdapter
									// adapter)��adapterҪ����ListAdapter��grid�Ƕ�ά�ģ�ʵ����һ��һ�еش���ÿ��ʵ��Ҳ��һ��list��
	}
}