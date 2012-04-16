package de.fhdw.atpinfo.linafm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;


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
		spielfeld = LevelHandler.loadLevel(levelId, context);
		
		setContentView(R.layout.spielfeld);
	}
	
	
	
	

}
