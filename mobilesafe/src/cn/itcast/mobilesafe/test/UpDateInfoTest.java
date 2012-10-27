package cn.itcast.mobilesafe.test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.itcast.mobilesafe.domain.UpdateInfo;
import cn.itcast.mobilesafe.engine.UpdateInfoService;
import android.test.AndroidTestCase;

public class UpDateInfoTest extends AndroidTestCase {

	public void testUpdateInfo() throws Exception{
		URL url = new URL("http://192.168.1.247:8080/update.xml");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		InputStream is = conn.getInputStream();
		UpdateInfo info = UpdateInfoService.getUpdateInfo(is);
		assertEquals("2.0", info.getVersion());
	}
	
}
