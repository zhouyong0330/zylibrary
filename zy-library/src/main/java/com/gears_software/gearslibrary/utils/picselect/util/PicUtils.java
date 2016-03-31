package com.gears_software.gearslibrary.utils.picselect.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.gears_software.gearslibrary.R;
import com.gears_software.gearslibrary.utils.picselect.TestPicActivity;
import com.gears_software.gearslibrary.utils.picselect.Bimp;
import com.gears_software.gearslibrary.utils.picselect.PhotoActivity;

import java.io.File;

/**
 * 
 * 
 * 图片选择器帮助类
 * 
 *  * 相册选择器使用事例
 * 
 *  首先在使用的项目中注册 相关的  activity
 *   <activity android:name="com.gears_software.common.utils.picselect.TestPicActivity"></activity>
        <activity android:name="com.gears_software.common.utils.picselect.ImageGridActivity"></activity>
        <activity android:name="com.gears_software.common.utils.picselect.PhotoActivity"></activity>
 * 
 * initData();数据初始化功能  此功能必须在activity启动时调用
 * setTitleHeight();设置标题栏高度  不传默认为48dp
 * setBackgroundResource();//设置标题栏资源背景 不传默认粉色
 * setBackgroundDrawable();//设置标题栏drawable 不传默认粉色
 * setBottomDrawableBackground();//设置图片选择界面底部按钮颜色 不传默认粉色 使用这个设置颜色的话，底部字体颜色不会变
 * setBottomResourceBackground();//设置图片选择界面底部按钮颜色 不传默认粉色  
 * WindowShow();//启动相机相册选取框
 * photo();//直接启动相机功能
 * photoDataAdd();//在使用相机功能的时候需要在activity中的onActivityResult()方法中调用
 * startPhotoResource();直接启动相册选取功能
 * 
 * List<String> list = new ArrayList<String>();				
				for (int i = 0; i < Bimp.drr.size(); i++) {
					String Str = Bimp.drr.get(i).substring( 
							Bimp.drr.get(i).lastIndexOf("/") + 1,
							Bimp.drr.get(i).lastIndexOf("."));
					list.add(FileUtils.SDPATH+Str+".JPEG");				
				}
				// 高清的压缩图片全部就在  list 路径里面了
				// 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
				// 完成上传服务器后 .........
				FileUtils.deleteDir();
				
 * 本地的图片路径存在Bimp.drr里面，如果需要压缩可以调用Bimp.revitionImageSize();方法
 * 
 * setOnPhoneImageDeleteLinear  这个方法可以监听，当前删除的图片是那个一个
 * 
 * @author zhouyong
 *
 */
public class PicUtils {
	public static Drawable drawableBackgrund = null;//顶部布局背景
	public static int resourceBackgrund = -1;//背景资源
	
	public static Drawable bottomDrawableBackground = null;//选择图片底部完成按钮颜色
	public static int bottomResourceBackground = -1;
	
	public static Class resultClass = null;//需要返回的类
	
	public static int titleHeight = 72;//顶部布局高度
	
	public static Activity context; //上下文
	private PopupWindows popupWindows = null; //弹出窗口
	private View view; //弹窗需要的view
	private String path = null; //相机拍照保存路径
	
	private static int ACTIVITY_COUNT = 2512130;
	public static int count = 9;//最大允许选择几张照片
	
	/**
	 * 
	 * @param context 上下文
	 * @param resultClass 选取照片完毕之后需要跳转的界面
	 * @param view 是否弹出选择窗口  不传入则不弹出，传入则弹出
	 */
	public PicUtils(Activity context,Class resultClass,View view){
		this.context = context;
		this.resultClass = resultClass;
		if(view!=null){
			popupWindows = new PopupWindows(context, view);
			this.view = view;
		}
		
	}
	
	//用于修改图片选择的最大数量
	public void setCount(int count){
		if(count>0){
			this.count = count;
		}
	}
	
	public void startPhotoResource(){
		Intent i = new Intent(context,TestPicActivity.class);
		context.startActivity(i);
	}
	
	public void initData(){
		Bimp.max = 0;
		Bimp.act_bool = true;
		Bimp.bmp.removeAll(Bimp.bmp);
		Bimp.drr.removeAll(Bimp.drr);
	}
	
	
	public void setTitleHeight(int titleHeight){
		final float scale = context.getResources().getDisplayMetrics().density;  
		this.titleHeight = (int) (titleHeight * scale + 0.5f);  
	}
	
	public void setBottomDrawableBackground(Drawable drawable){
		this.bottomDrawableBackground = drawable;
		this.bottomResourceBackground = -1;
	}
	
	public void setBottomResourceBackground(int resource){
		this.bottomResourceBackground = resource;
		this.bottomDrawableBackground = null;
	}
	
	public void setBackgroundResource(int resource){
		this.resourceBackgrund = resource;
		drawableBackgrund = null;
	}
	
	public void setBackgroundDrawable(Drawable drawable){
		this.drawableBackgrund = drawable;
		resourceBackgrund = -1;
	}
	
	
	public void WindowShow(){
		if(popupWindows!=null){
			popupWindows.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		}
	}
	
	/**
	 * 传入 adapter 点击事件中的position 传入的position必须是选中图片的。
	 * 此方法用于开发者调用自己写好的界面，判断需要自己在方法中判断。
	 */
	public void setShowImageClick(Integer arg2){
			Intent intent = new Intent(context,
					PhotoActivity.class);
			intent.putExtra("ID", arg2);
			context.startActivity(intent);
	}
	
	/**
	 * 传入 adapter 点击事件中的position
	 */
	public void setOnImageClick(int arg2){
		if(arg2==Bimp.bmp.size()){
			WindowShow();
		}else{
			Intent intent = new Intent(context,
					PhotoActivity.class);
			intent.putExtra("ID", arg2);
			context.startActivity(intent);
		}
	}
	
	public class PopupWindows extends PopupWindow {
		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(context,
							TestPicActivity.class);
					context.startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}
	
	public void photo() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File dis = new File(Environment.getExternalStorageDirectory()+
				"/IMGDATA");
		if(!dis.exists()){
			dis.mkdirs();
		}
		File file = new File(Environment.getExternalStorageDirectory()+"/IMGDATA/",
				"IMG"+System.currentTimeMillis()+".png");
		path  = file.getPath();
		Uri uri = Uri.fromFile(file);
		i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		context.startActivityForResult(i, ACTIVITY_COUNT);
	}
	
	public void photoDataAdd(int requestCode,int resultCode){
		if(requestCode==ACTIVITY_COUNT){
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
			}
		}
	}
	
	//获取到删除的图片，然后经过接口推送
	public static void getDeleteImage(String url){
		onPhoneImageDeleteLinear.onDeleteBitmap(url);
	}
	
	
	public void setOnPhoneImageDeleteLinear(OnPhoneImageDeleteLinear onPhoneImageDeleteLinear){
		this.onPhoneImageDeleteLinear = onPhoneImageDeleteLinear;
	}
	
	public static OnPhoneImageDeleteLinear onPhoneImageDeleteLinear;
	
	public interface OnPhoneImageDeleteLinear{
		public void onDeleteBitmap(String url);
	}
}
