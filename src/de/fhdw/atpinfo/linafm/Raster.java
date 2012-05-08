package de.fhdw.atpinfo.linafm;

import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Die Plättchen werden innerhalb des Rasters angeordnet
 * 
 * @author Esther Hentrich, Daniel Philipp, Alexander Brügmann
 * @version 0.1
 *
 */
public class Raster extends TableLayout {
	
	/**
	 * Enthält die Plättchen
	 */
	private Tile[] felder;
	
	/**
	 * Größe des Rasters
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
		
		buildRaster(context);
	}
	
	public Raster(Context context, Tile[] tiles) {
		super(context);
		
		felder = tiles;
		size = felder.length;
		
		buildRaster(context);
	}
	
	/**
	 * Diese Methode baut aus dem Plättchen-Array das Raster auf
	 * @param context Context
	 */
	public void buildRaster(Context context)
	{
		// Wie viele Plättchen in einer Reihe?
		int columns = (int)Math.ceil( size / rows);
		
		// Insgesamt x Zeilen im Raster
		setWeightSum((float)rows);
		
		// Raster aufbauen und füllen
		int j = 0;
		for (int i = 0; i < rows; i++)
		{
			// Neue Zeile im Table-Layout
			TableRow tr = new TableRow(context);
	        tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT, // Breite
                    LayoutParams.FILL_PARENT, // Höhe
                    1.0f)); // layout_weight
			
	        // Zeile befüllen
			do
			{
				tr.addView(felder[j]);
			} 
			while ( (++j % columns) != 0 ); // wird false, sobald die Zeile voll ist
			
			// Zeile dem Layout hinzufügen
			this.addView(tr);
		}
	}
	
	
	/**
	 * Prüft, ob alle Felder des Rasters mit einem Plättchen belegt ist 
	 * @return Raster komplett?
	 */
	public boolean isComplete() {
		for (int i = 0; i < size; i++)
			if ( felder[i].isDummy() )
				return false;
		
		return true;
	}
	
	/**
	 * Prüft, ob Raster leer (also nur mit Dummy-Tiles gefüllt) ist 
	 * @return Raster leer?
	 */
	public boolean isEmpty() {
		for (int i = 0; i < size; i++)
			if ( !felder[i].isDummy() )
				return false;
		
		return true;
	}
	
	/**
	 * Plättchen an einer bestimmten Position zum Raster hinzufügen. -1 übergeben, um
	 * Plättchen an der ersten freien Position einzufügen
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
				// Position hochzählen, bis ein freies Feld gefunden wird (oder auch nicht...)
				position++;
				if (position >= size)
					throw new ArrayIndexOutOfBoundsException(R.string.ex_no_free_space_in_raster);
			}
			while
				(felder[position] != null);
		
		felder[position] = tile;
		
		
	}
	
	/**
	 * Plättchen an einer bestimmten Position löschen
	 * @param position
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void removeTile(int position) throws ArrayIndexOutOfBoundsException {
		// Befindet sich die Position innerhalb der Arraygrenzen?
		if (position >= size || position < 0)
			throw new ArrayIndexOutOfBoundsException(R.string.ex_insert_tile_out_of_raster);
		
		felder[position] = null;
	}
	
	/**
	 * Liefert die IDs aller Plättchen im Raster zurück
	 * [position] --> id
	 * @return Array mit allen IDs
	 */
	public int[] getTileIDs() {
		int[] result = new int[size];
		
		for (int i = 0; i < size; i++)
			result[i] = felder[i].getTileId();
		
		return result;
	}
	
	/**
	 * Liefert ein Array mit allen Plättchen des Rasters zurück
	 * @return Array aller Tiles
	 */
	public Tile[] getTiles() {
		return felder;
	}
	
	/**
	 * Setzt für alle Plättchen des Rasters den übergebenen OnClickListener
	 * @param l OnClickListener
	 */
	public void setOnClickListenerForAllTiles(OnClickListener l) {
		for ( Tile t : felder )
			t.setOnClickListener(l);
	}
	
}
