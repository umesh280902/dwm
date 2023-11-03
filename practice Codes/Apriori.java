import java.util.ArrayList;
import java.util.List;

public class Apriori {
    public static void main(String[] args) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] { "T100", "Mobile", "Laptop", "Tablet" });
        data.add(new String[] { "T200", "Laptop", "Charger" });
        data.add(new String[] { "T300", "Laptop", "Headphones" });
        data.add(new String[] { "T400", "Mobile", "Laptop", "Charger" });
        data.add(new String[] { "T500", "Mobile", "Tablet" });
        data.add(new String[] { "T600", "Laptop", "Headphones" });
        data.add(new String[] { "T700", "Mobile", "Laptop", "Headphones" });
        data.add(new String[] { "T800", "Mobile", "Laptop", "Tablet", "Charger" });
        data.add(new String[] { "T900", "Mobile", "Laptop", "Tablet", "Headphones" });
        int minSupport = 5;
        List<String> uniqueItems = new ArrayList<>();
        for (String[] transaction : data) {
            for (int i = 1; i < transaction.length; i++) {
                if (!uniqueItems.contains(transaction[i])) {
                    uniqueItems.add(transaction[i]);
                }
            }
        }
        uniqueItems.sort(String::compareTo);
        List<List<List<String>>> frequentItemsets = generateFrequentItemsets(data,
                uniqueItems, minSupport);
        double minConfidence = 0.5;
        List<AssociationRule> associationRules = generateAssociationRules(frequentItemsets,
                minConfidence, data);
        System.out.println("Frequent Itemsets:");
        for (int i = 0; i < frequentItemsets.size(); i++) {
            System.out.println("L" + (i + 1) + ":");
            List<List<String>> itemsetList = frequentItemsets.get(i);
            for (List<String> itemset : itemsetList) {
                System.out.println(itemset + " (Support: " + countSupport(data, itemset) + ")");
            }
        }
        System.out.println("\nAssociation Rules:");
        for (AssociationRule rule : associationRules) {
            System.out.println(rule.antecedent + " => " + rule.consequent + " (Confidence: " +
                    rule.confidence + ")");
        }
    }

    public static List<List<List<String>>> generateFrequentItemsets(List<String[]> data,
            List<String> uniqueItems, int minSupport) {
        List<List<List<String>>> frequentItemsets = new ArrayList<>();
        List<List<String>> itemsets = new ArrayList<>();
        for (String item : uniqueItems) {
            List<String> itemset = new ArrayList<>();
            itemset.add(item);
            itemsets.add(itemset);
        }
        frequentItemsets.add(itemsets);
        int k = 2;
        while (true) {
            List<List<String>> ck = generateCandidateItemsets(frequentItemsets.get(k - 2), k);
            List<List<String>> lk = new ArrayList<>();
            for (List<String> itemset : ck) {
                if (countSupport(data, itemset) >= minSupport) {
                    lk.add(itemset);
                }
            }
            if (lk.isEmpty()) {
                break;
            }
            frequentItemsets.add(lk);
            k++;
        }
        return frequentItemsets;
    }

    public static List<List<String>> generateCandidateItemsets(List<List<String>> lkMinus2,
            int k) {
        List<List<String>> ck = new ArrayList<>();
        for (int i = 0; i < lkMinus2.size(); i++) {
            for (int j = i + 1; j < lkMinus2.size(); j++) {
                List<String> itemset1 = lkMinus2.get(i);
                List<String> itemset2 = lkMinus2.get(j);
                if (canJoin(itemset1, itemset2, k)) {
                    List<String> joinedItemset = join(itemset1, itemset2);
                    if (!isAlreadyIn(ck, joinedItemset)) {
                        ck.add(joinedItemset);
                    }
                }
            }
        }
        return ck;
    }

    public static boolean canJoin(List<String> itemset1, List<String> itemset2, int k) {
        if (itemset1.subList(0, k - 2).equals(itemset2.subList(0, k - 2)) &&
                itemset1.get(k - 2).compareTo(itemset2.get(k - 2)) < 0) {
            return true;
        }
        return false;
    }

    public static List<String> join(List<String> itemset1, List<String> itemset2) {
        List<String> joinedItemset = new ArrayList<>(itemset1);
        joinedItemset.add(itemset2.get(itemset2.size() - 1));
        return joinedItemset;
    }

    public static boolean isAlreadyIn(List<List<String>> itemsets, List<String> itemset) {
        for (List<String> i : itemsets) {
            if (i.equals(itemset)) {
                return true;
            }
        }
        return false;
    }

    public static int countSupport(List<String[]> data, List<String> itemset) {
        int support = 0;
        for (String[] transaction : data) {
            List<String> transactionList = new ArrayList<>();
            for (int i = 1; i < transaction.length; i++) {
                transactionList.add(transaction[i]);
            }
            if (transactionList.containsAll(itemset)) {
                support++;
            }
        }
        return support;
    }

    public static List<AssociationRule> generateAssociationRules(List<List<List<String>>> frequentItemsets,
            double minConfidence, List<String[]> data) {
        List<AssociationRule> associationRules = new ArrayList<>();
        for (int i = 1; i < frequentItemsets.size(); i++) {
            List<List<String>> itemsetList = frequentItemsets.get(i);
            for (List<String> itemset : itemsetList) {
                int support = countSupport(data, itemset);
                List<List<String>> subsets = generateSubsets(itemset);
                for (List<String> antecedent : subsets) {
                    List<String> consequent = new ArrayList<>(itemset);
                    consequent.removeAll(antecedent);
                    double confidence = (double) support / countSupport(data, antecedent);
                    if (confidence >= minConfidence) {
                        AssociationRule rule = new AssociationRule(antecedent, consequent,
                                confidence);
                        associationRules.add(rule);
                    }
                }
            }
        }
        return associationRules;
    }

    public static List<List<String>> generateSubsets(List<String> itemset) {
        List<List<String>> subsets = new ArrayList<>();
        int n = itemset.size();
        for (int i = 1; i < (1 << n); i++) {
            List<String> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    subset.add(itemset.get(j));
                }
            }
            subsets.add(subset);
        }
        return subsets;
    }
}

class AssociationRule {
    List<String> antecedent;
    List<String> consequent;
    double confidence;

 public AssociationRule(List<String> antecedent, List<String> consequent, double
confidence) {
 this.antecedent = antecedent;
 this.consequent = consequent;
 this.confidence = confidence;
 }
}