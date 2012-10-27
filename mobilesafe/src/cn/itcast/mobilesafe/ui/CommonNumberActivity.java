package cn.itcast.mobilesafe.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.db.dao.CommonNumDao;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class CommonNumberActivity extends Activity {
	ExpandableListView elv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_number);
		elv = (ExpandableListView) this.findViewById(R.id.elv_common_number);
		try {
		  InputStream is = getResources().getAssets().open("commonnum.db");
		  File file = new File(Environment.getExternalStorageDirectory(),"num.db");
		  FileOutputStream fos = new FileOutputStream(file);
		  byte[] buffer = new byte[1024];
		  int len = 0;
		  while((len =is.read(buffer))!=-1){
			  fos.write(buffer, 0, len);
		  }
		  fos.flush();
		  fos.close();
		  is.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		elv.setAdapter(new CommonNumberAdapter());
	}
	
	private class CommonNumberAdapter extends BaseExpandableListAdapter{
		List<Map<String,String>> lists;
		
		//返回有多少个组
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return CommonNumDao.getGroupCount();
		}
		//返回某个位置对应的小组里面的条目的个数 
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return CommonNumDao.getChildCount(groupPosition+1);
		}
		//返回某个小组对应的obj对象 
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}
		
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}
		
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
		    List<String> lists =	CommonNumDao.getGroupItem();
			TextView tv = new TextView(CommonNumberActivity.this);
			tv.setText("           "+lists.get(groupPosition));
			return tv;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			  lists= CommonNumDao.getChildrenItem(groupPosition+1);
			 Map<String, String> map =lists.get(childPosition);
			 TextView tv = new TextView(CommonNumberActivity.this);
			 Set<Entry<String, String>>  sets =  map.entrySet();
			 String text = null;
			 for(Entry<String, String> set :sets ){
				 String name = set.getKey();
				 String number = set.getValue();
				 text = name+number;
			 }
			tv.setText(text);
			return tv;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
		
	}

}
