package cn.itcast.mobilesafe.db.provider;

import java.util.regex.Matcher;

import cn.itcast.mobilesafe.db.dao.LockAppDao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class LockAppInfoProvider extends ContentProvider {
	LockAppDao dao;
	private Uri baseuri;
	// 定义一个规范 ,
	// content://cn.itcast.mobilesafe.lockappprovider/allapp
	// 以上的uri 代表获取 lockapp 表中的所有的信息
	// 定义一个路径的匹配器, 匹配规则的执法官
	private static final String authority = "cn.itcast.mobilesafe.lockappprovider";
	private static final int GET_ALL_APP = 1; // 代表获取所有的数据
	private static final int INSERT = 2;
	private static final int DELETE = 3;
	static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		matcher.addURI(authority, "allapp", GET_ALL_APP);
		matcher.addURI(authority, "insertapp", INSERT);
		matcher.addURI(authority, "deleteapp", DELETE);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dao = new LockAppDao(getContext());
		baseuri = Uri.parse("content://cn.itcast.mobilesafe.lockappprovider");
		return false;
	}

	@Override
	public String getType(Uri uri) { // 返回数据的数据类型 MIME jpeg MP3
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (matcher.match(uri)) {
		case GET_ALL_APP:
			// 把数据以游标结果集返回回来
			return dao.findAllCursor();

		default:
			throw new IllegalArgumentException("uri 不匹配");
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		switch (matcher.match(uri)) {
		case INSERT:
			if (values != null) {
				String packname = (String) values.get("packname");
				dao.add(packname);
				// 注册一个内容观察者的事件
				getContext().getContentResolver().notifyChange(baseuri, null);
			} else {
				throw new IllegalArgumentException("插入的参数不正确");
			}
			break;
		default:
			throw new IllegalArgumentException("uri 不匹配");
		}

		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (matcher.match(uri)) {
		case DELETE:
			if (selectionArgs.length > 0) {
				dao.delete(selectionArgs[0]);
				// 注册一个内容观察者的事件
				getContext().getContentResolver().notifyChange(baseuri, null);
			} else {
				throw new IllegalArgumentException("删除条件不匹配");
			}
			break;
		default:
			throw new IllegalArgumentException("uri 不匹配");

		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
