package jfreerails.client.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import jfreerails.client.renderer.CityNamesRenderer;
import jfreerails.client.renderer.MapBackgroundRender;
import jfreerails.client.renderer.MapLayerRenderer;
import jfreerails.client.renderer.MapRenderer;
import jfreerails.client.renderer.SquareTileBackgroundRenderer;
import jfreerails.client.renderer.StationNamesRenderer;
import jfreerails.client.renderer.TileRendererList;
import jfreerails.client.renderer.TrackPieceRendererList;
import jfreerails.world.top.World;

public class DetailMapView implements MapRenderer {

	private final MapLayerRenderer background;
	
	private final Dimension mapSizeInPixels;
	
	private final TestOverHeadTrainView trainsview;
	
	private final CityNamesRenderer cityNames;
	
	private final StationNamesRenderer stationNames;

	public DetailMapView(
		World world,
		TileRendererList tiles,
		TrackPieceRendererList trackPieceViewList) {
		trainsview = new TestOverHeadTrainView(world);
		background = new SquareTileBackgroundRenderer(new MapBackgroundRender(world, tiles, trackPieceViewList), 30);
		Dimension mapSize=new Dimension(world.getMapWidth(), world.getMapHeight());
		mapSizeInPixels=new Dimension(mapSize.width*30,mapSize.height*30);
		
		cityNames = new CityNamesRenderer(world);
		stationNames = new StationNamesRenderer(world);
	}

	public float getScale() {
		return 30;
	}

	public Dimension getMapSizeInPixels() {
		return mapSizeInPixels;
	}

	public void paintTile(Graphics g, int tileX, int tileY) {
		background.paintTile(g, tileX, tileY);
		trainsview.paint((Graphics2D)g);
		cityNames.paint((Graphics2D)g);
		stationNames.paint((Graphics2D)g);
	}

	public void paintRectangleOfTiles(
		Graphics g,
		int x,
		int y,
		int width,
		int height) {
		background.paintRectangleOfTiles(g, x, y, width, height);
		trainsview.paint((Graphics2D)g);
		cityNames.paint((Graphics2D)g);
		stationNames.paint((Graphics2D)g);
	}

	public void refreshTile(int x, int y) {
		background.refreshTile(x, y);
	}

	public void refreshRectangleOfTiles(int x, int y, int width, int height) {
		background.refreshRectangleOfTiles(x, y, width, height);
	}

	public void paintRect(Graphics g, Rectangle visibleRect) {
		background.paintRect(g, visibleRect);
		trainsview.paint((Graphics2D)g);
		cityNames.paint((Graphics2D)g);
		stationNames.paint((Graphics2D)g);
	}
}