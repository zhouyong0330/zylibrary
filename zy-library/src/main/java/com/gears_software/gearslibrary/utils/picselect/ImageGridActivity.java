package com.gears_software.gearslibrary.utils.picselect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gears_software.gearslibrary.R;
import com.gears_software.gearslibrary.utils.picselect.ImageGridAdapter.TextCallback;
import com.gears_software.gearslibrary.utils.picselect.util.PicUtils;

public class ImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;
	AlbumHelper helper;
	Button bt;
	TextView line;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择"+PicUtils.count+"张图片",Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_grid);
		getActionBar().hide();

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);

		initView();
		bt = (Button) findViewById(R.id.bt);
		line = (TextView) findViewById(R.id.line);
		if(PicUtils.bottomDrawableBackground!=null){
			line.setBackground(PicUtils.bottomDrawableBackground);
		}
		if(PicUtils.bottomResourceBackground!=-1){
			line.setBackgroundResource(PicUtils.bottomResourceBackground);
			bt.setTextColor(PicUtils.bottomResourceBackground);
		}
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}

				if (Bimp.act_bool) {
					if(PicUtils.resultClass!=null){
						Intent intent = new Intent(ImageGridActivity.this,
								PicUtils.resultClass);
						startActivity(intent);
					}
					Bimp.act_bool = false;
					
					
				}
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.drr.size() < PicUtils.count) {
						Bimp.drr.add(list.get(i));
					}
				}
				finish();
			}

		});
	}

	private void initView() {
		RelativeLayout relative = (RelativeLayout) findViewById(R.id.relaltive_title2);
		TextView gridimg_cancle = (TextView) findViewById(R.id.gridimg_cancle);
		gridimg_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		if(PicUtils.drawableBackgrund!=null){ 
			relative.setBackground(PicUtils.drawableBackgrund);
		} 
		if(PicUtils.resourceBackgrund!=-1){
			relative.setBackgroundResource(PicUtils.resourceBackgrund);
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, PicUtils.titleHeight);
		relative.setLayoutParams(layoutParams);
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				adapter.notifyDataSetChanged();
			}

		});

	}
}
