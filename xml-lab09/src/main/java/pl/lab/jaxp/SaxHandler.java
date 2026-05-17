package pl.lab.jaxp;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler {

    private final StringBuilder currentValue = new StringBuilder();
    private boolean inItem = false;
    private boolean inTitle = false;
    private boolean inDesc = false;
    private String itemTitle = "";
    private int itemCount = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes){
            currentValue.setLength(0);
            switch (qName){
                case "item" -> inItem = true;
                case "title" -> inTitle = true;
                case "description" -> inDesc = true;
            }
    }

    @Override
    public void characters(char[] ch, int start, int length){
        if(inTitle || inDesc){
                currentValue.append(ch,start,length);

        }
    }

    @Override
    public void endElement(String uri, String localName, String qName){
        switch (qName){
            case "title" -> {
                if (inItem) itemTitle = currentValue.toString().trim();
                inTitle = false;
            }
            case "description" -> {
                if(inItem) {
                    itemCount++;
                    System.out.printf(" [SAX #%02d} %-45s | %s%n", itemCount, itemTitle, currentValue.toString().trim());

                }
                inDesc = false;
            }
            case "item" -> inItem = false;
        }
        currentValue.setLength(0);
    }



}
