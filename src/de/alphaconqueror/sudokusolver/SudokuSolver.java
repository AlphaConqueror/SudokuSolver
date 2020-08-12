/*
 * @author: AlphaConqueror
 * Copyright (c) 2020
 * All rights reserved.
 */

package de.alphaconqueror.sudokusolver;

import de.alphaconqueror.sudokusolver.structure.Sudoku;
import de.alphaconqueror.sudokusolver.utils.IOManager;
import de.alphaconqueror.sudokusolver.utils.SolverManager;

public class SudokuSolver {

    public static void main(String[] args) {
        if(args.length == 0)
            System.err.println("Please specify a file containing the sudoku and its board specifications.");

        Sudoku sudoku = IOManager.readSudoku(args[0]);

        printSudoku(sudoku);

        SolverManager solverManager = new SolverManager(sudoku);

        long millis = System.currentTimeMillis();

        Sudoku solvedSudoku = solverManager.solveSudoku(sudoku);

        System.out.println("\nSOLUTION: ");
        printSudoku(solvedSudoku);
        System.out.println("Calculated in " + (System.currentTimeMillis() - millis)/1000.0 + " seconds.");
    }

    /**
     * Prints the {@link Sudoku} to the standard output {@link java.io.PrintStream}.
     *
     * @param sudoku The sudoku to be printed.
     */
    public static void printSudoku(Sudoku sudoku) {
        int boardWidth = sudoku.getBoardWidth(),
            boardHeight = sudoku.getBoardHeight(),
            width = sudoku.getWidth(),
            height = sudoku.getHeight(),
            fieldLength = Math.floorDiv(boardWidth * boardHeight, 10) + 1;
        String horizontalSeparator = repeatString("-", Math.max(0, width * (boardWidth * (fieldLength + 1) + 1) + (width - 1)));

        System.out.println(horizontalSeparator);

        for(int y = 0; y < height * boardHeight; y++) {
            StringBuilder line = new StringBuilder(" ");

            for(int x = 0; x < width * boardWidth; x++) {
                int value = sudoku.getFieldAt(x, y).getValue();

                line.append(repeatString(" ", fieldLength - (value + "").length())).append(value).append(" ");

                if(x < width * boardWidth - 1 && (x + 1) % boardWidth == 0)
                    line.append("| ");
            }

            System.out.println(line);

            if(y < height * boardHeight - 1 && (y + 1) % boardHeight == 0)
                System.out.println(horizontalSeparator);
        }

        System.out.println(horizontalSeparator);
    }

    /**
     * Repeats a {@link String} a certain amount of times.
     *
     * @param s     The string to be repeated.
     * @param times The amount of times the string should be repeated.
     *
     * @return The repeated string.
     */
    private static String repeatString(String s, int times) {
        StringBuilder repeated = new StringBuilder();

        for(int ignored = 0; ignored < times; ignored++)
            repeated.append(s);

        return repeated.toString();
    }
}
