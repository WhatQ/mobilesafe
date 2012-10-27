package cn.itcast.mobilesafe.ui.adapter;

import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.ContactInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ContactListAdapter extends BaseAdapter {
	private Context  context;
	private  List<ContactInfo> infos;
	private LayoutInflater inflater;
	
	public ContactListAdapter(Context context,List<ContactInfo> infos) {
		this.context = context;
		this.infos = infos;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infos.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ContactInfo info = infos.get(position);
		View view =  inflater.inflate(R.layout.select_contact_item, null);
		TextView et_name = (TextView) view.findViewById(R.id.tv_contact_item_name);
		TextView et_number = (TextView)view.findViewById(R.id.tv_contact_item_number);
		et_name.setText(info.getName());
		et_number.setText(info.getPhone());
		
		return view;
	}

}
