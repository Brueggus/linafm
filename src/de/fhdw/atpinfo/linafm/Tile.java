package de.fhdw.atpinfo.linafm;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Klasse Plättchen, abgeleitet vom ImageButton
 * Die werden wir später hin und her drehen!
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Brügmann
 * @version 0.1
 *
 */
public class Tile extends ImageButton {
	
	/**
	 * Id des Plättchens: Wird pro Spiel von 0 bis x vergeben
	 */
	private int id;
	
	/**
	 * Bild für die Vorderseite
	 */
	private Bitmap front;
	
	/**
	 * Bild für die Rückseite
	 */
	private Bitmap back;
	
	/**
	 * Ist das Plättchen umgedreht (Rückseite oben?)
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
	 * Plättchen gedreht?
	 * @return true, falls Plättchen umgedreht
	 */
	public boolean isTurned() {
		return turned;
	}

	/**
	 * Plättchen umdrehen
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
	 * Methode um das Bild eines Plättchens zu ändern
	 * 
	 * @param img Bild
	 * @param back true setzen um Rückseite zu ändern
	 */
	public void setImage(Bitmap img, boolean back) {
		if (!back)
			this.front = img;
		else
			this.back = img;
	}
	
	

}
