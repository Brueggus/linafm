package de.fhdw.atpinfo.linafm;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Klasse Pl�ttchen, abgeleitet vom ImageButton
 * Die werden wir sp�ter hin und her drehen!
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Br�gmann
 * @version 0.1
 *
 */
public class Tile extends ImageButton {
	
	/**
	 * Id des Pl�ttchens: Wird pro Spiel von 0 bis x vergeben
	 */
	private int id;
	
	/**
	 * Bild f�r die Vorderseite
	 */
	private Bitmap front;
	
	/**
	 * Bild f�r die R�ckseite
	 */
	private Bitmap back;
	
	/**
	 * Ist das Pl�ttchen umgedreht (R�ckseite oben?)
	 */
	private boolean turned = false;

	public Tile(Context context, int id, Bitmap front, Bitmap back) {
		super(context);
		this.id = id;
		this.front = front;
		this.back = back;
		
		setImageBitmap(front);
	}
	
	/**
	 * Pl�ttchen gedreht?
	 * @return true, falls Pl�ttchen umgedreht
	 */
	public boolean isTurned() {
		return turned;
	}

	/**
	 * Pl�ttchen umdrehen
	 */
	public void turnAround()
	{
		if (turned)
			setImageBitmap(front);
		else
			setImageBitmap(back);
		
		turned = !turned;
	}

	/**
	 * Methode um das Bild eines Pl�ttchens zu �ndern
	 * 
	 * @param img Bild
	 * @param back true setzen um R�ckseite zu �ndern
	 */
	public void setImage(Bitmap img, boolean back) {
		if (!back)
			this.front = img;
		else
			this.back = img;
	}
	
	

}
