package plots;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.*;
import java.util.*;

public class PlotGenerator {

    private static class Stat {
        double totalTime = 0;
        int totalDepth = 0;
        int count = 0;

        void add(int depth, double time) {
            totalDepth += depth;
            totalTime += time;
            count++;
        }

        double avgTime() {
            return totalTime / count;
        }

        double avgDepth() {
            return (double) totalDepth / count;
        }
    }

    public static void main(String[] args) throws IOException {
        String csv = "results.csv";       // your raw results file
        String timePlot = "time_vs_n.png";
        String depthPlot = "depth_vs_n.png";

        // (algo, n) → stats
        Map<String, Map<Integer, Stat>> stats = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String header = br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace(")", ""); // cleanup if needed
                String[] parts = line.split("[,(]");

                String algo = parts[0].trim();
                int n = Integer.parseInt(parts[1].trim());
                int depth = Integer.parseInt(parts[3].trim());
                double time = Double.parseDouble(parts[4].trim());

                stats
                        .computeIfAbsent(algo, k -> new HashMap<>())
                        .computeIfAbsent(n, k -> new Stat())
                        .add(depth, time);
            }
        }

        DefaultCategoryDataset timeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset depthDataset = new DefaultCategoryDataset();

        for (var algoEntry : stats.entrySet()) {
            String algo = algoEntry.getKey();

            List<Integer> sortedNs = new ArrayList<>(algoEntry.getValue().keySet());
            Collections.sort(sortedNs);

            for (int n : sortedNs) {
                Stat stat = algoEntry.getValue().get(n);

                timeDataset.addValue(stat.avgTime(), algo, String.valueOf(n));
                depthDataset.addValue(stat.avgDepth(), algo, String.valueOf(n));
            }
        }


        JFreeChart timeChart = ChartFactory.createLineChart(
                "Runtime vs n",
                "n",
                "Time (ms)",
                timeDataset
        );

        JFreeChart depthChart = ChartFactory.createLineChart(
                "Depth vs n",
                "n",
                "Depth",
                depthDataset
        );

        ChartUtils.saveChartAsPNG(new File(timePlot), timeChart, 800, 600);
        ChartUtils.saveChartAsPNG(new File(depthPlot), depthChart, 800, 600);

        System.out.println("Charts saved as " + timePlot + " and " + depthPlot);
    }
}
