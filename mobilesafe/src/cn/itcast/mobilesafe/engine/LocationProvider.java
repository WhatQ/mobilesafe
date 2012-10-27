package cn.itcast.mobilesafe.engine;

import cn.itcast.mobilesafe.util.Logger;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import cn.itcast.mobilesafe.util.Logger;

public class LocationProvider {
	private Context context;
	LocationManager locationManager;
	private static MyLoactionListener listener;
	SharedPreferences sp ;
	
	private static LocationProvider locationProvider; 
	private LocationProvider(Context context) {
		this.context = context;
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}
	public synchronized static LocationProvider getInstance(Context context){
		if(locationProvider==null){
			locationProvider = new LocationProvider(context);
			return locationProvider;
		}else{
			return locationProvider;
		}
	}


	public String getLocation(){
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true); //设置是否允许产生费用
		criteria.setSpeedRequired(true);//设置是否对速度敏感
		criteria.setAltitudeRequired(true);//设置是否海拔敏感 
		criteria.setAccuracy(Criteria.ACCURACY_FINE); //设置准确的定位 coarse 大体的位置
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		String provider = locationManager.getBestProvider(criteria, true);
		//获取手机位置信息的变化 ，最小时间，最小距离
		locationManager.requestLocationUpdates(provider, 60000, 100, getListenerInstance());
		
		String lastlocation =  sp.getString("lastlocation", "");
		return lastlocation;
	}
	
	public void unregisterLocationUpdate(){
		locationManager.removeUpdates(getListenerInstance());
	}
	

	public synchronized MyLoactionListener getListenerInstance(){
		if(listener==null){
			listener = new MyLoactionListener();

			return listener;
		}else{
			return listener;
		}
	}
	
	private class MyLoactionListener implements LocationListener{

		
		private static final String TAG = "MyLoactionListener";
		//当位置改变的时候 调用的方法
		public void onLocationChanged(Location location) {
			String latitude = "weidu "+ location.getLatitude();//获取到了纬度信息 
			String longitude = "jingdu "+ location.getLongitude(); //获取经度信息
			
			Editor editor = sp.edit();
			editor.putString("lastlocation", latitude+"-"+longitude);
			Logger.i(TAG,"WEIZHIWEI "+ latitude+"-"+longitude);
			editor.commit();
		}
		//状态发生了改变 
		public void onStatusChanged(String provider, int status, Bundle extras) {
		
			
		}
		// 当位置提供 可用的时候 
		public void onProviderEnabled(String provider) {
			
			
		}
		// 位置提供者被禁用的时候调用 
		public void onProviderDisabled(String provider) {
		
			
		}
	}
}
