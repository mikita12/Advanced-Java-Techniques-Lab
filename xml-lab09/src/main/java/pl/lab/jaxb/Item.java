package pl.lab.jaxb;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

    @XmlElement(name="title")
    private String title;

    @XmlElement(name = "description")
    private String description;

    public String getTitle() { return title; }
    public String getDescription() { return description; }

    @Override
    public String toString(){
        return String.format(" %-45s | %s", title, description);
    }
}
