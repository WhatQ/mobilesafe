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
		criteria.setCostAllowed(true); //�����Ƿ������������
		criteria.setSpeedRequired(true);//�����Ƿ���ٶ�����
		criteria.setAltitudeRequired(true);//�����Ƿ񺣰����� 
		criteria.setAccuracy(Criteria.ACCURACY_FINE); //����׼ȷ�Ķ�λ coarse �����λ��
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		String provider = locationManager.getBestProvider(criteria, true);
		//��ȡ�ֻ�λ����Ϣ�ı仯 ����Сʱ�䣬��С����
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
		//��λ�øı��ʱ�� ���õķ���
		public void onLocationChanged(Location location) {
			String latitude = "weidu "+ location.getLatitude();//��ȡ����γ����Ϣ 
			String longitude = "jingdu "+ location.getLongitude(); //��ȡ������Ϣ
			
			Editor editor = sp.edit();
			editor.putString("lastlocation", latitude+"-"+longitude);
			Logger.i(TAG,"WEIZHIWEI "+ latitude+"-"+longitude);
			editor.commit();
		}
		//״̬�����˸ı� 
		public void onStatusChanged(String provider, int status, Bundle extras) {
		
			
		}
		// ��λ���ṩ ���õ�ʱ�� 
		public void onProviderEnabled(String provider) {
			
			
		}
		// λ���ṩ�߱����õ�ʱ����� 
		public void onProviderDisabled(String provider) {
		
			
		}
	}
}
