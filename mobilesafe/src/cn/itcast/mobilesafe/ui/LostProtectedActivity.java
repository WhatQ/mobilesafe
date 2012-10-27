package cn.itcast.mobilesafe.ui;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.util.MD5Encoder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import cn.itcast.mobilesafe.util.Logger;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LostProtectedActivity extends Activity {
	private static final String TAG = "LostProtectedActivity";
	SharedPreferences sp;
	LayoutInflater inflater;
	//对话框全局的引用
	AlertDialog dialog;
	ImageView iv_isprotected;
	TextView tv_isprotecting ,tv_lost_protect_number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		if(isSetupPwd()){
			Logger.i(TAG,"进入正常输入密码界面");
			showNormalDialog();
		}else{
			Logger.i(TAG,"进入到第一次输入密码的界面");
			showFirstEntryDialog();
		}
		
		
		
	}
	
	/**
	 * 第一次进入的对话框
	 */
	private void showFirstEntryDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = inflater.inflate(R.layout.first_entry_dialog, null);
		builder.setTitle("请设置密码");
		//可以让对话框显现自定义的样式 
		builder.setView(view);
		builder.setCancelable(false);
		final EditText et_pwd = (EditText) view.findViewById(R.id.et_first_entry_pwd);
		final EditText et_pwd_confirm = (EditText)view.findViewById(R.id.et_first_entry_pwd_confirm);
		Button bt_ok = (Button) view.findViewById(R.id.bt_first_entry_ok);
		Button bt_cancle = (Button) view.findViewById(R.id.bt_first_entry_cancle);
		bt_ok.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String pwd = et_pwd.getText().toString();
				String pwd_confirm = et_pwd_confirm.getText().toString();
				if("".equals(pwd)||"".equals(pwd_confirm)){
					Toast.makeText(getApplicationContext(), "请输入密码", 1).show();
					return;
				}else if(pwd.equals(pwd_confirm)){
					try {
						Editor editor = sp.edit();
						editor.putString("password", MD5Encoder.encode(pwd_confirm));
						editor.putBoolean("issetuppwd", true);
						editor.commit(); // commit 类似于事务 保证了我们的两个数据 string boolean都同时被提交
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 关闭对话框 
					dialog.dismiss();
				}else{
					Toast.makeText(getApplicationContext(), "两次输入的密码不一致", 1).show();
					return;
				}
			}
		});
		bt_cancle.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// 关闭对话框 
				dialog.dismiss();
				
			}
		});
		dialog  = builder.create();
		dialog.show();
	}

	/**
	 * 正常进入的对话框
	 */
	private void showNormalDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = inflater.inflate(R.layout.normal_entry_dialog, null);
		builder.setView(view);
		builder.setTitle("请输入密码");
		builder.setCancelable(false);
		final EditText et_pwd = (EditText) view.findViewById(R.id.et_normal_entry_pwd);
		Button bt_ok = (Button) view.findViewById(R.id.bt_normal_entry_ok);
		Button bt_cancle = (Button) view.findViewById(R.id.bt_normal_entry_cancle);
		bt_cancle.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String encodedpwd = null;
				String pwd = et_pwd.getText().toString().trim();
				String realpwd = sp.getString("password", null);
				try {
					encodedpwd = MD5Encoder.encode(pwd);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if("".equals(pwd)){
					Toast.makeText(getApplicationContext(), "请输入密码", 1).show();
					return ;
				}else if(encodedpwd!=null&& realpwd!=null && encodedpwd.equals(realpwd)){

					dialog.dismiss();
					// 判断用户是否已经进行了设置向导
					// 如果用户是第一次进入手机防盗界面,没有进行设置向导
					//引导用户进入设置向导界面 
					boolean issetup = sp.getBoolean("issetup", false);
					if(issetup){
						Logger.i(TAG, "进入到手机防盗界面 ");
						setContentView(R.layout.lostprotected);
						iv_isprotected = (ImageView) findViewById(R.id.iv_isprotected);
						tv_isprotecting =  (TextView) findViewById(R.id.tv_isprotecting);
						tv_lost_protect_number =  (TextView)findViewById(R.id.tv_lost_protect_number);
						// 完成 界面的一些初始化的操作
						initView();
					}else{
						Logger.i(TAG,"进入手机防盗的设置向导");
						loadSetupWizard();
					}
					
				}else{
					Toast.makeText(getApplicationContext(), "密码输入错误", 1).show();
					return ;
				}
			}
		});
		
		dialog = builder.create();
		dialog.show();
	}

	protected void initView() {
		boolean isprotecting = sp.getBoolean("isprotecting", false);
		String number = sp.getString("safenumber", "");
		tv_lost_protect_number.setText("手机的安全号码为:  "+ number);
		if(!isprotecting){

			tv_isprotecting.setText("停止保护");
			iv_isprotected.setImageResource(R.drawable.unlock);
		}else{

			tv_isprotecting.setText("正在保护");
			iv_isprotected.setImageResource(R.drawable.lock);
		}
		
	}

	/**
	 * 判断用户是否已经设置密码
	 * @return
	 */
	private boolean isSetupPwd(){
		boolean result = sp.getBoolean("issetuppwd", false);
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.safemenu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.safe_change_name:
			Logger.i(TAG,"按钮被点击");
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("设置标题");
			View view=  inflater.inflate(R.layout.change_name_dialog, null);
			builder.setView(view);
			Button bt_cancle = (Button) view.findViewById(R.id.bt_change_cancle);
			Button bt_ok = (Button) view.findViewById(R.id.bt_change_ok);
			final EditText et_change_name = (EditText) view.findViewById(R.id.et_change_name);
			
			bt_cancle.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
				   dialog.dismiss();
					
				}
			});
			bt_ok.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					dialog.dismiss();
					String newname = et_change_name.getText().toString();
					Editor editor = sp.edit();
					editor.putString("newname", newname);
					editor.commit();
				}
			});
			dialog = builder.create();
			dialog.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 进入设置向导
	 */
	private void loadSetupWizard() {
		Intent intent = new Intent(LostProtectedActivity.this,SetupWizard1Activity.class);
		startActivity(intent);
		finish();
	}
	/**
	 * 重新进入设置向导的点击事件
	 * @param view
	 */
	public void  entrySetupWizard(View view){
		loadSetupWizard();
	}
	/**
	 * 更改保护的状态
	 */
	public void changeProtect(View view){
		TextView tv = (TextView)view;
		boolean isprotecting = sp.getBoolean("isprotecting", false);
		if(isprotecting){
			Editor editor = sp.edit();
			editor.putBoolean("isprotecting", false);
			editor.commit();
			tv.setText("停止保护");
			iv_isprotected.setImageResource(R.drawable.unlock);
		}else{
			Editor editor = sp.edit();
			editor.putBoolean("isprotecting", true);
			editor.commit();
			tv.setText("正在保护");
			iv_isprotected.setImageResource(R.drawable.lock);
		}
	}
}
