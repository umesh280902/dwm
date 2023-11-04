import java.util.ArrayList;
import java.util.List;

class Cluster {
    double[] centroid;
    List<Cluster> children = new ArrayList<>();

    Cluster(double[] centroid) {
        this.centroid = centroid;
    }
}

public class HierarchicalClusteringSimple {
    public static void main(String[] args) {
        int numPoints = Integer.parseInt(System.console().readLine("Enter the number of data points: "));
        List<double[]> dataPoints = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            int numDimensions = Integer.parseInt(System.console().readLine("Enter the number of dimensions (2 or 3): "));
            double[] point = new double[numDimensions];
            for (int j = 0; j < numDimensions; j++) {
                point[j] = Double.parseDouble(System.console().readLine("Enter value " + (j + 1) + ": "));
            }
            dataPoints.add(point);
        }

        hierarchicalClustering(dataPoints);
    }

    static void hierarchicalClustering(List<double[]> dataPoints) {
        List<Cluster> clusters = new ArrayList<>();
        for (double[] point : dataPoints) {
            clusters.add(new Cluster(point));
        }

        int clusterNumber = 1;
        while (clusters.size() > 1) {
            System.out.println("\nDistance Matrix (Step " + clusterNumber + "):");
            printDistanceMatrix(clusters);

            int[] closestPair = findClosestClusters(clusters);
            Cluster mergedCluster = mergeClusters(clusters.get(closestPair[0]), clusters.get(closestPair[1]));
            clusters.remove(closestPair[1]);
            clusters.remove(closestPair[0]);
            clusters.add(mergedCluster);

            System.out.println("Cluster " + clusterNumber + ":");
            printCluster(mergedCluster);
            clusterNumber++;
        }

        if (clusters.size() == 1) {
            System.out.println("\nFinal Cluster:");
            printCluster(clusters.get(0));
        }
    }

    static double calculateDistance(double[] centroid1, double[] centroid2) {
        double sum = 0.0;
        for (int i = 0; i < centroid1.length; i++) {
            double diff = centroid1[i] - centroid2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    static int[] findClosestClusters(List<Cluster> clusters) {
        double minDistance = Double.POSITIVE_INFINITY;
        int[] closestPair = {0, 1};

        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                double distance = calculateDistance(clusters.get(i).centroid, clusters.get(j).centroid);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPair[0] = i;
                    closestPair[1] = j;
                }
            }
        }

        return closestPair;
    }

    static Cluster mergeClusters(Cluster cluster1, Cluster cluster2) {
        double[] mergedCentroid = new double[cluster1.centroid.length];
        for (int i = 0; i < cluster1.centroid.length; i++) {
            mergedCentroid[i] = (cluster1.centroid[i] + cluster2.centroid[i]) / 2.0;
        }

        List<Cluster> children = new ArrayList<>();
        children.add(cluster1);
        children.add(cluster2);

        Cluster mergedCluster = new Cluster(mergedCentroid);
        mergedCluster.children = children;
        return mergedCluster;
    }

    static void printCluster(Cluster cluster) {
        printCluster(cluster, 0);
    }

    static void printCluster(Cluster cluster, int level) {
        String indentation = "  ".repeat(level);
        if (cluster.children.size() > 0) {
            System.out.println(indentation + "Data Points: " + cluster.children.size());
            for (Cluster child : cluster.children) {
                printCluster(child, level + 1);
            }
        } else {
            System.out.println(indentation + "Data Point: " + arrayToString(cluster.centroid));
        }
    }

    static void printDistanceMatrix(List<Cluster> clusters) {
        int numClusters = clusters.size();
        double[][] distanceMatrix = new double[numClusters][numClusters];

        for (int i = 0; i < numClusters; i++) {
            for (int j = i + 1; j < numClusters; j++) {
                distanceMatrix[i][j] = calculateDistance(clusters.get(i).centroid, clusters.get(j).centroid);
                distanceMatrix[j][i] = distanceMatrix[i][j];
            }
        }

        for (int i = 0; i < numClusters; i++) {
            System.out.print("Cluster " + i + ": ");
            for (int j = 0; j < numClusters; j++) {
                System.out.printf("%.2f ", distanceMatrix[i][j]);
            }
            System.out.println();
        }
    }

    static String arrayToString(double[] array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
