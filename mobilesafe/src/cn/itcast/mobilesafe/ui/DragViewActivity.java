package cn.itcast.mobilesafe.ui;
import cn.itcast.mobilesafe.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


public class DragViewActivity extends Activity {
	ImageView iv_location;
	TextView tv_change_location;
	SharedPreferences sp ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//实现了 界面的半透明效果 
		setTheme(R.style.Transparent); 
		//代码更改背景颜色 
		//getwindow() 只对当前窗体有效 
		//		ColorDrawable drawable = new ColorDrawable(Color.RED);
//		getWindow().setBackgroundDrawable(drawable);
		setContentView(R.layout.drag_view);
		iv_location = (ImageView) this.findViewById(R.id.iv_location);
		tv_change_location = (TextView) this.findViewById(R.id.tv_change_location);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		iv_location.setOnTouchListener(new OnTouchListener() {
			//view 代表的被触摸的对象
			// event 这个参数 代表的是触摸的事件
			int startx; // 手指第一次触摸屏幕的时候 x的值
			int starty;  // ......................y的值
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					int lastx = v.getLeft();
					int lasty = v.getTop();
					Editor editor = sp.edit();
					editor.putInt("lastx", lastx);
					editor.putInt("lasty", lasty);
					editor.commit();
					break;
				case MotionEvent.ACTION_MOVE:
					int x= (int) event.getRawX();
					int y= (int) event.getRawY();
					//水平和竖直方向手指移动的距离
					int dx = x-startx;
					int dy = y-starty;
					//动态的更改view对象的位置 
					v.layout(v.getLeft()+dx, v.getTop()+dy, v.getRight()+dx, v.getBottom()+dy);
					//重新设置手指点击的初始位置
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					if(starty>240){
//						LayoutParams  params =(LayoutParams) tv_change_location.getLayoutParams();
//						params.topMargin = 100;
//						tv_change_location.setLayoutParams(params);
						tv_change_location.layout(tv_change_location.getLeft(),100 , tv_change_location.getRight(), 150);
					}else{
//						LayoutParams  params =(LayoutParams) tv_change_location.getLayoutParams();
//						params.topMargin = 300;
//						tv_change_location.setLayoutParams(params);
						tv_change_location.layout(tv_change_location.getLeft(), 300, tv_change_location.getRight(),350);
					}
				
					//重新渲染 imageview
					v.invalidate();
					break;
			
				}
				return true;
			}
		});
	}

	

}
