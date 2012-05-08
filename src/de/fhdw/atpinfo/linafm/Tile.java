package de.fhdw.atpinfo.linafm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageButton;
import android.widget.TableRow.LayoutParams;

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
		
		// Dummy-Tiles sind leer, die anderen normal
		if ( isDummy() )
			setStateEmpty();
		else
			setStateNormal();
		
		setImageBitmap(front);

		// Breite auf 0 Pixel festgesetzt, da anderenfalls die Buttons nicht
		// gleichmäßig in der Zeile verteilt werden.
		LayoutParams params = new LayoutParams(
                0, // width
                LayoutParams.MATCH_PARENT, // height 
                1.0f); // layout_weight
		
		// Rahmen, ansonsten kleben die alle aneinander
		params.setMargins(3, 3, 3, 3);
		
		setScaleType(ScaleType.FIT_CENTER);
		setLayoutParams(params);

	}
	
	/**
	 * @return the id
	 */
	public int getTileId() {
		return id;
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
	 * Ermittelt, ob das Plättchen ein Dummy ist
	 * @return true, falls Dummy (also Feld leer)
	 */
	public boolean isDummy() {
		if ( id == -1 )
			return true;
		else
			return false;
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
	
	/**
	 * Setze einen leeren Status für das Tile.
	 */
	public void setStateEmpty() {
		setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_empty));
	}
	
	/**
	 * Versetze das Tile in den Ausgangsstatus.
	 */
	public void setStateNormal() {
		setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_blue));
		
	}

	/**
	 * Markiere das Tile als ausgewählt.
	 */
	public void setStateSelected() {
		setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_green));
	}
	
	public void setNumeralImage(int numeral) {
		// Alle unsere Bilder... (die 0 bleibt außen vor)
		final int[] images = {
	    		/*R.drawable.tile_0,*/ R.drawable.new_tile_1, R.drawable.new_tile_2,
	    		R.drawable.new_tile_3, R.drawable.new_tile_4, R.drawable.new_tile_5,
	    		R.drawable.new_tile_6, R.drawable.new_tile_7, R.drawable.new_tile_8,
	    		R.drawable.new_tile_9, R.drawable.new_tile_10, R.drawable.new_tile_11, 
	    		R.drawable.new_tile_12
	    };
		
		Bitmap bitmap = null;
	    
	    // Wir haben nur 1 - 12 im Angebot
	    if (numeral < 0 || numeral > 11)
	    	front = null;
	    
	    try {
	        BitmapDrawable drawable = (BitmapDrawable)getContext().getResources().getDrawable(images[numeral]);
	        bitmap = drawable.getBitmap();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    setImageBitmap(bitmap);
	}
}
