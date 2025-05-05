import java.util.*;

public class ResultCombinations {
    public enum Result {
        CORRECT,
        PRESENT,
        INCORRECT
    }

    public static Set<List<Result>> generateUniqueCombinations(int y) {
        Set<List<Result>> combinations = new HashSet<>();
        Result[] values = Result.values();
        generateCombinationsRecursive(values, y, 0, new ArrayList<>(), combinations);
        return combinations;
    }

    private static void generateCombinationsRecursive(Result[] values, int y, int start,
                                                      List<Result> current, Set<List<Result>> result) {
        if (current.size() == y) {
            List<Result> sorted = new ArrayList<>(current);
            sorted.sort(Comparator.naturalOrder()); // normalize to avoid duplicates
            result.add(sorted);
            return;
        }

        for (int i = start; i < values.length; i++) {
            current.add(values[i]);
            generateCombinationsRecursive(values, y, i, current, result); // allow same value again
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        int y = 4; // Change this to generate different sizes
        Set<List<Result>> combos = generateUniqueCombinations(y);
        for (List<Result> combo : combos) {
            System.out.println(combo);
        }
        System.out.println("Total combinations of size " + y + ": " + combos.size());
    }
}
