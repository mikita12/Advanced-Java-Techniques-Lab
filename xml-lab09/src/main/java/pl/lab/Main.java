package pl.lab;

import jakarta.xml.bind.*;
import org.xml.sax.InputSource;
import pl.lab.jaxb.ExchangeRates;
import pl.lab.jaxp.DomParser;
import pl.lab.jaxp.SaxHandler;
import pl.lab.xslt.XsltTransformer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * Lab09 – Przetwarzanie XML w Javie
 *
 * Dane: FloatRates USD feed (darmowy, bez klucza API)
 * URL : https://www.floatrates.com/daily/usd.xml
 *
 * Uruchomienie z konkretnym arkuszem XSLT:
 *   java -jar xml-lab09.jar rates_list.xsl
 * (domyślnie używany jest rates_table.xsl)
 */
public class Main {

    private static final String FEED_URL =
            "https://www.floatrates.com/daily/usd.xml";

    public static void main(String[] args) throws Exception {

        // Wybór arkusza XSLT – można zmienić BEZ rekompilacji przez argument
        String xslName = (args.length > 0) ? args[0] : "rates_table.xsl";

        System.out.println("=================================================");
        System.out.println("  Lab09 – XML Processing Demo");
        System.out.println("  Źródło: " + FEED_URL);
        System.out.println("=================================================\n");

        // Pobierz XML raz i zapisz w pamięci
        byte[] xmlBytes = fetchXml(FEED_URL);
        System.out.println("Pobrano " + xmlBytes.length + " bajtów XML.\n");

        // ── 1. JAXB ─────────────────────────────────────────────────────────
        System.out.println("┌─────────────────────────────────────────────────");
        System.out.println("│  1. JAXB – deserializacja XML → obiekty Java");
        System.out.println("└─────────────────────────────────────────────────");
        runJaxb(new ByteArrayInputStream(xmlBytes));

        // ── 2a. JAXP / SAX ──────────────────────────────────────────────────
        System.out.println("\n┌─────────────────────────────────────────────────");
        System.out.println("│  2a. JAXP / SAX – parser zdarzeniowy (streaming)");
        System.out.println("└─────────────────────────────────────────────────");
        runSax(new ByteArrayInputStream(xmlBytes));

        // ── 2b. JAXP / DOM ──────────────────────────────────────────────────
        System.out.println("\n┌─────────────────────────────────────────────────");
        System.out.println("│  2b. JAXP / DOM – parser drzewowy (in-memory)");
        System.out.println("└─────────────────────────────────────────────────");
        DomParser.parse(new ByteArrayInputStream(xmlBytes));

        // ── 3. XSLT ─────────────────────────────────────────────────────────
        System.out.println("\n┌─────────────────────────────────────────────────");
        System.out.println("│  3. XSLT – transformacja → HTML");
        System.out.println("│     Arkusz: " + xslName);
        System.out.println("└─────────────────────────────────────────────────");
        runXslt(new ByteArrayInputStream(xmlBytes), xslName);

        System.out.println("\n=================================================");
        System.out.println("  Gotowe!");
        System.out.println("=================================================");
    }

    // ────────────────────────────────────────────────────────────────────────
    // 1. JAXB
    // ────────────────────────────────────────────────────────────────────────
    private static void runJaxb(InputStream xmlStream) throws Exception {
        // Wyciągamy <channel> z środka <rss> ręcznie przez DOM
        javax.xml.parsers.DocumentBuilderFactory dbf =
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        org.w3c.dom.Document doc = dbf.newDocumentBuilder().parse(xmlStream);
        org.w3c.dom.Node channelNode =
                doc.getElementsByTagName("channel").item(0);

        JAXBContext ctx = JAXBContext.newInstance(ExchangeRates.class);
        Unmarshaller um = ctx.createUnmarshaller();

        ExchangeRates rates = um.unmarshal(channelNode, ExchangeRates.class).getValue();

        if (rates == null) {
            System.out.println("  [JAXB] Brak danych.");
            return;
        }

        System.out.println("  Kanał : " + rates.getTitle());
        System.out.println("  Opis  : " + rates.getDescription());
        System.out.printf("%n  Kursów: %d%n%n", rates.getItems().size());
        rates.getItems().forEach(System.out::println);
    }
    // ────────────────────────────────────────────────────────────────────────
    // 2a. SAX
    // ────────────────────────────────────────────────────────────────────────
    private static void runSax(InputStream xmlStream) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        SAXParser parser = factory.newSAXParser();
        SaxHandler handler = new SaxHandler();
        parser.parse(new InputSource(xmlStream), handler);
    }

    // ────────────────────────────────────────────────────────────────────────
    // 3. XSLT
    // ────────────────────────────────────────────────────────────────────────
    private static void runXslt(InputStream xmlStream, String xslName) throws Exception {
        String html = XsltTransformer.transformXml(xmlStream, xslName);

        // Zapis do pliku output.html w katalogu roboczym
        Path outPath = Path.of("output.html");
        Files.writeString(outPath, html, StandardCharsets.UTF_8);
        System.out.println("\n  HTML zapisany do: " + outPath.toAbsolutePath());
        System.out.println("\n  Pierwsze 500 znaków wyniku:");
        System.out.println("  " + html.substring(0, Math.min(500, html.length())).replace("\n", "\n  "));
    }

    // ────────────────────────────────────────────────────────────────────────
    // HTTP helper (Java 11+ HttpClient)
    // ────────────────────────────────────────────────────────────────────────
    private static byte[] fetchXml(String url) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/xml, text/xml, */*")
                .header("User-Agent", "Mozilla/5.0 (Java Lab09)")
                .GET().build();
        HttpResponse<byte[]> response =
                client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() != 200) {
            throw new IOException("HTTP " + response.statusCode() + " dla: " + url);
        }
        return response.body();
    }
}