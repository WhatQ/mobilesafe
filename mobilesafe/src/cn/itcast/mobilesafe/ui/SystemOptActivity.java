package cn.itcast.mobilesafe.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.AppInfo;
import cn.itcast.mobilesafe.engine.AppInfoProvider;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SystemOptActivity extends Activity {
	ProgressBar progressBar1;
	TextView tv_system_opt;
	Handler hander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String text = (String) msg.obj;
			tv_system_opt.setText(text);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��Ҫ���Ż������ݿ���Ϣ ���ͷŵ��ֻ�����

		try {
			InputStream is = getResources().getAssets().open("clearpath.db");
			File file = new File(Environment.getExternalStorageDirectory(),
					"clearpath.db");
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
			fos.close();
			is.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		setContentView(R.layout.system_opt);
		progressBar1 = (ProgressBar) this.findViewById(R.id.progressBar1);
		tv_system_opt = (TextView) this.findViewById(R.id.tv_system_opt);
	}

	public void clear(View view) {
		new Thread(){
			@Override
			public void run() {
				SQLiteDatabase db;
				// 1.��ȡһ�µ�ǰ�ֻ��û���װ�ĳ�������Щ
				AppInfoProvider provider = new AppInfoProvider(SystemOptActivity.this);
				List<AppInfo> appinfos = provider.getAllAppInfos();
				progressBar1.setMax(appinfos.size());
				int total =0;
				// 2.��ѯ���ݿ� ����Щ����İ����Ƿ�����������
				for (AppInfo appinfo : appinfos) {
					Message msg = new Message();
					msg.obj = "����ɨ��"+appinfo.getName();
					hander.sendMessage(msg);
					String path = null;
					db = SQLiteDatabase.openDatabase("/sdcard/clearpath.db", null,
							SQLiteDatabase.OPEN_READONLY);
					if (db.isOpen()) {
						Cursor cursor = db.rawQuery(
								"select * from softdetail where apkname=?",
								new String[] { appinfo.getPackname() });
						if (cursor.moveToFirst()) {
							path = cursor.getString(cursor.getColumnIndex("filepath"));
							cursor.close();
						}

						db.close();
					}
					// 3.ɾ������������Ӧ�Ļ���Ŀ¼
					if (path != null) {
						//tv_system_opt.setText("��������"+appinfo.getName());
						msg = new Message();
						msg.obj = "��������"+appinfo.getName();
						hander.sendMessage(msg);
						File file = new File(Environment.getExternalStorageDirectory(),
								path);
						deleteDir(file);
					}
					total++;
					progressBar1.setProgress(total);
					try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//tv_system_opt.setText("�������");
				Message msg = new Message();
				msg.obj = "�������";
				hander.sendMessage(msg);
			}
		}.start();

	}

	/**
	 * �����ļ��е����� ɾ�����ļ��о�����Ϣ
	 */
	private boolean deleteDir(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				boolean success = deleteDir(files[i]);
				if (!success) {
					return false;
				}
			}
		}
		// �Ѿ���Ŀ¼��������ݶ�ɾ����
		return file.delete();

	}
}
