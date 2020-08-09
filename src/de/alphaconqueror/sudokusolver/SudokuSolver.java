package de.alphaconqueror.sudokusolver;

import de.alphaconqueror.sudokusolver.utils.Sudoku;

public class SudokuSolver {

    private final static Sudoku sudoku = new Sudoku(new int[][] {
                {0, 0, 0, 5, 4, 6, 0, 0, 9},
                {0, 2, 0, 0, 0, 0, 0, 0, 7},
                {0, 0, 3, 9, 0, 0, 0, 0, 4},
                {9, 0, 5, 0, 0, 0, 0, 7, 0},
                {7, 0, 0, 0, 0, 0, 0, 2, 0},
                {0, 0, 0, 0, 9, 3, 0, 0, 0},
                {0, 5, 6, 0, 0, 8, 0, 0, 0},
                {0, 1, 0, 0, 3, 9, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 8, 0, 6}
            }, 3, 3);

    public static void main(String[] args) {
        //TODO: Read input

        printSudoku(sudoku);

        SolverManager solverManager = new SolverManager(sudoku);

        long millis = System.currentTimeMillis();

        Sudoku solvedSudoku = solverManager.solveSudoku(sudoku);

        printSudoku(solvedSudoku);
        System.out.println("Calculated in " + (System.currentTimeMillis() - millis)/1000.0 + " seconds.");
    }

    public static void printSudoku(Sudoku sudoku) {
        int boardWidth = sudoku.getBoardWidth(),
            boardHeight = sudoku.getBoardHeight(),
            width = sudoku.getWidth(),
            height = sudoku.getHeight();
        String horizontalSeparator = "-".repeat(Math.max(0, width * (2 * boardWidth + 1) + (width - 1)));

        System.out.println(horizontalSeparator);

        for(int y = 0; y < height * boardHeight; y++) {
            StringBuilder line = new StringBuilder(" ");

            for(int x = 0; x < width * boardWidth; x++) {
                line.append(sudoku.getFieldAt(x, y).getValue()).append(" ");

                if(x < width * boardWidth - 1 && (x + 1) % boardWidth == 0)
                    line.append("| ");
            }

            System.out.println(line);

            if(y < height * boardHeight - 1 && (y + 1) % boardHeight == 0)
                System.out.println(horizontalSeparator);
        }

        System.out.println(horizontalSeparator);
    }
}
