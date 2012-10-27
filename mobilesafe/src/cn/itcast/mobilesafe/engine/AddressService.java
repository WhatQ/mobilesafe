package cn.itcast.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressService {

	public static String getAddress(String number) {
		String address = number;
		// �ж� ���number�ǲ���һ���ֻ�����
		// 13x 15x 18x 14x ����Ϊ11λ
		// ^1[3458]\d{9}$
		if (number.matches("^1[3458]\\d{9}$")) {
			// �ֻ�����
			// ����sd���ϵ����ݿ�
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
			// ��ĺ���
			SQLiteDatabase db = SQLiteDatabase.openDatabase(
					"/sdcard/address.db", null, SQLiteDatabase.OPEN_READWRITE);
			switch (number.length()) {

			case 4:
				address = "ģ����";
				break;
			case 7:
				address = "���غ���";
				break;

			case 8:
				address = "���غ���";
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
