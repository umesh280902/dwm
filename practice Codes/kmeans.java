import java.util.*;

class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        return Double.compare(Math.round(x), Math.round(other.x)) == 0 &&
                Double.compare(Math.round(y), Math.round(other.y)) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(Math.round(x)) + Double.hashCode(Math.round(y));
    }
}

class Cluster {
    Point centroid;
    List<Point> points;

    Cluster(Point centroid) {
        this.centroid = centroid;
        this.points = new ArrayList<>();
    }

    void clearPoints() {
        points.clear();
    }

    void addPoint(Point point) {
        points.add(point);
    }
}

class kmeans {
    private int k;
    private int maxIterations;
    private List<Point> data;
    private List<Cluster> clusters;

    public kmeans(int k, int maxIterations, List<Point> data) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.data = data;
        this.clusters = new ArrayList<>();
    }

    double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    void initializeClusters() {
        if (data.size() < k) {
            throw new IllegalArgumentException("Not enough data points to initialize " + k + " clusters.");
        }
        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            Point randomPoint = data.get(rand.nextInt(data.size()));
            clusters.add(new Cluster(randomPoint));
        }
    }

    void assignPointsToClusters() {
        clusters.forEach(Cluster::clearPoints);
        for (Point point : data) {
            Cluster nearestCluster = null;
            double minDistance = Double.MAX_VALUE;
            for (Cluster cluster : clusters) {
                double d = distance(point, cluster.centroid);
                if (d < minDistance) {
                    minDistance = d;
                    nearestCluster = cluster;
                }
            }
            if (nearestCluster != null) {
                nearestCluster.addPoint(point);
            }
        }
    }

    void updateClusterCentroids() {
        for (Cluster cluster : clusters) {
            List<Point> clusterPoints = cluster.points;
            if (!clusterPoints.isEmpty()) {
                double sumX = clusterPoints.stream().mapToDouble(p -> p.x).sum();
                double sumY = clusterPoints.stream().mapToDouble(p -> p.y).sum();
                int numPoints = clusterPoints.size();
                cluster.centroid.x = sumX / numPoints;
                cluster.centroid.y = sumY / numPoints;
            }
        }
    }

    void runKMeans() {
        initializeClusters();
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            assignPointsToClusters();
            updateClusterCentroids();
        }
    }

    List<Cluster> getClusters() {
        return clusters;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Point> data = new ArrayList<>();
        System.out.println("Enter the number of data points:");
        int numPoints = scanner.nextInt();
        for (int i = 0; i < numPoints; i++) {
            System.out.println("Enter data point " + (i + 1) + " (x y):");
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            data.add(new Point(x, y));
        }
        System.out.println("\nEnter the number of clusters (k):");
        int k = scanner.nextInt();
        System.out.println("Enter the maximum number of iterations:");
        int maxIterations = scanner.nextInt();
        kmeans kMeans = new kmeans(k, maxIterations, data);
        kMeans.runKMeans();
        List<Cluster> clusters = kMeans.getClusters();
        for (int i = 0; i < clusters.size(); i++) {
            Cluster cluster = clusters.get(i);
            System.out.println(
                    "\nCluster " + (i + 1) + "Centroid: (" + cluster.centroid.x + ", " + cluster.centroid.y + ")");
            System.out.println("Points in Cluster " + (i +
                    1) + ": " + cluster.points.size());
            System.out.println("Original Points in Cluster" + (i + 1) + ":");
            for (Point originalPoint : cluster.points) {
                if (cluster.points.contains(originalPoint)) {
                    System.out.println("(" +
                            originalPoint.x + ", " + originalPoint.y + ")");
                }
            }
        }

    }
}
