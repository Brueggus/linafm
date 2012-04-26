package de.fhdw.atpinfo.linafm;

import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Die Pl�ttchen werden innerhalb des Rasters angeordnet
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Br�gmann
 * @version 0.1
 *
 */
public class Raster extends TableLayout {
	
	/**
	 * Enth�lt die Pl�ttchen
	 */
	private Tile[] felder;
	
	/**
	 * Gr��e des Rasters
	 */
	private int size = 0;
	
	/**
	 * Anzahl der Zeilen
	 * !! als Double-Wert !!
	 */
	private final double rows = 2.0;

	public Raster(Context context, int size) {
		super(context);
		
		if (size < 1)
			size = 1;

		felder = new Tile[size];
		this.size = size;
		
//		buildRaster(context);
	}
	
	public Raster(Context context, Tile[] tiles) {
		super(context);
		
		felder = tiles;
		size = felder.length;
		
//		buildRaster(context);
	}
	
	/**
	 * Diese Methode baut aus dem Pl�ttchen-Array das Raster auf
	 * @param context Context
	 */
	public void buildRaster(Context context)
	{
		// Wie viele Pl�ttchen in einer Reihe?
		int columns = (int)Math.ceil( size / rows);
		
		// Insgesamt x Zeilen im Raster
		setWeightSum((float)rows);
		
		// Raster aufbauen und f�llen
		int j = 0;
		for (int i = 0; i < rows; i++)
		{
			// Neue Zeile im Table-Layout
			TableRow tr = new TableRow(context);
	        tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT, // Breite
                    LayoutParams.FILL_PARENT, // H�he
                    1.0f)); // layout_weight
			
	        // Zeile bef�llen
			do
			{
				tr.addView(felder[j]);
			} 
			while ( (++j % columns) != 0 ); // wird false, sobald die Zeile voll ist
			
			// Zeile dem Layout hinzuf�gen
			this.addView(tr);
		}
	}
	
	
	/**
	 * Pr�ft, ob alle Felder des Rasters mit einem Pl�ttchen belegt ist 
	 * @return Raster komplett?
	 */
	public boolean isComplete() {
		for (int i = 0; i < size; i++)
			if (felder[i] == null)
				return false;
		
		return true;
	}
	
	/**
	 * Pr�ft, ob Raster leer ist 
	 * @return Raster leer?
	 */
	public boolean isEmpty() {
		for (int i = 0; i < size; i++)
			if (felder[i] != null)
				return false;
		
		return true;
	}
	
	/**
	 * Pl�ttchen an einer bestimmten Position zum Raster hinzuf�gen. -1 �bergeben, um
	 * Pl�ttchen an der ersten freien Position einzuf�gen
	 * @param tile
	 * @param position
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void addTile(Tile tile, int position) throws ArrayIndexOutOfBoundsException {
		// Befindet sich die Position innerhalb der Arraygrenzen?
		if (position >= size || position < -1)
			throw new ArrayIndexOutOfBoundsException(R.string.ex_insert_tile_out_of_raster);
		else if (position == -1)
			do {
				// Position hochz�hlen, bis ein freies Feld gefunden wird (oder auch nicht...)
				position++;
				if (position >= size)
					throw new ArrayIndexOutOfBoundsException(R.string.ex_no_free_space_in_raster);
			}
			while
				(felder[position] != null);
		
		felder[position] = tile;
	}
	
	/**
	 * Pl�ttchen an einer bestimmten Position l�schen
	 * @param position
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void removeTile(int position) throws ArrayIndexOutOfBoundsException {
		// Befindet sich die Position innerhalb der Arraygrenzen?
		if (position >= size || position < 0)
			throw new ArrayIndexOutOfBoundsException(R.string.ex_insert_tile_out_of_raster);
		
		felder[position] = null;
	}
	
	
}
