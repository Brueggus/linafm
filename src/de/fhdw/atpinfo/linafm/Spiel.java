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
		
		mBtnPopup = (Button) findViewById(R.id.btnPopup);
		mBtnPopup.setOnClickListener(this);
	}
	
	
	/**
	 * Popup öffnen
	 * 
	 * Methode zum Popup wird geöffnet
	 */
	private void showPopup() {
		// Popup schon offen?
		if ( spielfeld.getPopupOpen() )
			return;
		
        // Neuen Dialog initialisieren
		final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup);
        dialog.setTitle(R.string.popup);
        dialog.setCancelable(true);
        
        FrameLayout fl = (FrameLayout) dialog.findViewById(R.id.popup);
        Raster popUpRaster = spielfeld.getRasterPopup();
        popUpRaster.removeAllViews();
        
        // hier fehlt die Überprüfung, ob Raster bereits schonmal gemalt wurde.
        // dies ist der Fall, wenn das Popup schonmal geöffnet wurde
        // derzeit -> Exception
        
        // tableLayout wird noch nicht angezeigt, warum?
        popUpRaster.buildRaster(dialog.getContext());

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
				showPopup();
				break;
		}
	}
		
	public void onTileClick(Tile v)
	{
		// Dummy-Tiles sollen nicht klickbar sein
		if ( v.getTileId() > -1 )
			return;
		// befindet sich unser Plättchen im unteren Raster?
		else if ( ((View)v.getParent()).getId() == R.id.rasterUnten ) {
			showPopup();
		}
	}

}
