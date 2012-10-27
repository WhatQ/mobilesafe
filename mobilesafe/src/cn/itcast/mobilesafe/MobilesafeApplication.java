package cn.itcast.mobilesafe;

import cn.itcast.mobilesafe.domain.AppInfo;
import cn.itcast.mobilesafe.domain.TaskInfo;
import android.app.Application;

public class MobilesafeApplication extends Application {

	//在全局的盒子里面定义一个变量 存放 appinfo的数据 
	public TaskInfo taskInfo;

	@Override
	public void onCreate() {
		
		super.onCreate();
		//getResources().get
	}
	
}
