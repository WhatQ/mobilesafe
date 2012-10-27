package cn.itcast.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.itcast.mobilesafe.db.BlackNumberDBOpenHelper;

public class BlackNumberDao {
	BlackNumberDBOpenHelper dbOpenHelper;

	public BlackNumberDao(Context context) {
		dbOpenHelper = new BlackNumberDBOpenHelper(context);
	}

	/**
	 * add增加黑名单数据
	 */

	public void add(String number) {
		//如果号码存在 就不添加了. 如果号码不存在 加入到数据库里面
		if (find(number)) {
			return;
		} else {

			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			if (db.isOpen()) {
				db.execSQL("insert into blacknumber (number) values (?)",
						new Object[] { number });
				db.close();
			}
		}
	}

	/**
	 * find 操作
	 * 
	 */
	public boolean find(String number) {
		boolean result = false;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from blacknumber where number = ?",
					new String[] { number });
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
	 * delete的操作
	 */
	public void delete(String number){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from blacknumber where number = ? ", new Object[]{number});
			db.close();
		}
	}
	
	/**
	 * update 的操作
	 */
	public void update(String oldnumber,String newnumber){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("update blacknumber set number =? where number =?",new Object[]{newnumber,oldnumber});
			db.close();
		}
	}
	
	/**
	 * 查找全部的黑名单数据
	 */
	public List<String> findAll(){
		List<String> numbers = new ArrayList<String>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from blacknumber", null);
			while(cursor.moveToNext()){
			  String number =  cursor.getString(cursor.getColumnIndex("number"));
			  numbers.add(number);
			}
			cursor.close();
			db.close();
		}
		return numbers;
	}
}
