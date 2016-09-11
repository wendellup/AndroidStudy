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

/* 创建我们的BookProvider，先对数据源进行处理，为下一步对provider提供的增删改查等接口进行准备，即先处理数据源，然后对数据源的I/O接口（provider）进行处理。 */
public class BookProvider extends ContentProvider {
	/*
	 * 步骤3: setup projection
	 * Map，将Provider对外开放的projection名字与内部数据源数据库的列名进行对应。通过HashMap将provider的对外公布名字
	 * （即projection
	 * ）和实际数据源的内部名字进行映射。目的是提供一个稳定provider接口。当数据源发生变化时，例如变更了表格的列名，这是只需要修改相应的映射关系
	 * ，仍能保持对外接口的一致
	 * ，不影响通过provider访问数据的其他应用。同时也可用于消除起义，例如数据库列名为name，可以定义projection的名字为people
	 * .name。这个映射关系在QueryBuilder类，通过setProjectMap()设定，见query()的实现
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
	 * 步骤1：对数据源进行处理，本例采用内部类方式。本例数据源采用SQLite，SQLite通过对SQLiteOpenHelper的继承来实现，
	 * 可以参考Android学习笔记（四一）：SQLite的使用 ， 必须重写onCreate()和onUpgrade()以及构造函数
	 */
	private class DatabaseBookHelper extends SQLiteOpenHelper {
		/* 步骤1.1 重写构造函数，给出数据库信息 */
		DatabaseBookHelper(Context context) {
			super(context, BookProviderMetaData.DATABASE_NAME, null,
					BookProviderMetaData.DATABASE_VERSION);
		}

		/* 步骤1.2：@Override onCreate()，通CREATE TABLE创建数据库的表格 */
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
		 * 步骤1.3：@Override onUpgrade，当发现数据库的版本低于当前版本时，对数据库进行升级，本例将删除原来的
		 * 全部数据，重新建立新的表格
		 */
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "
					+ BookProviderMetaData.BOOKS_TABLE_NAME);
			onCreate(db);
		}

	} // End of 步骤1

	private DatabaseBookHelper mDataSource = null; // 步骤2：设置并获取数据源对象

	/*
	 * 步骤4：提供判读输入uri样式的机制。 通过UriMatch对input的Uri进行匹配，判断uri是否合法，是collection还是item
	 * 在uriMatch对象上通过addURI登记uri的样式和一个唯一的number，当匹配时，uriMatch将返回该number。
	 */
	private static final UriMatcher sUriMatch;
	// 定义匹配时返回的number，本例只有1个table，故有collection和item两种。
	private static final int BOOK_COLLECTION_URI_INDICATOR = 1;
	private static final int BOOK_ITEM_URI_INDICATION = 2;
	static {
		// UriMatther的创建函数可以含有如果既没有path_segment也没有authorities时返回的number。
		// 此外如果不匹配，UriMatch也会返回NO_MATCH，本例也可以写成sUriMatch = new UriMatcher();
		sUriMatch = new UriMatcher(UriMatcher.NO_MATCH);
		// 登记uri的样式及其对应的number值
		sUriMatch.addURI(BookProviderMetaData.AUTHORITY, "books",
				BOOK_COLLECTION_URI_INDICATOR);
		sUriMatch.addURI(BookProviderMetaData.AUTHORITY, "books/#",
				BOOK_ITEM_URI_INDICATION);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
	    int count = 0; //记录删除item数目 
	    SQLiteDatabase db = mDataSource.getWritableDatabase(); 
	    //删除：根据uri分为collection和item两种情况 
	    switch(sUriMatch.match(uri)){ 
	    case BOOK_COLLECTION_URI_INDICATOR: 
	        count = db.delete(BookTableMetaData.TABLE_NAME, where, whereArgs); 
	        break; 
	    case BOOK_ITEM_URI_INDICATION: 
	        String id = uri.getPathSegments().get(1); //从0开始计算，本例中0为content://xxxx/books/2中的books 
	        count = db.delete(BookTableMetaData.TABLE_NAME, BookTableMetaData._ID + "=" + id
	                +(TextUtils.isEmpty(where) ? "" : " AND ("  + where + ")"), whereArgs);
	        break; 
	    default: 
	        throw new IllegalArgumentException("Unknown Uri " + uri); 
	    } 
	    getContext().getContentResolver().notifyChange(uri, null);  //通知该uri的数据发生变化 
	    return count; 
	}

	/* 通过getType()，Provider对外提供uri获取的数据类型（MIME Type），分为collection和item两种情况。 */
	@Override
	public String getType(Uri uri) {
		switch (sUriMatch.match(uri)) {
		case BOOK_COLLECTION_URI_INDICATOR:
			return BookTableMetaData.CONTENT_TYPE;
		case BOOK_ITEM_URI_INDICATION:
			return BookTableMetaData.CONTENT_ITEM_TYPE;
		default: // 无效uri
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initalValue) { 
		   //1、检查uri是否合法，由于新增item，故uri只能是collection方式 
		    if(sUriMatch.match(uri)  != BOOK_COLLECTION_URI_INDICATOR){ 
		        throw new IllegalArgumentException("Unknown Uri " + uri); 
		    } 

		    //2、从ContentValues中获取数据，检查数据是否有效和完备 
		    ContentValues cv ; 
		    if(initalValue == null){   //允许插入空的一行数据 
		        cv = new ContentValues(); 
		    }else{ 
		        cv = new ContentValues(initalValue); 
		    } 
		   //检查数据是否缺失，并填入缺省值
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

		   //3、在数据库表格中增加row 
		    SQLiteDatabase db = mDataSource.getWritableDatabase(); 
		    long rowId = db.insert(BookTableMetaData.TABLE_NAME, BookTableMetaData.BOOK_NAME, cv);
		    if(rowId > 0){ 
		        Uri insertUri = ContentUris.withAppendedId(BookTableMetaData.CONTENT_URI, rowId);
		        //4、通知该uri的数据发生变化 
		        getContext().getContentResolver().notifyChange(insertUri, null); 
		        return insertUri; 
		    } 
		    throw new SQLException("Failed to insert row into " + uri); 
		}

	/* 步骤2：关联数据源，本例创建数据库的对象 */
	public boolean onCreate() {
		mDataSource = new DatabaseBookHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] protection, String selection,
			String[] selectionArgs, String order) {
		// 1、通过SQLiteQueryBuilder，设置数据库查询的信息。Uri有两种情况，一种是collect，一种已经指定某个item，
		// 两者需要区别对待，item将获取_ID，并在where中增加一个匹配条件。
		SQLiteQueryBuilder dbQuery = new SQLiteQueryBuilder();
		switch (sUriMatch.match(uri)) {
		case BOOK_COLLECTION_URI_INDICATOR:
			dbQuery.setTables(BookTableMetaData.TABLE_NAME);
			dbQuery.setProjectionMap(sBooksProjectionMap); // 设定与project的映射关系
			break;
		case BOOK_ITEM_URI_INDICATION:
			dbQuery.setTables(BookTableMetaData.TABLE_NAME);
			dbQuery.setProjectionMap(sBooksProjectionMap); // 设定与project的映射关系
			// 通过getPathSegments( )获取具体的ID，path segment从0还是计算，本例中0为books，1为#
			dbQuery.appendWhere(BookTableMetaData._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}
		// 2、对缺省进行设置，如排序
		String orderBy;
		if (TextUtils.isEmpty(order)) {
			orderBy = BookTableMetaData.DEFAULE_SORT_ORDER;
		} else {
			orderBy = order;
		}
		// 3、查询数据库
		SQLiteDatabase db = mDataSource.getReadableDatabase();
		Cursor c = dbQuery.query(db, protection, selection, selectionArgs,
				null, null, orderBy);

		// 4、向系统注册通知：观察所要查询的数据，即Uri对应的数据，是否发生变化。开发者通过provider接口获取数据，
		// 可通过通知获知数据已经发生变更。
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
	    int count = 0; //记录更新item数目 
	    SQLiteDatabase db = mDataSource.getWritableDatabase(); 
	    //更新：根据uri分为collection和item两种情况 
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
	    getContext().getContentResolver().notifyChange(uri,null); //通知该uri的数据发生变化 
	    return count; 
	}

}