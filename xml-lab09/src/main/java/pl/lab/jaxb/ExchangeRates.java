package pl.lab.jaxb;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "channel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExchangeRates {

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "item")
    private List<Item> items;

    public String getTitle()       { return title; }
    public String getDescription() { return description; }
    public List<Item> getItems()   { return items; }
}