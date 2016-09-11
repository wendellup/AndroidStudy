package com.imooc.toast;

import android.net.Uri;
import android.provider.BaseColumns;

/* ����һ����ר�Ÿ���provider�ṹ���Գ�����ʽ���������ݾ��������Դ�ṹ�����ݿ��Ӧһ��authority������ͬ�ı��Ϊ��ͬ��page segment������ֻ��һ����񡣱���Ϊ����ṹ��Content://authority/table_name/book_item */
public class BookProviderMetaData {
	/* ����1������content://authority�Լ���Ӧ����Դ��db����������� */
	/* ����1.1 ����provider�����ݣ���constant�ķ�ʽ����authority */
	public static final String AUTHORITY = "cn.flowingflying.provider.BookProvider";
	/* ����1.2 ��������Դ�������Ϣ����������ص�SQLite���ݿ���ж��� */
	public static final String DATABASE_NAME = "books.db";
	public static final int DATABASE_VERSION = 1;
	public static final String BOOKS_TABLE_NAME = "books";

	/* ����1.3 ����˽�еĹ��캯����Ŀ���Ǳ���ֻ���нṹ������ֻ���ڶ������ã����ܶԱ��ഴ������ */
	private BookProviderMetaData() {
	};

	/*
	 * ����2�������ڲ��ඨ��content://authority/page_segment�������item�Ͷ�Ӧ����Դ��table��������ֻ��һ�����
	 * ��uri�����÷�ʽΪBookProviderMetaData
	 * .BookTableMetaData.CONTENT_URI��SQLite�ı��ʵ��BaseColumns�ӿ�
	 * ������_id�Ǹýӿ��Դ��ģ�����Ϊ_ID��ÿ����ϵͳ�Զ�����Ψһ��_id
	 */
	public static final class BookTableMetaData implements BaseColumns {
		/* ����2.3 ����˽�й����࣬�������������������ã������ɶ��� */
		private BookTableMetaData() {
		}; // ��Ӧ�ý��иö���Ĵ�����ֻ��������

		/* ����2.1 ����provider�Ľṹ������ṹ�Ѿ�ӳ�䵽�����item��������ص�uri��MIME */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/books");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cn.flowingflying.book";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.cn.flowingflying.book";
		/* ����2.2 ��������Դ����صĽṹ�������������Ϣ */
		public static final String TABLE_NAME = "books";
		public static final String BOOK_NAME = "name";
		public static final String BOOK_ISBN = "isbn";
		public static final String BOOK_AUTHOR = "author";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
		public static final String DEFAULE_SORT_ORDER = "modified DESC";
	}
}