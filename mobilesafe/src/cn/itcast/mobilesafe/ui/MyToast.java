package cn.itcast.mobilesafe.ui;

import cn.itcast.mobilesafe.R;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {

	public static void  showMyToast(Context context, int icon , String text){
		Toast toast = new Toast(context);
		View view = View.inflate(context, R.layout.mytoast, null);
		TextView tv_toast = (TextView) view.findViewById(R.id.tv_toast);
		ImageView iv_toast = (ImageView) view.findViewById(R.id.iv_toast);
		tv_toast.setText(text);
		iv_toast.setImageResource(icon);
		
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}
}
