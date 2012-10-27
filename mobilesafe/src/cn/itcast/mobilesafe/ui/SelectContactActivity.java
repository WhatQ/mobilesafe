package cn.itcast.mobilesafe.ui;

import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.ContactInfo;
import cn.itcast.mobilesafe.engine.ContactInfoService;
import cn.itcast.mobilesafe.ui.adapter.ContactListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SelectContactActivity extends Activity {
	ListView lv;
	ContactListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_contact);
		lv = (ListView) this.findViewById(R.id.lv_select_contact);
		ContactInfoService service = new ContactInfoService(this);
		List<ContactInfo> infos = service.getContantInfos();
		adapter = new ContactListAdapter(this, infos);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ContactInfo info = (ContactInfo) lv.getItemAtPosition(position);
				String number = info.getPhone();
				Intent data = new Intent();
				data.putExtra("number", number);
				setResult(0, data);
				finish();
			}
		});
	}

}
