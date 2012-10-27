package cn.itcast.mobilesafe.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.TaskInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskManagerAdapter extends BaseAdapter {
	private Context context;
	private List<TaskInfo> taskinfos;
	// 整理这个集合 
	
	private List<TaskInfo> usertaskinfos;
	private List<TaskInfo> systemtaskinfos;
	LayoutInflater inflater;
	
	Map<String , Boolean> map;
	
	//设置条目的选择状态 
	public void setChecked( Map<String , Boolean> map){
		this.map = map;
	}
	
	private void arrangeNewTaskInfos (List<TaskInfo> taskinfos){
		usertaskinfos.clear();
		systemtaskinfos.clear();
		for(TaskInfo taskinfo : taskinfos){
			if(taskinfo.isIsuserapp()){
				usertaskinfos.add(taskinfo);
			}else{
				systemtaskinfos.add(taskinfo);
			}
		}
		
	}
	public TaskManagerAdapter(Context context, List<TaskInfo> taskinfos) {

		usertaskinfos = new ArrayList<TaskInfo>();
		systemtaskinfos = new ArrayList<TaskInfo>();
		this.context = context;
		this.taskinfos = taskinfos;
		inflater =LayoutInflater.from(context);
		arrangeNewTaskInfos(taskinfos);
	}

	public void setTaskInfos(List<TaskInfo> taskinfos){
		this.taskinfos = taskinfos;
		arrangeNewTaskInfos(taskinfos);
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
//		return taskinfos.size()+2; // 让listview多返回两个条目 
		return usertaskinfos.size()+systemtaskinfos.size()+2;
	}

	public Object getItem(int position) {
		if(position==0){
			return 0;  // 显示一个textview 系统程序的数目
		}else if(position<systemtaskinfos.size()+1){
			return systemtaskinfos.get(position-1); // 返回的是系统的程序的对象
		}else if(position==systemtaskinfos.size()+1){ //返回的是用户程序的textview的位置
			return  position;
		}else{
			int newposition = position - systemtaskinfos.size()-2;
			return usertaskinfos.get(newposition);
		}
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if(position==0){
			//显示textview的提示
			TextView tv = new TextView(context);
			tv.setText("系统程序数量("+systemtaskinfos.size()+")");
			return tv;
		}else if(position<systemtaskinfos.size()+1){
			TaskInfo info = systemtaskinfos.get(position-1);
			view = inflater.inflate(R.layout.task_item, null);
			ImageView iv_task_icon = (ImageView) view.findViewById(R.id.iv_task_icon);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_task_name);
			TextView tv_memory = (TextView) view.findViewById(R.id.tv_task_memory);
			CheckBox cb = (CheckBox) view.findViewById(R.id.cb_task_checked);
			iv_task_icon.setImageDrawable(info.getTaskicon());
			tv_name.setText(info.getTaskname());
			tv_memory.setText("占用内存"+info.getMemory());
			//cb.setChecked(info.isIschecked());
			if(map!=null&&map.containsKey(info.getPackname())){
				if(map.get(info.getPackname())){
					cb.setChecked(true);
				}
			}else{
				cb.setChecked(false);
			}
			if("system".equals(info.getPackname())){
				cb.setClickable(false);
				cb.setVisibility(View.INVISIBLE);
			}
			return view;
		
		}else if(position==systemtaskinfos.size()+1){
			TextView tv = new TextView(context);
			tv.setText("用户的程序数量("+usertaskinfos.size()+")");
			return tv;
		}else{
			int newposition = position - systemtaskinfos.size()-2;
			TaskInfo info = usertaskinfos.get(newposition);
			view = inflater.inflate(R.layout.task_item, null);
			ImageView iv_task_icon = (ImageView) view.findViewById(R.id.iv_task_icon);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_task_name);
			TextView tv_memory = (TextView) view.findViewById(R.id.tv_task_memory);
			CheckBox cb = (CheckBox) view.findViewById(R.id.cb_task_checked);
			iv_task_icon.setImageDrawable(info.getTaskicon());
			tv_name.setText(info.getTaskname());
			tv_memory.setText("占用内存"+info.getMemory());
			//cb.setChecked(info.isIschecked());
			if(map!=null&&map.containsKey(info.getPackname())){
				if(map.get(info.getPackname())){
					cb.setChecked(true);
					
				}
			}else{
				cb.setChecked(false);
			}
			if("cn.itcast.mobilesafe".equals(info.getPackname())){
				cb.setClickable(false);
				cb.setVisibility(View.INVISIBLE);
			}
			return view;
		}
	}

}
