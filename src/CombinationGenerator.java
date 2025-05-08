import java.util.ArrayList;
import java.util.List;

public class CombinationGenerator {
    // Generates all lists of length n comprised of elements 0-m
    public static List<List<Integer>> generateAllCombinations(int n, int m) {
        List<List<Integer>> results = new ArrayList<>();
        generateRecursive(n, m, new ArrayList<>(n), results);
        return results;
    }

    private static void generateRecursive(int n, int m, List<Integer> current, List<List<Integer>> results) {
        if (current.size() == n) {
            results.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i <= m; i++) {
            current.add(i);
            generateRecursive(n, m, current, results);
            current.removeLast();
        }
    }
}
