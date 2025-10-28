package mdp.tirexchageservice.util;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;

public class XmlUtils {

    public static String getRootElementName(String xml) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new java.io.ByteArrayInputStream(xml.getBytes()));
            return doc.getDocumentElement().getNodeName();
        } catch (Exception e) {
            throw new RuntimeException("XML parsing error: " + e.getMessage());
        }
    }

    public static String extract(String xml, String tagName) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new java.io.ByteArrayInputStream(xml.getBytes()));
            NodeList nodes = doc.getElementsByTagName(tagName);
            if (nodes.getLength() > 0) return nodes.item(0).getTextContent();
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}