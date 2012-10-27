package cn.itcast.mobilesafe.ui;

import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.AppInfo;
import cn.itcast.mobilesafe.engine.AppInfoProvider;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.itcast.mobilesafe.util.Logger;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppManagerActivity extends Activity implements OnClickListener {
	protected static final int LOAD_APP_FINSIH = 40;
	private static final String TAG = "AppManagerActivity";
	List<AppInfo> appInfos;
	ListView lv_appmanager;
	ProgressBar pb_app_manager;
	LayoutInflater inflater;
	RelativeLayout rl_app_list_main;
	static ImageView iv_appicon;
	static TextView tv_appnam;
	AppListAdapter adapter;
	PopupWindow popupWindow;
	Interpolator mInterpolator = new Interpolator() {

		public float getInterpolation(float input) {

			return (float) Math.pow(input, 2 * 5);

		}
	};
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_APP_FINSIH:
				pb_app_manager.setVisibility(View.GONE);
				rl_app_list_main.setBackgroundColor(Color.WHITE);
				if (adapter == null) {
					adapter = new AppListAdapter(appInfos);
					lv_appmanager.setAdapter(adapter);
				} else {
					// 通知数据适配器 数据放生了更新
					adapter.setAppinfos(appInfos);
					adapter.notifyDataSetChanged();
				}
				break;

			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_manager);
		lv_appmanager = (ListView) this.findViewById(R.id.lv_appmanage);
		pb_app_manager = (ProgressBar) this.findViewById(R.id.pb_app_manager);
		rl_app_list_main = (RelativeLayout) this
				.findViewById(R.id.rl_app_list_main);
		initUI();

		lv_appmanager.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 关闭原来存在的popupwindow
				dismissPopupWindow();
				// TextView tv = new TextView(AppManagerActivity.this);
				// tv.setText(appInfos.get(position).getPackname());
				// tv.setTextSize(20);
				// tv.setTextColor(Color.RED);
				View tv = View.inflate(AppManagerActivity.this,
						R.layout.popup_item, null);
				LinearLayout ll_popup_item = (LinearLayout) tv
						.findViewById(R.id.ll_popup_item);
				ImageView iv_start = (ImageView) tv.findViewById(R.id.iv_start);
				ImageView iv_share = (ImageView) tv.findViewById(R.id.iv_share);
				ImageView iv_uninstall = (ImageView) tv
						.findViewById(R.id.iv_uninstall);
				iv_start.setOnClickListener(AppManagerActivity.this);
				iv_share.setOnClickListener(AppManagerActivity.this);
				iv_uninstall.setOnClickListener(AppManagerActivity.this);
				iv_start.setTag(position);
				iv_share.setTag(position);
				iv_uninstall.setTag(position);

				popupWindow = new PopupWindow(tv, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				int[] arrayOfInt = new int[2];
				view.getLocationInWindow(arrayOfInt);
				int i = arrayOfInt[0] + 60;
				int j = arrayOfInt[1];

				Animation animation = AnimationUtils.loadAnimation(
						AppManagerActivity.this, R.anim.popup_enter);
				animation.setInterpolator(mInterpolator);
				// popwindow 一定要在代码中设置背景资源
				// 非常重要: popwindow一定要指定背景图片
				popupWindow.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.local_popup_bg));

				animation.setDuration(200);
				ll_popup_item.startAnimation(animation);
				popupWindow.setAnimationStyle(R.anim.popup_enter);
				popupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, i,
						j);
			}
		});
		// 给listview注册一个滚动的监听事件
		lv_appmanager.setOnScrollListener(new OnScrollListener() {

			// 当滚动状态发生改变的时候 调用的方法
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				dismissPopupWindow();
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPopupWindow();

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

	private void dismissPopupWindow() {
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	private class AppListAdapter extends BaseAdapter {

		private List<AppInfo> adapterappInfos;

		public void setAppinfos(List<AppInfo> adapterappInfos) {
			this.adapterappInfos = adapterappInfos;
		};

		public AppListAdapter(List<AppInfo> adapterappInfos) {
			this.adapterappInfos = adapterappInfos;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return adapterappInfos.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return adapterappInfos.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			AppInfo info = adapterappInfos.get(position);
			View view = inflater.inflate(R.layout.app_item, null);
			iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
			tv_appnam = (TextView) view.findViewById(R.id.tv_appname);
			iv_appicon.setImageDrawable(info.getIcon());
			tv_appnam.setText(info.getName());
			return view;
		}

	}

	/**
	 * imageview对象被点击的回调方法
	 */
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		dismissPopupWindow();
		switch (v.getId()) {
		case R.id.iv_share:
			Logger.i(TAG, "分享  " + appInfos.get(position).getPackname());
			shareApp(appInfos.get(position).getName());
			break;
		case R.id.iv_start:
			Logger.i(TAG, "开启  " + appInfos.get(position).getPackname());
			startApp(appInfos.get(position).getPackname());

			break;
		case R.id.iv_uninstall:
			String packname = appInfos.get(position).getPackname();
			Logger.i(TAG, "删除 " + packname);
			// 系统的应用程序是不能被删除
			ApplicationInfo info;
			try {
				info = getPackageManager().getApplicationInfo(packname, 0);
				if (!filterApp(info)) {
					Toast.makeText(this, "系统应用不能被卸载", 1).show();
					return;
				} else {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_DELETE);
					intent.setData(Uri.parse("package:" + packname));
					startActivityForResult(intent, 0);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		initUI();
	}

	/**
	 * 分享应用程序
	 * 
	 * @param name
	 */
	private void shareApp(String name) {
		// TODO Auto-generated method stub
		String content = "亲, 推荐你使用一款软件 ,名字是" + name;
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra("android.intent.extra.SUBJECT", "f分享");
		intent.putExtra("android.intent.extra.TEXT", content);
		startActivity(intent);
	}

	/**
	 * 开启某个指定包名的activity
	 * 
	 * @param packname
	 */
	private void startApp(String packname) {
		// 只能把用户自己安装的程序的intent获取出来 启动
		// 很多系统的app 找不到 lunchintent
		// Intent intent =
		// getPackageManager().getLaunchIntentForPackage(packname);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(intent);
		try {
			PackageInfo info = getPackageManager().getPackageInfo(packname,
					PackageManager.GET_ACTIVITIES);
			ActivityInfo[] activityinfos = info.activities;
			ActivityInfo activityinfo = activityinfos[0];
			if (activityinfo != null) {
				String classname = activityinfo.name;
				Logger.i(TAG, "classname " + classname);
				Intent intent = new Intent();
				intent.setClassName(packname, classname);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} else {
				Toast.makeText(this, "无法启动该应用", 1).show();
			}

		} catch (Exception e) {

			e.printStackTrace();
			if (e instanceof ActivityNotFoundException) {
				Toast.makeText(this, "无法启动该应用", 1).show();
			}
		}

	}

	/**
	 * 
	 * @param info
	 * @return 是否是三方的app
	 */
	private boolean filterApp(ApplicationInfo info) {
		// 系统程序 被我们用户升级了 , 三方的app
		// 带了qq ,
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true; // 用户自己下载的app
		}
		return false;
	}
}
