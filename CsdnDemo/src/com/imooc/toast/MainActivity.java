package com.imooc.toast;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imooc.toast.BookProviderMetaData.BookTableMetaData;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initEvent();
	}

	/**
	 * 初始化点击事件
	 */
	private void initEvent() {
		findViewById(R.id.toast_btn1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showToast1();
			}
		});
		findViewById(R.id.toast_btn2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showToast2();
			}
		});
		findViewById(R.id.toast_btn3).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showToast3();
			}
		});
		findViewById(R.id.toast_btn4).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showToast4();
			}
		});

		findViewById(R.id.btn_data_add).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						addBook("book1", "isbn", "authoer");
					}
				});
		
		
		findViewById(R.id.btn_data_query).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						showBooks();
					}
				});

	}

	/**
	 * 显示默认toast
	 */
	private void showToast1() {
		// Toast toast =
		// Toast.makeText(this,"这是一个默认的Toast!",Toast.LENGTH_SHORT);
		Toast toast = Toast
				.makeText(this, R.string.app_name, Toast.LENGTH_LONG);
		toast.show();
	}

	/**
	 * 显示自定义位置的Toast
	 */
	private void showToast2() {
		Toast toast = Toast.makeText(this, "改变位置的Toast!", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 带有图片的toast；
	 */
	private void showToast3() {
		Toast toast = Toast.makeText(this, "带有图片的Toast！", Toast.LENGTH_LONG);
		LinearLayout toast_layout = (LinearLayout) toast.getView();
		ImageView iv = new ImageView(this);
		iv.setImageResource(R.drawable.topimg);
		toast_layout.addView(iv, 0);
		toast.show();
	}

	/**
	 * 完全自定义的Toast
	 */
	private void showToast4() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View toast_view = inflater.inflate(R.layout.toast_layout, null);
		Toast toast = new Toast(this);
		toast.setView(toast_view);
		toast.show();
	}

	private void addBook(String name, String isbn, String author) {
		/*
		 * 从ContentProvider的insert()方法的参数可以看到，通过ContentValues来进行数据的传递。
		 * ContentValues是key/values对，可以存储多个组，非常适合传递信息
		 */
		ContentValues cv = new ContentValues();
		cv.put(BookTableMetaData.BOOK_NAME, name);
		cv.put(BookTableMetaData.BOOK_ISBN, isbn);
		cv.put(BookTableMetaData.BOOK_AUTHOR, author);
		/*
		 * ContentResolver负责将URI
		 * reference解析到正确的provider上，并对该provider的公开的读写接口进行操作
		 * 。它的方法和provider提供的方法一一对应，并增加了uri参数。
		 */
		ContentResolver cr = getContentResolver();
		Uri insertUri = cr.insert(
				BookProviderMetaData.BookTableMetaData.CONTENT_URI, cv); // 对应BookProvider的insert()
	}

	private void showBooks() {
		Uri uri = BookProviderMetaData.BookTableMetaData.CONTENT_URI;
		/* 方式一，通过managedQuery( ) */
		@SuppressWarnings("deprecation")
		Cursor c = managedQuery(uri, null, null, null, null);
		/* 方式二：利用ContentResolver的query来获取 */
		// ContentResolver cr = getContentResolver();
		// Cursor c = cr.query(uri, null, null, null, null);
		int iIndex = c.getColumnIndex(BookTableMetaData._ID);
		int iName = c.getColumnIndex(BookTableMetaData.BOOK_NAME);
		int iIsbn = c.getColumnIndex(BookTableMetaData.BOOK_ISBN);
		int iAuthor = c.getColumnIndex(BookTableMetaData.BOOK_AUTHOR);

		/*
		 * 需要注意，通过下面showInfo显示，各列的Id并非按我们的创建table的列的顺序，所以一定要先获取列的ID。至于row的_id，是按顺序提供
		 * ，为当前最大的_id+1。但除非我们能很明确清楚，一般不使用这些系统内部安排的Id
		 */
		showInfo("ColumnIndex _id=" + iIndex + ",name=" + iName + ",isdn="
				+ iIsbn + ", author=" + iAuthor);
		showInfo("Totle books : " + c.getCount());
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			String id = c.getString(iIndex);
			String name = c.getString(iName);
			String isbn = c.getString(iIsbn);
			String author = c.getString(iAuthor);
			showInfo("[" + id + "]" + "\t" + name + "\t" + isbn + "\t" + author);
		}
		c.close();
	}

	private void showInfo(String string) {
		TextView tv = (TextView)findViewById(R.id.tv_result);
		String text = tv.getText().toString();
		if(string!=null && !"".equals(string)){
			tv.setText(text+"\r\n"+string);
		}
	}
}
