package de.alphaconqueror.sudokusolver;

import de.alphaconqueror.sudokusolver.utils.Sudoku;

public class SudokuSolver {

    private final static Sudoku sudoku = new Sudoku(new int[][] {
                {0, 0, 0, 0, 0, 0, 1, 9, 0},
                {2, 3, 0, 0, 0, 0, 6, 0, 0},
                {0, 0, 0, 2, 4, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 9, 6, 0},
                {0, 0, 0, 1, 6, 0, 0, 7, 0},
                {0, 4, 8, 0, 7, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 3, 4, 0, 5},
                {0, 0, 9, 0, 0, 8, 0, 0, 0},
                {0, 0, 6, 0, 0, 5, 8, 0, 0}
            }, 3, 3);

    public static void main(String[] args) {
        //TODO: Read input

        printSudoku(sudoku);

        SolverManager solverManager = new SolverManager(sudoku);

        System.out.println("\nSolved first degree:");

        printSudoku(solverManager.solveSudokuFirstDegree(sudoku.clone(), "1"));
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
