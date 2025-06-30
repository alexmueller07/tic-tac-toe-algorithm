public class Rules {
    public int checkWin(int[][] board) {
        // Check rows for win
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != -1 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0]; // Return winner (0 for x, 1 for o)
            }
        }

        // Check columns for win
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != -1 && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return board[0][j]; // Return winner (0 for x, 1 for o)
            }
        }

        // Check diagonals for win
        if (board[0][0] != -1 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0]; // Return winner (0 for x, 1 for o)
        }
        if (board[0][2] != -1 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2]; // Return winner (0 for x, 1 for o)
        }

        // Check for tie (all positions filled)
        boolean isTie = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == -1) {
                    isTie = false;
                    break;
                }
            }
        }

        if (isTie) {
            return 2; // Tie
        }

        return -1; // Game still ongoing
    }

    public int[] validMove(int[][] board, String move) {
        try {
            String[] parts = move.split(" ");
            int[] position = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                position[i] = Integer.parseInt(parts[i]);
            }
            if (board[position[0]][position[1]] == -1) {
                return position;

            }
            return null;

        } catch (Exception e) {
            System.out.println("Please enter a valid input in the form 'x y'");
            return null;
        }
    }

    public void printBoard(int[][] board) {
        System.out.println("=BOARD=\n  0 1 2");
        for (int i = 0; i < board.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == -1) {
                    System.out.print("- ");
                } else if (board[i][j] == 0) {
                    System.out.print("x ");
                } else if (board[i][j] == 1) {
                    System.out.print("o ");
                }

            }
            System.out.println();
        }
    }

    public int[][] updateBoard(int[][] oldBoard, int[] move, int symbol) {
        oldBoard[move[0]][move[1]] = symbol;
        return oldBoard;
    }

}
