package cn.itcast.mobilesafe.ui;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.receiver.MyAdmin;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import cn.itcast.mobilesafe.util.Logger;
import android.view.View;
import android.widget.TextView;

public class SetupWizard4Activity extends Activity {
	private static final String TAG = "SetupWizard4Activity";
	SharedPreferences sp;
	TextView tv_protecting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_wizard4);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		tv_protecting = (TextView) this.findViewById(R.id.tv_setup4_protect);
		boolean isprotecting = sp.getBoolean("isprotecting", false);
		if (isprotecting) {
			tv_protecting.setText("���ڱ�����");
		} else {
			tv_protecting.setText("����δ����");
		}
	}

	public void next(View view) {
		// Intent intent = new Intent(this,SetupWizard4Activity.class);
		// startActivity(intent);
		// finish();
		// //����ϵͳĬ�ϵĶ���Ч��
		// //��дϵͳ�Ķ���Ч��
		// overridePendingTransition(android.R.anim.fade_in,
		// android.R.anim.fade_out);

		Logger.i(TAG, "������浽�ֻ�����");
		Editor editor = sp.edit();
		editor.putBoolean("issetup", true);
		editor.commit();

		Intent intent = new Intent(this, LostProtectedActivity.class);
		startActivity(intent);
		finish();
	}

	public void pre(View view) {
		Intent intent = new Intent(this, SetupWizard3Activity.class);
		startActivity(intent);
		finish();
		// ����ϵͳĬ�ϵĶ���Ч��
		// ��дϵͳ�Ķ���Ч��
		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

	}

	/**
	 * ��������
	 */
	public void startProtecting(View view) {
		Editor editor = sp.edit();
		editor.putBoolean("isprotecting", true);
		editor.commit();
		tv_protecting.setText("���ڱ�����");
		ComponentName mAdminName = new ComponentName(this, MyAdmin.class);
		DevicePolicyManager manager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		// �ж� ���ǵ�myadimn�Ƿ��Ѿ���Ȩ
		if (!manager.isAdminActive(mAdminName)) {
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
			startActivity(intent);
		}

	}

	/**
	 * ֹͣ����
	 */
	public void stopProtecting(View view) {
		Editor editor = sp.edit();
		editor.putBoolean("isprotecting", false);
		editor.commit();
		tv_protecting.setText("����δ����");
	}
}
