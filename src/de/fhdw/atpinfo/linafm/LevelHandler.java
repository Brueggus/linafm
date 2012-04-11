package de.fhdw.atpinfo.linafm;

import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.AssetManager;

public class LevelHandler {
	private static final String LEVEL_SUBDIR = "levels";
	private static final String LEVEL_DESC_FILE = "desc.xml";

	public static String[] getLevelCaptionsForMenu(Context context) 
			throws IOException, XmlPullParserException
	{
		AssetManager assetMgr = context.getAssets();
		String[] levels = assetMgr.list(LEVEL_SUBDIR);
		int levelCount = levels.length, eventType;
		String[] captions = new String[levelCount];
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		
		for (int i = 0; i < levelCount; i++)
		{
			InputStream in = assetMgr.open(LEVEL_SUBDIR + "/" + levels[i] + "/" + LEVEL_DESC_FILE);
			parser.setInput(in, "UTF-8");
			
			eventType = parser.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG && parser.getName().equalsIgnoreCase("level"))
				{
					captions[i] = parser.getAttributeValue(null, "name");
					break;
				}
				eventType = parser.next();
					
			}
		}
		
		return captions;
	}
	
	private void getLevelFromXML(Context context)
	{
		// NYI
	}
		
}
