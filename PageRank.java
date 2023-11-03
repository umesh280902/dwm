import java.util.Scanner;

public class PageRank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of websites: ");
        int n = scanner.nextInt();
        int[][] relation = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            System.out.println("Enter '1' if website " + (i + 1) + " contains a hyperlink directing to following website(s) else enter '0': ");
            for (int j = 0; j < n; j++) {
                int x = scanner.nextInt();
                relation[i][j] = x;
            }
        }

        double[] pageRank = new double[n];
        for (int i = 0; i < n; i++) {
            pageRank[i] = 1.0 / n;
        }

        System.out.print("Initially the page rank for each page is: ");
        for (int i = 0; i < n; i++) {
            System.out.print(pageRank[i] + " ");
        }

        int m = 0;
        while (m < 3) {
            m++;
            for (int i = 0; i < n; i++) {
                double[] temp = new double[n];
                for (int j = 0; j < n; j++) {
                    if (relation[j][i] == 1) {
                        int count = 0;
                        for (int k = 0; k < n; k++) {
                            if (relation[j][k] == 1) {
                                count++;
                            }
                        }
                        double y = pageRank[j] / count;
                        temp[j] = y;
                    }
                }
                double sum = 0;
                for (int j = 0; j < n; j++) {
                    sum += temp[j];
                }
                pageRank[i] = sum;
            }
            System.out.print("\nThe new Page Ranks after iteration " + m + " are: ");
            for (int i = 0; i < n; i++) {
                System.out.print(pageRank[i] + " ");
            }
        }

        double maxPageRank = pageRank[0];
        double minPageRank = pageRank[0];
        int maxIndex = 0;
        int minIndex = 0;

        for (int i = 1; i < n; i++) {
            if (pageRank[i] > maxPageRank) {
                maxPageRank = pageRank[i];
                maxIndex = i;
            }
            if (pageRank[i] < minPageRank) {
                minPageRank = pageRank[i];
                minIndex = i;
            }
        }

        System.out.println("\nWebsite " + (maxIndex + 1) + " has the highest page rank whereas Website " + (minIndex + 1) + " has the lowest page rank.");
    }
}
