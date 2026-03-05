package org.example.archiveapp;

import org.example.archivelib.HashService;
import org.example.archivelib.ZipService;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {

        ZipService zipService = new ZipService();
        HashService hashService = new HashService();

        if (args.length == 0) {
            printHelp();
            return;
        }

        try {

            switch (args[0]) {

                case "zip" -> {

                    if (args.length != 3) {
                        printHelp();
                        return;
                    }

                    Path source = Path.of(args[1]);
                    Path zipFile = Path.of(args[2]);

                    zipService.zip(source, zipFile);

                    System.out.println("Archive created: " + zipFile);

                }

                case "hash" -> {

                    if (args.length != 2) {
                        printHelp();
                        return;
                    }

                    Path file = Path.of(args[1]);

                    String hash = hashService.calculateSha256(file);

                    hashService.saveHashToFile(file, hash);

                    System.out.println("SHA-256: " + hash);
                    System.out.println("Saved to: " + file + ".sha256");

                }

                case "verify" -> {

                    if (args.length != 3) {
                        printHelp();
                        return;
                    }

                    Path file = Path.of(args[1]);
                    Path hashFile = Path.of(args[2]);

                    boolean result = hashService.verify(file, hashFile);

                    if (result) {
                        System.out.println("Hash matches");
                    } else {
                        System.out.println("Hash does NOT match");
                    }

                }

                default -> printHelp();
            }

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());

        }

    }

    private static void printHelp() {

        System.out.println("Usage:");
        System.out.println("  zip <source> <zipfile>");
        System.out.println("  hash <file>");
        System.out.println("  verify <file> <hashfile>");

    }

}