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
		// 需要把优化的数据库信息 给释放到手机里面

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
				// 1.获取一下当前手机用户安装的程序有哪些
				AppInfoProvider provider = new AppInfoProvider(SystemOptActivity.this);
				List<AppInfo> appinfos = provider.getAllAppInfos();
				progressBar1.setMax(appinfos.size());
				int total =0;
				// 2.查询数据库 看这些程序的包名是否在数据里面
				for (AppInfo appinfo : appinfos) {
					Message msg = new Message();
					msg.obj = "正在扫描"+appinfo.getName();
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
					// 3.删除掉程序所对应的缓存目录
					if (path != null) {
						//tv_system_opt.setText("正在清理"+appinfo.getName());
						msg = new Message();
						msg.obj = "正在清理"+appinfo.getName();
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
				//tv_system_opt.setText("清理完毕");
				Message msg = new Message();
				msg.obj = "清理完毕";
				hander.sendMessage(msg);
			}
		}.start();

	}

	/**
	 * 根据文件夹的名字 删除掉文件夹具体信息
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
		// 已经把目录里面的内容都删除了
		return file.delete();

	}
}
