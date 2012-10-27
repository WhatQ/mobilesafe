package cn.itcast.mobilesafe.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.TrafficInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TrafficManagerActivity extends Activity {
	ListView lv_traffic_manager;
	TextView tv_traffic_mobile_total;
	TextView tv_traffic_wifi_total;
	TrafficManagerAdapter adapter;
	Timer timer;
	TimerTask task;
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			fillData();
		}
		
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.traffic_manager);
		lv_traffic_manager = (ListView) this
				.findViewById(R.id.lv_traffic_manager);
		View  view = View.inflate(TrafficManagerActivity.this, R.layout.traffic_stat_item, null);
		ViewHolder.icon = (ImageView) view.findViewById(R.id.iv_traffic_item_icon);
		ViewHolder.tv_name = (TextView) view.findViewById(R.id.tv_traffic_item_name);
		ViewHolder.tv_rx= (TextView) view.findViewById(R.id.tv_traffic_item_rx);
		ViewHolder.tv_tx = (TextView) view.findViewById(R.id.tv_traffic_item_tx);
		ViewHolder.icon.setImageResource(R.drawable.ic_launcher);
		ViewHolder.tv_name.setText("程序名");
		ViewHolder.tv_rx.setText("下载的流量");
		ViewHolder.tv_tx.setText("上传的流量");
		
		
		
		lv_traffic_manager.addHeaderView(view);
		tv_traffic_mobile_total = (TextView) this
				.findViewById(R.id.tv_traffic_manager_mobile_total);
		tv_traffic_wifi_total = (TextView) this
				.findViewById(R.id.tv_traffic_manager_wifi_total);
		getMobileTotal();
		getWifiTotal();
		fillData();

	}

	private void fillData() {
		Set<TrafficInfo> trafficinfos = new HashSet<TrafficInfo>();
		// 第一步 我们获取所有的在桌面上产生图标的应用程序
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");

		List<ResolveInfo> infos = getPackageManager().queryIntentActivities(
				intent, 0);
		for (ResolveInfo info : infos) {
			try {

				String packname = info.activityInfo.packageName;
				ApplicationInfo appinfo = getPackageManager()
						.getApplicationInfo(packname, 0);
				int uid = appinfo.uid;
				// 判断应用程序是否产生了流量
				if (TrafficStats.getUidRxBytes(uid) > 0
						|| TrafficStats.getUidTxBytes(uid) > 0) {
					TrafficInfo trafficinfo = new TrafficInfo();
					String name = appinfo.loadLabel(getPackageManager())
							.toString();
					trafficinfo.setName(name);
					Drawable icon = appinfo.loadIcon(getPackageManager());
					trafficinfo.setIcon(icon);
					trafficinfo.setRxstr(getTrafficStr(TrafficStats
							.getUidRxBytes(uid)));
					trafficinfo.setTxstr(getTrafficStr(TrafficStats
							.getUidTxBytes(uid)));
					trafficinfos.add(trafficinfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//更新数据显示的操作 
		// 把数据设置给数据适配器
		if(adapter==null){
		    adapter = new 	TrafficManagerAdapter(trafficinfos);
			lv_traffic_manager.setAdapter(adapter);
		}else{
			adapter.setTrafficInfo(trafficinfos);
			adapter.notifyDataSetChanged();
		}
		

	}

	
	
	//当前界面获取焦点的时候调用 
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
				
			}
		};
		timer.schedule(task, 1000, 1000);
	}

	
	
	
	@Override
	protected void onPause() {
		timer.cancel();
		timer= null;
		task =null;
		super.onPause();
	}

	private void getWifiTotal() {
		long totalrx = TrafficStats.getTotalRxBytes()
				- TrafficStats.getMobileRxBytes();
		long totaltx = TrafficStats.getTotalTxBytes()
				- TrafficStats.getMobileTxBytes();
		long total = totalrx + totaltx;
		String result = getTrafficStr(total);
		tv_traffic_wifi_total.setText("wifi的流量 :" + result);
	}

	private void getMobileTotal() {
		// 所有3g/2g下载的流量信息
		long totalrx = TrafficStats.getMobileRxBytes();
		long totaltx = TrafficStats.getMobileTxBytes();
		long total = totalrx + totaltx;
		String result = getTrafficStr(total);
		tv_traffic_mobile_total.setText("2g/3g的流量: " + result);

	}

	private String getTrafficStr(long total) {
		DecimalFormat format = new DecimalFormat("###.00");
		if (total < 1024) {
			return total + "bytes";
		} else if (total < 1024 * 1024) {
			return format.format(total / 1024f) + "KB";
		} else if (total < 1024 * 1024 * 1024) {
			return format.format(total / 1024f / 1024f) + "MB";
		} else if (total < 1024 * 1024 * 1024 * 1024) {
			return format.format(total / 1024f / 1024f / 1024f) + "GB";
		} else {
			return "流量错误";
		}

	}

	private class TrafficManagerAdapter extends BaseAdapter {

		List<TrafficInfo> listtrafficinfos;
		
		
		public TrafficManagerAdapter(Set<TrafficInfo> trafficinfos) {
			listtrafficinfos = new ArrayList<TrafficInfo>();
			Iterator<TrafficInfo>  iterator =trafficinfos.iterator();
			
			while( iterator.hasNext()){
				listtrafficinfos.add(iterator.next());
			}
			
		}

		public void setTrafficInfo(Set<TrafficInfo> trafficinfos) {
			listtrafficinfos = new ArrayList<TrafficInfo>();
			Iterator<TrafficInfo>  iterator =trafficinfos.iterator();
			
			while( iterator.hasNext()){
				listtrafficinfos.add(iterator.next());
			}
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return listtrafficinfos.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listtrafficinfos.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			TrafficInfo trafficinfo = listtrafficinfos.get(position);
			
			
			View view = null;
			if(convertView ==null){
				view = View.inflate(TrafficManagerActivity.this, R.layout.traffic_stat_item, null);
				
			}else{
				view = convertView;
			}
			ViewHolder.icon = (ImageView) view.findViewById(R.id.iv_traffic_item_icon);
			ViewHolder.tv_name = (TextView) view.findViewById(R.id.tv_traffic_item_name);
			ViewHolder.tv_rx= (TextView) view.findViewById(R.id.tv_traffic_item_rx);
			ViewHolder.tv_tx = (TextView) view.findViewById(R.id.tv_traffic_item_tx);
			ViewHolder.icon.setImageDrawable(trafficinfo.getIcon());
			ViewHolder.tv_name.setText(trafficinfo.getName());
			ViewHolder.tv_rx.setText(trafficinfo.getRxstr());
			ViewHolder.tv_tx.setText(trafficinfo.getTxstr());
			return view;
		}

	}

	static class ViewHolder{
	   static ImageView icon;
	   static TextView tv_name;
	   static TextView tv_tx;
	   static TextView tv_rx;
   }
}
