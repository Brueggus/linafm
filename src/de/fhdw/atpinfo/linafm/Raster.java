package de.fhdw.atpinfo.linafm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	 * Enthält den initialen Zustand der Plättchen
	 * Benötigt, um beim Resetten darauf zurückzugreifen und das untere 
	 * Raster wieder initial aufzubauen
	 */
	private Tile[] originalFelder;
	
	/**
	 * Größe des Rasters
	 */
	private int size = 0;
	
	/**
	 * Die Zeilen unseres Rasters
	 */
	private List<TableRow> rows = new ArrayList<TableRow>();
	
	/**
	 * Anzahl der Spalten pro Zeile
	 */
	private int columns;
	
	/**
	 * Anzahl der Zeilen
	 * !! als Double-Wert !!
	 */
	private final double rowCount = 2.0;

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
		
		originalFelder = felder.clone();
		
		buildRaster(context);
	}
	
	/**
	 * Diese Methode baut aus dem Plättchen-Array das Raster auf
	 * @param context Context
	 */
	public void buildRaster(Context context)
	{
		// Wie viele Plättchen in einer Reihe?
		columns = (int)Math.ceil( size / rowCount);
		
		// Insgesamt x Zeilen im Raster
		setWeightSum((float)rowCount);
		
		// Raster aufbauen und füllen
		for (int i = 0; i < rowCount; i++)
		{
			// Neue Zeile im Table-Layout
			TableRow tr = new TableRow(context);
			rows.add(tr);
	        tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT, // Breite
                    LayoutParams.FILL_PARENT, // Höhe
                    1.0f)); // layout_weight
	        
	        refreshRow(i);
			
			// Zeile dem Layout hinzufügen
			this.addView(tr);
		}
	}
	
	/**
	 * Baut eine Zeile des Rasters mittels der Daten des Felder-Arrays neu auf
	 * @param rowId Zeilennummer beginnend bei 0
	 */
	void refreshRow(int rowId) {
		TableRow row = rows.get(rowId);
		
		// erst mal leer machen
		row.removeAllViews();
		
		// Beispiel: Zeile 3 bei 8 Spalten: Position 16 im Raster 
		int start = rowId * columns;
		// eine ganze Zeile lang hochzählen (also bis Pos. 23)
		int end = start + columns;
		
		for ( int i = start; i < end; i++ ) {
			if ( felder[i] != null )
				row.addView(felder[i]);
		}
	}
	
	/**
	 * Leert das komplette Raster
	 */
	public void clear() {
		// Alle Reihen leeren...
		for ( TableRow row : rows ) {
			row.removeAllViews();
		}
		
		// ...und auch das Felder-Array
		Arrays.fill(felder, null);
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
		
		// Falls es sich um ein Dummy-Tile handelt, müssen wir noch das Bild entsprechend setzen
		if ( tile.isDummy() )
			// unteres Raster --> kein Bild
			if ( this.getId() == R.id.rasterUnten )
				tile.setImage(null);
			// Popup-Raster --> Zahl
			else if ( this.getId() == R.id.rasterPopup )
				tile.setNumeralImage(position);
		
		felder[position] = tile;
		
		// Ermitteln, in welcher Zeile Veränderungen vorgenommen wurden
		Coordinate<Integer> newPosInRaster = getLayoutPosition(position);
		refreshRow(newPosInRaster.row);
	}
	
	/**
	 * Plättchen an einer bestimmten Position löschen
	 * @param position Position
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void removeTile(int position) throws ArrayIndexOutOfBoundsException {
		// Befindet sich die Position innerhalb der Arraygrenzen?
		if (position >= size || position < 0)
			throw new ArrayIndexOutOfBoundsException(R.string.ex_insert_tile_out_of_raster);
		
		felder[position] = null;

		// Ermitteln, in welcher Zeile Veränderungen vorgenommen wurden
		Coordinate<Integer> newPosInRaster = getLayoutPosition(position);
		refreshRow(newPosInRaster.row);
	}
	
	/**
	 * ermitteln, in welcher Zeile und welcher Spalte das Plättchen hinzugefügt wurde
	 * Beispiel: 12 Plättchen = 2 Zeilen, 6 Spalten
	 * +----+----+----+----+----+----+
	 * |  0 |  1 |  2 |  3 |  4 |  5 |
	 * +----+----+----+----+----+----+
	 * |  6 |  7 |  8 |  9 | 10 | 11 |
	 * +----+----+----+----+----+----+
	 * @param position Position im Felder-Array
	 * @return Zeile/Spalte als Koordinate
	 */
	public Coordinate<Integer> getLayoutPosition(int position) {
		int row = position / columns;
		int column = position % columns;
		
		return new Coordinate<Integer>(row, column);
	}
	
	/**
	 * Koordinate bestehend aus Zeile und Spalte
	 * @author Alexander Brügmann
	 *
	 * @param <T> Typ (in der Regel Integer)
	 */
	class Coordinate<T> {
	    public final T row;
	    public final T column;

	    public Coordinate(T first, T second) {
	        row = first;
	        column = second;
	    }
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
	
	public void setTiles(Tile[] tiles) {
		felder = tiles;
	}
	
	/**
	 * Ermittelt die Position eines bestimmten Plättchens im Raster
	 * @param tile Plättchen, dessen Position gesucht wird
	 * @return Position des Plättchens, -1 wenn nicht gefunden
	 */
	public int getTilePosition(Tile tile) {
		for (int i = 0; i < size; i++)
			if ( felder[i] == tile )
				return i;
		
		return -1;
	}
	
	/**
	 * Setzt für alle Plättchen des Rasters den übergebenen OnClickListener
	 * @param l OnClickListener
	 */
	public void setOnClickListenerForAllTiles(OnClickListener l) {
		for ( Tile t : felder )
			t.setOnClickListener(l);
	}

	/**
	 * Liefert die Größe des Rasters
	 * @return Anzahl der Plättchen
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Liefert die Original Felder zu Spielbeginn 
	 * @return Original Felder beim initialisieren
	 */
	public Tile[] getOriginalFelder() {
		return originalFelder;
	}

	/**
	 * setzt die Originalfelder
	 * @param originalFelder
	 */
	public void setOriginalFelder(Tile[] originalFelder) {
		this.originalFelder = originalFelder;
	}

	/**
	 * Setzt für alle Plättchen des Rasters den übergebenen OnLongClickListener
	 * @param l OnLongClickListener
	 */	
	public void setOnLongClickListenerForAllTiles(OnLongClickListener l) {
		for ( Tile t : felder ){
			t.setOnLongClickListener(l);			
		}
	}
	
}
