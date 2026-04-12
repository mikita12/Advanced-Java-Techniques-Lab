package loader;

import processing.Processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginManager {

    private final String dir;

    public PluginManager(String dir) {
        this.dir = dir;
    }

    public List<Processor> loadProcessors() {
        List<Processor> list = new ArrayList<>();

        File folder = new File(dir);
        File[] files = folder.listFiles((d, name) -> name.endsWith(".class"));

        if (files == null) return list;

        try {
            MyClassLoader loader = new MyClassLoader(dir);

            for (File f : files) {
                String name = f.getName().replace(".class", "");
                Class<?> clazz = loader.loadClass(name);

                if (Processor.class.isAssignableFrom(clazz)) {
                    Processor p = (Processor) clazz.getDeclaredConstructor().newInstance();
                    list.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}