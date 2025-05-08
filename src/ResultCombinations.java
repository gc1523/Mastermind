import java.util.*;

public class ResultCombinations {
    public enum Result {
        CORRECT,
        PRESENT,
        INCORRECT
    }

    public static List<List<Result>> generateUniqueCombinations(int y) {
        List<List<Result>> combinations = new ArrayList<>();
        Result[] values = Result.values();
        Arrays.sort(values); // ensure consistent order
        generateCombinationsRecursive(values, y, 0, new ArrayList<>(y), combinations);
        return combinations;
    }

    private static void generateCombinationsRecursive(Result[] values, int y, int index,
                                                      List<Result> current, List<List<Result>> result) {
        if (current.size() == y) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = index; i < values.length; i++) {
            current.add(values[i]);
            generateCombinationsRecursive(values, y, i, current, result); // allow reuse
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        int y = 4;
        List<List<Result>> combos = generateUniqueCombinations(y);
        for (List<Result> combo : combos) {
            System.out.println(combo);
        }
        System.out.println("Total unique combinations of size " + y + ": " + combos.size());
    }
}
