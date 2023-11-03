import random
import math

class Point:
    def __init__(self, x, y):
        self.x, self.y = x, y

class Cluster:
    def __init__(self, centroid):
        self.centroid = centroid
        self.points = []

    def clear_points(self):
        self.points.clear()

class KMeans:
    def __init__(self, k, max_iterations, data):
        self.k, self.max_iterations, self.data, self.clusters = k, max_iterations, data, []

    def distance(self, p1, p2):
        return math.sqrt((p1.x - p2.x) ** 2 + (p1.y - p2.y) ** 2)

    def assign_points_to_clusters(self):
        for cluster in self.clusters:
            cluster.clear_points()

        for point in self.data:
            nearest_cluster = min(self.clusters, key=lambda cluster: self.distance(point, cluster.centroid))
            nearest_cluster.points.append(point)

    def update_cluster_centroids(self):
        for cluster in self.clusters:
            points = cluster.points
            if points:
                cluster.centroid = Point(sum(p.x for p in points) / len(points), sum(p.y for p in points) / len(points))

    def compute_wcss(self):
        return sum(self.distance(point, cluster.centroid) ** 2 for cluster in self.clusters for point in cluster.points)

    def run_kmeans(self, num_runs=10):
        best_clusters, best_wcss = None, float('inf')

        for _ in range(num_runs):
            self.clusters.clear()
            self.clusters = [Cluster(random.choice(self.data)) for _ in range(self.k)]

            for _ in range(self.max_iterations):
                self.assign_points_to_clusters()
                self.update_cluster_centroids()

            wcss = self.compute_wcss()
            if wcss < best_wcss:
                best_wcss = wcss
                best_clusters = [cluster.points.copy() for cluster in self.clusters]

        self.clusters = best_clusters

    def get_clusters(self):
        return self.clusters

if __name__ == "__main__":
    random.seed(42) 
    data = [Point(185, 72), Point(170, 56), Point(168, 60), Point(179, 68), Point(182, 72), Point(188, 77), Point(180, 71), Point(180, 70), Point(183, 84), Point(180, 88), Point(180, 67), Point(177, 76)]
    k, max_iterations = 2, 100
    
    kmeans = KMeans(k, max_iterations, data)
    kmeans.run_kmeans(num_runs=10)

    for i, cluster in enumerate(kmeans.get_clusters()):
        print(f"\nCluster {i + 1} Centroid: ({cluster[0].x}, {cluster[0].y})")
        print(f"Points in Cluster {i + 1} ({len(cluster)} points):")
        for point in cluster:
            print(f"({point.x}, {point.y})")
