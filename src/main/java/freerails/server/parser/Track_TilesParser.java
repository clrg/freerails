package freerails.server.parser;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * The class reads XML documents according to specified DTD and translates all
 * related events into Track_TilesHandler events.
 * <p>
 * Usage sample:
 * <p>
 * <pre>
 *      Track_TilesParser parser = new Track_TilesParser(...);
 *      parser.parse(new InputSource(&quot;...&quot;));
 * </pre>
 * <p>
 * Date: 21 January 2002 18:00
 *
 * @author lindsal
 */
final public class Track_TilesParser implements org.xml.sax.ContentHandler {
    private static final Logger logger = Logger
            .getLogger(Track_TilesParser.class.getName());

    private final java.lang.StringBuffer buffer;

    private final Track_TilesHandler handler;

    private final java.util.Stack<Object[]> context;

    /**
     *
     * @param handler
     */
    public Track_TilesParser(final Track_TilesHandler handler) {
        this.handler = handler;
        buffer = new StringBuffer(111);
        context = new java.util.Stack<>();
    }

    /**
     * The recognizer entry method taking an Inputsource.
     *
     * @param input InputSource to be parsed.
     * @param handler
     * @throws java.io.IOException                            on I/O error.
     * @throws SAXException                                   propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be
     *                                                        created.
     */
    public static void parse(final InputSource input,
                             final Track_TilesHandler handler) throws SAXException,
            ParserConfigurationException, IOException {
        parse(input, new Track_TilesParser(handler));
    }

    /**
     * The recognizer entry method taking a URL.
     *
     * @param url URL source to be parsed.
     * @param handler
     * @throws java.io.IOException                            on I/O error.
     * @throws SAXException                                   propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be
     *                                                        created.
     */
    public static void parse(final java.net.URL url,
                             final Track_TilesHandler handler) throws SAXException,
            ParserConfigurationException, IOException {
        parse(new InputSource(url.toExternalForm()), handler);
    }

    private static void parse(final InputSource input,
                              final Track_TilesParser recognizer) throws SAXException,
            ParserConfigurationException, IOException {
        javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory
                .newInstance();
        factory.setValidating(true); // the code was generated according DTD
        factory.setNamespaceAware(false); // the code was generated according
        // DTD

        org.xml.sax.XMLReader parser = factory.newSAXParser().getXMLReader();
        parser.setContentHandler(recognizer);
        parser.setErrorHandler(recognizer.getDefaultErrorHandler());
        parser.parse(input);
    }

    public void setDocumentLocator(org.xml.sax.Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startElement(java.lang.String ns, java.lang.String name,
                             java.lang.String qname, org.xml.sax.Attributes attrs)
            throws SAXException {
        dispatch(true);
        context.push(new Object[]{qname,
                new org.xml.sax.helpers.AttributesImpl(attrs)});

        switch (qname) {
            case "CanOnlyBuildOnTheseTerrainTypes":
                handler.start_CanOnlyBuildOnTheseTerrainTypes(attrs);
                break;
            case "ListOfTrackPieceTemplates":
                handler.start_ListOfTrackPieceTemplates(attrs);
                break;
            case "ListOfLegalRoutesAcrossNode":
                handler.start_ListOfLegalRoutesAcrossNode(attrs);
                break;
            case "LegalRouteAcrossNode":
                handler.handle_LegalRouteAcrossNode(attrs);
                break;
            case "CannotBuildOnTheseTerrainTypes":
                handler.start_CannotBuildOnTheseTerrainTypes(attrs);
                break;
            case "TrackType":
                handler.start_TrackType(attrs);
                break;
            case "TerrainType":
                handler.handle_TerrainType(attrs);
                break;
            case "Tiles":
                handler.start_Tiles(attrs);
                break;
            case "TrackPieceTemplate":
                handler.start_TrackPieceTemplate(attrs);
                break;
            case "TrackSet":
                handler.start_TrackSet(attrs);
                break;
        }
    }

    public void endElement(java.lang.String ns, java.lang.String name,
                           java.lang.String qname) throws SAXException {
        dispatch(false);
        context.pop();

        switch (qname) {
            case "CanOnlyBuildOnTheseTerrainTypes":
                handler.end_CanOnlyBuildOnTheseTerrainTypes();
                break;
            case "ListOfTrackPieceTemplates":
                handler.end_ListOfTrackPieceTemplates();
                break;
            case "ListOfLegalRoutesAcrossNode":
                handler.end_ListOfLegalRoutesAcrossNode();
                break;
            case "CannotBuildOnTheseTerrainTypes":
                handler.end_CannotBuildOnTheseTerrainTypes();
                break;
            case "TrackType":
                handler.end_TrackType();
                break;
            case "Tiles":
                handler.end_Tiles();
                break;
            case "TrackPieceTemplate":
                handler.end_TrackPieceTemplate();
                break;
            case "TrackSet":
                handler.end_TrackSet();
                break;
        }
    }

    public void characters(char[] chars, int start, int len)
            throws SAXException {
        buffer.append(chars, start, len);
    }

    public void ignorableWhitespace(char[] chars, int start, int len)
            throws SAXException {
    }

    public void processingInstruction(java.lang.String target,
                                      java.lang.String data) throws SAXException {
    }

    public void startPrefixMapping(final java.lang.String prefix,
                                   final java.lang.String uri) throws SAXException {
    }

    public void endPrefixMapping(final java.lang.String prefix)
            throws SAXException {
    }

    public void skippedEntity(java.lang.String name) throws SAXException {
    }

    private void dispatch(final boolean fireOnlyIfMixed) throws SAXException {
        if (fireOnlyIfMixed && buffer.length() == 0) {
            return; // skip it
        }

        buffer.delete(0, buffer.length());
    }

    private org.xml.sax.ErrorHandler getDefaultErrorHandler() {
        return new org.xml.sax.ErrorHandler() {
            public void error(org.xml.sax.SAXParseException ex)
                    throws SAXException {
                if (context.isEmpty()) {
                    logger.error("Missing DOCTYPE.");
                }

                throw ex;
            }

            public void fatalError(org.xml.sax.SAXParseException ex)
                    throws SAXException {
                throw ex;
            }

            public void warning(org.xml.sax.SAXParseException ex)
                    throws SAXException {
                // ignore
            }
        };
    }
}