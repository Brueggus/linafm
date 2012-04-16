package de.fhdw.atpinfo.linafm;

import android.graphics.Bitmap;

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

	public Spielfeld(Raster rUnten, Raster rPopup, Bitmap img, int[] solution, String name) {
		rasterUnten = rUnten;
		rasterPopup = rPopup;
		this.img = img;
		this.solution = solution;
		this.name = name;
	}
	
	/**
	 * Popup öffnen
	 */
	public void showPopup() {
		// NYI
	}
	
	/**
	 * Popup schließen
	 */
	public void hidePopup() {
		// NYI
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

}
