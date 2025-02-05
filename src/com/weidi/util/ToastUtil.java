package com.weidi.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weidi.QApp;
import com.weidi.R;

public class ToastUtil {

	public static void showShortToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void showLongToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showShortLuo(String message) {
		Toast.makeText(QApp.getInstance(), message, Toast.LENGTH_SHORT).show();
	}

	public static void showMine(Context context, String message) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.toast, null);
		TextView tvPrompt = (TextView) view.findViewById(R.id.tvPrompt);
		tvPrompt.setText(message);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0,
				PixelFormat.formatDipToPx(context, 10));
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.show();
	}
}
