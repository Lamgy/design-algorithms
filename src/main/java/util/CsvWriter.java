package util;

import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
    private final String file;

    public CsvWriter(String file) {
        this.file = file;
    }

    public void writeLine(String line) throws IOException {
        try (FileWriter w = new FileWriter(file, true)) {
            w.write(line + "\n");
        }
    }
}
