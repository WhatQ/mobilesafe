package cn.itcast.mobilesafe.ui;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.engine.AddressService;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class QureyAddressActivity extends Activity {

	EditText et_query_address_number;
	TextView tv_query_address;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_address);
		et_query_address_number = (EditText) this.findViewById(R.id.et_query_address_number);
		tv_query_address = (TextView) this.findViewById(R.id.tv_query_address);
	}
	
	public void query(View view){
		String number = et_query_address_number.getText().toString();
		// 根据number 从数据库里面查询 
		if("".equals(number)){
	        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	        et_query_address_number.startAnimation(shake);
	        return ;
		}else{
			String address = AddressService.getAddress(number);
			tv_query_address.setText(address);
		}
		
	}
}
