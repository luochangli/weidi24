package com.weidi.common.function;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import com.weidi.QApp;
import com.weidi.util.FileUtil;
import com.weidi.util.Logger;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-6-11 上午11:14:12
 *@Description 1.0
 */
public class RecordingHelper {

	private static String TAG = "RecordingHelper";
	private String mFileName = null;
	/** 用于完成录音 */
	private MediaRecorder mRecorder = null;


	/**
	 * 开始录音
	 * @return 保存路径
	 */
	public String startVoice() {
		String dir = String.valueOf(System.currentTimeMillis());
		// 设置录音保存路径
		mFileName = FileUtil.getRecentChatPath() + dir + ".amr";
		System.out.println(mFileName);
		String state = android.os.Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			Logger.i(TAG, "SD Card is not mounted,It is  " + state + ".");
		}
		File directory = new File(mFileName).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			Log.i(TAG, "Path to file could not be created");
		}
		Toast.makeText(QApp.getInstance(), "开始录音", 0).show();
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Logger.e(TAG, "开始录音失败prepare() failed");
		}
		mRecorder.start();
		return dir;
	}


	/**
	 * 停止录音
	 */
	public void stopVoice() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		Toast.makeText(QApp.getInstance(), "保存录音" + mFileName, 0).show();
	}
}
