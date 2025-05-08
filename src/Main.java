import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int NUM_COLOURS = 5;
        int NUM_SLOTS = 4;
        int NUM_ITERATIONS = 10000;
        List<Integer> guessesPerGame = new ArrayList<>();
        int maxGuess = 0;
        long startTime = System.nanoTime();
        System.out.printf("\n\n*** --- STARTING %d ITERATION(S) --- ***\nColours: %d\nSlots: %d\n%n", NUM_ITERATIONS, NUM_COLOURS, NUM_SLOTS);
        String progressbar = "[" + "-".repeat(25) + "]";
        System.out.print("\rProgress: " + progressbar + " 0.000%");
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            Board board = new Board(NUM_COLOURS, NUM_SLOTS);
            MastermindGuesser guesser = new MastermindGuesser(NUM_COLOURS, NUM_SLOTS);
            if (NUM_ITERATIONS < 5) {
                guesser.doPrinting = true;
            }
            while (board.inPlay) {
                List<Integer> guess = guesser.generateGuess();
                // System.out.println("GUESS:" + guess);
                List<ResultCombinations.Result> result = (board.guess(guess));
                // System.out.println("RESULT:" + result);
                guesser.provideInfo(guess, result);
            }
            // System.out.println("Game went to: " + board.rounds + " rounds.");
            guessesPerGame.add(board.rounds);
            maxGuess = Integer.max(maxGuess, board.rounds);
            progressbar = "[" + "#".repeat(i * 25 / NUM_ITERATIONS) +"-".repeat(25 - i * 25 / NUM_ITERATIONS) + "] ";
            System.out.print("\rProgress: " + progressbar + String.format("%.3f", (double) 100 * i / NUM_ITERATIONS) + "%");
        }
        progressbar = "[" + "#".repeat(25) + "]";
        System.out.print("\rProgress: " + progressbar + " 100%\n");
        System.out.println("\nAVERAGE ROUNDS PER GAME: " + String.format("%.3f",guessesPerGame.stream().mapToInt(Integer::intValue).average().getAsDouble()));
        System.out.println("MAX NO. GUESSES: " + maxGuess);
        System.out.printf("EXECUTION TOOK: %.4f seconds\n", ((double) (System.nanoTime() - startTime) /  1000000000)) ;
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
