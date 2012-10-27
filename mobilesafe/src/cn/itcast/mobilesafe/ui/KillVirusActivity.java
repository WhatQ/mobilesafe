package cn.itcast.mobilesafe.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.AppInfo;
import cn.itcast.mobilesafe.engine.AppInfoProvider;
import cn.itcast.mobilesafe.util.MD5Encoder;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class KillVirusActivity extends Activity {
	AnimationDrawable rocketAnimation;
	ProgressBar pb_kill_virus;
	TextView tv_kill_virus;
	ScrollView sv;
	boolean flag=false;// 判断是否正在杀毒
	Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			StringBuilder sb = (StringBuilder) msg.obj;
			tv_kill_virus.setText(sb.toString());
			sv.scrollBy(0, 20);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			InputStream is = getResources().getAssets().open("antivirus.db");
			File file = new File(Environment.getExternalStorageDirectory(),
					"antivirus.db");
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
		setContentView(R.layout.kill_virus);
		sv = (ScrollView) this.findViewById(R.id.sv);
		pb_kill_virus = (ProgressBar) this.findViewById(R.id.pb_kill_virus);
		tv_kill_virus = (TextView) this.findViewById(R.id.tv_kill_virus);
		ImageView rocketImage = (ImageView) findViewById(R.id.iv_kill_virus);
		rocketImage.setBackgroundResource(R.drawable.kill_virus_pic);
		rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			// 开启程序杀毒
			if (!flag) {
				rocketAnimation.start();
				killVirus();
			}

			return true;
		}
		return super.onTouchEvent(event);
	}

	private void killVirus() {
		new Thread() {
			@Override
			public void run() {
				flag = true;
				SQLiteDatabase db;
				// 1.获取一下当前手机用户安装的程序有哪些
				AppInfoProvider provider = new AppInfoProvider(
						KillVirusActivity.this);
				List<AppInfo> appinfos = provider.getAllAppInfos();
				pb_kill_virus.setMax(appinfos.size());
				int total = 0;
				int totalvirus = 0;
				StringBuilder sb = new StringBuilder();
				// 2.查询数据库 看这些程序的包名是否在数据里面
				for (AppInfo appinfo : appinfos) {
					Message msg = new Message();
					sb.append("正在扫描" + appinfo.getName());
					sb.append("\n");
					msg.obj = sb;
					hander.sendMessage(msg);
					String name = null;
					String desc = null;
					db = SQLiteDatabase.openDatabase("/sdcard/antivirus.db",
							null, SQLiteDatabase.OPEN_READONLY);
					if (db.isOpen()) {
						Cursor cursor = db.rawQuery(
								"select * from datable where md5=?",
								new String[] { getSignature(appinfo
										.getPackname()) });
						if (cursor.moveToFirst()) {
							name = cursor.getString(cursor
									.getColumnIndex("name"));
							desc = cursor.getString(cursor
									.getColumnIndex("desc"));

						}
						cursor.close();
						db.close();
					}
					if (name != null && desc != null) {
						sb.append("发现病毒" + appinfo.getName());
						sb.append("病毒类型" + name);
						sb.append("病毒的行为" + desc);
						sb.append("\n");
						msg = new Message();
						msg.obj = sb;
						hander.sendMessage(msg);
						totalvirus++;
					}
					total++;
					pb_kill_virus.setProgress(total);
				}
				Message msg = new Message();
				msg.obj = sb.append("发现了" + totalvirus + "病毒");
				hander.sendMessage(msg);
				flag = false;
				rocketAnimation.stop();
			}
		}.start();
	}

	private String getSignature(String packname) {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(packname,
					PackageManager.GET_SIGNATURES);
			Signature[] s = info.signatures;
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < s.length; i++) {
				sb.append(s[i]);
			}
			return MD5Encoder.encode(sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
