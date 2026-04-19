package app;

import ex.api.AnalysisService;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class PluginLoader {

    public static List<AnalysisService> load() {
        List<AnalysisService> result = new ArrayList<>();

        try {
            File folder = new File("plugins");
            File[] files = folder.listFiles((d, name) -> name.endsWith(".jar"));
            if (files == null) return result;

            for (File file : files) {
                URL[] urls = { file.toURI().toURL() };

                URLClassLoader cl = new URLClassLoader(urls);

                ServiceLoader<AnalysisService> loader =
                        ServiceLoader.load(AnalysisService.class, cl);

                for (AnalysisService s : loader) {
                    result.add(s);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
