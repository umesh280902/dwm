import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PageRank {
    public static void main(String[] args) {
        int n = 4;
        int[][] adjMat = {
                { 0, 1, 1, 1 },
                { 0, 1, 0, 1 },
                { 1, 0, 1, 1 },
                { 1, 0, 1, 0 }
        };
        System.out.println("Entered Adjacency Matrix is:\n");
        for (int[] row : adjMat) {
            System.out.println(Arrays.toString(row));
        }
        int[] outDegree = new int[n];
        for (int i = 0; i < n; i++) {
            int outCount = 0;
            for (int j = 0; j < n; j++) {
                if (adjMat[i][j] == 1) {
                    outCount++;
                }
            }
            outDegree[i] = outCount;
        }
        System.out.println("\nTotal Outgoing Links from Each Node:\n");
        for (int i = 0; i < n; i++) {
            System.out.println("Node " + (i + 1) + ": " + outDegree[i]);
        }
        double[][] A = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (adjMat[j][i] == 1) {
                    A[j][i] = 1.0 / outDegree[j];
                } else {
                    A[j][i] = 0.0;
                }
            }
        }
        System.out.println("\nTransition Probability Matrix A:\n");
        for (double[] row : A) {
            System.out.println(Arrays.toString(row));
        }
        double[][] X = new double[n][1];
        for (int i = 0; i < n; i++) {
            X[i][0] = 1.0;
        }
        double[][] prev = new double[n][1];
        System.out.println("Final matrix after 3 iterations\n");
        for (int iter = 0; iter < 3; iter++) {
            prev = X;
            X = multiplyMatrix(A, X);
        }
        for (double[] row : X) {
            System.out.println(Arrays.toString(row));
        }
        final int[] ctn = { 1 }; // Use an array to make it effectively final
        Map<String, Double> ranks = new HashMap<>();
        for (int i = 0; i < X.length; i++) {
            ranks.put("Page " + (i + 1), X[i][0]);
        }
        ranks.entrySet()
                .stream()
                .sorted((entry1, entry2) -> -Double.compare(entry1.getValue(),
                        entry2.getValue()))
                .forEach(entry -> System.out.println("Rank " + (ctn[0]++) + ": " +
                        entry.getKey()));
    }

    public static double[][] multiplyMatrix(double[][] A, double[][] B) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int colsB = B[0].length;
        double[][] C = new double[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                double sum = 0;
                for (int k = 0; k < colsA; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }
        return C;
    }
}