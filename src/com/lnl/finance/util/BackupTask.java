package com.lnl.finance.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class BackupTask extends AsyncTask<String, Void, Integer> {
	private static final String COMMAND_BACKUP = "backupDatabase";
	public static final String COMMAND_RESTORE = "restroeDatabase";
	private Context mContext;

	public BackupTask(Context context) {
		this.mContext = context;
	}

	public interface OnBakcupTaskListener {

		public void backuped();

		public void restroed();
	}

	private OnBakcupTaskListener onBakcupTaskListener;

	public void setOnBackupTaskListener(
			OnBakcupTaskListener onBakcupTaskListener) {
		this.onBakcupTaskListener = onBakcupTaskListener;
	}

	@Override
	protected Integer doInBackground(String... params) {
		// TODO Auto-generated method stub

		// 获得正在使用的数据库路径，我的是 sdcard 目录下的 /dlion/db_dlion.db
		// 默认路径是 /data/data/(包名)/databases/*.db
		File dbFile = mContext.getDatabasePath("/data/data/com.lnl.finance/databases/FinanceApp.db");
		File exportDir = new File(Environment.getExternalStorageDirectory(), "Android/data/com.xun.finance/databases/");
		if (!exportDir.exists()) {
			exportDir.mkdirs();
		}
		File backup = new File(exportDir, dbFile.getName());
		String command = params[0];
		if (command.equals(COMMAND_BACKUP)) {
			try {
				backup.createNewFile();
				fileCopy(dbFile, backup);
				return Log.d("backup", "ok");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return Log.d("backup", "fail");
			}
		} else if (command.equals(COMMAND_RESTORE)) {
			try {
				fileCopy(backup, dbFile);
				
				return Log.d("restore", "success");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return Log.d("restore", "fail");
			}
			
		} else {
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		
		if(onBakcupTaskListener != null) {
			onBakcupTaskListener.restroed();
		}
	}

	private void fileCopy(File dbFile, File backup) throws IOException {
		// TODO Auto-generated method stub
		FileChannel inChannel = new FileInputStream(dbFile).getChannel();
		FileChannel outChannel = new FileOutputStream(backup).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}

	public static  void clearBackup() {
		
		try {
			File exportDir = new File(Environment.getExternalStorageDirectory(), "Android/data/com.xun.finance/databases/");
			if (exportDir.exists()) { // 判断文件是否存在
				if (exportDir.isFile()) { // 判断是否是文件
					exportDir.delete(); // delete()方法 你应该知道 是删除的意思;
				} else if (exportDir.isDirectory()) { // 否则如果它是一个目录
					File files[] = exportDir.listFiles(); // 声明目录下所有的文件 files[];
					for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
						files[i].delete(); // 把每个文件 用这个方法进行迭代
					}
				}
				exportDir.delete();
			} else {
				
			}
		} catch (Exception e) {
		}
		
	}
	
	public static String getBackupLength(){
		
		File exportDir = new File(Environment.getExternalStorageDirectory(), "Android/data/com.xun.finance/databases/");
		if (exportDir.exists()) {
			
			return (exportDir.length()/1024.0)+"KB";
		}else{
			return "0KB";
		}
	}
}
