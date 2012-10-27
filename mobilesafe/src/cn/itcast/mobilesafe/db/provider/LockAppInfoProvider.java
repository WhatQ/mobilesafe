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
	// ����һ���淶 ,
	// content://cn.itcast.mobilesafe.lockappprovider/allapp
	// ���ϵ�uri �����ȡ lockapp ���е����е���Ϣ
	// ����һ��·����ƥ����, ƥ������ִ����
	private static final String authority = "cn.itcast.mobilesafe.lockappprovider";
	private static final int GET_ALL_APP = 1; // �����ȡ���е�����
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
	public String getType(Uri uri) { // �������ݵ��������� MIME jpeg MP3
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (matcher.match(uri)) {
		case GET_ALL_APP:
			// ���������α��������ػ���
			return dao.findAllCursor();

		default:
			throw new IllegalArgumentException("uri ��ƥ��");
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		switch (matcher.match(uri)) {
		case INSERT:
			if (values != null) {
				String packname = (String) values.get("packname");
				dao.add(packname);
				// ע��һ�����ݹ۲��ߵ��¼�
				getContext().getContentResolver().notifyChange(baseuri, null);
			} else {
				throw new IllegalArgumentException("����Ĳ�������ȷ");
			}
			break;
		default:
			throw new IllegalArgumentException("uri ��ƥ��");
		}

		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (matcher.match(uri)) {
		case DELETE:
			if (selectionArgs.length > 0) {
				dao.delete(selectionArgs[0]);
				// ע��һ�����ݹ۲��ߵ��¼�
				getContext().getContentResolver().notifyChange(baseuri, null);
			} else {
				throw new IllegalArgumentException("ɾ��������ƥ��");
			}
			break;
		default:
			throw new IllegalArgumentException("uri ��ƥ��");

		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
