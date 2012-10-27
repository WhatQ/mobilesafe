package cn.itcast.mobilesafe.ui;

import cn.itcast.mobilesafe.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SetupWizard2Activity extends Activity {
	SharedPreferences sp;
	Button bt_bind, bt_unbind;
	TextView tv_bind;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setup_wizard2);
		bt_bind = (Button) this.findViewById(R.id.bt_bind);
		bt_unbind = (Button) this.findViewById(R.id.bt_unbind);
		tv_bind = (TextView) this.findViewById(R.id.tv_setup2_bind);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		String simserial = sp.getString("simserial", "");
		if("".equals(simserial)){
			bt_bind.setClickable(true);
			bt_bind.setEnabled(true);
			
			
			bt_unbind.setClickable(false);
			bt_unbind.setEnabled(false);
			tv_bind.setText("没有绑定");
		}else{
			bt_bind.setClickable(false);
			bt_bind.setEnabled(false);
			
			
			bt_unbind.setClickable(true);
			bt_unbind.setEnabled(true);
			tv_bind.setText("已经绑定");
		}
		
	}

	
	public void next(View view){
		Intent intent = new Intent(this,SetupWizard3Activity.class);
		startActivity(intent);
		finish();
		//更改系统默认的动画效果
		//重写系统的动画效果 
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	public void pre(View view){
		Intent intent = new Intent(this,SetupWizard1Activity.class);
		startActivity(intent);
		finish();
		//更改系统默认的动画效果
		//重写系统的动画效果 
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
	}
	/**
	 * 绑定 sim卡 
	 */
	public void bind(View view){
		//获取手机的sim卡信息 存到sp里面 
		TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// 返回的就是你的sim卡的手机号  
		// manager.getLine1Number();
		// 获取sim卡的序列号 需要一个权限 READ_PHONE_STATES
		String serial = manager.getSimSerialNumber();
		Editor editor = sp.edit();
		editor.putString("simserial", serial);
		editor.commit();
		//把绑定的按钮置为不可点击 
		bt_bind.setClickable(false);
		bt_bind.setEnabled(false);
		
		
		bt_unbind.setClickable(true);
		bt_unbind.setEnabled(true);
		
		//世面上某一款安全软件可以绑定你的手机号 
		//work around 
		//产品经理  
	}
	/**
	 * 解除绑定 sim卡 
	 */
	public void unbind(View view){
		Editor editor = sp.edit();
		editor.putString("simserial", "");
		editor.commit();
		bt_bind.setClickable(true);
		bt_bind.setEnabled(true);
		
		
		bt_unbind.setClickable(false);
		bt_unbind.setEnabled(false);
	}
}
