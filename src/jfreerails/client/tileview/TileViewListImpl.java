
/*
*  TileViewList.java
*
*  Created on 08 August 2001, 17:11
*/
package jfreerails.client.tileview;
import java.util.HashMap;
import java.util.Iterator;

import jfreerails.world.TerrainTileTypesList;
import jfreerails.world.terrain.TerrainType;

/**
*@author           Luke Lindsay
*@created          09 October 2001
*@createdpublic    class TileViewListImplrsion
*/

final public class TileViewListImpl implements TileViewList {

	private HashMap tiles;

	public TileView getTileViewWithRGBValue(int rgb) {
		return (TileView) tiles.get(new Integer(rgb));
	}

	public TileView getTileViewWithNumber() {
		return null;
	}

	public int getLength() {
		return tiles.size();
	}

	public boolean TestRGBValue(int rgb) {
		return tiles.containsKey(new Integer(rgb));
	}

	public boolean TestTileViewNumber() {
		return false;
	}

	public TileViewListImpl(HashMap t) {
		tiles = t;
	}

	public java.util.Iterator getIterator() {
		return tiles.values().iterator();
	}

	public boolean validate(TerrainTileTypesList terrainTypes) {
		Iterator iterator = terrainTypes.getIterator();
		boolean okSoFar = true;
		while (iterator.hasNext()) {
			TerrainType terrainType = (TerrainType) iterator.next();
			if (!tiles.containsKey(new Integer(terrainType.getRGB()))) {
				okSoFar= false;
				System.out.println("No tile view for the following tile type: "+terrainType.getTerrainTypeName());
			}
		}
		return okSoFar;

	}
}