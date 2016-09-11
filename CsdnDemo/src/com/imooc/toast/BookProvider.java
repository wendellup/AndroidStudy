package com.imooc.toast;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.imooc.toast.BookProviderMetaData.BookTableMetaData;

/* �������ǵ�BookProvider���ȶ�����Դ���д���Ϊ��һ����provider�ṩ����ɾ�Ĳ�Ƚӿڽ���׼�������ȴ�������Դ��Ȼ�������Դ��I/O�ӿڣ�provider�����д��� */
public class BookProvider extends ContentProvider {
	/*
	 * ����3: setup projection
	 * Map����Provider���⿪�ŵ�projection�������ڲ�����Դ���ݿ���������ж�Ӧ��ͨ��HashMap��provider�Ķ��⹫������
	 * ����projection
	 * ����ʵ������Դ���ڲ����ֽ���ӳ�䡣Ŀ�����ṩһ���ȶ�provider�ӿڡ�������Դ�����仯ʱ���������˱�������������ֻ��Ҫ�޸���Ӧ��ӳ���ϵ
	 * �����ܱ��ֶ���ӿڵ�һ��
	 * ����Ӱ��ͨ��provider�������ݵ�����Ӧ�á�ͬʱҲ�������������壬�������ݿ�����Ϊname�����Զ���projection������Ϊpeople
	 * .name�����ӳ���ϵ��QueryBuilder�࣬ͨ��setProjectMap()�趨����query()��ʵ��
	 */
	private static HashMap<String, String> sBooksProjectionMap;
	static {
		sBooksProjectionMap = new HashMap<String, String>();
		sBooksProjectionMap.put(BookTableMetaData._ID, BookTableMetaData._ID);
		sBooksProjectionMap.put(BookTableMetaData.BOOK_NAME,
				BookTableMetaData.BOOK_NAME);
		sBooksProjectionMap.put(BookTableMetaData.BOOK_ISBN,
				BookTableMetaData.BOOK_ISBN);
		sBooksProjectionMap.put(BookTableMetaData.BOOK_AUTHOR,
				BookTableMetaData.BOOK_AUTHOR);
		sBooksProjectionMap.put(BookTableMetaData.CREATED_DATE,
				BookTableMetaData.CREATED_DATE);
		sBooksProjectionMap.put(BookTableMetaData.MODIFIED_DATE,
				BookTableMetaData.MODIFIED_DATE);
	}

	/*
	 * ����1��������Դ���д������������ڲ��෽ʽ����������Դ����SQLite��SQLiteͨ����SQLiteOpenHelper�ļ̳���ʵ�֣�
	 * ���Բο�Androidѧϰ�ʼǣ���һ����SQLite��ʹ�� �� ������дonCreate()��onUpgrade()�Լ����캯��
	 */
	private class DatabaseBookHelper extends SQLiteOpenHelper {
		/* ����1.1 ��д���캯�����������ݿ���Ϣ */
		DatabaseBookHelper(Context context) {
			super(context, BookProviderMetaData.DATABASE_NAME, null,
					BookProviderMetaData.DATABASE_VERSION);
		}

		/* ����1.2��@Override onCreate()��ͨCREATE TABLE�������ݿ�ı�� */
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + BookProviderMetaData.BOOKS_TABLE_NAME
					+ " (" + BookTableMetaData._ID + " INTEGER PRIMARY KEY,"
					+ BookTableMetaData.BOOK_NAME + " TEXT,"
					+ BookTableMetaData.BOOK_ISBN + " TEXT,"
					+ BookTableMetaData.BOOK_AUTHOR + " TEXT,"
					+ BookTableMetaData.CREATED_DATE + " INTEGER,"
					+ BookTableMetaData.MODIFIED_DATE + " INTEGER" + ");");
		}

		/*
		 * ����1.3��@Override onUpgrade�����������ݿ�İ汾���ڵ�ǰ�汾ʱ�������ݿ����������������ɾ��ԭ����
		 * ȫ�����ݣ����½����µı��
		 */
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "
					+ BookProviderMetaData.BOOKS_TABLE_NAME);
			onCreate(db);
		}

	} // End of ����1

	private DatabaseBookHelper mDataSource = null; // ����2�����ò���ȡ����Դ����

	/*
	 * ����4���ṩ�ж�����uri��ʽ�Ļ��ơ� ͨ��UriMatch��input��Uri����ƥ�䣬�ж�uri�Ƿ�Ϸ�����collection����item
	 * ��uriMatch������ͨ��addURI�Ǽ�uri����ʽ��һ��Ψһ��number����ƥ��ʱ��uriMatch�����ظ�number��
	 */
	private static final UriMatcher sUriMatch;
	// ����ƥ��ʱ���ص�number������ֻ��1��table������collection��item���֡�
	private static final int BOOK_COLLECTION_URI_INDICATOR = 1;
	private static final int BOOK_ITEM_URI_INDICATION = 2;
	static {
		// UriMatther�Ĵ����������Ժ��������û��path_segmentҲû��authoritiesʱ���ص�number��
		// ���������ƥ�䣬UriMatchҲ�᷵��NO_MATCH������Ҳ����д��sUriMatch = new UriMatcher();
		sUriMatch = new UriMatcher(UriMatcher.NO_MATCH);
		// �Ǽ�uri����ʽ�����Ӧ��numberֵ
		sUriMatch.addURI(BookProviderMetaData.AUTHORITY, "books",
				BOOK_COLLECTION_URI_INDICATOR);
		sUriMatch.addURI(BookProviderMetaData.AUTHORITY, "books/#",
				BOOK_ITEM_URI_INDICATION);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
	    int count = 0; //��¼ɾ��item��Ŀ 
	    SQLiteDatabase db = mDataSource.getWritableDatabase(); 
	    //ɾ��������uri��Ϊcollection��item������� 
	    switch(sUriMatch.match(uri)){ 
	    case BOOK_COLLECTION_URI_INDICATOR: 
	        count = db.delete(BookTableMetaData.TABLE_NAME, where, whereArgs); 
	        break; 
	    case BOOK_ITEM_URI_INDICATION: 
	        String id = uri.getPathSegments().get(1); //��0��ʼ���㣬������0Ϊcontent://xxxx/books/2�е�books 
	        count = db.delete(BookTableMetaData.TABLE_NAME, BookTableMetaData._ID + "=" + id
	                +(TextUtils.isEmpty(where) ? "" : " AND ("  + where + ")"), whereArgs);
	        break; 
	    default: 
	        throw new IllegalArgumentException("Unknown Uri " + uri); 
	    } 
	    getContext().getContentResolver().notifyChange(uri, null);  //֪ͨ��uri�����ݷ����仯 
	    return count; 
	}

	/* ͨ��getType()��Provider�����ṩuri��ȡ���������ͣ�MIME Type������Ϊcollection��item��������� */
	@Override
	public String getType(Uri uri) {
		switch (sUriMatch.match(uri)) {
		case BOOK_COLLECTION_URI_INDICATOR:
			return BookTableMetaData.CONTENT_TYPE;
		case BOOK_ITEM_URI_INDICATION:
			return BookTableMetaData.CONTENT_ITEM_TYPE;
		default: // ��Чuri
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initalValue) { 
		   //1�����uri�Ƿ�Ϸ�����������item����uriֻ����collection��ʽ 
		    if(sUriMatch.match(uri)  != BOOK_COLLECTION_URI_INDICATOR){ 
		        throw new IllegalArgumentException("Unknown Uri " + uri); 
		    } 

		    //2����ContentValues�л�ȡ���ݣ���������Ƿ���Ч���걸 
		    ContentValues cv ; 
		    if(initalValue == null){   //�������յ�һ������ 
		        cv = new ContentValues(); 
		    }else{ 
		        cv = new ContentValues(initalValue); 
		    } 
		   //��������Ƿ�ȱʧ��������ȱʡֵ
		    if(!cv.containsKey(BookTableMetaData.BOOK_NAME)){ 
		        throw new SQLException("Failed to insert row because Book Name is needed " + uri);
		    } 
		    if(!cv.containsKey(BookTableMetaData.BOOK_ISBN)){ 
		        cv.put(BookTableMetaData.BOOK_ISBN, "Unknown ISBN"); 
		    } 
		    if(!cv.containsKey(BookTableMetaData.BOOK_AUTHOR)){ 
		        cv.put(BookTableMetaData.BOOK_AUTHOR, "Unknown author"); 
		    }        
		    Long now = Long.valueOf(System.currentTimeMillis()); 
		    if(!cv.containsKey(BookTableMetaData.CREATED_DATE)){ 
		        cv.put(BookTableMetaData.CREATED_DATE, now); 
		    } 
		    if(!cv.containsKey(BookTableMetaData.MODIFIED_DATE)){ 
		        cv.put(BookTableMetaData.MODIFIED_DATE, now); 
		    } 

		   //3�������ݿ���������row 
		    SQLiteDatabase db = mDataSource.getWritableDatabase(); 
		    long rowId = db.insert(BookTableMetaData.TABLE_NAME, BookTableMetaData.BOOK_NAME, cv);
		    if(rowId > 0){ 
		        Uri insertUri = ContentUris.withAppendedId(BookTableMetaData.CONTENT_URI, rowId);
		        //4��֪ͨ��uri�����ݷ����仯 
		        getContext().getContentResolver().notifyChange(insertUri, null); 
		        return insertUri; 
		    } 
		    throw new SQLException("Failed to insert row into " + uri); 
		}

	/* ����2����������Դ�������������ݿ�Ķ��� */
	public boolean onCreate() {
		mDataSource = new DatabaseBookHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] protection, String selection,
			String[] selectionArgs, String order) {
		// 1��ͨ��SQLiteQueryBuilder���������ݿ��ѯ����Ϣ��Uri�����������һ����collect��һ���Ѿ�ָ��ĳ��item��
		// ������Ҫ����Դ���item����ȡ_ID������where������һ��ƥ��������
		SQLiteQueryBuilder dbQuery = new SQLiteQueryBuilder();
		switch (sUriMatch.match(uri)) {
		case BOOK_COLLECTION_URI_INDICATOR:
			dbQuery.setTables(BookTableMetaData.TABLE_NAME);
			dbQuery.setProjectionMap(sBooksProjectionMap); // �趨��project��ӳ���ϵ
			break;
		case BOOK_ITEM_URI_INDICATION:
			dbQuery.setTables(BookTableMetaData.TABLE_NAME);
			dbQuery.setProjectionMap(sBooksProjectionMap); // �趨��project��ӳ���ϵ
			// ͨ��getPathSegments( )��ȡ�����ID��path segment��0���Ǽ��㣬������0Ϊbooks��1Ϊ#
			dbQuery.appendWhere(BookTableMetaData._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}
		// 2����ȱʡ�������ã�������
		String orderBy;
		if (TextUtils.isEmpty(order)) {
			orderBy = BookTableMetaData.DEFAULE_SORT_ORDER;
		} else {
			orderBy = order;
		}
		// 3����ѯ���ݿ�
		SQLiteDatabase db = mDataSource.getReadableDatabase();
		Cursor c = dbQuery.query(db, protection, selection, selectionArgs,
				null, null, orderBy);

		// 4����ϵͳע��֪ͨ���۲���Ҫ��ѯ�����ݣ���Uri��Ӧ�����ݣ��Ƿ����仯��������ͨ��provider�ӿڻ�ȡ���ݣ�
		// ��ͨ��֪ͨ��֪�����Ѿ����������
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
	    int count = 0; //��¼����item��Ŀ 
	    SQLiteDatabase db = mDataSource.getWritableDatabase(); 
	    //���£�����uri��Ϊcollection��item������� 
	    switch(sUriMatch.match(uri)){ 
	    case BOOK_COLLECTION_URI_INDICATOR: 
	        count = db.update(BookTableMetaData.TABLE_NAME, values, whereClause, whereArgs);
	        break; 
	    case BOOK_ITEM_URI_INDICATION: 
	        count = db.update(BookTableMetaData.TABLE_NAME, values, 
	                BookTableMetaData._ID + "=" + uri.getPathSegments().get(1) 
	                + (!TextUtils.isEmpty(whereClause)? " AND (" + whereClause + ")" : ""), whereArgs);
	        break; 
	    default: 
	        throw new IllegalArgumentException("Unknown Uri " + uri); 
	    } 
	    getContext().getContentResolver().notifyChange(uri,null); //֪ͨ��uri�����ݷ����仯 
	    return count; 
	}

}