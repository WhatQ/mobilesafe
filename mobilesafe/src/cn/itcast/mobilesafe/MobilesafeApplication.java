package cn.itcast.mobilesafe;

import cn.itcast.mobilesafe.domain.AppInfo;
import cn.itcast.mobilesafe.domain.TaskInfo;
import android.app.Application;

public class MobilesafeApplication extends Application {

	//��ȫ�ֵĺ������涨��һ������ ��� appinfo������ 
	public TaskInfo taskInfo;

	@Override
	public void onCreate() {
		
		super.onCreate();
		//getResources().get
	}
	
}
