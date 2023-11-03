import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HierarchicalClusteringSimple {

    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        System.out.print("Enter the number of data points: ");
        int numPoints = scanner.nextInt();

        List<double[]> dataPoints = generateData(numPoints); // Generate user-defined data
        List<Cluster> clusters = initializeClusters(dataPoints);

        int clusterNumber = 1;

        while (clusters.size() > 1) {
            int[] closestPair = findClosestClusters(clusters);
            Cluster mergedCluster = mergeClusters(clusters.remove(closestPair[1]), clusters.remove(closestPair[0]));
            clusters.add(mergedCluster);

            System.out.println("Cluster " + clusterNumber + ":");
            printCluster(mergedCluster, 1);
            System.out.println();

            // Calculate and print the distance matrix
            double[][] distanceMatrix = calculateDistanceMatrix(clusters);
            System.out.println("Distance Matrix:");
            printDistanceMatrix(distanceMatrix);
            System.out.println();

            clusterNumber++;
        }

        // Handle the case when there's only one cluster left
        if (clusters.size() == 1) {
            System.out.println("Final Cluster:");
            printCluster(clusters.get(0), 1);
        }
    }

    private static List<double[]> generateData(int numPoints) {
        List<double[]> data = new ArrayList<>();

        for (int i = 0; i < numPoints; i++) {
            System.out.print("Enter the number of dimensions (2 or 3): ");
            int numDimensions = scanner.nextInt();

            double[] point = new double[numDimensions];
            System.out.println("Enter " + numDimensions + " values for data point " + (i + 1) + ":");

            for (int j = 0; j < numDimensions; j++) {
                point[j] = scanner.nextDouble();
            }

            data.add(point);
        }
        return data;
    }

    private static List<Cluster> initializeClusters(List<double[]> data) {
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            clusters.add(new Cluster(data.get(i), i));
        }
        return clusters;
    }

    private static double calculateDistance(Cluster cluster1, Cluster cluster2) {
        double[] centroid1 = cluster1.getCentroid();
        double[] centroid2 = cluster2.getCentroid();
        double sumSquaredDifferences = 0;
        for (int i = 0; i < centroid1.length; i++) {
            double difference = centroid1[i] - centroid2[i];
            sumSquaredDifferences += difference * difference;
        }
        return Math.sqrt(sumSquaredDifferences);
    }

    private static int[] findClosestClusters(List<Cluster> clusters) {
        int[] closestPair = { 0, 1 };
        double minDistance = calculateDistance(clusters.get(0), clusters.get(1));

        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                double distance = calculateDistance(clusters.get(i), clusters.get(j));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPair[0] = i;
                    closestPair[1] = j;
                }
            }
        }

        return closestPair;
    }

    private static Cluster mergeClusters(Cluster cluster1, Cluster cluster2) {
        double[] mergedCentroid = new double[cluster1.getCentroid().length];
        for (int i = 0; i < mergedCentroid.length; i++) {
            mergedCentroid[i] = (cluster1.getCentroid()[i] + cluster2.getCentroid()[i]) / 2;
        }

        List<Integer> mergedDataPoints = new ArrayList<>(cluster1.getDataPoints());
        mergedDataPoints.addAll(cluster2.getDataPoints());

        return new Cluster(mergedCentroid, mergedDataPoints);
    }

    private static void printCluster(Cluster cluster, int level) {
        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indentation.append("  ");
        }

        if (cluster.getDataPoints().size() > 1) {
            System.out.println(indentation.toString() + "Data Points: " + cluster.getDataPoints());
            for (Cluster child : cluster.getChildren()) {
                printCluster(child, level + 1);
            }
        } else {
            System.out.println(indentation.toString() + "Data Point: " + cluster.getDataPoints().get(0));
        }
    }

    private static double[][] calculateDistanceMatrix(List<Cluster> clusters) {
        int numClusters = clusters.size();
        double[][] distanceMatrix = new double[numClusters][numClusters];

        for (int i = 0; i < numClusters; i++) {
            for (int j = 0; j < numClusters; j++) {
                if (i != j) {
                    distanceMatrix[i][j] = calculateDistance(clusters.get(i), clusters.get(j));
                } else {
                    distanceMatrix[i][j] = 0.0; // Diagonal elements are 0
                }
            }
        }

        return distanceMatrix;
    }

    private static void printDistanceMatrix(double[][] distanceMatrix) {
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[0].length; j++) {
                System.out.printf("%.2f ", distanceMatrix[i][j]);
            }
            System.out.println();
        }
    }

    public static class Cluster {
        private double[] centroid;
        private List<Integer> dataPoints;
        private List<Cluster> children;

        public Cluster(double[] centroid, int dataPoint) {
            this.centroid = centroid;
            this.dataPoints = new ArrayList<>();
            this.children = new ArrayList<>();
            dataPoints.add(dataPoint);
        }

        public Cluster(double[] centroid, List<Integer> dataPoints) {
            this.centroid = centroid;
            this.dataPoints = dataPoints;
            this.children = new ArrayList<>();
        }

        public double[] getCentroid() {
            return centroid;
        }

        public List<Integer> getDataPoints() {
            return dataPoints;
        }

        public List<Cluster> getChildren() {
            return children;
        }
    }
}
