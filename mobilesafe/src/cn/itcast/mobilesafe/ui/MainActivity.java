package cn.itcast.mobilesafe.ui;

import cn.itcast.mobilesafe.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import cn.itcast.mobilesafe.util.Logger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener {
	public static final String TAG = "MainActivity";
	private GridView gv_main;
	private SharedPreferences sp;
	private String[] names = { "手机防盗", "通讯卫士", "程序管理", "进程管理", "流量管理", "程序杀毒",
			"系统清理", "高级工具", "程序设置" };
	private int[] icons = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };
	static ImageView iv_icon;
	static TextView tv_name;
	// 布局填充的服务
	LayoutInflater inflater;
	private String newname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		newname = sp.getString("newname", null);
		gv_main = (GridView) this.findViewById(R.id.gv_main);
		// 完成布局填充服务的初始化
		// inflater = LayoutInflater.from(this);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		gv_main.setAdapter(new MainScreenAdapter());
		// 给gridview的条目设置点击事件
		gv_main.setOnItemClickListener(this);

	}

	private class MainScreenAdapter extends BaseAdapter {

		// 返回gridview里面有多少个条目
		public int getCount() {
			return names.length;
		}

		// 返回当前postion对应的条目的对象
		public Object getItem(int position) {
			return position;
		}

		// 获取当前某个位置条目的id
		public long getItemId(int position) {
			return position;
		}

		// 返回每一个item的view对象
		public View getView(int position, View convertView, ViewGroup parent) {
			// 问题: getview的方法调用了多少次?
			Logger.i(TAG, "GETVIEW ----" + position);
			View view = inflater.inflate(R.layout.main_item, null);
			iv_icon = (ImageView) view.findViewById(R.id.iv_main_icon);
			tv_name = (TextView) view.findViewById(R.id.tv_main_name);
			iv_icon.setImageResource(icons[position]);
			if (position==0  && newname != null ) {
				tv_name.setText(newname);
			} else {
				tv_name.setText(names[position]);
			}

			return view;
		}
	}

	/**
	 * item点击对应的点击事件 parent ->代表的是item的父容器 , gridview view -> 每一个条目 所对应的view对象
	 * position 当前条目的位置 id 行号
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0: // 手机防盗
			Intent lostProtectedIntent = new Intent(this,
					LostProtectedActivity.class);
			startActivity(lostProtectedIntent);
			break;
		case 1: // 通讯卫士
			Intent callsmsSafeIntent = new Intent(this,
					CallSmsSafeActivity.class);
			startActivity(callsmsSafeIntent);
			break;	
		case 2: // 程序管理
			Intent appManagerIntent = new Intent(this,
					AppManagerActivity.class);
			startActivity(appManagerIntent);
			break;
		case 3: // 任务管理
			Intent taskManagerIntent = new Intent(this,
					TaskManagerActivity.class);
			startActivity(taskManagerIntent);
			break;	
		case 4: // 流量管理
			Intent trafficManagerIntent = new Intent(this,
					TrafficManagerActivity.class);
			startActivity(trafficManagerIntent);
			break;	
		case 5: // 手机杀毒
			Intent killVirusIntent = new Intent(this,
					KillVirusActivity.class);
			startActivity(killVirusIntent);
			break;	
		case 6: // 系统清理
			Intent systemoptIntent = new Intent(this,
					SystemOptActivity.class);
			startActivity(systemoptIntent);
			break;	
		case 7: // 高级工具
			Intent atoolsIntent = new Intent(this,
					AtoolsActivity.class);
			startActivity(atoolsIntent);
			break;	
		case 8: // 程序设置
			Intent settingIntent = new Intent(this,
					SettingActivity.class);
			startActivity(settingIntent);
			break;
		}
	}
}
