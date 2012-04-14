package de.fhdw.atpinfo.linafm;

import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * Klasse zum Auslesen der Leveldaten aus den XML-Dateien
 * Die Levels werden unter /assets/levels abgelegt!
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Brügmann
 * @version 0.1
 *
 */
public class LevelHandler {
	/**
	 * Name des Levelverzeichnisses unter /assets
	 */
	private static final String LEVEL_SUBDIR = "levels";

	/**
	 * XML-Datei zu jedem Level. Diese muss immer den gleiche Namen haben!
	 */
	private static final String LEVEL_DESC_FILE = "desc.xml";
	
	/**
	 * Ordnername aller Level (xyz ==> /assets/levels/xyz)
	 */
	private static String[] levels = null;
	
	/**
	 * Titel aller Level (aus XML-Datei ausgelesen)
	 */
	private static String[] captions = null;
	
	/**
	 * Anzahl der vorhandenen Level
	 */
	private static int levelCount;

	/**
	 * Liefert die Namen aller Level für das Auswahlmenü
	 * 
	 * @param context Context (wird ggf. für das Neu-Einlesen der Levelliste benötigt)
	 * @return String-Array, welches die Namen der Level enthält
	 * @throws IOException  Von refreshLevelCaptions() durchgereicht (s.u.)
	 * @throws XmlPullParserException s.u. Von refreshLevelCaptions() durchgereicht (s.u.)
	 */
	public static String[] getLevelCaptionsForMenu(Context context) 
			throws IOException, XmlPullParserException
	{
		// Levelliste bereits initialisiert? Sollte eigentlich immer der Fall sein...
		if (captions == null || levels == null)
			refreshLevelCaptions(context);
		
		return captions;
	}
	
	/**
	 * Levelliste neu aus XML-Dateien aufbauen
	 * 
	 * @param context Context, wird benötigt, um auf die assets zugreifen zu können
	 * @throws IOException z.B. Datei konnte nicht geöffnet/gelesen werden
	 * @throws XmlPullParserException Fehler beim Parsen der XML-Datei
	 */
	public static void refreshLevelCaptions(Context context)
			throws IOException, XmlPullParserException
	{
		// AssetManager zum Zugriff auf den asset-Ordner initialisieren
		AssetManager assetMgr = context.getAssets();
		
		// Eine Liste aller Verzeichnisse unter /assets/levels und deren Anzahl
		levels = assetMgr.list(LEVEL_SUBDIR);
		levelCount = levels.length;
		
		int eventType;
		captions = new String[levelCount];
		
		// XML-Parser initialisieren
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		
		for (int i = 0; i < levelCount; i++)
		{
			// für jedes Level wird die jeweilige XML-Datei ausgelesen
			InputStream in = assetMgr.open(LEVEL_SUBDIR + "/" + levels[i] + "/" + LEVEL_DESC_FILE);
			parser.setInput(in, "UTF-8");

			// Der XmlPullParser geht von oben nach unten durch die XML-Datei.
			// Trifft er z.B. auf ein öffnendes Tag, hält er an und gibt ein Event aus
			eventType = parser.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// Sind wir auf ein öffnendes Tag mit dem Namen "level" gestoßen?
				if (eventType == XmlPullParser.START_TAG && parser.getName().equalsIgnoreCase("level"))
				{
					// Name des Levels auslesen.
					// Da wir alles haben, was wir wollen: Abbruch der Schleife
					captions[i] = parser.getAttributeValue(null, "name");
					break;
				}
				
				eventType = parser.next();
			}
		}
	}
	
	/**
	 * Bestimmtes Level laden
	 * 
	 * @param id ID des Levels
	 * @param context ...damit wir an die assets drankommen
	 */
	private void loadLevel(int id, Context context)
	{
		// NYI
	}
		
}
