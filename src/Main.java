import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Board board = new Board();
        MastermindGuesser guesser = new MastermindGuesser();
        while(board.inPlay) {
            System.out.println(guesser.generateGuess());
            List<Integer> guess = parseGuess();
            System.out.println("GUESS:" + guess);
            List<ResultCombinations.Result> result = (board.guess(guess));
            System.out.println("RESULT:" + result);
            guesser.provideInfo(guess, result);
        }
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
