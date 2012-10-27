package cn.itcast.mobilesafe.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.SmsInfo;
import cn.itcast.mobilesafe.engine.FileDownLoadService;
import cn.itcast.mobilesafe.service.BackUpSmsService;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import cn.itcast.mobilesafe.util.Logger;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AtoolsActivity extends Activity {

	protected static final int DOWNLOAD_ERROR = 20;
	protected static final String TAG = "AtoolsActivity";
	ProgressDialog pd;
	SharedPreferences sp;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DOWNLOAD_ERROR:
				pd.dismiss();
				Toast.makeText(getApplicationContext(), "�������ݿ�ʧ��", 1).show();
				finish();
				break;

			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atools);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setTitle("�����������ݿ�");
	}

	public void queryAddress(View view) {
		// ���� �ж� sd�����ǲ����Ѿ��������ݿ�
		if (isDBExist()) {
			// �����ֻ������ѯ����
			Logger.i(TAG, "��������ز�ѯ����");
			loadQueryUI();
		} else {
			// �������ݿ�
			pd.show();
			new Thread() {
				@Override
				public void run() {
					try {
						String path = getResources().getString(R.string.dburl);
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"address.db");
						FileOutputStream fos = new FileOutputStream(file);
						FileDownLoadService.downFile(path, fos, pd);
						pd.dismiss();
						Logger.i(TAG, "��������ز�ѯ����");
					} catch (Exception e) {
						e.printStackTrace();
						Message msg = new Message();
						msg.what = DOWNLOAD_ERROR;
						handler.sendMessage(msg);
					}
				}
			}.start();
		}

	}

	private void loadQueryUI() {
		Intent intent = new Intent(this, QureyAddressActivity.class);
		startActivity(intent);
	}

	/**
	 * �ж�sd�����Ƿ���� address.db�����ݿ�
	 * 
	 * @return
	 */
	private boolean isDBExist() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"address.db");
		boolean result = file.exists();
		return result;
	}

	public void autoIpdail(View view) {
		// 1.����һ���µ�activity
		// 2.�����Ի���
		// 3.�������õ�ǰactivity�Ĳ���
		setContentView(R.layout.auto_ip_dail);
	}

	public void setAutoDail(View view) {
		EditText et_auto_ip_dail = (EditText) this
				.findViewById(R.id.et_auto_ip_dail);
		String ipnumber = et_auto_ip_dail.getText().toString().trim();
		Editor editor = sp.edit();
		editor.putString("ipnumber", ipnumber);
		editor.commit();
		// Toast.makeText(this, "ip�����Ѿ�����Ϊ"+ ipnumber, 1).show();
		// ͨ�� notification ֪ͨ�û�ip�����������
		// 1.����notification �Ĺ�����
		NotificationManager manager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		// 2 .����notification��ʵ��

		Notification notification = new Notification(R.drawable.notification,
				"ip��������Ϊ:" + ipnumber, System.currentTimeMillis());
		// ���ڵ���ͼ , Intent
		Intent intent = new Intent(this, AtoolsActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		// notification.flags = Notification.FLAG_AUTO_CANCEL; �����Ŀ�Զ����
		// notification.flags = Notification.FLAG_NO_CLEAR; ������ʾ�����Ա����
		// notification.sound = Uri.parse("/sdcard/ddz.mp3");
		// notification.vibrate = new long[]{1,2,1,2,2};

		// 3. ����notification�ľ�������
		notification.setLatestEventInfo(this, "ip�������óɹ�", "����Ϊ" + ipnumber,
				pendingIntent);
		// 4.ͨ��notification�Ĺ����� ��notification��ʾ��������
		manager.notify(0, notification);

		// �ѽ������¶��� �߼����ý���
		setContentView(R.layout.atools);
	}

	/**
	 * ���ű��ݵĵ���¼�
	 */
	public void smsBackUp(View view) {
		// Ϊ�˷�ֹ��ǰ�Ľ��̱�ϵͳ����
		// ���ǰѱ��ݵĶ�Ϣ�Ĳ�������service����ȥִ��
		Intent service = new Intent(this, BackUpSmsService.class);
		startService(service);
	}

	public void restoreSms(View view) {
		pd.setTitle("��ʾ");
		pd.setMessage("���ڻָ���������");
		pd.setCancelable(false);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.show();

		new Thread() {
			SmsInfo info;

			@Override
			public void run() {
				try {
					
					InputStream is = openFileInput("smsback.xml");
					XmlPullParser parser = Xml.newPullParser();
					parser.setInput(is, "utf-8");
					int type = parser.getEventType();
					while (type != XmlPullParser.END_DOCUMENT) {
						switch (type) {
						case XmlPullParser.START_TAG:
							if ("sms".equals(parser.getName())) {
								info = new SmsInfo();
							} else if ("time".equals(parser.getName())) {
								info.setTime(Long.parseLong(parser.nextText()));
							} else if ("address".equals(parser.getName())) {
								info.setAddress(parser.nextText());
							} else if ("type".equals(parser.getName())) {
								String sendtype = parser.nextText();
								if ("����".equals(sendtype)) {
									info.setType("1");
								} else {
									info.setType("2");
								}
							} else if ("content".equals(parser.getName())) {
								info.setContent(parser.nextText());
							}

							break;
						case XmlPullParser.END_TAG:
							if ("sms".equals(parser.getName())) {
								// �����ɹ���һ����Ϣ
								// ���뵽ϵͳ�Ķ���Ӧ������
								ContentValues values = new ContentValues();
								values.put("address", info.getAddress());
								values.put("date", info.getTime());
								values.put("type", info.getType());
								values.put("body", info.getContent());
								getContentResolver().insert(
										Uri.parse("content://sms/"), values);
								info = null;
							} else if ("smss".equals(parser.getName())) {
								pd.dismiss();
							}

							break;

						}

						type = parser.next();
					}
				} catch (Exception e) {
					e.printStackTrace();
					pd.dismiss();
				}
			}
		}.start();

	}
	
	/**
	 * ��������Ӧ�ĵ���¼�
	 */
	public void applock(View view){
		Intent intent = new Intent(this,LockAppActivity.class);
		startActivity(intent);
		
		
	}
	/**
	 * ���ú����ѯ
	 */
	public void queryCommonNumber(View  view){
		Intent intent = new Intent(this,CommonNumberActivity.class);
		startActivity(intent);
	}
}
