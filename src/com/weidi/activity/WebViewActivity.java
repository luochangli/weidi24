package com.weidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.weidi.R;

public class WebViewActivity  extends Activity{
	
	private ImageView topLeft;
	private TextView topTitle, topRight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		topTitle.setText("最新公告");
		topRight.setVisibility(View.GONE);
		topLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		WebView web=(WebView)findViewById(R.id.webView1);
		Intent intent = this.getIntent();
		Bundle bundle=intent.getExtras();
		String pathurl=(String)bundle.get("httpurl");
		web.reload();
		web.setWebViewClient(new WebViewClient());
		web.loadUrl(pathurl);
	}
}
