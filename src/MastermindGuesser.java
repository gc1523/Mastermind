import java.util.*;

public class MastermindGuesser {
    int NUM_SLOTS = 4;
    int NUM_COLOURS = 6;

    private Map<List<Integer>, List<Result>> information = new HashMap<>();
    private Set<List<Integer>> workingSet = CombinationGenerator.generateAllCombinations(NUM_SLOTS, NUM_COLOURS - 1);

    // Provide the feedback about a guess
    void provideInfo(List<Integer> guess, List<Result> result) {
        information.put(guess, result);
        updateWorkingSet();
    }

    public List<Integer> generateGuess() {
        if (information.isEmpty()) {
            return List.of(0, 0, 1, 1); // Knuth's first guess
        } else {

        }
        return null;
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
            for (Map.Entry<List<Integer>, List<Result>> info : information.entrySet()) {
                if (sharedValues(info.getKey(), possibleGuess) < getValids(info.getValue())) {
                    toRemove.add(possibleGuess);
                }
            }
        }

        toRemove.forEach(workingSet::remove);

        System.out.println("WORKING SET IS SIZE:" + workingSet.size() + workingSet);
    }

    private int getValids(List<Result> value) {
        int count = 0;
        for (Result r : value) {
            if (r != Result.INCORRECT) {
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
