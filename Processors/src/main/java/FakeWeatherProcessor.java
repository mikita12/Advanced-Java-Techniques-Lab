import processing.Processor;
import processing.StatusListener;
import processing.Status;

import java.util.Random;

public class FakeWeatherProcessor implements Processor {

    private String result = "";

    public FakeWeatherProcessor() {}

    @Override
    public String getInfo() {
        return "pogoda: miasto, data";
    }

    @Override
    public boolean submitTask(String task, StatusListener listener) {

        listener.statusChanged(new Status(1, 0)); // start

        Random r = new Random();
        int temp = r.nextInt(30);
        int pressure = 950 + r.nextInt(100);

        result = temp + "C, " + pressure + " hPa";

        listener.statusChanged(new Status(1, 100)); // done

        return true;
    }

    @Override
    public String getResult() {
        return result;
    }
}