package com.imooc.toast;

import android.net.Uri;
import android.provider.BaseColumns;

/* 定义一个类专门给出provider结构，以常量方式给出，根据具体的数据源结构，数据库对应一个authority，而不同的表格为不同的page segment，本例只有一个表格。本例为二层结构。Content://authority/table_name/book_item */
public class BookProviderMetaData {
	/* 步骤1：定义content://authority以及对应数据源（db）的相关属性 */
	/* 步骤1.1 定义provider的内容：以constant的方式定义authority */
	public static final String AUTHORITY = "cn.flowingflying.provider.BookProvider";
	/* 步骤1.2 定义数据源的相关信息，本例对相关的SQLite数据库进行定义 */
	public static final String DATABASE_NAME = "books.db";
	public static final int DATABASE_VERSION = 1;
	public static final String BOOKS_TABLE_NAME = "books";

	/* 步骤1.3 定义私有的构造函数，目的是本类只进行结构描述，只用于定义引用，不能对本类创建对象 */
	private BookProviderMetaData() {
	};

	/*
	 * 步骤2：采用内部类定义content://authority/page_segment、具体的item和对应数据源（table），本例只有一个表格，
	 * 其uri的引用方式为BookProviderMetaData
	 * .BookTableMetaData.CONTENT_URI。SQLite的表格实现BaseColumns接口
	 * ，其中_id是该接口自带的，常量为_ID，每行由系统自动生成唯一的_id
	 */
	public static final class BookTableMetaData implements BaseColumns {
		/* 步骤2.3 定义私有构造类，表明本类制作定义引用，不生成对象 */
		private BookTableMetaData() {
		}; // 不应该进行该对象的创建。只用于描述

		/* 步骤2.1 定义provider的结构，本层结构已经映射到具体的item，定义相关的uri和MIME */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/books");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cn.flowingflying.book";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.cn.flowingflying.book";
		/* 步骤2.2 定义数据源的相关的结构，即表格的相关信息 */
		public static final String TABLE_NAME = "books";
		public static final String BOOK_NAME = "name";
		public static final String BOOK_ISBN = "isbn";
		public static final String BOOK_AUTHOR = "author";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
		public static final String DEFAULE_SORT_ORDER = "modified DESC";
	}
}