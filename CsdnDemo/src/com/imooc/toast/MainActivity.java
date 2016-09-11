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
	 * ��ʼ������¼�
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
	 * ��ʾĬ��toast
	 */
	private void showToast1() {
		// Toast toast =
		// Toast.makeText(this,"����һ��Ĭ�ϵ�Toast!",Toast.LENGTH_SHORT);
		Toast toast = Toast
				.makeText(this, R.string.app_name, Toast.LENGTH_LONG);
		toast.show();
	}

	/**
	 * ��ʾ�Զ���λ�õ�Toast
	 */
	private void showToast2() {
		Toast toast = Toast.makeText(this, "�ı�λ�õ�Toast!", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * ����ͼƬ��toast��
	 */
	private void showToast3() {
		Toast toast = Toast.makeText(this, "����ͼƬ��Toast��", Toast.LENGTH_LONG);
		LinearLayout toast_layout = (LinearLayout) toast.getView();
		ImageView iv = new ImageView(this);
		iv.setImageResource(R.drawable.topimg);
		toast_layout.addView(iv, 0);
		toast.show();
	}

	/**
	 * ��ȫ�Զ����Toast
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
		 * ��ContentProvider��insert()�����Ĳ������Կ�����ͨ��ContentValues���������ݵĴ��ݡ�
		 * ContentValues��key/values�ԣ����Դ洢����飬�ǳ��ʺϴ�����Ϣ
		 */
		ContentValues cv = new ContentValues();
		cv.put(BookTableMetaData.BOOK_NAME, name);
		cv.put(BookTableMetaData.BOOK_ISBN, isbn);
		cv.put(BookTableMetaData.BOOK_AUTHOR, author);
		/*
		 * ContentResolver����URI
		 * reference��������ȷ��provider�ϣ����Ը�provider�Ĺ����Ķ�д�ӿڽ��в���
		 * �����ķ�����provider�ṩ�ķ���һһ��Ӧ����������uri������
		 */
		ContentResolver cr = getContentResolver();
		Uri insertUri = cr.insert(
				BookProviderMetaData.BookTableMetaData.CONTENT_URI, cv); // ��ӦBookProvider��insert()
	}

	private void showBooks() {
		Uri uri = BookProviderMetaData.BookTableMetaData.CONTENT_URI;
		/* ��ʽһ��ͨ��managedQuery( ) */
		@SuppressWarnings("deprecation")
		Cursor c = managedQuery(uri, null, null, null, null);
		/* ��ʽ��������ContentResolver��query����ȡ */
		// ContentResolver cr = getContentResolver();
		// Cursor c = cr.query(uri, null, null, null, null);
		int iIndex = c.getColumnIndex(BookTableMetaData._ID);
		int iName = c.getColumnIndex(BookTableMetaData.BOOK_NAME);
		int iIsbn = c.getColumnIndex(BookTableMetaData.BOOK_ISBN);
		int iAuthor = c.getColumnIndex(BookTableMetaData.BOOK_AUTHOR);

		/*
		 * ��Ҫע�⣬ͨ������showInfo��ʾ�����е�Id���ǰ����ǵĴ���table���е�˳������һ��Ҫ�Ȼ�ȡ�е�ID������row��_id���ǰ�˳���ṩ
		 * ��Ϊ��ǰ����_id+1�������������ܺ���ȷ�����һ�㲻ʹ����Щϵͳ�ڲ����ŵ�Id
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
