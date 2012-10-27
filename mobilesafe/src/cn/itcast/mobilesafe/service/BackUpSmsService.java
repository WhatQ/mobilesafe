package cn.itcast.mobilesafe.service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import cn.itcast.mobilesafe.domain.SmsInfo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import cn.itcast.mobilesafe.util.Logger;
import android.util.Xml;
import android.widget.Toast;

public class BackUpSmsService extends Service {

	public static final String TAG = "BackUpSmsService";
	List<SmsInfo> smsinfos;
 	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// �����߳���ɶ��ű��ݵĲ���
		

	}

	
	/**
	 * ��֤ÿһ�ε���������� ����ִ�б���sms�Ĳ���
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		new Thread(new BackSmsTask()).start();
	}


	private class BackSmsTask implements Runnable {
		/**
		 * <time></time> <address></address> <type></type> <content></content>
		 */
		public void run() {
			Uri uri = Uri.parse("content://sms/");
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			smsinfos = new ArrayList<SmsInfo>();
			while (cursor.moveToNext()) {
				SmsInfo smsinfo = new SmsInfo();
				String address = cursor.getString(cursor
						.getColumnIndex("address"));
				Logger.i(TAG, "address = " + address);
				smsinfo.setAddress(address);
				String date = cursor.getString(cursor.getColumnIndex("date"));
				Logger.i(TAG, "date = " + date);
				smsinfo.setTime(Long.parseLong(date));
				String type = cursor.getString(cursor.getColumnIndex("type"));
				if ("1".equals(type)) {
					Logger.i(TAG, "type = " + "���� ");
					smsinfo.setType("����");
				} else {
					Logger.i(TAG, "type = " + "����");
					smsinfo.setType("����");
				}
				String body = cursor.getString(cursor.getColumnIndex("body"));
				Logger.i(TAG, "body = " + body);
				smsinfo.setContent(body);
				smsinfos.add(smsinfo);

			}
			cursor.close();
			Logger.i(TAG,"���ݱ������");
			// �Ѽ������������д��xml�ļ���  (�־û��Ĳ���)
			try {
				saveSmsToSDcard();
				//���ű������
				Looper.prepare();
				// �����߳�����׼������һ��looper
				Toast.makeText(BackUpSmsService.this, "���ű������", 1).show();
				// ���� ��¿
				Looper.loop();
				
			} catch (Exception e) {
				e.printStackTrace();
				
				//�����쳣 
				Looper.prepare();
				// �����߳�����׼������һ��looper
				Toast.makeText(BackUpSmsService.this, "�����쳣", 1).show();
				// ���� ��¿
				Looper.loop();
			}
		}
	}

	
	public void saveSmsToSDcard() throws Exception{
		//�õ�xml�ļ������л���
		XmlSerializer  serializer = Xml.newSerializer();
		//��ʼ�����л���
		//data/data Ŀ¼ 
		OutputStream os = this.openFileOutput("smsback.xml", Context.MODE_PRIVATE);
		serializer.setOutput(os, "UTF-8");
		serializer.startDocument("UTF-8", true);
		serializer.startTag(null, "smss");
		for(SmsInfo info : smsinfos){
			serializer.startTag(null, "sms");
			  serializer.startTag(null, "time");
			  serializer.text(info.getTime()+"");
			  serializer.endTag(null, "time");

			  serializer.startTag(null, "address");
			  serializer.text(info.getAddress());
			  serializer.endTag(null, "address");
			  
			  serializer.startTag(null, "type");
			  serializer.text(info.getType());
			  serializer.endTag(null, "type");
			  
			  serializer.startTag(null, "content");
			  serializer.text(info.getContent());
			  serializer.endTag(null, "content");
			  
			  
			serializer.endTag(null, "sms");
		}
		serializer.endTag(null, "smss");
		serializer.endDocument();
		// ע��: �ǳ���Ҫ
		os.flush();
		os.close();
	}

}
