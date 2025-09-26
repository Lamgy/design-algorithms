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

    // convenience method for metrics (comparisons is long)
    public void writeRow(int n, long comparisons, int depth, double timeMs) {
        String line = n + "," + comparisons + "," + depth + "," + timeMs;
        writeLine(line);
    }

    // optionally write header only if file doesn't exist yet
    public void writeHeaderIfNeeded() {
        try {
            Path p = Path.of(file);
            if (!Files.exists(p)) {
                writeLine("n,comparisons,depth,time_ms");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
