package de.fhdw.atpinfo.linafm;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;


/**
 * Diese Klasse beinhaltet alles, was zum aktuellen Spiel gehört.
 * Sie wird instantiiert, sobald ein neues Spiel gestartet wird
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Brügmann
 * @version 0.1
 *
 */
public class Spiel extends Activity {
	
	private Spielfeld spielfeld;
	private Context context;
	private int levelId;


	/**
	 * Wird aufgerufen, sobald ein neues Spiel erstellt wird
	 * 
	 * @param savedInstanceState
	 * @param spielfeld
	 */
	protected void onCreate(Bundle savedInstanceState) {
		context = Spiel.this;
		super.onCreate(savedInstanceState);
		
		// die Level-ID steckt in den Extras
		Bundle b = getIntent().getExtras();
		levelId = b.getInt("levelId");

		// Level laden
		try {
			spielfeld = LevelHandler.loadLevel(levelId, context);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Spielfeld anzeigen
		setContentView(R.layout.spielfeld);
		
		// Bild oben
		ImageView image = (ImageView)findViewById(R.id.imgOben);
		image.setImageBitmap(spielfeld.getImg());
		
		// Raster unten
		FrameLayout frame = (FrameLayout)findViewById(R.id.tilesUnten);
		frame.addView(spielfeld.getRasterUnten());
	}
	
	
	
	

}
