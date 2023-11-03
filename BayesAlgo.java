import java.util.*;

class Data{
    int count=0;
    Map<String,Integer>freq=new HashMap<>();
    double probVariable=1;

    void calculateProbability(String s) {
        Integer frequency = freq.get(s);
        if (frequency != null && count != 0) {
            probVariable *= frequency / (double) count;
        }
    }
}

public class BayesAlgo {
     public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        
        System.out.println("Enter the number of row and column:");
        int n=sc.nextInt();
        int m=sc.nextInt();
        sc.next();
        String data[][]=new String[n][m];
        for (int i = 0; i < n; i++) {
            String row = sc.nextLine();
            String[] values = row.split(" ");

            if (values.length != m) {
                System.out.println("Invalid input. Number of columns does not match.");
                sc.close();
                return;
            }

            for (int j = 0; j < m; j++) {
                data[i][j] = values[j];
            }
        }
        
        System.out.println("Enter the column name for class variable:");
        String classVariable=sc.next();
         
        Map<String,Data>distinct=new HashMap<>();

        for (int i = 0; i < data[0].length; i++) {
            if (data[0][i].equals(classVariable)) {
                for (int j = 1; j < data.length; j++) {
                    String classValue = data[j][i];
                    if (!distinct.containsKey(classValue)) {
                        distinct.put(classValue, new Data());
                    }
                    Data classData = distinct.get(classValue);
                    classData.count++;
                    for (int k = 0; k < data[0].length; k++) {
                        if (k == i)
                            continue;
                        int cnt = classData.freq.getOrDefault(data[j][k], 0);
                        classData.freq.put(data[j][k], ++cnt);
                    }
                }
            }
        }
        
        System.out.println("Enter the number of inputs:");
        int col=sc.nextInt();

        System.out.println("Enter the tuple in columnValue:");
        
        for (int i = 0; i < col; i++) {
            String s=sc.next();
            for(Map.Entry<String,Data> it : distinct.entrySet()){
                 it.getValue().calculateProbability(s);
            }
        }
        
        String ans="";
        double ansProb=0;
        double total=0;

        for(Map.Entry<String,Data> it : distinct.entrySet()){
            total+=(it.getValue().probVariable*(it.getValue().count*1.0)/(n-1));
            if((it.getValue().probVariable*(it.getValue().count*1.0)/(n-1))>ansProb){
                ans=it.getKey();
                ansProb=(it.getValue().probVariable*(it.getValue().count*1.0)/(n-1));
            }
        }
        
        System.out.println("\nFinal class:"+ans);
        System.out.println("Probability:"+(ansProb/total));
        sc.close();
    }
    
}


/*
OUTPUT:
Enter the number of row and column:
6
4
Outlook Temperature Humidity PlayTennis
Sunny Hot High No
Sunny Hot High Yes
Overcast Hot High No
Rainy Mild High No
Rainy Cool Normal Yes
Enter the column name for class variable:
PlayTennis
Enter the number of inputs:
2
Enter the tuple in columnValue:
Rainy Cool

Final class:No
Probability:0.6666666666666666
 */