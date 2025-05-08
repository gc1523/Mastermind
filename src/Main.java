import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int NUM_COLOURS = 6;
        int NUM_SLOTS = 4;
        int NUM_ITERATIONS = 10000;
        List<Integer> guessesPerGame = new ArrayList<>();
        System.out.println(String.format("\n\n*** --- STARTING %d ITERATIONS --- ***\nColours: %d\nSlots: %d\n", NUM_ITERATIONS, NUM_COLOURS, NUM_SLOTS));
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            Board board = new Board(NUM_COLOURS, NUM_SLOTS);
            MastermindGuesser guesser = new MastermindGuesser(NUM_COLOURS, NUM_SLOTS);
            while (board.inPlay) {
                List<Integer> guess = guesser.generateGuess();
                // System.out.println("GUESS:" + guess);
                List<ResultCombinations.Result> result = (board.guess(guess));
                // System.out.println("RESULT:" + result);
                guesser.provideInfo(guess, result);
            }
            // System.out.println("Game went to: " + board.rounds + " rounds.");
            guessesPerGame.add(board.rounds);
            String progressbar = "[" + "#".repeat(i * 25 / NUM_ITERATIONS) +"-".repeat(25 - i * 25 / NUM_ITERATIONS) + "] ";
            System.out.print("\rProgress: " + progressbar + ((double) 100 * i / NUM_ITERATIONS) + "%");
        }
        String progressbar = "[" + "#".repeat(25) + "]";
        System.out.print("\rProgress: " + progressbar + " 100%");
        System.out.println("\nAVERAGE ROUNDS PER GAME: " + guessesPerGame.stream().mapToInt(Integer::intValue).average().getAsDouble());
    }

    private static List<Integer> parseGuess() {
        Scanner scanner = new Scanner(System.in);
        List<Integer> numbers = new ArrayList<>();

        System.out.println("Enter 4 integers:");

        for (int i = 0; i < 4; i++) {
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer:");
                scanner.next();
            }
            numbers.add(scanner.nextInt());
        }

        return numbers;
    }
}
