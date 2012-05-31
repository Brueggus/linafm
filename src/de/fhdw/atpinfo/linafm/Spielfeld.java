package de.fhdw.atpinfo.linafm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.fhdw.atpinfo.linafm.Tile.TileState;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Unser Spielfeld
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Brügmann
 * @version 0.1
 *
 */
public class Spielfeld {

	/**
	 * Das Raster, welches auf dem Spielfeld zu sehen ist
	 */
	private Raster rasterUnten;

	/**
	 * Dieses Raster erscheint im Popup, welches sich nach dem Anwählen
	 * eines Plättchens im unteren Raster öffnet
	 */
	private Raster rasterPopup;
	
	/**
	 * Dieses Bild erscheint oben im Spielfeld
	 */
	private Bitmap img;
	
	/**
	 * Die Lösung des Spiels:
	 * Welches Plättchen gehört an welche Position
	 * position --> tile.Id
	 * Beispiel: solution[4] = 2
	 *  --> das Plättchen mit der ID 2 muss am Ende auf Position 4 liegen
	 */
	private int[] solution;
	
	/**
	 * Der Name des Levels
	 */
	private String name;
	
	/**
	 * Ist das Popup gerade aktiv?
	 */
	private boolean popupOpen = false;
	
	/**
	 * Ein Zufallsgenerator für die Tipps
	 */
	Random generator = new Random();

	public Spielfeld(Raster rUnten, Raster rPopup, Bitmap img, int[] solution, String name) {
		rasterUnten = rUnten;
		rasterPopup = rPopup;
		this.img = img;
		this.solution = solution;
		this.name = name;
	}
	
	/**
	 * @return the img
	 */
	public Bitmap getImg() {
		return img;
	}

	/**
	 * @return the rasterUnten
	 */
	public Raster getRasterUnten() {
		return rasterUnten;
	}

	/**
	 * @return the rasterPopup
	 */
	public Raster getRasterPopup() {
		return rasterPopup;
	}
	
	/**
	 * @return true, falls Popup geöffnet ist
	 */
	public boolean getPopupOpen()
	{
		return popupOpen;
	}
	
	/**
	 * Prüft, ob alle Plätchen im Popup auf der richtigen Position liegen
	 * @return true, wenn Level korrekt gelöst wurde
	 */
	private boolean vaildate() {
		if ( !rasterPopup.isComplete() )
			return false;
		
	return Arrays.equals(solution, rasterPopup.getTileIDs());
	}
	
	
	/**
	 * Tipp geben: Ein falsch liegendes Plättchen im Popup wird hervorgehoben
	 */
	public void tipp(Context context) {
		// Ein Moment dauert 1,5 Sekunden
		final long MOMENT = 1500;
		
		// liegen überhaupt schon Plättchen im Popup?
		if ( rasterPopup.isEmpty() ) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
			View toast_layout = inflater.inflate(R.layout.toastlayout, null);
			
			ImageView image = (ImageView) toast_layout.findViewById(R.id.toastimage);
			image.setImageResource(R.drawable.toast);
			
			TextView text = (TextView) toast_layout.findViewById(R.id.toasttext);
			text.setText(R.string.tipp_raster_empty);
			
			Toast t = new Toast(context);
			// mittig positionieren
			t.setGravity(Gravity.CENTER, 0, 0);
			t.setDuration(Toast.LENGTH_LONG);
			t.setView(toast_layout);
			t.show();
		}
		else {
			// falsch abgelegte Plättchen
			List<Tile> wrongTiles = new ArrayList<Tile>();
			
			// alle Plättchen im Raster
			Tile[] allTiles = rasterPopup.getTiles();
			
			for ( int i = 0; i < allTiles.length; i++ ) {
				// ist das Feld leer? Oder liegt das Plättchen richtig?
				if ( allTiles[i].isDummy() || allTiles[i].getTileId() == solution[i] )
					continue;
				// ansonsten: Zur Liste der falschen Plättchen hinzufügen
				else
					wrongTiles.add(allTiles[i]);
			}
			
			// Gibt's überhaupt falsche Plättchen?
			if ( wrongTiles.isEmpty() ) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
				View toast_layout = inflater.inflate(R.layout.toastlayout, null);
				
				ImageView image = (ImageView) toast_layout.findViewById(R.id.toastimage);
				image.setImageResource(R.drawable.toast);
				
				TextView text = (TextView) toast_layout.findViewById(R.id.toasttext);
				text.setText(R.string.tipp_no_errors);
				
				Toast t = new Toast(context);
				// mittig positionieren
				t.setGravity(Gravity.CENTER, 0, 0);
				t.setDuration(Toast.LENGTH_LONG);
				t.setView(toast_layout);
				t.show();
			}
			else {
				// nun suchen wir uns zufällig ein Plättchen aus...
				int rndIndex = generator.nextInt(wrongTiles.size());
				Tile hlTile = wrongTiles.get(rndIndex);
				
				// ...und heben es hervor...
 				hlTile.setState(TileState.HIGHLIGHTED);
				
				// ...und setzen es nach einem Moment wieder auf Normalzustand zurück
 				// Da dies zeitverzögert geschehen soll, erstellen wir eine neue Klasse "TileResetter",
 				// die das Zurücksetzen für uns übernimmt.
 				class TileResetter implements Runnable {
 						
 						Tile tile;
 						
 						TileResetter( Tile t ) {
 							tile = t;
 						}

 						@Override
 						public void run() {
 							tile.setState(TileState.NORMAL);
 						}
 				}
 				
 				// Der Handler kümmert sich dann darum.
				Handler myHandler = new Handler();
				TileResetter myTileResetter = new TileResetter(hlTile);
				
				// Einen Moment warten, dann den TileResetter anstoßen
				myHandler.postDelayed(myTileResetter, MOMENT);
			}
			
		}		
	}
}
