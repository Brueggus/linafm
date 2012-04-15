package de.fhdw.atpinfo.linafm;

import android.content.Context;
import android.widget.TableLayout;

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
	 * Gr��e es Rasters
	 */
	private int size = 0;

	public Raster(Context context, int size) {
		super(context);
		
		if (size < 1)
			size = 1;

		felder = new Tile[size];
		this.size = size;
	}
	
	public Raster(Context context, Tile[] tiles) {
		super(context);
		
		felder = tiles;
		size = felder.length;
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
	
	public void removeTile(int position) throws ArrayIndexOutOfBoundsException {
		// Befindet sich die Position innerhalb der Arraygrenzen?
		if (position >= size || position < 0)
			throw new ArrayIndexOutOfBoundsException(R.string.ex_insert_tile_out_of_raster);
		
		felder[position] = null;
	}
}
