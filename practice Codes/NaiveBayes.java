import java.util.Scanner;

public class NaiveBayes {
    static String[][] data = {
            { "Good", "High", "A", "Pass" },
            { "Good", "Low", "B", "Pass" },
            { "Poor", "High", "C", "Fail" },
            { "Poor", "Low", "B", "Fail" },
            { "Good", "Low", "A", "Pass" },
            { "Good", "High", "C", "Pass" }
    };
    static double P_PASS;
    static double P_FAIL;

    public static void main(String[] args) {
        computeProbabilities();
        testClassifier();
    }

    public static void computeProbabilities() {
        int passCount = 0;
        int failCount = 0;
        int totalStudents = data.length;
        for (String[] row : data) {
            String study = row[0];
            String sports = row[1];
            String grade = row[2];
            String result = row[3];
            if (result.equals("Pass")) {
                passCount++;
            } else {
                failCount++;
            }
        }
        P_PASS = (double) passCount / totalStudents;
        P_FAIL = (double) failCount / totalStudents;
    }

    public static void testClassifier() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter comma-separated test tuple (e.g., 'Good,High,A'): ");
        String input = scanner.nextLine();
        String[] test = input.split(",");
        double pPass = P_PASS;
        double pFail = P_FAIL;
        for (String[] row : data) {
            String study = row[0];
            String sports = row[1];
            String grade = row[2];
            String result = row[3];
            if (study.equals(test[0]) && sports.equals(test[1]) && grade.equals(test[2])) {
                if (result.equals("Pass")) {
                    pPass++;
                } else {
                    pFail++;
                }
            }
        }
        pPass /= (P_PASS + P_FAIL);
        pFail /= (P_PASS + P_FAIL);
        System.out.println("Probability of Passing: " + pPass);
        System.out.println("Probability of Failing: " + pFail);
        if (pPass > pFail) {
            System.out.println("Prediction: Pass");
        } else {
            System.out.println("Prediction: Fail");
        }
    }
}