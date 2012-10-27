package cn.itcast.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import cn.itcast.mobilesafe.domain.ContactInfo;

public class ContactInfoService {
	private Context context;

	public ContactInfoService(Context context) {
		this.context = context;
	}

	
	public List<ContactInfo> getContantInfos(){
		List<ContactInfo> infos = new ArrayList<ContactInfo>();
		ContentResolver resolver = context.getContentResolver();
		String base = "content://com.android.contacts/contacts";
		String phonebase ="content://com.android.contacts/data/phones/";
		Cursor cursor = resolver.query(Uri.parse(base), null, null, null, null);
		while(cursor.moveToNext()){
			ContactInfo info = new ContactInfo();
			// 得到联系人的id
			String id = cursor.getString(cursor.getColumnIndex("_id"));
			// 得到联系人的姓名
			String name = cursor.getString(cursor.getColumnIndex("display_name"));
			info.setName(name);
			Uri uri = Uri.parse(phonebase);
			Cursor phonecursor = resolver.query(uri, null, "contact_id=?",new String[]{id}, null);
			while(phonecursor.moveToNext()){
//				String[] names = phonecursor.getColumnNames();
//				for(int i=0;i<names.length;i++){
//					System.out.println(names[i]);
//				}
//              获取所有的 mimetype = 5 的信息  
				String mime = phonecursor.getString(phonecursor.getColumnIndex("mimetype"));
				if("vnd.android.cursor.item/phone_v2".equals(mime)){
				 String number = phonecursor.getString(phonecursor.getColumnIndex("data1"));
				 info.setPhone(number);
				}
			}
			phonecursor.close();
			infos.add(info);
		}
		cursor.close();
		return infos;
	}
}
