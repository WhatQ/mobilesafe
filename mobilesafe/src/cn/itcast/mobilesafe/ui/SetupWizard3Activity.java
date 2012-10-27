package cn.itcast.mobilesafe.ui;

import cn.itcast.mobilesafe.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SetupWizard3Activity extends Activity {
	EditText et_number;
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_wizard3);
		et_number = (EditText) this.findViewById(R.id.et_setup3_number);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	/**
	 * 选择联系人
	 * 
	 * @param view
	 */
	public void selectContact(View view) {
		// 会把手机通讯录里面所有的联系人获取出现
		Intent intent = new Intent(this, SelectContactActivity.class);
		// startActivity(intent);
		// 开启一个新的activity 需要这个activity返回一个数据
		startActivityForResult(intent, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			String number = data.getStringExtra("number");
			et_number.setText(number);
		}
		
	}

	public void next(View view) {
		String safenumber = et_number.getText().toString();
		if("".equals(safenumber)){
			
			Toast.makeText(this, "安全号码不能为空", 1).show();
			return;
		}else{
			Editor editor = sp.edit();
			editor.putString("safenumber", safenumber);
			editor.commit();
		}
		
		Intent intent = new Intent(this, SetupWizard4Activity.class);
		startActivity(intent);
		finish();
		// 更改系统默认的动画效果
		// 重写系统的动画效果
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	public void pre(View view) {
		Intent intent = new Intent(this, SetupWizard2Activity.class);
		startActivity(intent);
		finish();
		// 更改系统默认的动画效果
		// 重写系统的动画效果
		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

	}
}
