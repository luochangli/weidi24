package com.weidi.activity;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.polites.android.GestureImageView;
import com.weidi.R;
import com.weidi.util.Const;
import com.weidi.util.Logger;

public class ImgPageActivity extends Activity{
    private static String TAG = "ImgPageActivity";
	private GestureImageView img;
	private RelativeLayout loading_progress;
	private TextView  loadingText;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	String url;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_img_page);
		options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true)
		.cacheInMemory(false)
		.cacheOnDisk(true)//设置图片不缓存于内存中
		.imageScaleType(ImageScaleType.EXACTLY)
		.considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(0))
		.build();
		url=getIntent().getStringExtra("url");
	    
		initView();
		initData();
	}


	private void initView() {
		img=(GestureImageView) findViewById(R.id.img);
		loading_progress= (RelativeLayout)findViewById(R.id.loading_progress);
		loadingText=(TextView) findViewById(R.id.loadingText);
	}
	
	private void initData() {
		imageLoader.displayImage(url.trim(), img,options,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				loading_progress.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				Logger.e(TAG, imageUri);
				Logger.e(TAG, failReason.getCause().toString());
				switch (failReason.getType()) {
					case IO_ERROR:
						message = "网络异常";
						break;
					case DECODING_ERROR:
						message = "图片解析失败";
						break;
					case NETWORK_DENIED:
						message = "图片加载失败";
						break;
					case OUT_OF_MEMORY:
						message = "内存溢出";
						break;
					case UNKNOWN:
						message = "未知错误";
						break;
				}
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				loading_progress.setVisibility(View.GONE);
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				loading_progress.setVisibility(View.GONE);
				
			}
	
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		},new ImageLoadingProgressListener() {
			
			@Override
			public void onProgressUpdate(String imageUri, View view, int current,int total) {
			     //在这里更新 ProgressBar的进度信息  
				   int progress=0;
				   if(current!=0&&current<=total){
					   progress=(int)((float) current/(float)total*100);
					   loadingText.setText(""+progress+"%");
				   }
			     }  
			   });  
		
//		imageLoader.displayImage(url, img, options, new SimpleImageLoadingListener() {
//			@Override
//			public void onLoadingStarted(String imageUri, View view) {
//				loading_progress.setVisibility(View.VISIBLE);
//			}
//			@Override
//			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//				String message = null;
//				switch (failReason.getType()) {
//					case IO_ERROR:
//						message = "网络异常";
//						break;
//					case DECODING_ERROR:
//						message = "图片解析失败";
//						break;
//					case NETWORK_DENIED:
//						message = "图片加载失败";
//						break;
//					case OUT_OF_MEMORY:
//						message = "内存溢出";
//						break;
//					case UNKNOWN:
//						message = "未知错误";
//						break;
//				}
//				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//				loading_progress.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				loading_progress.setVisibility(View.GONE);
//			}
//		},new ImageLoadingProgressListener() {        
//		   @Override  
//		    public void onProgressUpdate(String imageUri, View view, int current,int total) {     
//		     //在这里更新 ProgressBar的进度信息  
//			   int progress=0;
//			   if(current!=0&&current<=total){
//				   progress=(int)((float) current/(float)total*100);
//				   loadingText.setText(""+progress+"%");
//			   }
//		     }  
//		   });  
	}
	
    @Override
    protected void onResume() {
    	super.onResume();
    };
    
    @Override
    protected void onPause() {
    	super.onPause();
    };

}
