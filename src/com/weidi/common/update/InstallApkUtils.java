package com.weidi.common.update;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.weidi.util.Logger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-13 下午3:28:24
 * @Description 1.0
 */
public class InstallApkUtils {
	private static String TAG = InstallApkUtils.class.getSimpleName();

	public static void installAndStartApk(final Context context,
			final String apkPath) {
		if ((apkPath == null) || (context == null)) {
			return;
		}

		File file = new File(apkPath);
		if (file.exists() == false) {
			return;
		}

		new Thread() {
			public void run() {
				String packageName = getUninstallApkPackageName(context,
						apkPath);
				if (silentInstall(apkPath)) {
					List<ResolveInfo> matches = findActivitiesForPackage(
							context, packageName);
					if ((matches != null) && (matches.size() > 0)) {
						ResolveInfo resolveInfo = matches.get(0);
						ActivityInfo activityInfo = resolveInfo.activityInfo;
						startApk(activityInfo.packageName, activityInfo.name);
					}
				}
			};
		}.start();

	}

	public static String getUninstallApkPackageName(Context context,
			String apkPath) {
		String packageName = null;
		if (apkPath == null) {
			return packageName;
		}

		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);
		if (info == null) {
			return packageName;
		}

		packageName = info.packageName;
		return packageName;
	}

	public static List<ResolveInfo> findActivitiesForPackage(Context context,
			String packageName) {
		final PackageManager pm = context.getPackageManager();

		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		mainIntent.setPackage(packageName);

		final List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0);
		return apps != null ? apps : new ArrayList<ResolveInfo>();
	}

	public static boolean silentInstall(String apkPath) {
		String cmd1 = "chmod 777 " + apkPath + " \n";
		String cmd2 = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r "
				+ apkPath + " \n";
		return execWithSID(cmd1, cmd2);
	}

	private static boolean execWithSID(String... args) {
		boolean isSuccess = false;
		Process process = null;
		OutputStream out = null;
		try {
			process = Runtime.getRuntime().exec("su");
			out = process.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(out);

			for (String tmp : args) {
				dataOutputStream.writeBytes(tmp);
			}

			dataOutputStream.flush(); // 提交命令
			dataOutputStream.close(); // 关闭流操作
			out.close();

			isSuccess = waitForProcess(process);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isSuccess;
	}

	public static boolean startApk(String packageName, String activityName) {
		boolean isSuccess = false;

		String cmd = "am start -n " + packageName + "/" + activityName + " \n";
		try {
			Process process = Runtime.getRuntime().exec(cmd);

			isSuccess = waitForProcess(process);
		} catch (IOException e) {
			Logger.i(TAG, e.getMessage());
			e.printStackTrace();
		}
		return isSuccess;
	}

	private static boolean waitForProcess(Process p) {
		boolean isSuccess = false;
		int returnCode;
		try {
			returnCode = p.waitFor();
			switch (returnCode) {
			case 0:
				isSuccess = true;
				break;

			case 1:
				break;

			default:
				break;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return isSuccess;
	}
}