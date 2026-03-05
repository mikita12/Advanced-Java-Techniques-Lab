package org.example.archivelib;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashService {

    public String calculateSha256(Path path) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");


            try (InputStream is = Files.newInputStream(path)) {

                byte[] buffer = new byte[8192];
                int bytesRead;

                while ((bytesRead = is.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }

                byte[] hash = digest.digest();
                StringBuilder hashHex = new StringBuilder();
                for(byte b : hash){
                    hashHex.append(String.format("%02x", b));
                }
                return hashHex.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveHashToFile(Path path, String hash) throws IOException{
            Path hashPath = Path.of(path.toString() + ".sha256");
            Files.writeString(hashPath, hash);
    }

    public boolean verify(Path file, Path hashFile) throws IOException{
        return  calculateSha256(file).equals(Files.readString(hashFile).trim());
    }
}
