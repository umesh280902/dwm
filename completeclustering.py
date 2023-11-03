import math

def calculate_distance(centroid1, centroid2):
    return math.sqrt(sum((a - b) ** 2 for a, b in zip(centroid1, centroid2)))

def find_closest_clusters(clusters):
    min_max_distance, closest_pair = float('inf'), [0, 1]
    for i in range(len(clusters)):
        for j in range(i + 1, len(clusters)):
            max_distance = max(calculate_distance(a, b) for a in clusters[i].data_points for b in clusters[j].data_points)
            if max_distance < min_max_distance:
                min_max_distance, closest_pair = max_distance, [i, j]
    return closest_pair

def merge_clusters(cluster1, cluster2):
    merged_data_points = cluster1.data_points + cluster2.data_points
    return Cluster(merged_data_points)

def print_cluster(cluster, level):
    indentation = "  " * level
    print(indentation + "Data Points: " + str(cluster.data_points))

def print_distance_matrix(clusters):
    num_clusters = len(clusters)
    distance_matrix = [[0.0] * num_clusters for _ in range(num_clusters)]

    for i in range(num_clusters):
        for j in range(i + 1, num_clusters):
            max_distance = max(calculate_distance(a, b) for a in clusters[i].data_points for b in clusters[j].data_points)
            distance_matrix[i][j] = max_distance
            distance_matrix[j][i] = max_distance

    for row in distance_matrix:
        print(" ".join(f"{distance:.2f}" for distance in row))

class Cluster:
    def __init__(self, data_points):
        self.data_points = data_points

def hierarchical_clustering_complete(num_points, data_points):
    clusters = [Cluster([point]) for point in data_points]
    cluster_number = 1

    while len(clusters) > 1:
        print("Distance Matrix (Step {}):".format(cluster_number))
        for i, cluster in enumerate(clusters):
            print(f"Cluster {i}: {cluster.data_points}")

        closest_pair = find_closest_clusters(clusters)
        merged_cluster = merge_clusters(clusters[closest_pair[0]], clusters[closest_pair[1]])
        clusters.pop(closest_pair[1])
        clusters[closest_pair[0]] = merged_cluster

        print(f"Cluster {cluster_number}:")
        print_cluster(merged_cluster, 1)
        print()

        print("Distance Matrix:")
        print_distance_matrix(clusters)
        print()

        cluster_number += 1

    if len(clusters) == 1:
        print("Final Cluster:")
        print_cluster(clusters[0], 1)

if __name__ == "__main__":
    num_points = int(input("Enter the number of data points: "))
    data_points = []
    for i in range(num_points):
        num_dimensions = int(input("Enter the number of dimensions (2 or 3): "))
        point = []
        print(f"Enter {num_dimensions} values for data point {i + 1}:")
        for j in range(num_dimensions):
            point.append(float(input()))
        data_points.append(point)
    hierarchical_clustering_complete(num_points, data_points)
