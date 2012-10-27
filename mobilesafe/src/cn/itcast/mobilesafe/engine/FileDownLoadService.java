package cn.itcast.mobilesafe.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;

public class FileDownLoadService {
	/**
	 * 
	 * @param path 下载文件的路径
	 * @param fos 下载文件写入到哪个输出流里面
	 * @param pd 下载的进度条对话框
	 * @return
	 * @throws Exception
	 */
	public static void downFile(String path,FileOutputStream fos,ProgressDialog pd ) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		
		//conn.setRequestMethod("GET");
		InputStream is = conn.getInputStream();
		//得到服务资源文件的长度
		int max = conn.getContentLength();
		pd.setMax(max);
		int len = 0;
		int total = 0;
		byte[] buffer = new byte[1024];
		while((len =is.read(buffer))!=-1){
			fos.write(buffer, 0, len);
			total+= len;
			pd.setProgress(total);
		}
		fos.flush();
		fos.close();
		is.close();
	}
	
}
