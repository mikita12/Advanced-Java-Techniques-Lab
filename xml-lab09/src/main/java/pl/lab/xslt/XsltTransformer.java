package pl.lab.xslt;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.nio.file.*;


public class XsltTransformer {

    /**
     * @param xmlStream  wejściowy dokument XML
     * @param xslName    nazwa pliku .xsl w katalogu resources/xslt/
     *                   (np. "rates_table.xsl" lub "rates_list.xsl")
     */
    public static String transformXml(InputStream xmlStream, String xslName) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();

        // Ładowanie arkusza z classpath (po kompilacji jest w JAR)
        InputStream xslStream = XsltTransformer.class
                .getClassLoader()
                .getResourceAsStream("xslt/" + xslName);

        if (xslStream == null) {
            // Fallback: próba odczytu z systemu plików (podmienianie bez rekompilacji)
            Path fsPath = Path.of("src/main/resources/xslt/" + xslName);
            if (Files.exists(fsPath)) {
                xslStream = Files.newInputStream(fsPath);
                System.out.println("  [XSLT] Załadowano arkusz z systemu plików: " + fsPath);
            } else {
                throw new FileNotFoundException("Nie znaleziono arkusza: " + xslName);
            }
        } else {
            System.out.println("  [XSLT] Załadowano arkusz z classpath: xslt/" + xslName);
        }

        Source xslSource = new StreamSource(xslStream);
        Transformer transformer = factory.newTransformer(xslSource);

        StringWriter output = new StringWriter();
        transformer.transform(new StreamSource(xmlStream), new StreamResult(output));
        return output.toString();
    }
}