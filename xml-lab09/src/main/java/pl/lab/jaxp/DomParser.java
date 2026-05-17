package pl.lab.jaxp;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.InputStream;

public class DomParser {

    public static void parse(InputStream xmlStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlStream);
        doc.getDocumentElement().normalize();

        NodeList channels = doc.getElementsByTagName("channel");
        if(channels.getLength()>0){
            Element channel = (Element) channels.item(0);
            String chanTitle = getDirectChildText(channel, "title");
            System.out.println(" Kanał: "+ chanTitle);
            System.out.println();
        }

        NodeList items = doc.getElementsByTagName("item");
        System.out.printf(" Znaleziono %d kursów walut: %n%n", items.getLength());
        for(int i = 0; i< items.getLength(); i++){
            Element item = (Element) items.item(i);
            String title = getDirectChildText(item, "title");
            String desc = getDirectChildText(item, "description");
            System.out.printf(" [DOM #%02d] %-45s | %s%n", i+1, title, desc);
        }
    }

    private static String getDirectChildText(Element parent, String tagName){
        NodeList list = parent.getChildNodes();
        for(int i = 0; i< list.getLength(); i++){
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE
            && node.getNodeName().equals(tagName)){
                return node.getTextContent().trim();
            }
        }
        return "";
    }
}
