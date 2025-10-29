package mdp.tirexchageservice.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.print.Doc;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtils {

    // Безопасность XML (XXE)
    private static DocumentBuilderFactory getSecureDocumentBuilderFactory() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        return factory;
    };

    // Получает имя корневого элемента (например, EPD015)
    public static String getRootElementNameSecure(String xml) {
        try {
            DocumentBuilderFactory factory = getSecureDocumentBuilderFactory();
            Document doc = factory.newDocumentBuilder()
                    .parse(new java.io.ByteArrayInputStream(xml.getBytes()));
            return doc.getDocumentElement().getNodeName();
        } catch (Exception e) {
            throw new RuntimeException("XML parsing error: " + e.getMessage(), e);
        }
    }

    // Извлекает значение из XML по названию тега.
    public static String extract(String xml, String tagName) {
        try {
            Document doc = getSecureDocumentBuilderFactory().newDocumentBuilder()
                    .parse(new java.io.ByteArrayInputStream(xml.getBytes()));
            NodeList nodes = doc.getElementsByTagName(tagName);
            if (nodes.getLength() > 0) return nodes.item(0).getTextContent();
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    //Secure XML -> DTO
    public static <T> T fromXmlSecure(String xml, Class<T> clazz) throws JAXBException {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            // Безопасный SAX-парсер
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            spf.setXIncludeAware(false);

            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            SAXSource source = new SAXSource(xmlReader, new InputSource(new StringReader(xml)));

            return (T) unmarshaller.unmarshal(source);
        } catch (ParserConfigurationException | SAXException e) {
            throw new JAXBException("Secure XML parsing error: " + e.getMessage(), e);
        }
    }

    public static String toXml(Object obj) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }
}