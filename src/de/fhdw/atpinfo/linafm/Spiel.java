package de.fhdw.atpinfo.linafm;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import de.fhdw.atpinfo.linafm.Tile.TileState;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Diese Klasse beinhaltet alles, was zum aktuellen Spiel gehört.
 * Sie wird instanziiert, sobald ein neues Spiel gestartet wird
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Brügmann
 * @version 0.1
 *
 */
public class Spiel extends Activity implements OnClickListener {

	/**
	 * Unser Spielfeld
	 */
	private Spielfeld spielfeld;
	
	/**
	 * Der Context
	 */
	private Context context;
	
	/**
	 * Die ID des aktuellen Levels
	 */
	private int levelId;
	
	/**
	 * Der Button zum Öffnen des Popups
	 */
	private Button mBtnPopup;
	
	/**
	 * Der Popup-Dialog
	 */
	private Dialog mDlgPopup;
	
	/**
	 * Das aktuell zum Tausch ausgewählte Plättchen
	 */
	private Tile activeTile = null;
	

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
		Raster raster = spielfeld.getRasterUnten();
		raster.setOnClickListenerForAllTiles(this);
		frame.addView(raster);
		
		// auch Plättchen im Popup sollen klickbar sein
		raster = spielfeld.getRasterPopup();
		raster.setOnClickListenerForAllTiles(this);
		
		// Popup generieren
		mDlgPopup = drawPopupDialog();
		
		mBtnPopup = (Button) findViewById(R.id.btnPopup);
		mBtnPopup.setOnClickListener(this);
	}
	
	
	/**
	 * Popup generieren
	 */
	private Dialog drawPopupDialog() {
        // Neuen Dialog initialisieren
		final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup);
        dialog.setTitle(R.string.popup);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        
        FrameLayout fl = (FrameLayout) dialog.findViewById(R.id.popup);
        Raster popUpRaster = spielfeld.getRasterPopup();
        // Raster zum FrameLayout hinzufügen
        fl.addView(popUpRaster);
        
        // Abbrechen-Button
        Button button = (Button) dialog.findViewById(R.id.btnAbbruch);
        button.setOnClickListener(this);
        
        // Tipp-Button
        button = (Button) dialog.findViewById(R.id.btnTipp);
        button.setOnClickListener(this);
        
        return dialog;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnPopup:
				if( mDlgPopup.isShowing() ) {
					hidePopup();
				} else {
					showPopup();
				}
				break;
			case R.id.btnAbbruch:
				mDlgPopup.cancel();
				break;
			case R.id.btnTipp:
				spielfeld.tipp(context);
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
		// Falls wir kein Dummy sind und noch kein Plättchen zum Tausch ausgewählt wurde...
		if ( !v.isDummy() && activeTile == null ) {
			// ... dann wählen wir uns selbst aus
			activeTile = v;
			v.setState(TileState.SELECTED);
			
			// befindet sich unser Plättchen im unteren Raster?
			// ( getParent():  Tile --> TableRow --> Raster )
			if ( ((View)v.getParent().getParent()).getId() == R.id.rasterUnten ) {
				showPopup();
			}
		}
		else if ( activeTile != null ) {
			// Wurde ein anderes Plättchen gedrückt?
			if ( v != activeTile )
			{
				v.setState(TileState.SELECTED);
				
				// Raster der zwei Tauschpartner
				Raster srcRaster = (Raster) activeTile.getParent().getParent();
				Raster destRaster = (Raster) v.getParent().getParent();
				
				// Position der zwei Plättchen im Raster
				int srcPos = srcRaster.getTilePosition(activeTile);
				int destPos = destRaster.getTilePosition(v);
				
				// Plättchen entfernen...
				// keine Aktualisierung der Ansicht, da dies eh gleich durch das Hinzufügen 
				// neuer Plättchen geschieht
				srcRaster.removeTile(srcPos);
				destRaster.removeTile(destPos);
				
				// ...und umgekehrt wieder einfügen.
				srcRaster.addTile(v, srcPos);
				destRaster.addTile(activeTile, destPos);
				
				// Status wieder normal setzen
				if ( v.isDummy() )
					v.setState(TileState.EMPTY);
				else
					v.setState(TileState.NORMAL);
			}
			
			// kein aktives Plättchen mehr
			activeTile.setState(TileState.NORMAL);
			activeTile = null;
		}
		
		

	}
	
	/**
	 * Popup schließen
	 */
	public void hidePopup() {
			mDlgPopup.hide();
	}
	
	/**
	 * Popup öffnen
	 */
	public void showPopup() {
		// Popup schon offen?
		if ( mDlgPopup.isShowing() )
			return;
		
		// Achtung, Pfusch! Das geht bestimmt auch irgendwie schöner...
		// Größe des Popups an das untere Raster angleichen
		FrameLayout fl = (FrameLayout) mDlgPopup.findViewById(R.id.popup); 
		// Höhe des Containers für das Popup-Raster setzen
		fl.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 
				spielfeld.getRasterUnten().getHeight() // Höhe unteres Raster
		));
		// Breite des Popup-Fensters setzen
		mDlgPopup.getWindow().setLayout(
				spielfeld.getRasterUnten().getWidth(), // Breite unteres Raster
				LayoutParams.WRAP_CONTENT
		);
		// -- Pfusch Ende --
		
		mDlgPopup.show();
	}
}
