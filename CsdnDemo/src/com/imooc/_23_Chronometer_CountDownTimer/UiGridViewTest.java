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

public class UiGridViewTest extends Activity { // 注意没有像ListView那样有extends
												// ListActivity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_gridview);
		GridView gv = (GridView) findViewById(R.id.ui_grid);
		// 仍然以联系人为例子，获取数据源和设置adapter和之前的listview例子相同，我们对子view的设置仍选择系统自带的简单方式。
		CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),
				ContactsContract.Contacts.CONTENT_URI, null, null, null,
				ContactsContract.Contacts.DISPLAY_NAME + " ASC");
		Cursor cursor = cursorLoader.loadInBackground();
		String[] cols = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
		int[] views = new int[] { android.R.id.text1 };
		SimpleCursorAdapter simAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor, cols, views,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		// 为GridView关联adapter
		gv.setAdapter(simAdapter); // 去查reference发现GridView的setAdapter(ListAdapter
									// adapter)，adapter要求是ListAdapter，grid是二维的，实际上一行一行地处理，每行实际也是一个list。
	}
}