package loader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MyClassLoader extends ClassLoader {

    private final String dir;

    public MyClassLoader(String dir) {
        this.dir = dir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            File file = new File(dir + "/" + name + ".class");
            byte[] bytes = Files.readAllBytes(file.toPath());
            return defineClass(null, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name);
        }
    }
}