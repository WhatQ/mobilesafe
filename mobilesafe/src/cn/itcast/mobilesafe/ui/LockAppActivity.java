package cn.itcast.mobilesafe.ui;

import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.db.dao.LockAppDao;
import cn.itcast.mobilesafe.domain.AppInfo;
import cn.itcast.mobilesafe.engine.AppInfoProvider;
import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LockAppActivity extends Activity {
	protected static final int LOAD_APP_FINSIH = 50;
	ListView lv_appmanager;
	ProgressBar pb_app_manager;
	RelativeLayout rl_app_list_main;
	LayoutInflater inflater;
	List<AppInfo> appInfos;
	static ImageView iv_icon;
	static ImageView iv_is_lock;
	static TextView tv_name;
	LockAppDao dao;
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_APP_FINSIH:
				pb_app_manager.setVisibility(View.GONE);
				rl_app_list_main.setBackgroundColor(Color.WHITE);
				lv_appmanager.setAdapter(new LockAppAdapter());
				break;

			}

		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_manager);
		dao = new LockAppDao(this);
		lv_appmanager = (ListView) this.findViewById(R.id.lv_appmanage);
		pb_app_manager = (ProgressBar) this.findViewById(R.id.pb_app_manager);
		rl_app_list_main = (RelativeLayout) this
				.findViewById(R.id.rl_app_list_main);
		
		initUI();
		
		lv_appmanager.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageView iv =  (ImageView) view.findViewById(R.id.iv_app_islock);
				boolean result = (Boolean) view.getTag();
				if(result){
					iv.setImageResource(R.drawable.unlock);
					// dao.delete(appInfos.get(position).getPackname());
					//通过内容提供者进行增删改查 
					Uri uri = Uri.parse("content://cn.itcast.mobilesafe.lockappprovider/deleteapp");
					getContentResolver().delete(uri, null, new String[]{appInfos.get(position).getPackname()});
					view.setTag(false);
				}else{
					iv.setImageResource(R.drawable.lock);
					//通过内容提供者进行增删改查
					Uri uri = Uri.parse("content://cn.itcast.mobilesafe.lockappprovider/insertapp");
					ContentValues values = new ContentValues();
					values.put("packname", appInfos.get(position).getPackname());
					getContentResolver().insert(uri, values);
					view.setTag(true);
				}
				
			}
		});
		
		
	}
	private void initUI() {
		pb_app_manager.setVisibility(View.VISIBLE);
		rl_app_list_main.setBackgroundColor(Color.BLACK);
		inflater = LayoutInflater.from(this);
		new Thread() {

			@Override
			public void run() {
				fillData();
				// 执行完毕后 发送一个消息 通知界面更新
				Message msg = Message.obtain();
				msg.what = LOAD_APP_FINSIH;
				handler.sendMessage(msg);
			}
		}.start();
	}

	/*
	 * 获取所有的程序数据的方法
	 */
	private void fillData() {
		AppInfoProvider provider = new AppInfoProvider(this);
		appInfos = provider.getAllAppInfos();

	}
	
	
	private class LockAppAdapter extends BaseAdapter{
		
		
		public int getCount() {
			// TODO Auto-generated method stub
			return appInfos.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return appInfos.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			AppInfo info = appInfos.get(position);
			View view = null;
			if(convertView==null){
				view = 	View.inflate(LockAppActivity.this, R.layout.lock_app_item, null);
			}else{
				view  = convertView;
			}
		    iv_icon = (ImageView) view.findViewById(R.id.iv_appicon);
		    tv_name = (TextView) view.findViewById(R.id.tv_appname);
		    iv_is_lock = (ImageView) view.findViewById(R.id.iv_app_islock);
		    iv_icon.setImageDrawable(info.getIcon());
			tv_name.setText(info.getName());
			// 判断当前程序的锁定状态 //根据状态设置程序的图片 
			// iv_is_lock.setImageResource(resId);
			if(dao.find(info.getPackname())){
				iv_is_lock.setImageResource(R.drawable.lock);
				view.setTag(true);
			}else{
				iv_is_lock.setImageResource(R.drawable.unlock);
				view.setTag(false);
			}
			
			return view;
		}
		
	}
}
