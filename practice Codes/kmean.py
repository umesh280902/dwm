import random
import math

class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def distance(self, other):
        return math.sqrt((self.x - other.x) ** 2 + (self.y - other.y) ** 2)

class Cluster:
    def __init__(self, centroid):
        self.centroid = centroid
        self.points = []

    def clear(self):
        self.points.clear()

    def add_point(self, point):
        self.points.append(point)

    def update_centroid(self):
        if self.points:
            self.centroid.x = sum(point.x for point in self.points) / len(self.points)
            self.centroid.y = sum(point.y for point in self.points) / len(self.points)

def generate_random_point(data):
    return Point(random.choice(data).x, 0)

def initialize_clusters(data, k):
    random.seed(0)  # You can change the seed for reproducibility
    return [Cluster(generate_random_point(data)) for _ in range(k)]

def main():
    data = [
        Point(1,1),
        Point(2,1),
        Point(4,3),
        Point(5,4),
            ]

    k = 2  # Number of clusters
    clusters = initialize_clusters(data, k)

    converged = False
    max_iterations = 100
    iter = 0

    while not converged and iter < max_iterations:
        converged = True  # Assume convergence
        iter += 1

        for cluster in clusters:
            cluster.clear()

        for point in data:
            nearest_cluster = min(clusters, key=lambda cluster: point.distance(cluster.centroid))
            nearest_cluster.add_point(point)

        for cluster in clusters:
            if cluster.points:  # Check if the cluster has points before updating centroid
                old_centroid = Point(cluster.centroid.x, cluster.centroid.y)
                cluster.update_centroid()

                if old_centroid.distance(cluster.centroid) > 0.0001:
                    converged = False

    for i, cluster in enumerate(clusters):
        print(f"Cluster {i + 1} - Centroid: ({cluster.centroid.x}, {cluster.centroid.y})")
        print("Points in the cluster:")
        for point in cluster.points:
            print(f"({point.x}, {point.y})")
        print()

if __name__ == "__main__":
    main()
