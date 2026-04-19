package algo;

import ex.api.*;

import java.util.Arrays;
import java.util.Random;

public class KMedianService implements AnalysisService {

    private DataSet input;
    private DataSet result;
    private boolean ready = false;

    private int K = 2;
    private static final int MAX_ITERATIONS = 100;

    @Override
    public void setOptions(String[] options) throws AnalysisException {
        try {
            if (options != null && options.length > 0) {
                int newK = Integer.parseInt(options[0]);
                if (newK <= 0) throw new Exception();
                this.K = newK;
            }
        } catch (Exception e) {
            throw new AnalysisException("Niepoprawna wartość K");
        }
    }

    @Override
    public String getName() {
        return "K-Median";
    }

    @Override
    public void submit(DataSet ds) throws AnalysisException {
        if (input != null && !ready) {
            throw new AnalysisException("Analiza już trwa. Poczekaj.");
        }

        this.input = ds;
        this.ready = false;

        runKMedian();
        ready = true;
    }

    @Override
    public DataSet retrieve(boolean clear) throws AnalysisException {
        if (!ready) return null;

        DataSet out = result;

        if (clear) {
            result = null;
            input = null;
            ready = false;
        }

        return out;
    }

    // 🔥 GŁÓWNY ALGORYTM
    private void runKMedian() {

        String[][] raw = input.getData();

        if (raw.length == 0 || raw[0].length == 0) {
            result = new DataSet();
            result.setData(new String[][]{});
            return;
        }

        int n = raw.length;
        int dim = raw[0].length;

        double[][] data = new double[n][dim];

        // konwersja String → double
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < dim; j++) {
                try {
                    data[i][j] = Double.parseDouble(raw[i][j]);
                } catch (Exception e) {
                    throw new RuntimeException("Niepoprawne dane w tabeli!");
                }
            }
        }

        int[] labels = new int[n];
        double[][] centroids = new double[K][dim];

        Random rand = new Random();

        // losowa inicjalizacja
        for (int i = 0; i < K; i++) {
            centroids[i] = data[rand.nextInt(n)].clone();
        }

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {

            // przypisanie punktów
            for (int i = 0; i < n; i++) {
                labels[i] = nearest(data[i], centroids);
            }

            double[][] newCentroids = new double[K][dim];

            for (int k = 0; k < K; k++) {

                // zbierz punkty w klastrze
                int count = 0;
                for (int i = 0; i < n; i++) {
                    if (labels[i] == k) count++;
                }

                if (count == 0) {
                    newCentroids[k] = data[rand.nextInt(n)].clone();
                    continue;
                }

                double[][] clusterPoints = new double[count][dim];
                int idx = 0;

                for (int i = 0; i < n; i++) {
                    if (labels[i] == k) {
                        clusterPoints[idx++] = data[i];
                    }
                }

                // 🔥 MEDIANA dla każdej kolumny
                for (int d = 0; d < dim; d++) {
                    double[] column = new double[count];

                    for (int i = 0; i < count; i++) {
                        column[i] = clusterPoints[i][d];
                    }

                    Arrays.sort(column);

                    if (count % 2 == 0) {
                        newCentroids[k][d] =
                                (column[count/2 - 1] + column[count/2]) / 2.0;
                    } else {
                        newCentroids[k][d] = column[count/2];
                    }
                }
            }

            centroids = newCentroids;
        }

        // wynik
        String[][] out = new String[n][1];

        for (int i = 0; i < n; i++) {
            out[i][0] = String.valueOf(labels[i]);
        }

        result = new DataSet();
        result.setData(out);
    }

    private int nearest(double[] p, double[][] centroids) {
        int best = 0;
        double min = Double.MAX_VALUE;

        for (int i = 0; i < centroids.length; i++) {
            double dist = 0;

            for (int j = 0; j < p.length; j++) {
                double diff = p[j] - centroids[i][j];
                dist += diff * diff;
            }

            if (dist < min) {
                min = dist;
                best = i;
            }
        }

        return best;
    }
}