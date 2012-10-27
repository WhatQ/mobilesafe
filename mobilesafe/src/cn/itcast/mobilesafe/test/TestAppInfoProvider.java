package cn.itcast.mobilesafe.test;

import cn.itcast.mobilesafe.engine.AppInfoProvider;
import android.test.AndroidTestCase;

public class TestAppInfoProvider extends AndroidTestCase {

	public void testGetApps() throws Exception{
		AppInfoProvider provider = new AppInfoProvider(getContext());
		provider.getAllAppInfos();
	}
}
