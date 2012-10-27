package cn.itcast.mobilesafe.test;

import cn.itcast.mobilesafe.engine.ContactInfoService;
import android.test.AndroidTestCase;

public class TestContactInfoService extends AndroidTestCase {
   public void testGetContacts() throws Exception{
	    ContactInfoService service = new ContactInfoService(getContext());
	    service.getContantInfos();
   }
}
