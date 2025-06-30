import java.util.Scanner;

public class User {
    public int[] getUserMove(int[][] board) {
        Scanner scanner = new Scanner(System.in);
        Rules rules = new Rules();

        int[] move = null;
        while (move == null) {
            System.out.print("Enter 2 numbers separated by spaces: ");
            String input = scanner.nextLine();
            move = rules.validMove(board, input);
        }
        return move;
    }
}
