package com.weidi.common.scanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.weidi.R;
import com.weidi.common.base.BaseActivity;
import com.weidi.common.image.ImgConfig;
import com.weidi.util.Const;
import com.weidi.util.FileUtil;
import com.weidi.util.ImageCache;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-23 上午10:59:29
 * @Description 1.0 显示二维码 内容是jid
 */
public class ShowBarcode extends BaseActivity {

	public static ShowBarcode Instance;

	@ViewInject(R.id.ivMeBarcode)
	ImageView ivMeBarcode;
	@ViewInject(R.id.tvNick)
	TextView tvNick;
	@ViewInject(R.id.tvAdr)
	TextView tvAdr;
	@ViewInject(R.id.civHeadImg)
	CircleImageView civHead;

	public static final String filePath = FileUtil.getRecentChatPath()
			+ Const.USER_ACCOUNT + ".jpg";

	@Override
	protected void initView(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_barcode_acti);
		Instance = this;
		ViewUtils.inject(this);
		initData();

	}

	private void initData() {
		
		showbarcode();
		/*ImgConfig.showHeadImg(Const.USER_NAME, civHead);
		if (Const.loginUser.getNickname() != null) {
			tvNick.setText(Const.loginUser.getNickname());
		}
		if (Const.loginUser.getAdr() != null) {
			tvAdr.setText(Const.loginUser.getAdr());
		}*/
	}

	private void showbarcode() {

		// 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中

		Bitmap barcode = ImageCache.getInstance().get(filePath);
		if (barcode != null) {
			ivMeBarcode.setImageBitmap(barcode);
		} else {
			createBarcode(filePath);
		}

	}

	private void createBarcode(final String filePath) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				Bitmap mine = ImageCache.getInstance().get(Const.USER_ACCOUNT);
				boolean success = CreateBarcode.createQRImage(
						XmppUtil.getFullUsername(Const.USER_ACCOUNT), 1000, 1000,
						mine, filePath);

				if (success) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Bitmap bitmap = BitmapFactory.decodeFile(filePath);
							ImageCache.getInstance().put(filePath, bitmap);
							ivMeBarcode.setImageBitmap(bitmap);
						}
					});
				}
			}
		}).start();
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		Instance = null;
		super.onDestroy();
	}

}
