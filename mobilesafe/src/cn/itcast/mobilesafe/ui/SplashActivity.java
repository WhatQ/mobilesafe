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
				//��װ���غõ�apk
				File file = (File) msg.obj;
				// ����ϵͳ��apk�İ�װ�� ��ɵ�apk�İ�װ  
				install(file);
			break;
			case DOWNLOAD_ERROR:
				Toast.makeText(getApplicationContext(), "����ʧ��", 1).show();
				pd.dismiss();
				loadMainUI();
				break;
			case NEED_UPDATE:
				//�����Ի��� 
				AlertDialog.Builder builder = new Builder(SplashActivity.this);
				builder.setTitle("��������");
				builder.setMessage(info.getDescription());

				builder.setNegativeButton("��", new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						
						loadMainUI();
					}
				});
				builder.setPositiveButton("��", new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						//�ӷ������������µ�apk ��װ
						Logger.i(TAG,"�������µ�apk���Ұ�װ");
						pd.setMessage("�������ظ���");
						pd.show();
						new Thread(new DownloadApkTask()).start();
					}
				});
				builder.create().show();
				
				break;
			case UPDATE_ERROR:
				Toast.makeText(SplashActivity.this, "��ȡ������������Ϣʧ��", 1).show();
				//����������
				loadMainUI();
				break;
			}
		}


	};
	
	/**
	 * ʹ�����غõ�apk���ļ�,��װ
	 * @param file
	 */
	private void install(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
//		intent.setData(Uri.fromFile(file));
//		intent.setType("application/vnd.android.package-archive"); //mime���������� plain/text image/jpeg 
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��һ����� ����д�� setcontview֮ǰ 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//����splash����Ĳ��� 
		setContentView(R.layout.splash);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		pd = new ProgressDialog(this);
		
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		tv_version = (TextView) this.findViewById(R.id.tv_version);
		ll_splash = (LinearLayout) this.findViewById(R.id.ll_splash);
		version = getVersion();
		
		tv_version.setText(version);
		// ����һ������Ķ���Ч�� 
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
	 * �ж��Ƿ���Ҫ���� apk
	 * @return
	 */
	private void checkUpdate() {
		//���ӷ����� ��ȡxml�ļ�  ����xml�ļ� 
		
		
		new Thread(new CheckVersionTask()).start();
		
		
	}
	
	
	private class CheckVersionTask implements Runnable{



		public void run() {
			try {
				Thread.sleep(2000);
				//1.��ȡҪ���ʵķ������ĵ�ַ
				String path = getResources().getString(R.string.serverurl);
				//2.��װ һ��url���� 
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(2000);
				InputStream is = conn.getInputStream();
				info = UpdateInfoService.getUpdateInfo(is);
				if( version.equals(info.getVersion())){
					//�汾����ͬ 
					Logger.i(TAG,"�汾����ͬ,����������");
					loadMainUI();
				} else{
					//�汾�Ų�ͬ ,����һ����Ϣ ֪ͨui�߳�(main�߳�) �������Ի��� ��ʾ�û�����
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
	 * ��ȡ��ǰ����İ汾��
	 * @return ��ǰ����İ汾��
	 */
	private String getVersion(){
		String versionname;
		//1 .��ȡ�����Ĺ�����
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
	 * ���س���������
	 */
	private void loadMainUI() {
		//ֱ�ӽ������������

		Intent intent = new Intent(SplashActivity.this,MainActivity.class);
		startActivity(intent);
		// �ѵ�ǰactivity������ջ������� 
		finish();
	}
	
	
	/**
	 * ����apk������ 
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
				//���ִ�гɹ� ,֪ͨ������ ����ui ���Ұ�װapk
				Message msg = new Message();
				// �����غõ��ļ���������ü��뵽message���� 
				msg.obj = file;
				msg.what = DOWNLOAD_SUCCESS;
				handler.sendMessage(msg);
				
			} catch (Exception e) {
				e.printStackTrace();
				//ʧ�� ����ui ���������� 
				Message msg = new Message();
				msg.what = DOWNLOAD_ERROR;
				handler.sendMessage(msg);
			}
			
		}
		
	}
}
