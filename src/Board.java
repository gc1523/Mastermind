import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
    private static final int MAX_ROUNDS = 20;
    private final int NUM_COLOURS;
    private final int NUM_SLOTS;
    private List<Integer> solution;
    int rounds = 0;
    boolean inPlay = true;

    public Board(int num_colours, int num_slots) {
        this.NUM_COLOURS = num_colours;
        this.NUM_SLOTS = num_slots;
        this.solution = generateSolution();
    }

    private List<Integer> generateSolution() {
        List<Integer> solution = new ArrayList<>();
        for (int i = 0; i < NUM_SLOTS; i++) {
            solution.add(ThreadLocalRandom.current().nextInt(NUM_COLOURS));
        }
        // System.out.println("SOLUTION GENERATED:" + solution);
        return solution;
    }

    public List<ResultCombinations.Result> guess(List<Integer> guess){
        rounds++;
        List<ResultCombinations.Result> results = new ArrayList<>(Collections.nCopies(NUM_SLOTS, ResultCombinations.Result.INCORRECT));
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (Objects.equals(guess.get(i), solution.get(i))) {
                results.set(i, ResultCombinations.Result.CORRECT);
            } else {
                freqMap.put(solution.get(i), freqMap.getOrDefault(solution.get(i), 0) + 1);
            }
        }
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (results.get(i) == ResultCombinations.Result.CORRECT) continue;

            int g = guess.get(i);
            if (freqMap.getOrDefault(g, 0) > 0) {
                results.set(i, ResultCombinations.Result.PRESENT);
                freqMap.put(g, freqMap.get(g) - 1);
            }
        }

        Collections.shuffle(results);
        if (results.size() == NUM_SLOTS && results.stream().allMatch((r -> r == ResultCombinations.Result.CORRECT))) {
            // System.out.println("CORRECT GUESS");
            inPlay = false;
        } else if (rounds >= MAX_ROUNDS) {
            System.out.println("TOO MANY GUESSES\n Solution was:" + solution);
        }
        return results;
    }
}
