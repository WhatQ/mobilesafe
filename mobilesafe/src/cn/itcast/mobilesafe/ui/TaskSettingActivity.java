package cn.itcast.mobilesafe.ui;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.service.KillTaskService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class TaskSettingActivity extends Activity {
	SharedPreferences sp;
	CheckBox cb_task_setting;
	TextView tv_task_setting;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		intent = new Intent(this,KillTaskService.class);
		sp =getSharedPreferences("config", Context.MODE_PRIVATE);
		setContentView(R.layout.task_setting);
		cb_task_setting = (CheckBox) this.findViewById(R.id.cb_task_setting);
		tv_task_setting = (TextView) this.findViewById(R.id.tv_task_setting_label);
		boolean isautokill = sp.getBoolean("isautokill", false);
		if(isautokill){
			cb_task_setting.setChecked(true);
			tv_task_setting.setText("自动清理内存已经开启");
		}else{
			cb_task_setting.setChecked(false);
			tv_task_setting.setText("自动清理内存未被开启");
		}
		
		cb_task_setting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
				
					tv_task_setting.setText("自动清理内存已经开启");
					Editor editor = sp.edit();
					editor.putBoolean("isautokill", true);
					editor.commit();
					
					//定期的清理任务 
					//设置清理列表的操作
					//
					//开启一个后台的服务 定期清理这些进程 
					startService(intent);
					
				}else{

					tv_task_setting.setText("自动清理内存未被开启");
					Editor editor = sp.edit();
					editor.putBoolean("isautokill", false);
							editor.commit();
				    stopService(intent);
				}
				
			}
		});
		
	}

}
