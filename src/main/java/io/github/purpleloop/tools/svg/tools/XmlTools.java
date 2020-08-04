package io.github.purpleloop.tools.svg.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlTools {

    public static final Log LOG = LogFactory.getLog(XmlTools.class);

    /** Lists attributes of an element. */
    public static void listElementAttributes(Element element) {

        // Attributes
        NamedNodeMap nnm = element.getAttributes();

        for (int i = 0; i < nnm.getLength(); i++) {
            Node nn = nnm.item(i);
            LOG.debug(nn.getNodeName());
        }

    }

    /**
     * Builds the list of elements that are children of the given one.
     * 
     * @param ancestorElement ancestor element
     * @return list of children elements (empty if ancestorElement is null)
     */
    public static List<Element> getChildrenElements(Element ancestorElement) {

        if (ancestorElement==null) {
            return Collections.emptyList();
        }
        
        ArrayList<Element> childrenElements = new ArrayList<Element>();

        NodeList nodeList = ancestorElement.getChildNodes();

        for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); nodeIndex++) {
            Node node = nodeList.item(nodeIndex);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                childrenElements.add((Element) node);
            }
        }

        return childrenElements;
    }

    /**
     * Parses an XML file and returns the correponding DOM.
     * 
     * @param xmlFile XML file to parse
     * @return XML DOM Document
     * @throws XmlException in case of problem
     */
    public static Document getDocument(File xmlFile) throws XmlException {

        if (xmlFile == null) {
            return null;
        }

        Document doc = null;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();

            // Parses the file
            doc = builder.parse(xmlFile);

        } catch (ParserConfigurationException e) {
            throw new XmlException(
                    "Error during XML parsing of file " + xmlFile.getAbsolutePath() + ".", e);
        } catch (SAXException e) {
            throw new XmlException(
                    "XML Syntax error encountered in the file " + xmlFile.getAbsolutePath() + ".",
                    e);
        } catch (IOException e) {
            throw new XmlException(
                    "XML parser is unable to access the file " + xmlFile.getAbsolutePath() + ".",
                    e);
        }

        return doc;
    }
}
