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
				Toast.makeText(getApplicationContext(), "下载数据库失败", 1).show();
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
		pd.setTitle("正在下载数据库");
	}

	public void queryAddress(View view) {
		// 首先 判断 sd卡上是不是已经有了数据库
		if (isDBExist()) {
			// 进入手机号码查询界面
			Logger.i(TAG, "进入归属地查询界面");
			loadQueryUI();
		} else {
			// 下载数据库
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
						Logger.i(TAG, "进入归属地查询界面");
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
	 * 判断sd卡上是否存在 address.db的数据库
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
		// 1.定义一个新的activity
		// 2.弹出对话框
		// 3.重新设置当前activity的布局
		setContentView(R.layout.auto_ip_dail);
	}

	public void setAutoDail(View view) {
		EditText et_auto_ip_dail = (EditText) this
				.findViewById(R.id.et_auto_ip_dail);
		String ipnumber = et_auto_ip_dail.getText().toString().trim();
		Editor editor = sp.edit();
		editor.putString("ipnumber", ipnumber);
		editor.commit();
		// Toast.makeText(this, "ip号码已经设置为"+ ipnumber, 1).show();
		// 通过 notification 通知用户ip号码设置完成
		// 1.创建notification 的管理器
		NotificationManager manager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		// 2 .创建notification的实例

		Notification notification = new Notification(R.drawable.notification,
				"ip拨号设置为:" + ipnumber, System.currentTimeMillis());
		// 延期的意图 , Intent
		Intent intent = new Intent(this, AtoolsActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		// notification.flags = Notification.FLAG_AUTO_CANCEL; 点击条目自动清除
		// notification.flags = Notification.FLAG_NO_CLEAR; 设置提示不可以被清除
		// notification.sound = Uri.parse("/sdcard/ddz.mp3");
		// notification.vibrate = new long[]{1,2,1,2,2};

		// 3. 设置notification的具体内容
		notification.setLatestEventInfo(this, "ip号码设置成功", "号码为" + ipnumber,
				pendingIntent);
		// 4.通过notification的管理器 把notification显示到界面上
		manager.notify(0, notification);

		// 把界面重新定向到 高级设置界面
		setContentView(R.layout.atools);
	}

	/**
	 * 短信备份的点击事件
	 */
	public void smsBackUp(View view) {
		// 为了防止当前的进程被系统回收
		// 我们把备份的短息的操作防到service里面去执行
		Intent service = new Intent(this, BackUpSmsService.class);
		startService(service);
	}

	public void restoreSms(View view) {
		pd.setTitle("提示");
		pd.setMessage("正在恢复短信数据");
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
								if ("接受".equals(sendtype)) {
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
								// 解析成功了一条信息
								// 插入到系统的短信应用里面
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
	 * 程序锁对应的点击事件
	 */
	public void applock(View view){
		Intent intent = new Intent(this,LockAppActivity.class);
		startActivity(intent);
		
		
	}
	/**
	 * 常用号码查询
	 */
	public void queryCommonNumber(View  view){
		Intent intent = new Intent(this,CommonNumberActivity.class);
		startActivity(intent);
	}
}
