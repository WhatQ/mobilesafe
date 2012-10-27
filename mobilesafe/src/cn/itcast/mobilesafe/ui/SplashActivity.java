package cn.itcast.mobilesafe.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.UpdateInfo;
import cn.itcast.mobilesafe.engine.UpdateInfoService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import cn.itcast.mobilesafe.util.Logger;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	private static final String TAG = "SplashActivity";
	private TextView tv_version;
	private LinearLayout ll_splash;
	private String version;
	private static final int NEED_UPDATE = 10;
	private static final int UPDATE_ERROR = 11;
	private static final int DOWNLOAD_SUCCESS = 12;
	private static final int DOWNLOAD_ERROR=13;
	private UpdateInfo info;
	ProgressDialog pd ;
	SharedPreferences sp;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DOWNLOAD_SUCCESS:
				pd.dismiss();
				//安装下载好的apk
				File file = (File) msg.obj;
				// 调用系统的apk的安装器 完成的apk的安装  
				install(file);
			break;
			case DOWNLOAD_ERROR:
				Toast.makeText(getApplicationContext(), "下载失败", 1).show();
				pd.dismiss();
				loadMainUI();
				break;
			case NEED_UPDATE:
				//弹出对话框 
				AlertDialog.Builder builder = new Builder(SplashActivity.this);
				builder.setTitle("升级提醒");
				builder.setMessage(info.getDescription());

				builder.setNegativeButton("否", new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						
						loadMainUI();
					}
				});
				builder.setPositiveButton("是", new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						//从服务器下载最新的apk 安装
						Logger.i(TAG,"下载最新的apk并且安装");
						pd.setMessage("正在下载更新");
						pd.show();
						new Thread(new DownloadApkTask()).start();
					}
				});
				builder.create().show();
				
				break;
			case UPDATE_ERROR:
				Toast.makeText(SplashActivity.this, "获取服务器更新信息失败", 1).show();
				//加载主界面
				loadMainUI();
				break;
			}
		}


	};
	
	/**
	 * 使用下载好的apk的文件,安装
	 * @param file
	 */
	private void install(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
//		intent.setData(Uri.fromFile(file));
//		intent.setType("application/vnd.android.package-archive"); //mime的数据类型 plain/text image/jpeg 
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 这一句代码 必须写到 setcontview之前 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置splash界面的布局 
		setContentView(R.layout.splash);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		pd = new ProgressDialog(this);
		
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		tv_version = (TextView) this.findViewById(R.id.tv_version);
		ll_splash = (LinearLayout) this.findViewById(R.id.ll_splash);
		version = getVersion();
		
		tv_version.setText(version);
		// 定义一个渐变的动画效果 
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000);
		
		ll_splash.startAnimation(aa);
		
		boolean shouldupdate = sp.getBoolean("shouldupdate", true);
		if(shouldupdate){
			checkUpdate();
		}else{
			handler.postDelayed(new LoadMainUITask(), 2000);
		}
	}

	
	private class LoadMainUITask implements Runnable{

		public void run() {
			loadMainUI();
			
		}
		
	}
	
	
	/**
	 * 判断是否需要更新 apk
	 * @return
	 */
	private void checkUpdate() {
		//连接服务器 获取xml文件  解析xml文件 
		
		
		new Thread(new CheckVersionTask()).start();
		
		
	}
	
	
	private class CheckVersionTask implements Runnable{



		public void run() {
			try {
				Thread.sleep(2000);
				//1.获取要访问的服务器的地址
				String path = getResources().getString(R.string.serverurl);
				//2.包装 一个url对象 
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(2000);
				InputStream is = conn.getInputStream();
				info = UpdateInfoService.getUpdateInfo(is);
				if( version.equals(info.getVersion())){
					//版本好相同 
					Logger.i(TAG,"版本号相同,进入主界面");
					loadMainUI();
				} else{
					//版本号不同 ,发送一个消息 通知ui线程(main线程) 弹出来对话框 提示用户更新
					Message msg = new Message();
					msg.what = NEED_UPDATE;
					handler.sendMessage(msg);
				}	
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = UPDATE_ERROR;
				handler.sendMessage(msg);
			}
			
		}


		
	}
	

	/**
	 * 获取当前程序的版本号
	 * @return 当前程序的版本号
	 */
	private String getVersion(){
		String versionname;
		//1 .获取到包的管理者
		PackageManager pm = getPackageManager();
		try {
			PackageInfo  info = pm.getPackageInfo(getPackageName(), 0);
			versionname = info.versionName; 
			return versionname;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "version load error";
		}
	}
	
	
	/**
	 * 加载程序主界面
	 */
	private void loadMainUI() {
		//直接进入程序主界面

		Intent intent = new Intent(SplashActivity.this,MainActivity.class);
		startActivity(intent);
		// 把当前activity从任务栈里面清空 
		finish();
	}
	
	
	/**
	 * 下载apk的任务 
	 */
	private class DownloadApkTask  implements Runnable{

		public void run() {
			try {
				String path =info.getApkurl();
				URL url = new URL(path);
			    HttpURLConnection conn =	(HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				InputStream is = conn.getInputStream();
				String filename = path.substring( path.lastIndexOf("/")+1);
				File file = new File(Environment.getExternalStorageDirectory(),filename);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len =0;
				while ((len = is.read(buffer))!=-1){
					fos.write(buffer, 0, len);
				}
				fos.flush();
				fos.close();
				is.close();
				//如果执行成功 ,通知主程序 更新ui 并且安装apk
				Message msg = new Message();
				// 把下载好的文件对象的引用加入到message里面 
				msg.obj = file;
				msg.what = DOWNLOAD_SUCCESS;
				handler.sendMessage(msg);
				
			} catch (Exception e) {
				e.printStackTrace();
				//失败 更新ui 进入主界面 
				Message msg = new Message();
				msg.what = DOWNLOAD_ERROR;
				handler.sendMessage(msg);
			}
			
		}
		
	}
}
