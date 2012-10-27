package cn.itcast.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {

	public BlackNumberDBOpenHelper(Context context) {
		super(context, "itheima.db", null, 1);
	}
    // ���ݿ��һ�α�������ʱ�� ִ��
	@Override
	public void onCreate(SQLiteDatabase db) {
		//primary key autoincrement, name varchar(20)
		db.execSQL("create table blacknumber (_id integer primary key autoincrement, number varchar(20))");
		db.execSQL("create table applock (_id integer primary key autoincrement, packname varchar(50))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//db.execSQL("create table applock (_id integer primary key autoincrement, packname varchar(50))");
	}

}
