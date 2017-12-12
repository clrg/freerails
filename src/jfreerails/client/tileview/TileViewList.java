package jfreerails.client.tileview;
import java.util.Iterator;

import jfreerails.world.TerrainTileTypesList;

/**
*  Description of the Interface
*
*@author     Luke Lindsay
*@created    09 October 2001
*/

public interface TileViewList {

	TileView getTileViewWithNumber();

	boolean TestRGBValue(int rgb);

	boolean TestTileViewNumber();

	int getLength();

	Iterator getIterator();

	TileView getTileViewWithRGBValue(int rgb);
	
	/** Checks whether this tile view list has tile views for all
	 * the terrain types in the specifed list.
	 */
	
	boolean validate(TerrainTileTypesList terrainTypes);
}