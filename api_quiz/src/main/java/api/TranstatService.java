package api;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class TranstatService {

    private static final String TEMPLATE = "https://api-transtat.stat.gov.pl/api/v1/C004MInd114p/DataXPl/%s?year=%d&format=json";
    private final HttpClient client;

    public TranstatService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String fetchShips(String shipType, int year) throws IOException, InterruptedException {
        if (shipType == null) throw new IllegalArgumentException("shipType must not be null");
        String encoded = URLEncoder.encode(shipType, StandardCharsets.UTF_8);
        String url = String.format(TEMPLATE, encoded, year);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        int status = resp.statusCode();
        if (status >= 200 && status < 300) {
            return resp.body();
        } else {
            throw new IOException("API returned status " + status + " with body: " + resp.body());
        }
    }
}
