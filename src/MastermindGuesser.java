import java.util.*;
import java.util.stream.Collectors;

public class MastermindGuesser {
    private int NUM_SLOTS;
    private int NUM_COLOURS;
    boolean doPrinting = false;
    private Map<List<Integer>, List<ResultCombinations.Result>> information = new HashMap<>();
    private List<List<Integer>> allGuesses;
    private List<List<Integer>> workingSet;

    public MastermindGuesser(int num_colours, int num_slots) {
        this.NUM_COLOURS = num_colours;
        this.NUM_SLOTS = num_slots;
        this.workingSet = CombinationGenerator.generateAllCombinations(NUM_SLOTS, NUM_COLOURS - 1);
        this.allGuesses = CombinationGenerator.generateAllCombinations(NUM_SLOTS, NUM_COLOURS - 1);
    }
    void provideInfo(List<Integer> guess, List<ResultCombinations.Result> result) {
        information.put(guess, result);
        updateWorkingSet();
    }

    public List<Integer> generateGuess() {
        if (information.isEmpty()) {
            List<Integer> result = new ArrayList<>(NUM_SLOTS);
            int half = NUM_SLOTS / 2;
            for (int i = 0; i < NUM_SLOTS; i++) {
                result.add(i < half ? 0 : 1);
            }
            return result; // This is knuth's described starting guess.
        } else if (workingSet.size() == 1) {
            return workingSet.iterator().next();
        }

        Map<List<Integer>, Integer> guessScores = allGuesses
                .parallelStream()
                .collect(Collectors.toMap(
                        guess -> guess,
                        guess -> {
                            Map<String, Integer> responseCountMap = new HashMap<>();
                            for (List<Integer> possibleCode : workingSet) {
                                int[] feedback = getFeedback(possibleCode, guess);
                                String key = feedback[0] + "B" + feedback[1] + "W";
                                responseCountMap.put(key, responseCountMap.getOrDefault(key, 0) + 1);
                            }
                            return responseCountMap.values().stream().max(Integer::compareTo).orElse(0);
                        }
                ));
        return guessScores.entrySet()
                .stream()
                .min(Comparator
                        .comparingInt((Map.Entry<List<Integer>, Integer> e) -> e.getValue())
                        .thenComparing(e -> !workingSet.contains(e.getKey()))
                        )
                .map(Map.Entry::getKey)
                .orElse(null);
    }


    void updateWorkingSet() {
        if (doPrinting) {
           System.out.printf("Working set: %d -> ", workingSet.size());
        }

        Iterator<List<Integer>> it = workingSet.iterator();

        while (it.hasNext()) {
            List<Integer> possibleCode = it.next();
            boolean isValid = true;

            for (Map.Entry<List<Integer>, List<ResultCombinations.Result>> entry : information.entrySet()) {
                int[] expected = getFeedback(possibleCode, entry.getKey());
                int actualBlack = 0, actualWhite = 0;
                for (ResultCombinations.Result r : entry.getValue()) {
                    if (r == ResultCombinations.Result.CORRECT) actualBlack++;
                    else if (r == ResultCombinations.Result.PRESENT) actualWhite++;
                }

                if (expected[0] != actualBlack || expected[1] != actualWhite) {
                    isValid = false;
                    break;
                }
            }

            if (!isValid) it.remove();
        }

        if (doPrinting) {
            System.out.printf("%d\n", workingSet.size());
        }
    }


    private int[] getFeedback(List<Integer> code, List<Integer> guess) {
        int black = 0, white = 0;
        int[] codeCounts = new int[NUM_COLOURS];
        int[] guessCounts = new int[NUM_COLOURS];

        for (int i = 0; i < code.size(); i++) {
            if (code.get(i).equals(guess.get(i))) {
                black++;
            } else {
                codeCounts[code.get(i)]++;
                guessCounts[guess.get(i)]++;
            }
        }

        for (int i = 0; i < NUM_COLOURS; i++) {
            white += Math.min(codeCounts[i], guessCounts[i]);
        }

        return new int[]{black, white};
    }


}
