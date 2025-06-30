import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Rules rules = new Rules();
        User user = new User();
        Bot bot = new Bot();
        Scanner scanner = new Scanner(System.in);

        int[][] board = new int[3][3];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = -1;
            }
        }

        int userChoice = -1;
        while (userChoice != 0 && userChoice != 1) {
            System.out.println("Please enter 0 if you would like to play as 'x' or 1 if you would like to play as 'o'");
            userChoice = scanner.nextInt();
        }

        // X always goes first (player 0), O goes second (player 1)
        int current_player = 0; // Start with X

        while (rules.checkWin(board) == -1) {
            rules.printBoard(board);

            int[] move = null;
            if (current_player == 0) {
                // X's turn
                if (userChoice == 0) {
                    System.out.println("Your turn (x):");
                    move = user.getUserMove(board);
                } else {
                    System.out.println("Bot's turn (x):");
                    move = bot.getBotMove(board, 0);
                }
            } else {
                // O's turn
                if (userChoice == 1) {
                    System.out.println("Your turn (o):");
                    move = user.getUserMove(board);
                } else {
                    System.out.println("Bot's turn (o):");
                    move = bot.getBotMove(board, 1);
                }
            }

            board = rules.updateBoard(board, move, current_player);

            current_player = (current_player == 0) ? 1 : 0;
        }

        rules.printBoard(board);
        int result = rules.checkWin(board);
        if (result == 0) {
            System.out.println("X wins!");
        } else if (result == 1) {
            System.out.println("O wins!");
        } else if (result == 2) {
            System.out.println("It's a tie!");
        }

        scanner.close();
    }
}