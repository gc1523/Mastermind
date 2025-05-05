import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinationGenerator {
    // Generates all lists of length n comprised of elements 0-m
    public static Set<List<Integer>> generateAllCombinations(int n, int m) {
        Set<List<Integer>> results = new HashSet<>();
        generateRecursive(n, m, new ArrayList<>(), results);
        return results;
    }

    private static void generateRecursive(int n, int m, List<Integer> current, Set<List<Integer>> results) {
        if (current.size() == n) {
            results.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i <= m; i++) {
            current.add(i);
            generateRecursive(n, m, current, results);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        CombinationGenerator.generateAllCombinations(4,5);
    }
}