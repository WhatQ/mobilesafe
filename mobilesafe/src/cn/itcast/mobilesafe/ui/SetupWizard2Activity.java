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
			tv_bind.setText("û�а�");
		}else{
			bt_bind.setClickable(false);
			bt_bind.setEnabled(false);
			
			
			bt_unbind.setClickable(true);
			bt_unbind.setEnabled(true);
			tv_bind.setText("�Ѿ���");
		}
		
	}

	
	public void next(View view){
		Intent intent = new Intent(this,SetupWizard3Activity.class);
		startActivity(intent);
		finish();
		//����ϵͳĬ�ϵĶ���Ч��
		//��дϵͳ�Ķ���Ч�� 
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	public void pre(View view){
		Intent intent = new Intent(this,SetupWizard1Activity.class);
		startActivity(intent);
		finish();
		//����ϵͳĬ�ϵĶ���Ч��
		//��дϵͳ�Ķ���Ч�� 
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
	}
	/**
	 * �� sim�� 
	 */
	public void bind(View view){
		//��ȡ�ֻ���sim����Ϣ �浽sp���� 
		TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// ���صľ������sim�����ֻ���  
		// manager.getLine1Number();
		// ��ȡsim�������к� ��Ҫһ��Ȩ�� READ_PHONE_STATES
		String serial = manager.getSimSerialNumber();
		Editor editor = sp.edit();
		editor.putString("simserial", serial);
		editor.commit();
		//�Ѱ󶨵İ�ť��Ϊ���ɵ�� 
		bt_bind.setClickable(false);
		bt_bind.setEnabled(false);
		
		
		bt_unbind.setClickable(true);
		bt_unbind.setEnabled(true);
		
		//������ĳһ�ȫ������԰�����ֻ��� 
		//work around 
		//��Ʒ����  
	}
	/**
	 * ����� sim�� 
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
