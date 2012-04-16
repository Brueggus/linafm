package de.fhdw.atpinfo.linafm;

import android.app.Activity;
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


	/**
	 * Wird aufgerufen, sobald ein neues Spiel erstellt wird
	 * 
	 * @param savedInstanceState
	 * @param spielfeld
	 */
	protected void onCreate(Bundle savedInstanceState, Spielfeld spielfeld) {
		super.onCreate(savedInstanceState);
		this.spielfeld = spielfeld;
		
		setContentView(R.layout.spielfeld);
	}
	
	
	
	

}
