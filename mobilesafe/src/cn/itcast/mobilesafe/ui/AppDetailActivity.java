package cn.itcast.mobilesafe.ui;

import cn.itcast.mobilesafe.MobilesafeApplication;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.TaskInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class AppDetailActivity extends Activity {

	TextView tv_app_version;
	TextView tv_app_packname;
	TextView tv_app_premission;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		//想去修改activity的背景颜色 
		getWindow().setBackgroundDrawableResource(R.drawable.activity_background);
		setContentView(R.layout.app_detail);
		tv_app_packname = (TextView) this.findViewById(R.id.tv_app_detail_packname);
		tv_app_premission = (TextView) this.findViewById(R.id.tv_app_detail_premission);
		tv_app_version = (TextView) this.findViewById(R.id.tv_app_detail_version);
		
		//设置这些控件的数据 
		//2.从全局的盒子里把对象取出来
		MobilesafeApplication app = (MobilesafeApplication) getApplication();
		TaskInfo info = app.taskInfo;
		String packname = info.getPackname();
		tv_app_packname.setText(packname);
		
		try {
			PackageInfo  packinfo = getPackageManager().getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
			String version = packinfo.versionName;
			tv_app_version.setText(version);
			String[] permissions = packinfo.requestedPermissions;
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<permissions.length;i++){
				sb.append(permissions[i]);
				sb.append("\n");
			}
			String permissionstr = sb.toString();
			tv_app_premission.setText(permissionstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
