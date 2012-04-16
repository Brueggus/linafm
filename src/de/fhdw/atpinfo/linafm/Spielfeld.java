package de.fhdw.atpinfo.linafm;

import android.graphics.Bitmap;

/**
 * Unser Spielfeld
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Br�gmann
 * @version 0.1
 *
 */
public class Spielfeld {
	
	/**
	 * Das Raster, welches auf dem Spielfeld zu sehen ist
	 */
	private Raster rasterUnten;

	/**
	 * Dieses Raster erscheint im Popup, welches sich nach dem Anw�hlen
	 * eines Pl�ttchens im unteren Raster �ffnet
	 */
	private Raster rasterPopup;
	
	/**
	 * Dieses Bild erscheint oben im Spielfeld
	 */
	private Bitmap img;
	
	/**
	 * Die L�sung des Spiels:
	 * Welches Pl�ttchen geh�rt an welche Position
	 * position --> tile.Id
	 * Beispiel: solution[4] = 2
	 *  --> das Pl�ttchen mit der ID 2 muss am Ende auf Position 4 liegen
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
	 * Popup �ffnen
	 */
	public void showPopup() {
		// NYI
	}
	
	/**
	 * Popup schlie�en
	 */
	public void hidePopup() {
		// NYI
	}

}