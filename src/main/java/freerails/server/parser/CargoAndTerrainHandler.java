package freerails.server.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Defines methods to handle parsing the cargo and terrain types XML.
 *
 * @author Luke
 */
public interface CargoAndTerrainHandler {
    /**
     * An empty element event handling method.
     * @param meta
     * @throws org.xml.sax.SAXException
     */
    void handle_Converts(final Attributes meta) throws SAXException;

    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_Tile(final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     * @throws org.xml.sax.SAXException
     */
    void end_Tile() throws SAXException;

    /**
     * An empty element event handling method.
     * @param meta
     * @throws org.xml.sax.SAXException
     */
    void handle_Cargo(final Attributes meta) throws SAXException;

    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_Cargo_Types(final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     * @throws org.xml.sax.SAXException
     */
    void end_Cargo_Types() throws SAXException;

    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_Terrain_Types(final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     * @throws org.xml.sax.SAXException
     */
    void end_Terrain_Types() throws SAXException;

    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_Types(final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     * @throws org.xml.sax.SAXException
     */
    void end_Types() throws SAXException;

    /**
     * An empty element event handling method.
     * @param meta
     * @throws org.xml.sax.SAXException
     */
    void handle_Consumes(final Attributes meta) throws SAXException;

    /**
     * An empty element event handling method.
     * @param meta
     * @throws org.xml.sax.SAXException
     */
    void handle_Produces(final Attributes meta) throws SAXException;
}