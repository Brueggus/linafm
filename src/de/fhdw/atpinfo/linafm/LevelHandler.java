package de.fhdw.atpinfo.linafm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
	 * Dateiname des großen Bildes (oben auf dem Spielfeld)
	 * muss ebenfalls immer gleich sein!
	 */
	private static final String LEVEL_IMG_FILE = "image.jpg";
	
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
	 * Liefert den Namen des ausgewählten Levels
	 * 
	 * @param id int (id des ausgwählten Levels)
	 * @return String, welcher den Namen des Level enthält
	 */
	public static String getLevelById(int id) {
		String levelName = captions[id];
		return levelName;			
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
		
		captions = new String[levelCount];
		
		// XML-Parser initialisieren
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		int eventType;
		
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

			in.close();
		}
	}
	
	/**
	 * Shufflefunktion, um Bildplättchen zufällig zu mischen
	 * @param unshuffledTiles
	 * @return durchgemischtes Tile-Array
	 */
	public static Tile[] shuffleTiles(Tile[] unshuffledTiles) {
 
		List<Tile> hilfsliste = Arrays.asList(unshuffledTiles);
		
		Random rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		
		// Shuffle the elements in the array
		Collections.shuffle(hilfsliste);
		
		return (Tile[]) hilfsliste.toArray();
		
	}
	
	/**
	 * Bestimmtes Level laden
	 * 
	 * @param id ID des Levels
	 * @param context ...damit wir an die assets drankommen
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public static Spielfeld loadLevel(int id, Context context) throws XmlPullParserException, IOException
	{
		// Diese Variablen möchten wir belegen
		String name = "n/a";
		Bitmap imageOben;
		int[] solution = null;
		Tile[] tilesUnten = null;
		
		// AssetManager zum Zugriff auf den asset-Ordner initialisieren		
		AssetManager assetMgr = context.getAssets();
		String levelDir = LEVEL_SUBDIR + "/" + levels[id] + "/";
		
		// XML-Parser initialisieren
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		int eventType;
		
		// Hilfsvariablen
		String tmpFilename;
		InputStream imageLoader;
		Bitmap tileImage;
		int currTile = 0, totalTiles = 0, finalPos;
		
		// wir öffnen die XML-Datei des gewünschten Levels
		InputStream in = assetMgr.open(levelDir + LEVEL_DESC_FILE);
		parser.setInput(in, "UTF-8");
		
		// Der XmlPullParser geht von oben nach unten durch die XML-Datei.
		// Trifft er z.B. auf ein öffnendes Tag, hält er an und gibt ein Event aus
		eventType = parser.getEventType();
		
		while (eventType != XmlPullParser.END_DOCUMENT) {
			// Sind wir auf ein öffnendes Tag mit dem Namen "level" gestoßen?
			if (eventType == XmlPullParser.START_TAG)
			{
				// Namen des Levels auslesen
				if (parser.getName().equalsIgnoreCase("level"))
					name = parser.getAttributeValue(null, "name");
				// Plättchen laden + erstellen
				else if (parser.getName().equalsIgnoreCase("tiles"))
				{
					totalTiles = Integer.parseInt(parser.getAttributeValue(null, "total"));
					// jetzt wissen wir auch, wie groß unsere Arrays sein müssen
					// (1 Feld pro Plättchen)
					solution = new int[totalTiles];
					tilesUnten = new Tile[totalTiles];
					
				}
				else if (parser.getName().equalsIgnoreCase("tile"))
				{
					// Vorderseite
					tmpFilename = parser.getAttributeValue(null, "image");
					imageLoader = assetMgr.open(levelDir + tmpFilename);
					tileImage = BitmapFactory.decodeStream(imageLoader);
					
					// Plättchen erstellen
					tilesUnten[currTile] = new Tile(context, currTile, tileImage);
					
					// Eintrag im Lösungsarray
					finalPos = Integer.parseInt(parser.getAttributeValue(null, "final_pos"));
					solution[finalPos] = currTile;
					
					currTile++;
				}
				
			}
			
			eventType = parser.next();
		}
		
		// Aufräumen
		in.close();		
		tmpFilename = null;
		tileImage = null;		
		
		//shuffle-Aufruf ab hier möglich!
		shuffleTiles(tilesUnten);
		
		// Array für das Popup-Raster mit Dummy-Tiles
		Tile[] tilesPopup = new Tile[totalTiles];
		
		for (int i = 0; i < totalTiles; i++)
		{
			tilesPopup[i] = new Tile(context, -1, null);
			tilesPopup[i].setNumeralImage(i);
		}
		
		// Raster befüllen
		Raster rasterUnten = new Raster(context, tilesUnten);
		rasterUnten.setId(R.id.rasterUnten);
		Raster rasterPopup = new Raster(context, tilesPopup);
		rasterPopup.setId(R.id.rasterPopup);
		
		// Bild für oben
		imageLoader = assetMgr.open(levelDir + LEVEL_IMG_FILE);
		imageOben = BitmapFactory.decodeStream(imageLoader);
		// jetzt hat er ausgedient...
		imageLoader = null;
		
		// Spielfeld erstellen + befüllen
		Spielfeld result = new Spielfeld(rasterUnten, rasterPopup, imageOben, solution, name);

		return result;
	}
}
