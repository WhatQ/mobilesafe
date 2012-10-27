package cn.itcast.mobilesafe.test;

import cn.itcast.mobilesafe.db.dao.BlackNumberDao;
import android.test.AndroidTestCase;

public class TestBlackNumberDB extends AndroidTestCase {
	public void testadd() throws Exception{
		long number =13512345678L;
		BlackNumberDao dao = new BlackNumberDao(getContext());
		for(int i = 0;i<100;i++){
		
		dao.add(number+i+"");
		}
	}
	public void testdelete() throws Exception{
		String number = "110";
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.delete(number);
	}
	public void testupdate() throws Exception{
		String number = "13512345678";
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.update(number, "110");
	}
	public void testfind() throws Exception{
		String number = "112";
		BlackNumberDao dao = new BlackNumberDao(getContext());
		assertEquals(false, dao.find(number)) ;
	}
	public void testfindAll() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		System.out.println( dao.findAll().size());
	}
	
}
