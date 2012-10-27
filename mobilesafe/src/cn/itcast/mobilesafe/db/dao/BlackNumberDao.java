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
	 * add���Ӻ���������
	 */

	public void add(String number) {
		//���������� �Ͳ������. ������벻���� ���뵽���ݿ�����
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
	 * find ����
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
	 * delete�Ĳ���
	 */
	public void delete(String number){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from blacknumber where number = ? ", new Object[]{number});
			db.close();
		}
	}
	
	/**
	 * update �Ĳ���
	 */
	public void update(String oldnumber,String newnumber){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("update blacknumber set number =? where number =?",new Object[]{newnumber,oldnumber});
			db.close();
		}
	}
	
	/**
	 * ����ȫ���ĺ���������
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
