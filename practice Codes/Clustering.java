import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class DataPoint {
    double x, y;
    int id;

    public DataPoint(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public double distanceTo(DataPoint other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public int getId() {
        return id;
    }
}

class Cluster {
    List<DataPoint> points = new ArrayList<>();

    public Cluster(DataPoint initialPoint) {
        points.add(initialPoint);
    }

    public void merge(Cluster other) {
        points.addAll(other.points);
    }

    public double singleLinkageDistance(Cluster other) {
        double minDistance = Double.MAX_VALUE;
        for (DataPoint p1 : points) {
            for (DataPoint p2 : other.points) {
                double distance = p1.distanceTo(p2);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        return minDistance;
    }
}

public class Clustering {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        // Accept user input for data points
        List<DataPoint> dataPoints = new ArrayList<>();
        int pointId = 1;
        System.out.println("Enter data points (x y), one per line. Enter 'done' to finish.");
        while (true) {
            System.out.print("Enter data point " + pointId + " (x y): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) {
                break;
            }
            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid input. Please enter 'x y'.");
                continue;
            }
            try {
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                dataPoints.add(new DataPoint(pointId, x, y));
                pointId++;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numeric values.");
            }
        }

        // Initialize clusters with user input data points
        List<Cluster> clusters = new ArrayList<>();
        for (DataPoint point : dataPoints) {
            clusters.add(new Cluster(point));
        }

        int step = 0;
        // Print the clusters at each step
        while (clusters.size() > 1) {
            double[][] distanceMatrix = new double[clusters.size()][clusters.size()];
            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    double distance = clusters.get(i).singleLinkageDistance(clusters.get(j));
                    distanceMatrix[i][j] = distance;
                    distanceMatrix[j][i] = distance;
                }
            }
            printDistanceMatrix(distanceMatrix);

            double minDistance = Double.MAX_VALUE;
            int cluster1Index = 0;
            int cluster2Index = 0;

            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    double distance = distanceMatrix[i][j];
                    if (distance < minDistance) {
                        minDistance = distance;
                        cluster1Index = i;
                        cluster2Index = j;
                    }
                }
            }

            // Merge the two closest clusters
            Cluster mergedCluster = clusters.get(cluster1Index);
            Cluster removedCluster = clusters.get(cluster2Index);
            mergedCluster.merge(removedCluster);
            clusters.remove(removedCluster);

            step++;
            System.out.println("Step " + step + ":");
            for (int i = 0; i < clusters.size(); i++) {
                System.out.print("Cluster " + (i + 1) + ": ");
                for (DataPoint point : clusters.get(i).points) {
                    System.out.print("P" + point.getId() + " ");
                }
                System.out.println();
            }
        }

        // The final clusters
        System.out.println("Final Clusters:");
        for (int i = 0; i < clusters.size(); i++) {
            System.out.print("Cluster " + (i + 1) + ": ");
            for (DataPoint point : clusters.get(i).points) {
                System.out.print("P" + point.getId() + " ");
            }
            System.out.println();
        }
    }

    private static void printDistanceMatrix(double[][] distanceMatrix) {
        System.out.println("Distance Matrix:");
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                System.out.printf("%.2f\t", distanceMatrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
