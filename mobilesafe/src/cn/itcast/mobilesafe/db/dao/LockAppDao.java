package cn.itcast.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.itcast.mobilesafe.db.BlackNumberDBOpenHelper;

public class LockAppDao {
	BlackNumberDBOpenHelper dbOpenHelper;

	public LockAppDao(Context context) {
		dbOpenHelper = new BlackNumberDBOpenHelper(context);
	}

	/**
	 * add ��������
	 */

	public void add(String packname) {
		// ���������� �Ͳ������. ������벻���� ���뵽���ݿ�����
		if (find(packname)) {
			return;
		} else {

			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			if (db.isOpen()) {
				db.execSQL("insert into applock(packname) values (?)",
						new Object[] { packname });
				db.close();
			}
		}
	}

	/**
	 * find ����
	 * 
	 */
	public boolean find(String packname) {
		boolean result = false;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from applock where packname= ?",
					new String[] { packname });
			if (cursor.moveToFirst()) {
				result = true;
			}
			cursor.close();
			db.close();
			return result;
		} else {
			return result;
		}
	}

	/**
	 * delete�Ĳ���
	 */
	public void delete(String packname) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from applock where packname=? ",
					new Object[] { packname });
			db.close();
		}
	}

	/**
	 * ����ȫ���������ĳ���
	 */
	public List<String> findAll() {
		List<String> packnames = new ArrayList<String>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from applock", null);
			while (cursor.moveToNext()) {
				String packname = cursor.getString(cursor
						.getColumnIndex("packname"));
				packnames.add(packname);
			}
			cursor.close();
			db.close();
		}
		return packnames;
	}

	/**
	 * ���������������򼯺ϵİ����Ľ����
	 */
	public Cursor findAllCursor() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from applock", null);
			return cursor;
		}
		return null;
	}
}
