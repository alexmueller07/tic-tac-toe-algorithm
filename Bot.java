public class Bot {
    private static final int MAX_DEPTH = 9; // Maximum depth for minimax
    private static final int WIN_SCORE = 1000;
    private static final int LOSE_SCORE = -1000;

    // Priority moves for better move ordering (center, corners, edges)
    private static final int[][] MOVE_PRIORITY = {
            { 1, 1 }, // Center
            { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 }, // Corners
            { 0, 1 }, { 1, 0 }, { 1, 2 }, { 2, 1 } // Edges
    };

    public int[] getBotMove(int[][] board, int playerSymbol) {
        System.out.println("🤖 Bot is analyzing the board...");

        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // Use move ordering for better alpha-beta pruning
        for (int[] priorityMove : MOVE_PRIORITY) {
            int i = priorityMove[0];
            int j = priorityMove[1];

            if (board[i][j] == -1) {
                // Try this move
                board[i][j] = playerSymbol;

                // Get score for this move using minimax
                int score = minimax(board, 0, false, playerSymbol, alpha, beta);

                // Undo the move
                board[i][j] = -1;

                // Update best move if this score is better
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new int[] { i, j };
                }

                // Alpha-beta pruning
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha) {
                    break; // Beta cutoff
                }
            }
        }

        // If no move found in priority order, check remaining positions
        if (bestMove == null) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == -1) {
                        board[i][j] = playerSymbol;
                        int score = minimax(board, 0, false, playerSymbol, alpha, beta);
                        board[i][j] = -1;

                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = new int[] { i, j };
                        }

                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("✅ Bot has made its decision!");
        return bestMove;
    }

    private int minimax(int[][] board, int depth, boolean isMaximizing, int playerSymbol, int alpha, int beta) {
        // Check for terminal states
        int result = checkWin(board);
        if (result != -1) {
            return evaluateBoard(board, playerSymbol, depth);
        }

        // Check if we've reached maximum depth
        if (depth >= MAX_DEPTH) {
            return evaluatePosition(board, playerSymbol); // Position evaluation instead of 0
        }

        int opponentSymbol = (playerSymbol == 0) ? 1 : 0;

        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == -1) {
                        board[i][j] = playerSymbol;
                        int score = minimax(board, depth + 1, false, playerSymbol, alpha, beta);
                        board[i][j] = -1;

                        maxScore = Math.max(maxScore, score);
                        alpha = Math.max(alpha, score);
                        if (beta <= alpha) {
                            break; // Beta cutoff
                        }
                    }
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == -1) {
                        board[i][j] = opponentSymbol;
                        int score = minimax(board, depth + 1, true, playerSymbol, alpha, beta);
                        board[i][j] = -1;

                        minScore = Math.min(minScore, score);
                        beta = Math.min(beta, score);
                        if (beta <= alpha) {
                            break; // Alpha cutoff
                        }
                    }
                }
            }
            return minScore;
        }
    }

    private int evaluateBoard(int[][] board, int playerSymbol, int depth) {
        int opponentSymbol = (playerSymbol == 0) ? 1 : 0;

        // Check if current player won
        if (checkWin(board) == playerSymbol) {
            return WIN_SCORE - depth; // Higher score for faster wins
        }

        // Check if opponent won
        if (checkWin(board) == opponentSymbol) {
            return LOSE_SCORE + depth; // Lower score for faster losses
        }

        // It's a tie
        return 0;
    }

    private int evaluatePosition(int[][] board, int playerSymbol) {
        // Evaluate non-terminal positions based on potential winning lines
        int score = 0;
        int opponentSymbol = (playerSymbol == 0) ? 1 : 0;

        // Evaluate rows, columns, and diagonals
        score += evaluateLines(board, playerSymbol, opponentSymbol);

        return score;
    }

    private int evaluateLines(int[][] board, int playerSymbol, int opponentSymbol) {
        int score = 0;

        // Evaluate rows
        for (int i = 0; i < 3; i++) {
            score += evaluateLine(board[i][0], board[i][1], board[i][2], playerSymbol, opponentSymbol);
        }

        // Evaluate columns
        for (int j = 0; j < 3; j++) {
            score += evaluateLine(board[0][j], board[1][j], board[2][j], playerSymbol, opponentSymbol);
        }

        // Evaluate diagonals
        score += evaluateLine(board[0][0], board[1][1], board[2][2], playerSymbol, opponentSymbol);
        score += evaluateLine(board[0][2], board[1][1], board[2][0], playerSymbol, opponentSymbol);

        return score;
    }

    private int evaluateLine(int a, int b, int c, int playerSymbol, int opponentSymbol) {
        int playerCount = 0;
        int opponentCount = 0;
        int emptyCount = 0;

        if (a == playerSymbol)
            playerCount++;
        else if (a == opponentSymbol)
            opponentCount++;
        else
            emptyCount++;

        if (b == playerSymbol)
            playerCount++;
        else if (b == opponentSymbol)
            opponentCount++;
        else
            emptyCount++;

        if (c == playerSymbol)
            playerCount++;
        else if (c == opponentSymbol)
            opponentCount++;
        else
            emptyCount++;

        // Scoring based on potential
        if (opponentCount == 0) {
            return (int) Math.pow(10, playerCount); // Favor positions with more player pieces
        } else if (playerCount == 0) {
            return -(int) Math.pow(10, opponentCount); // Penalize positions with more opponent pieces
        }

        return 0; // Mixed line, neutral
    }

    private int checkWin(int[][] board) {
        // Check rows for win
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != -1 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
        }

        // Check columns for win
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != -1 && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return board[0][j];
            }
        }

        // Check diagonals for win
        if (board[0][0] != -1 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != -1 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }

        // Check for tie
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
}
