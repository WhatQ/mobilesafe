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
	private String[] names = { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���", "��������", "����ɱ��",
			"ϵͳ����", "�߼�����", "��������" };
	private int[] icons = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };
	static ImageView iv_icon;
	static TextView tv_name;
	// �������ķ���
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
		// ��ɲ���������ĳ�ʼ��
		// inflater = LayoutInflater.from(this);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		gv_main.setAdapter(new MainScreenAdapter());
		// ��gridview����Ŀ���õ���¼�
		gv_main.setOnItemClickListener(this);

	}

	private class MainScreenAdapter extends BaseAdapter {

		// ����gridview�����ж��ٸ���Ŀ
		public int getCount() {
			return names.length;
		}

		// ���ص�ǰpostion��Ӧ����Ŀ�Ķ���
		public Object getItem(int position) {
			return position;
		}

		// ��ȡ��ǰĳ��λ����Ŀ��id
		public long getItemId(int position) {
			return position;
		}

		// ����ÿһ��item��view����
		public View getView(int position, View convertView, ViewGroup parent) {
			// ����: getview�ķ��������˶��ٴ�?
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
	 * item�����Ӧ�ĵ���¼� parent ->�������item�ĸ����� , gridview view -> ÿһ����Ŀ ����Ӧ��view����
	 * position ��ǰ��Ŀ��λ�� id �к�
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0: // �ֻ�����
			Intent lostProtectedIntent = new Intent(this,
					LostProtectedActivity.class);
			startActivity(lostProtectedIntent);
			break;
		case 1: // ͨѶ��ʿ
			Intent callsmsSafeIntent = new Intent(this,
					CallSmsSafeActivity.class);
			startActivity(callsmsSafeIntent);
			break;	
		case 2: // �������
			Intent appManagerIntent = new Intent(this,
					AppManagerActivity.class);
			startActivity(appManagerIntent);
			break;
		case 3: // �������
			Intent taskManagerIntent = new Intent(this,
					TaskManagerActivity.class);
			startActivity(taskManagerIntent);
			break;	
		case 4: // ��������
			Intent trafficManagerIntent = new Intent(this,
					TrafficManagerActivity.class);
			startActivity(trafficManagerIntent);
			break;	
		case 5: // �ֻ�ɱ��
			Intent killVirusIntent = new Intent(this,
					KillVirusActivity.class);
			startActivity(killVirusIntent);
			break;	
		case 6: // ϵͳ����
			Intent systemoptIntent = new Intent(this,
					SystemOptActivity.class);
			startActivity(systemoptIntent);
			break;	
		case 7: // �߼�����
			Intent atoolsIntent = new Intent(this,
					AtoolsActivity.class);
			startActivity(atoolsIntent);
			break;	
		case 8: // ��������
			Intent settingIntent = new Intent(this,
					SettingActivity.class);
			startActivity(settingIntent);
			break;
		}
	}
}
