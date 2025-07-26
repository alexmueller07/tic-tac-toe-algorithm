import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create instances of game components
        Rules rules = new Rules();
        User user = new User();
        Bot bot = new Bot();
        Scanner scanner = new Scanner(System.in);

        // Initialize empty board: -1 = empty, 0 = X, 1 = O
        int[][] board = new int[3][3];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = -1;
            }
        }

        // Ask user to choose their side: X (0) or O (1)
        int userChoice = -1;
        while (userChoice != 0 && userChoice != 1) {
            System.out.println("Please enter 0 if you would like to play as 'x' or 1 if you would like to play as 'o'");
            userChoice = scanner.nextInt();
        }

        // X always starts first
        int current_player = 0; // 0 = X, 1 = O

        // Main game loop - continues until someone wins or there's a tie
        while (rules.checkWin(board) == -1) {
            rules.printBoard(board);

            int[] move = null;

            // Handle X's turn
            if (current_player == 0) {
                if (userChoice == 0) {
                    System.out.println("Your turn (x):");
                    move = user.getUserMove(board); // User plays X
                } else {
                    System.out.println("Bot's turn (x):");
                    move = bot.getBotMove(board, 0); // Bot plays X
                }
            }
            // Handle O's turn
            else {
                if (userChoice == 1) {
                    System.out.println("Your turn (o):");
                    move = user.getUserMove(board); // User plays O
                } else {
                    System.out.println("Bot's turn (o):");
                    move = bot.getBotMove(board, 1); // Bot plays O
                }
            }

            // Apply the move to the board
            board = rules.updateBoard(board, move, current_player);

            // Switch to the other player
            current_player = (current_player == 0) ? 1 : 0;
        }

        // Game has ended — print final board and outcome
        rules.printBoard(board);
        int result = rules.checkWin(board);
        if (result == 0) {
            System.out.println("X wins!");
        } else if (result == 1) {
            System.out.println("O wins!");
        } else if (result == 2) {
            System.out.println("It's a tie!");
        }

        scanner.close(); // Clean up input resource
    }
}
