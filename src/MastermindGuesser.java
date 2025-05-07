import java.util.*;

public class MastermindGuesser {
    int NUM_SLOTS = 4;
    int NUM_COLOURS = 6;

    private Map<List<Integer>, List<ResultCombinations.Result>> information = new HashMap<>();
    private Set<List<Integer>> workingSet = CombinationGenerator.generateAllCombinations(NUM_SLOTS, NUM_COLOURS - 1);

    void provideInfo(List<Integer> guess, List<ResultCombinations.Result> result) {
        information.put(guess, result);
        updateWorkingSet();
    }

    public List<Integer> generateGuess() {
        if (information.isEmpty()) {
            return List.of(0, 0, 1, 1); // Knuth's recommended starting guess
        }

        Map<List<Integer>, Integer> guessScores = new HashMap<>();

        Set<List<Integer>> allGuesses = CombinationGenerator.generateAllCombinations(NUM_SLOTS, NUM_COLOURS - 1);

        for (List<Integer> possibleGuess : allGuesses) {
            Map<String, Integer> responseCountMap = new HashMap<>();

            for (List<Integer> possibleCode : workingSet) {
                int[] feedback = getFeedback(possibleCode, possibleGuess);
                String key = feedback[0] + "B" + feedback[1] + "W";
                responseCountMap.put(key, responseCountMap.getOrDefault(key, 0) + 1);
            }

            int worstCaseSize = responseCountMap.values().stream().max(Integer::compareTo).orElse(0);
            guessScores.put(possibleGuess, worstCaseSize);
        }

        return guessScores.entrySet()
                .stream()
                .min(Comparator
                        .comparingInt((Map.Entry<List<Integer>, Integer> e) -> e.getValue())
                        .thenComparing(e -> !workingSet.contains(e.getKey())) // prefer guesses from workingSet
                        .thenComparing(e -> asNumber(e.getKey())))             // prefer lowest numeric code
                .map(Map.Entry::getKey)
                .orElse(null);
    }


    private int asNumber(List<Integer> code) {
        int num = 0;
        for (int digit : code) {
            num = num * 10 + digit;
        }
        return num;
    }


    void updateWorkingSet() {
        List<List<Integer>> toRemove = new ArrayList<>();
        System.out.println("Working set size: " + workingSet.size());
        for (List<Integer> possibleCode : workingSet) {
            for (Map.Entry<List<Integer>, List<ResultCombinations.Result>> entry : information.entrySet()) {
                List<Integer> pastGuess = entry.getKey();
                List<ResultCombinations.Result> actualFeedback = entry.getValue();
                int[] expectedFeedback = getFeedback(possibleCode, pastGuess);

                int expectedBlack = expectedFeedback[0];
                int expectedWhite = expectedFeedback[1];
                int actualBlack = 0;
                int actualWhite = 0;

                for (ResultCombinations.Result r : actualFeedback) {
                    if (r == ResultCombinations.Result.CORRECT) actualBlack++;
                    else if (r == ResultCombinations.Result.PRESENT) actualWhite++;
                }

                if (expectedBlack != actualBlack || expectedWhite != actualWhite) {
                    toRemove.add(possibleCode);
                    break;
                }
            }
        }
        toRemove.forEach(workingSet::remove);
        System.out.println("Working set size: " + workingSet.size());
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

    private int[] getFeedback(List<Integer> code, List<Integer> guess) {
        int black = 0, white = 0;
        Map<Integer, Integer> codeColorCounts = new HashMap<>();
        Map<Integer, Integer> guessColorCounts = new HashMap<>();

        for (int i = 0; i < code.size(); i++) {
            if (code.get(i).equals(guess.get(i))) {
                black++;
            } else {
                codeColorCounts.put(code.get(i), codeColorCounts.getOrDefault(code.get(i), 0) + 1);
                guessColorCounts.put(guess.get(i), guessColorCounts.getOrDefault(guess.get(i), 0) + 1);
            }
        }

        for (Integer color : guessColorCounts.keySet()) {
            if (codeColorCounts.containsKey(color)) {
                white += Math.min(codeColorCounts.get(color), guessColorCounts.get(color));
            }
        }

        return new int[]{black, white};
    }


}
