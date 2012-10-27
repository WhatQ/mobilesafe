package cn.itcast.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import cn.itcast.mobilesafe.domain.AppInfo;

public class AppInfoProvider {
	private static final String TAG = "AppInfoProvider";
	Context context;
	
	/**
	 * ���췽���а������Ĵ��ݽ���
	 * @param context
	 */
	public AppInfoProvider(Context context) {
		this.context = context;
	}


	/**
	 * �ṩ���б�װ�����ֻ����Ӧ�ó�������� 
	 */
	
	public List<AppInfo> getAllAppInfos(){
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		PackageManager  pm  =context.getPackageManager();
		List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(ApplicationInfo info: applicationInfos){
			AppInfo appinfo = new AppInfo();
			String packname = info.packageName;
			appinfo.setPackname(packname);
			String name = info.loadLabel(pm).toString();
			appinfo.setName(name);
			Drawable drawable = info.loadIcon(pm);
			appinfo.setIcon(drawable);
			appInfos.add(appinfo);
		}
		
		return appInfos;
	}
}
