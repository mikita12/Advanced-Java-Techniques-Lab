import processing.Processor;
import processing.StatusListener;
import processing.Status;

public class ReverseProcessor implements Processor {

    private String result = "";

    public ReverseProcessor() {}

    @Override
    public String getInfo() {
        return "reverse: #1";
    }

    @Override
    public boolean submitTask(String task, StatusListener listener) {

        listener.statusChanged(new Status(1, 0)); // start

        result = new StringBuilder(task).reverse().toString();

        listener.statusChanged(new Status(1, 100)); // done

        return true;
    }

    @Override
    public String getResult() {
        return result;
    }
}