package util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CsvWriter {
    private final String file;

    public CsvWriter(String file) {
        this.file = file;
    }

    public void writeLine(String line) {
        try (FileWriter w = new FileWriter(file, true)) {
            w.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeRow(String name, int n, long comparisons, int depth, double timeMs) {
        String line = name + " (" + n + "," + comparisons + "," + depth + "," + timeMs + ")";
        writeLine(line);
    }

    public void writeHeaderIfNeeded() {
        try {
            Path p = Path.of(file);
            if (!Files.exists(p)) {
                writeLine("name (n,comparisons,depth,time_ms)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
