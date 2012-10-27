package cn.itcast.mobilesafe.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.itcast.mobilesafe.MobilesafeApplication;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.AppInfo;
import cn.itcast.mobilesafe.domain.TaskInfo;
import cn.itcast.mobilesafe.ui.adapter.TaskManagerAdapter;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.itcast.mobilesafe.util.Logger;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class TaskManagerActivity extends Activity {
	protected static final int LOAD_TASK_FINISHED = 60;
	protected static final String TAG = "TaskManagerActivity";
	TextView tv_task_manager_process_count;
	TextView tv_task_manager_avail_memory;
	ActivityManager am;
	ProgressDialog pd;
	PackageManager pm;
	ListView lv_task_manager;
	List<TaskInfo> taskinfos;
	TaskManagerAdapter adapter;
	Map<String, Boolean> map;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_TASK_FINISHED:
				pd.dismiss();
				// ����һ�±�����
				setTitle();
				if (adapter == null) {
					adapter = new TaskManagerAdapter(TaskManagerActivity.this,
							taskinfos);
					lv_task_manager.setAdapter(adapter);
				} else {

					map.clear();
					adapter.setChecked(map);
					adapter.setTaskInfos(taskinfos);
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
		map = new HashMap<String, Boolean>();
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		 boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.task_manager);
		lv_task_manager = (ListView) this.findViewById(R.id.lv_task_manager);
		pd = new ProgressDialog(this);
		pm = getPackageManager();
		 if (flag) {
		 // ����ϵͳ��titleΪ �Զ�����Ǹ���Դ�ļ�
		 getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		 R.layout.custom_title);
		 }
		tv_task_manager_process_count = (TextView) this
				.findViewById(R.id.tv_task_manager_process_count);
		tv_task_manager_avail_memory = (TextView) this
				.findViewById(R.id.tv_task_manager_avail_memory);

		setTitle();

		fillData();

		// ��listviewע�����¼�

		lv_task_manager.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Logger.i(TAG, "postion");

				Object item = lv_task_manager.getItemAtPosition(position);
				if (item instanceof TaskInfo) {
					TaskInfo info = (TaskInfo) item;
					if ("system".equals(info.getPackname())
							|| "cn.itcast.mobilesafe".equals(info.getPackname())) {
						return;
					}
					Logger.i(TAG, info.getPackname());
					CheckBox cb = (CheckBox) view
							.findViewById(R.id.cb_task_checked);
					if (cb.isChecked()) {
						cb.setChecked(false);
						info.setIschecked(false);
						map.put(info.getPackname(), false);
						adapter.setChecked(map);
						adapter.notifyDataSetChanged();
					} else {
						cb.setChecked(true);
						info.setIschecked(true);
						map.put(info.getPackname(), true);
						adapter.setChecked(map);
						adapter.notifyDataSetChanged();
					}

				}
			}
		});

		lv_task_manager
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// ������һ������,�����Ĵ���.
						// ��ʾ ����İ汾��,�������,����Ȩ��

						Object item = lv_task_manager
								.getItemAtPosition(position);
						if (item instanceof TaskInfo) {
							// 1.��Ҫ���ݵĶ������һ��ȫ�ֵĺ�������
							MobilesafeApplication app = (MobilesafeApplication) getApplication();
							app.taskInfo = (TaskInfo) item;

							Intent intent = new Intent(TaskManagerActivity.this,AppDetailActivity.class);
							startActivity(intent);
							return false;
						} else {
							return false;
						}

					}
				});
	}

	private void setTitle() {
		List<RunningAppProcessInfo> runingappProcessInfos = am
				.getRunningAppProcesses();
		tv_task_manager_process_count.setText("��ǰ����ĸ���Ϊ"
				+ runingappProcessInfos.size());
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		float availMem = outInfo.availMem / 1024f / 1024f;
		// ��ʽ������ı��ַ���
		DecimalFormat format = new DecimalFormat("###.00");
		String availMemStr = format.format(availMem);
		tv_task_manager_avail_memory.setText("�����ڴ� "+availMemStr + "MB");
	}

	private void fillData() {
		pd.setMessage("���ڻ�ȡ������Ϣ");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				taskinfos = getRunningTaskInfo();
				// ����֪ͨ�����߳�
				Message msg = new Message();
				msg.what = LOAD_TASK_FINISHED;
				handler.sendMessage(msg);
			}
		}.start();

	}

	/**
	 * ��ȡ����ϵͳ�������е�������б�
	 * 
	 * @return
	 */
	private List<TaskInfo> getRunningTaskInfo() {
		List<TaskInfo> taskinfos = new ArrayList<TaskInfo>();
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : infos) {
			TaskInfo taskinfo = new TaskInfo();
			try {
				int pid = info.pid;
				taskinfo.setPid(pid);
				int[] pids = { pid };
				android.os.Debug.MemoryInfo[] memoryinfos = am
						.getProcessMemoryInfo(pids);
				int kbmemory = memoryinfos[0].getTotalPrivateDirty();
				String memory = getMemoryString(kbmemory);
				taskinfo.setMemory(memory);
				String packname = info.processName;
				taskinfo.setPackname(packname);

				ApplicationInfo appinfo = pm.getApplicationInfo(packname, 0);
				boolean isuserapp = filterApp(appinfo);
				// �����Ƿ����û�����
				taskinfo.setIsuserapp(isuserapp);
				Drawable icon = appinfo.loadIcon(pm);
				taskinfo.setTaskicon(icon);
				String taskname = appinfo.loadLabel(pm).toString();
				taskinfo.setTaskname(taskname);
				taskinfos.add(taskinfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return taskinfos;
	}

	/**
	 * ��ȡ�ڴ���Ϣ��Ӧ���ı�ֵ
	 */

	private String getMemoryString(int kbmemory) {
		if (kbmemory < 1024) {
			return kbmemory + "KB";
		} else if (kbmemory < 1024 * 1024) { // MB
			float result = kbmemory / 1024f;
			DecimalFormat format = new DecimalFormat("###.00");
			return format.format(result);
		} else {
			return "�ڴ���Ϣ����";
		}
	}

	/**
	 * 
	 * @param info
	 * @return �Ƿ���������app
	 */
	private boolean filterApp(ApplicationInfo info) {
		// ϵͳ���� �������û������� , ������app
		// ����qq ,
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true; // �û��Լ����ص�app
		}
		return false;
	}

	public void killTask(View view) {
		Set<Entry<String, Boolean>> set = map.entrySet();
		int count = 0;
		for (Entry<String, Boolean> entry : set) {
			if (entry.getValue()) {
				String packname = entry.getKey();
				am.restartPackage(packname);
				count++;
			}
		}
		MyToast.showMyToast(this, R.drawable.notification, "ɱ����" + count
				+ "������");

		fillData();
	}
	public void taskSetting(View view){
		Intent intent = new Intent(this,TaskSettingActivity.class);
		startActivity(intent);
		
	}
}
