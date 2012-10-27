package cn.itcast.mobilesafe.ui;

import cn.itcast.mobilesafe.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetupWizard1Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_wizard1);

		
		
	}

	
	public void next(View view){
		Intent intent = new Intent(this,SetupWizard2Activity.class);
		startActivity(intent);
		finish();
		//更改系统默认的动画效果
		//重写系统的动画效果 
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
	}
}
