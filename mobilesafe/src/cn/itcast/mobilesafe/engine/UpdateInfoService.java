package cn.itcast.mobilesafe.engine;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import cn.itcast.mobilesafe.domain.UpdateInfo;

/**
 * 提供解析 更新xml信息的服务
 * @author Administrator
 *
 */
public class UpdateInfoService {

	public static UpdateInfo getUpdateInfo(InputStream is) throws Exception{
		UpdateInfo updateInfo =  new UpdateInfo();
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		int type = parser.getEventType();
		while(type!=XmlPullParser.END_DOCUMENT){
			switch (type) {
			case XmlPullParser.START_TAG:
				if("version".equals(parser.getName())){
					String version = parser.nextText();
					updateInfo.setVersion(version);
				}else if ("description".equals(parser.getName())){
					String description = parser.nextText();
					updateInfo.setDescription(description);
				}else if ("url".equals(parser.getName())){
					String url = parser.nextText();
					updateInfo.setApkurl(url);
				}
				break;
			}
			type=parser.next();
		}
		return updateInfo;
	}
}
