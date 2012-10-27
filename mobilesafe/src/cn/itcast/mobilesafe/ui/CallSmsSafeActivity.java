package cn.itcast.mobilesafe.ui;

import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.db.dao.BlackNumberDao;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import cn.itcast.mobilesafe.util.Logger;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CallSmsSafeActivity extends Activity {
	LayoutInflater inflater;
	Dialog dialog;
	BlackNumberDao dao;
	ListView lv_call_sms_safe;
	CallSmsSafeAdapter adapter;
	public static final int ADD = 0;
	public static final int UPDATE = 1;
	public static final String TAG = "CallSmsSafeActivity";
	private String oldnumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_sms_safe);
		lv_call_sms_safe = (ListView) this.findViewById(R.id.lv_call_sms_safe);
		inflater = LayoutInflater.from(this);
		dao = new BlackNumberDao(this);
		List<String> numbers = dao.findAll();
		adapter = new CallSmsSafeAdapter(numbers);
		lv_call_sms_safe.setAdapter(adapter);
		// ��listviewע��һ�������Ĳ˵�
		registerForContextMenu(lv_call_sms_safe);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.call_sms_safe_menu, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// ����Ҫ֪�����������һ����Ŀ
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// ��ȡ��ǰ�����Ŀ��λ��
		int position = (int) info.id;
		String number = (String) lv_call_sms_safe.getItemAtPosition(position);
		switch (item.getItemId()) {
		case R.id.call_sms_safe_delete:
			dao.delete(number);
			refushUI();
			break;
		case R.id.call_sms_safe_update:
			oldnumber = number;
			showAddDialog(UPDATE);
			break;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * ��Ӻ�������Ӧ�ĵ���¼�
	 */
	public void add(View view) {
		showAddDialog(ADD);
	}

	private void showAddDialog(final int flag) {
		AlertDialog.Builder builder = new Builder(this);
		View dialogview = inflater.inflate(R.layout.add_number_dialog, null);
		builder.setView(dialogview);
		builder.setTitle("���������");
		final EditText et_add_number = (EditText) dialogview
				.findViewById(R.id.et_add_number);
		Button bt_ok = (Button) dialogview.findViewById(R.id.bt_add_number_ok);
		Button bt_cancle = (Button) dialogview.findViewById(R.id.bt_add_cancle);
		bt_ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String number = et_add_number.getText().toString().trim();
				if ("".equals(number)) {
					Toast.makeText(getApplicationContext(), "���벻��Ϊ��", 1).show();
					return;
				} else {
					if (flag == ADD) {
						dao.add(number);
						dialog.dismiss();
						refushUI();
					}else{
						dao.update(oldnumber, number);
						dialog.dismiss();
						refushUI();
					}
				}
			}

		
		});

		bt_cancle.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog = builder.create();
		dialog.show();
	}

	private class CallSmsSafeAdapter extends BaseAdapter {
		private List<String> numbers;

		// ��������listview������
		public void setNumbers(List<String> numbers) {
			this.numbers = numbers;
		}

		public CallSmsSafeAdapter(List<String> numbers) {
			this.numbers = numbers;
		}

		public int getCount() {
			return numbers.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return numbers.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		//  convertView ��ʷview����Ļ��� 
		public View getView(int position, View convertView, ViewGroup parent) {
			// �ᱻ���ö��ٴ�?
			Logger.i(TAG,"��ǰ���ص�view����Ϊ :"+ position+ "����getview�������ص�view����");
			TextView view = null;
			if(convertView==null){
				Logger.i(TAG,"��ʷ�Ļ���view����Ϊ�� ,�����µ�view����");
				view = new TextView(CallSmsSafeActivity.this);
			
			}else{
				view = (TextView) convertView;
				Logger.i(TAG,"��ʷ�Ļ���view����Ϊ�� ,ʹ��convertview");
			}
			view.setText(numbers.get(position));
			return view;
		}

	}
	private void refushUI() {
		
		// ˢ�����listview����ʾ������
		List<String> numbers = dao.findAll();
		adapter.setNumbers(numbers);
		// ֪ͨlistview���¶�Ӧ�Ľ���
		adapter.notifyDataSetChanged();
	}
}
