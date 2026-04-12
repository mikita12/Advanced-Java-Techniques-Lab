import processing.Processor;
import processing.StatusListener;
import processing.Status;

public class WordCountProcessor implements Processor {

    private String result = "";

    public WordCountProcessor() {}

    @Override
    public String getInfo() {
        return "wordcount: #1";
    }

    @Override
    public boolean submitTask(String task, StatusListener listener) {
        int count = (task == null || task.isEmpty())
                ? 0
                : task.trim().split("\\s+").length;

        result = String.valueOf(count);

        Status status = new Status(1,100); // 👈 bez wyniku!
        listener.statusChanged(status);

        return true;
    }

    @Override
    public String getResult() {
        return result;
    }
}