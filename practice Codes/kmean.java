import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Point other) {
        return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
    }
}

class Cluster {
    Point centroid;
    List<Point> points = new ArrayList<>();

    public Cluster(Point centroid) {
        this.centroid = centroid;
    }

    public void clear() {
        points.clear();
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void updateCentroid() {
        double sumX = 0, sumY = 0;
        for (Point point : points) {
            sumX += point.x;
            sumY += point.y;
        }
        centroid.x = sumX / points.size();
        centroid.y = sumY / points.size();
    }
}

public class kmean {
    public static void main(String[] args) {
        List<Point> data = new ArrayList<>();
        data.add(new Point(10,0));
        data.add(new Point(4,0));
        data.add(new Point(2,0));
        data.add(new Point(12,0));
        data.add(new Point(3,0));
        data.add(new Point(20,0));
        data.add(new Point(30,0));
        data.add(new Point(11,0));
        data.add(new Point(25,0));
        data.add(new Point(31,0));
        // Add your data points here

        int k = 2; // Number of clusters
        List<Cluster> clusters = initializeClusters(data, k);

        boolean converged = false;
        int maxIterations = 100;
        int iter = 0;

        while (!converged && iter < maxIterations) {
            converged = true; // Assume convergence
            iter++;

            // Clear the clusters
            for (Cluster cluster : clusters) {
                cluster.clear();
            }

            // Assign each point to the nearest cluster
            for (Point point : data) {
                double minDistance = Double.MAX_VALUE;
                Cluster nearestCluster = null;

                for (Cluster cluster : clusters) {
                    double distance = point.distance(cluster.centroid);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestCluster = cluster;
                    }
                }

                nearestCluster.addPoint(point);
            }

            // Update the centroids of the clusters and check for convergence
            for (Cluster cluster : clusters) {
                Point oldCentroid = new Point(cluster.centroid.x, cluster.centroid.y);
                cluster.updateCentroid();

                // Check for convergence by comparing old and new centroids
                if (oldCentroid.distance(cluster.centroid) > 0.0001) {
                    converged = false;
                }
            }
        }

        // Print the final clusters
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("Cluster " + (i + 1) + " - Centroid: (" + clusters.get(i).centroid.x + ", " + clusters.get(i).centroid.y + ")");
            System.out.println("Points in the cluster:");
            for (Point point : clusters.get(i).points) {
                System.out.println("(" + point.x + ", " + point.y + ")");
            }
            System.out.println();
        }
    }

    // Initialize K clusters with random data points as centroids
    private static List<Cluster> initializeClusters(List<Point> data, int k) {
        List<Cluster> clusters = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            Point randomPoint = data.get(random.nextInt(data.size()));
            clusters.add(new Cluster(new Point(randomPoint.x, randomPoint.y)));
        }
        return clusters;
    }
}
