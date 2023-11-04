import math

class Cluster:
    def __init__(self, centroid, data_point=None, data_points=None):
        self.centroid = centroid
        self.data_points = [data_point] if data_point is not None else data_points
        self.children = []

def calculate_distance(centroid1, centroid2):
    return math.sqrt(sum((a - b) ** 2 for a, b in zip(centroid1, centroid2)))

def find_closest_clusters(clusters):
    min_distance, closest_pair = float('inf'), [0, 1]
    for i in range(len(clusters)):
        for j in range(i + 1, len(clusters)):
            distance = calculate_distance(clusters[i].centroid, clusters[j].centroid)
            if distance < min_distance:
                min_distance, closest_pair = distance, [i, j]
    return closest_pair

def merge_clusters(cluster1, cluster2):
    merged_centroid = [(a + b) / 2 for a, b in zip(cluster1.centroid, cluster2.centroid)]
    merged_data_points = cluster1.data_points + cluster2.data_points
    return Cluster(merged_centroid, data_points=merged_data_points)

def print_cluster(cluster, level=0):
    indentation = "  " * level
    if len(cluster.data_points) > 1:
        print(indentation + "Data Points: " + str(cluster.data_points))
        for child in cluster.children:
            print_cluster(child, level + 1)
    else:
        print(indentation + "Data Point: " + str(cluster.data_points[0]))

def print_distance_matrix(clusters):
    num_clusters = len(clusters)
    distance_matrix = [[0.0] * num_clusters for _ in range(num_clusters)]

    for i in range(num_clusters):
        for j in range(i + 1, num_clusters):
            distance_matrix[i][j] = calculate_distance(clusters[i].centroid, clusters[j].centroid)
            distance_matrix[j][i] = distance_matrix[i][j]

    for row in distance_matrix:
        print(" ".join(f"{distance:.2f}" for distance in row))

def hierarchical_clustering(num_points, data_points):
    clusters = [Cluster(centroid, i) for i, centroid in enumerate(data_points)]
    cluster_number = 1

    while len(clusters) > 1:
        print("Distance Matrix (Step {}):".format(cluster_number))
        print_distance_matrix(clusters)

        closest_pair = find_closest_clusters(clusters)
        merged_cluster = merge_clusters(clusters.pop(closest_pair[1]), clusters.pop(closest_pair[0]))
        clusters.append(merged_cluster)

        print(f"Cluster {cluster_number}:")
        print_cluster(merged_cluster)

        cluster_number += 1

    if len(clusters) == 1:
        print("Final Cluster:")
        print_cluster(clusters[0])

if __name__ == "__main":
    num_points = int(input("Enter the number of data points: "))
    data_points = []
    for i in range(num_points):
        num_dimensions = int(input("Enter the number of dimensions (2 or 3): "))
        point = [float(input(f"Enter value {j + 1}: ")) for j in range(num_dimensions)]
        data_points.append(point)
    hierarchical_clustering(num_points, data_points)
