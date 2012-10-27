package cn.itcast.mobilesafe.test;

import cn.itcast.mobilesafe.db.dao.LockAppDao;
import android.test.AndroidTestCase;

public class TestLockAppDB extends AndroidTestCase {

		public void  testAdd() throws Exception{
			LockAppDao dao = new LockAppDao(getContext());
			dao.add("cn.itcast.lock");
			dao.add("cn.itcast.lock1");
			dao.add("cn.itcast.lock2");
		}
		
		
		public void testDelete() throws Exception{
			LockAppDao dao = new LockAppDao(getContext());
			dao.delete("cn.itcast.lock");
		}
		
		public void testfindAll() throws Exception{
			LockAppDao dao = new LockAppDao(getContext());
			System.out.println( dao.findAll().get(0));
		}
}
