package com.gears_software.gearslibrary.demos.AlbumSelect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.gears_software.gearslibrary.R;
import com.gears_software.gearslibrary.utils.picselect.adapter.GridAdapter;
import com.gears_software.gearslibrary.utils.picselect.util.PicUtils;

/**
 * 相册多选选择器使用事例
 *
 * 该文件不可运行，提供代码参考
 * 
 *  首先在使用的项目中注册 相关的  activity
 *   <activity android:name="com.gears_software.common.utils.picselect.TestPicActivity"></activity>
        <activity android:name="com.gears_software.common.utils.picselect.ImageGridActivity"></activity>
        <activity android:name="com.gears_software.common.utils.picselect.PhotoActivity"></activity>
 * 
 * 本地的图片路径存在Bimp.drr里面，如果需要压缩可以调用Bimp.revitionImageSize();方法
 * 
 * 更多方法请看 PicUtils中的备注
 * 
 * 
 * @author zhouyong
 *
 */
public class MainActivity extends Activity {
	//首先声明图片选择器帮助类
	PicUtils picUtils;
	
	//定义好的图片显示的适配器
	GridAdapter adapter;
	GridView grid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		GridView grid = (GridView) findViewById(R.id.gridView1);
		
		//创建图片选择器帮助类的实例
		picUtils = new PicUtils(this, null,grid);
		//首先使用之前要先初始化数据调用picUtils.initData();
		picUtils.initData();
		
		//设置选择器activity的titile背景颜色   不设置默认为粉色
		picUtils.setBackgroundResource(R.color.col_blue);
		
		//设置选择器activity的titile控件高度，不设置默认为48dp
		picUtils.setTitleHeight(48);
		
		//获取adapter实例传入当前activity 后两位参数是图片显示的宽高
		adapter = new GridAdapter(this,100,100);
		
		grid.setAdapter(adapter);
		
		
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//传入position 判断点击事件是否是图片还是新增
				picUtils.setOnImageClick(arg2);
			}
		});
		
	}

	//要实现onRestart()方法，以便在activity重新启动的时候调用adapter.update();来刷新图片
	@Override
	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//如果你调用了相机拍照  那么就要在onActivityResult 方法中调用这个数据添加方法，将拍照的数据存储起来
		picUtils.photoDataAdd(requestCode, resultCode);
	}
	

}
