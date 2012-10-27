package cn.itcast.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumDao {

	public static int getGroupCount() {
		int count = 0;
		SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/num.db", null,
				SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from classlist", null);
			if (cursor.moveToFirst()) {
				count = cursor.getCount();
				cursor.close();
			}
			db.close();
		}
		return count;
	}

	public static int getChildCount(int groupid) {
		int count = 0;
		SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/num.db", null,
				SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) {
			String sql = "select * from table" + groupid;
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				count = cursor.getCount();
				cursor.close();
			}
			db.close();
		}
		return count;
	}

	public static List<Map<String, String>> getChildrenItem(int groupid) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/num.db", null,
				SQLiteDatabase.OPEN_READWRITE);
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		if (db.isOpen()) {
			String sql = "select * from table" + groupid;
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String number = cursor.getString(cursor
						.getColumnIndex("number"));
				Map<String, String> map = new HashMap<String, String>();
				map.put(name, number);
				lists.add(map);

			}
			cursor.close();
			db.close();
		}
		return lists;
	}

	public static List<String> getGroupItem() {
		SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/num.db", null,
				SQLiteDatabase.OPEN_READWRITE);
		List<String> list = new ArrayList<String>();
		if (db.isOpen()) {
			String sql = "select * from classlist";
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				list.add(name);

			}
			cursor.close();
			db.close();
		}
		return list;
	}
}
