package de.fhdw.atpinfo.linafm;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;


/**
 * Diese Klasse beinhaltet alles, was zum aktuellen Spiel gehört.
 * Sie wird instanziiert, sobald ein neues Spiel gestartet wird
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Brügmann
 * @version 0.1
 *
 */
public class Spiel extends Activity implements OnClickListener {
	
	private Spielfeld spielfeld;
	private Context context;
	private int levelId;
	
	private Button mBtnPopup;
	private Dialog dialog = null;
	
	/**
	 * Ist das Popup gerade aktiv?
	 */
	private boolean popupOpen = false;


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
		
		Raster rUnten = spielfeld.getRasterUnten();
		rUnten.setOnClickListenerForAllTiles(this);
		frame.addView(rUnten);
		
		mBtnPopup = (Button) findViewById(R.id.btnPopup);
		mBtnPopup.setOnClickListener(this);
		
		
	}
	
	
	/**
	 * Popup öffnen
	 * 
	 * Methode zum Popup wird geöffnet
	 */
	private void initPopupDialog() {
		// Popup schon offen?
		if ( popupOpen )
			return;
		
        // Neuen Dialog initialisieren
		dialog = new Dialog(context);
		
        dialog.setContentView(R.layout.popup);
        dialog.setTitle(R.string.popup);
        dialog.setCancelable(true);
        
        FrameLayout fl = (FrameLayout) dialog.findViewById(R.id.popup);
        Raster popUpRaster = spielfeld.getRasterPopup();

        // tableLayout wird noch nicht angezeigt, warum?
        popUpRaster.buildRaster(dialog.getContext());

        fl.addView(popUpRaster);
        
        // Abbrechen-Button
        Button button = (Button) dialog.findViewById(R.id.btnAbbruch);
        button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        
        dialog.show();
   
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnPopup:
				if(popupOpen) {
					hidePopup();
				} else {
					showPopup();
				}
				break;
		}
		
		// Wurde ein Plättchen geklickt?
		if ( v instanceof Tile )
			onTileClick( (Tile)v );
	}

	/**
	 * Wird beim Klicken eines Plättchens aufgerufen
	 * @param v das geklickte Plättchen
	 */
	private void onTileClick(Tile v) {
		// befindet sich unser Plättchen im unteren Raster?
		// ( getParent():  Tile --> TableRow --> Raster ),
		// Dummy-Tiles sollen nicht klickbar sein
		if ( ((View)v.getParent().getParent()).getId() == R.id.rasterUnten  || v.getTileId() != -1 ) {
			showPopup();
		}
	}
	
	/**
	 * Popup schließen
	 */
	public void hidePopup() {
			dialog.hide();
			popupOpen = false;
	}
	
	/**
	 * Popup öffnen
	 * beim erstmaligen Aufruf wird das Popup über drapPopupDialog() initialisiert
	 */
	public void showPopup() {
		
		// einmalige Popup-Initialisierung
		if(dialog == null) {
			initPopupDialog();
		} else {
			dialog.show();
			popupOpen = true;
		}
	}

}
