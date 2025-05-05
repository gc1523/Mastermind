import java.util.*;

public class MastermindGuesser {
    int NUM_SLOTS = 4;
    int NUM_COLOURS = 6;

    private Map<List<Integer>, List<ResultCombinations.Result>> information = new HashMap<>();
    private Set<List<Integer>> workingSet = CombinationGenerator.generateAllCombinations(NUM_SLOTS, NUM_COLOURS - 1);

    // Provide the feedback about a guess
    void provideInfo(List<Integer> guess, List<ResultCombinations.Result> result) {
        information.put(guess, result);
        updateWorkingSet();
    }

    public List<Integer> generateGuess() {
        if (information.isEmpty()) {
            return List.of(0, 0, 1, 1); // Knuth's first guess
        } else {
            Map<List<Integer>, Integer> guessScores = new HashMap<>();
            for (List<Integer> possibleGuess : CombinationGenerator.generateAllCombinations(NUM_SLOTS, NUM_COLOURS - 1)) {
                int worstCaseScore = 0;
                for (List<ResultCombinations.Result> hypothesisedResult : ResultCombinations.generateUniqueCombinations(NUM_SLOTS)) {
                    int workingSetTheoreticalSize = workingSet.size();
                    // count how many codes in S would still be valid if you got that response from this guess.
                    // take the worst case as score
                    for (List<Integer> s : workingSet) {
                            // TODO: This condition is incorrect as a byproduct of the incorrect updateWorkingSet logic (I think :p)
                            if (sharedValues(possibleGuess, s) < getValids(hypothesisedResult)) {
                                workingSetTheoreticalSize--;
                            }
                    }

                    worstCaseScore = Math.max(worstCaseScore, workingSetTheoreticalSize);
                }
                guessScores.put(possibleGuess, worstCaseScore);
            }
            return guessScores.entrySet()
                    .stream()
                    .min(Comparator
                            .comparingInt((Map.Entry<List<Integer>, Integer> e) -> e.getValue())
                            .thenComparing(e -> !workingSet.contains(e.getKey()))
                            .thenComparing(e -> asNumber(e.getKey())))
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }
    }

    private int asNumber(List<Integer> code) {
        int num = 0;
        for (int digit : code) {
            num = num * 10 + digit;
        }
        return num;
    }


    void updateWorkingSet() {
        System.out.println("WORKING SET IS SIZE:" + workingSet.size() + workingSet);
        List<List<Integer>> toRemove = new ArrayList<>();
        for (List<Integer> possibleGuess : workingSet) {
            for (Map.Entry<List<Integer>, List<ResultCombinations.Result>> info : information.entrySet()) {
                if (sharedValues(info.getKey(), possibleGuess) < getValids(info.getValue()) || possibleGuess.equals(info.getKey()) ) {
                    toRemove.add(possibleGuess);
                    // TODO Fix this, logic is incorrect - consider wiki article sources
                }
            }
        }

        toRemove.forEach(workingSet::remove);

        System.out.println("WORKING SET IS SIZE:" + workingSet.size() + workingSet);
    }

    private int getValids(List<ResultCombinations.Result> value) {
        int count = 0;
        for (ResultCombinations.Result r : value) {
            if (r != ResultCombinations.Result.INCORRECT) {
                count++;
            }
        }
        return count;
    }

    private int sharedValues(List<Integer> key, List<Integer> possibleGuess) {
        Map<Integer, Integer> count1 = new HashMap<>();
        Map<Integer, Integer> count2 = new HashMap<>();

        for (int num : key) {
            count1.put(num, count1.getOrDefault(num, 0) + 1);
        }

        for (int num : possibleGuess) {
            count2.put(num, count2.getOrDefault(num, 0) + 1);
        }
        int shared = 0;
        for (int num : count1.keySet()) {
            if (count2.containsKey(num)) {
                shared += Math.min(count1.get(num), count2.get(num));
            }
        }
        return shared;
    }

    boolean nonMatchingResponse() {
        return false;
    }


}
