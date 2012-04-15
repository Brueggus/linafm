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
	 * Ist das Popup gerade aktiv?
	 */
	private boolean popupOpen = false;

	public Spielfeld(Raster rUnten, Raster rPopup, Bitmap img) {
		// TODO Auto-generated constructor stub
	}
	
	public void showPopup() {
		// NYI
	}
	
	public void hidePopup() {
		// NYI
	}

}
