package cn.itcast.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressService {

	public static String getAddress(String number) {
		String address = number;
		// 判断 这个number是不是一个手机号码
		// 13x 15x 18x 14x 长度为11位
		// ^1[3458]\d{9}$
		if (number.matches("^1[3458]\\d{9}$")) {
			// 手机号码
			// 打开了sd卡上的数据库
			SQLiteDatabase db = SQLiteDatabase.openDatabase(
					"/sdcard/address.db", null, SQLiteDatabase.OPEN_READWRITE);
			String numberprefix = number.substring(0, 7);
			if (db.isOpen()) {
				Cursor curosr = db.rawQuery(
						"select city from info where mobileprefix=?",
						new String[] { numberprefix });
				if (curosr.moveToFirst()) {
					address = curosr.getString(curosr.getColumnIndex("city"));
					curosr.close();
				}

				db.close();
			}
			return address;
		} else {
			// 别的号码
			SQLiteDatabase db = SQLiteDatabase.openDatabase(
					"/sdcard/address.db", null, SQLiteDatabase.OPEN_READWRITE);
			switch (number.length()) {

			case 4:
				address = "模拟器";
				break;
			case 7:
				address = "本地号码";
				break;

			case 8:
				address = "本地号码";
				break;
			case 10:

				String numberprefix = number.substring(0, 3);
				if (db.isOpen()) {
					Cursor curosr = db.rawQuery(
							"select city from info where area=?",
							new String[] { numberprefix });
					if (curosr.moveToFirst()) {
						address = curosr.getString(curosr.getColumnIndex("city"));
						curosr.close();
					}

					db.close();
				}
				break;
			case 11:

				String numberprefix2 = number.substring(0, 3);
				String numberprefix3 = number.substring(0, 4);
				if (db.isOpen()) {
					Cursor curosr = db.rawQuery(
							"select city from info where area=?",
							new String[] { numberprefix2 });
					if (curosr.moveToFirst()) {
						address = curosr.getString(curosr.getColumnIndex("city"));
						curosr.close();
					}
					Cursor curosr2 = db.rawQuery(
							"select city from info where area=?",
							new String[] { numberprefix3 });
					if (curosr2.moveToFirst()) {
						address = curosr2.getString(curosr2.getColumnIndex("city"));
						curosr2.close();
					}
					db.close();
				}
				break;
			case 12:

				String numberprefix4 = number.substring(0, 4);
				if (db.isOpen()) {
					Cursor curosr = db.rawQuery(
							"select city from info where area=?",
							new String[] { numberprefix4 });
					if (curosr.moveToFirst()) {
						address = curosr.getString(curosr.getColumnIndex("city"));
						curosr.close();
					}

					db.close();
				}
				break;
			}
		}

		return address;
	}
}
