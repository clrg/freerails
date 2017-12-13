package jfreerails.server.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import jfreerails.world.cargo.CargoType;
import jfreerails.world.cargo.CargoType.Categories;
import jfreerails.world.terrain.Consumption;
import jfreerails.world.terrain.Conversion;
import jfreerails.world.terrain.Production;
import jfreerails.world.terrain.TerrainType;
import jfreerails.world.terrain.TileTypeImpl;
import jfreerails.world.top.SKEY;
import jfreerails.world.top.World;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Processes CargoAndTerrainHandler events and adds terrain and cargo types to
 * the world object.
 * 
 * @see CargoAndTerrainHandler
 * @see CargoAndTerrainParser
 * @author Luke
 * @version generated by NetBeans XML module
 */
public class CargoAndTerrainHandlerImpl implements CargoAndTerrainHandler {
	private final World world;

	HashMap<String, Integer> cargoName2cargoTypeNumber = new HashMap<String, Integer>();

	HashSet<Integer> rgbValuesAlreadyUsed = new HashSet<Integer>();

	// Parsing variables for Tile
	String tileID;

	TerrainType.Category tileCategory;

	int tileRGB;

	int tileROW;

	int tileBuildCost;

	ArrayList<Consumption> typeConsumes = new ArrayList<Consumption>();

	ArrayList<Production> typeProduces = new ArrayList<Production>();

	ArrayList<Conversion> typeConverts = new ArrayList<Conversion>();

	public CargoAndTerrainHandlerImpl(World w) {
		world = w;
	}

	public void handle_Converts(final Attributes meta) throws SAXException {
		String inputCargo = meta.getValue("input");
		String outputCargo = meta.getValue("output");

		int input = string2CargoID(inputCargo);
		int output = string2CargoID(outputCargo);
		Conversion conversion = new Conversion(input, output);
		typeConverts.add(conversion);
	}

	public void start_Tile(final Attributes meta) throws SAXException {
		typeConsumes.clear();
		typeProduces.clear();
		typeConverts.clear();

		tileID = meta.getValue("id");
		tileCategory = TerrainType.Category.valueOf(meta.getValue("Category"));

		String rgbString = meta.getValue("rgb");
		tileRGB = string2RGBValue(rgbString);

		String buildCostString = meta.getValue("build_cost");

		if (null != buildCostString) {
			tileBuildCost = Integer.parseInt(buildCostString);
		} else {
			tileBuildCost = -1;
		}

		// Check if another type is already using this rgb value..
		Integer rgbInteger = new Integer(tileRGB);

		if (rgbValuesAlreadyUsed.contains(rgbInteger)) {
			throw new SAXException(tileID + " can't using rgb value "
					+ rgbString
					+ " because it is being used by another tile type!");
		}
		rgbValuesAlreadyUsed.add(rgbInteger);

		tileROW = Integer.parseInt(meta.getValue("right-of-way"));
	}

	public void end_Tile() throws SAXException {
		Consumption[] consumes = new Consumption[typeConsumes.size()];

		for (int i = 0; i < typeConsumes.size(); i++) {
			consumes[i] = typeConsumes.get(i);
		}

		Production[] produces = new Production[typeProduces.size()];

		for (int i = 0; i < typeProduces.size(); i++) {
			produces[i] = typeProduces.get(i);
		}

		Conversion[] converts = new Conversion[typeConverts.size()];

		for (int i = 0; i < typeConverts.size(); i++) {
			converts[i] = typeConverts.get(i);
		}

		TileTypeImpl tileType = new TileTypeImpl(tileRGB, tileCategory, tileID,
				tileROW, produces, consumes, converts, tileBuildCost);

		world.add(SKEY.TERRAIN_TYPES, tileType);
	}

	public void handle_Cargo(final Attributes meta) throws SAXException {
		String cargoID = meta.getValue("id");
		String cargoCategory = meta.getValue("Category");
		int unitWeight = Integer.parseInt(meta.getValue("unitWeight"));
        CargoType cargoType = new CargoType(unitWeight, cargoID, Categories
                .getCategory(cargoCategory));

		int cargoNumber = world.size(SKEY.CARGO_TYPES);
		cargoName2cargoTypeNumber.put(cargoID, new Integer(cargoNumber));
		world.add(SKEY.CARGO_TYPES, cargoType);
	}

	public void start_Cargo_Types(final Attributes meta) throws SAXException {
		// no need to do anything here.
	}

	public void end_Cargo_Types() throws SAXException {
		// no need to do anything here.
	}

	public void start_Terrain_Types(final Attributes meta) throws SAXException {
		// no need to do anything here.
	}

	public void end_Terrain_Types() throws SAXException {
		// no need to do anything here.
	}

	public void start_Types(final Attributes meta) throws SAXException {
		// no need to do anything here.
	}

	public void end_Types() throws SAXException {
		// no need to do anything here.
	}

	public void handle_Consumes(final Attributes meta) throws SAXException {
		int cargoConsumed = string2CargoID(meta.getValue("Cargo"));
		String prerequisiteString = meta.getValue("Prerequisite");

		// "Prerequisite" is an optional attribute, so may be null.
		int prerequisiteForConsumption = (null == prerequisiteString ? 1
				: Integer.parseInt(prerequisiteString));
		Consumption consumption = new Consumption(cargoConsumed,
				prerequisiteForConsumption);
		typeConsumes.add(consumption);
	}

	public void handle_Produces(final Attributes meta) throws SAXException {
		int cargoProduced = string2CargoID(meta.getValue("Cargo"));
		int rateOfProduction = Integer.parseInt(meta.getValue("Rate"));
		Production production = new Production(cargoProduced, rateOfProduction);
		typeProduces.add(production);
	}

	private int string2RGBValue(String temp_number) {
		int rgb = Integer.parseInt(temp_number, 16);

		/*
		 * We need to change the format of the rgb value to the same one as used
		 * by the the BufferedImage that stores the map. See
		 * jfreerails.common.Map
		 */
		rgb = new java.awt.Color(rgb).getRGB();

		return rgb;
	}

	/** Returns the index number of the cargo with the specified name. */
	private int string2CargoID(String cargoName) throws SAXException {
		if (cargoName2cargoTypeNumber.containsKey(cargoName)) {
			Integer integer = cargoName2cargoTypeNumber.get(cargoName);

			return integer.intValue();
		}
		throw new SAXException("Unknown cargo type: " + cargoName);
	}
}