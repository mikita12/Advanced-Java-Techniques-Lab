package org.example.archivelib;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipService {

    public void zip(Path source, Path zipFile) throws IOException {
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile));
            Stream<Path> paths = Files.walk(source)
        ){
            paths.forEach(path -> {
                try {
                    if (Files.isRegularFile(path)) { //if directory is empty, it will not be included in zip, is it a problem ?
                        Path relative = source.relativize(path);
                        String entryName = relative.toString();
                        if(entryName.isEmpty()){
                            entryName = path.getFileName().toString();
                        }
                        ZipEntry entry = new ZipEntry(entryName);
                        zipOutputStream.putNextEntry(entry);
                        Files.copy(path, zipOutputStream);
                        zipOutputStream.closeEntry();
                    }
                } catch (IOException e){
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
