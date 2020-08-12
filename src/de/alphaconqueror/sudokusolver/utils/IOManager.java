/*
 * @author: AlphaConqueror
 * Copyright (c) 2020
 * All rights reserved.
 */

package de.alphaconqueror.sudokusolver.utils;

import de.alphaconqueror.sudokusolver.structure.Board;
import de.alphaconqueror.sudokusolver.structure.Field;
import de.alphaconqueror.sudokusolver.structure.Sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class IOManager {

    /**
     * Reads a {@link Sudoku} from a file.
     *
     * @param path The path to the file.
     *
     * @return The read sudoku.
     */
    public static Sudoku readSudoku(String path) {
        Scanner scanner;

        try {
            scanner = new Scanner(new File(path));
        } catch(FileNotFoundException e) {
            throw new IllegalArgumentException("The file at '" + path + "' could not be found.");
        }

        if(!scanner.hasNext())
            throw new IllegalArgumentException("The given file is missing the board specifications.");

        int boardWidth = 0, boardHeight = 0;
        List<List<Integer>> pattern = new ArrayList<>();
        int rowCounter = 0, columnCounter = 0;
        boolean gotBoardSpecifications = false;

        while(scanner.hasNext()) {
            String line = scanner.nextLine();

            if(isStringBlank(line) || line.startsWith("#"))
                continue;

            String[] row = splitRow(line);

            if(!gotBoardSpecifications) {
                if(row.length < 2)
                    throw new IllegalArgumentException("Too few arguments for the board specifications were given.");
                else if(row.length > 2)
                    throw new IllegalArgumentException("Too many arguments for the board specifications were given.");

                try {
                    boardWidth = Integer.parseInt(row[0]);
                } catch(NumberFormatException e) {
                    throw new IllegalArgumentException("The given board width is not a number.");
                }

                try {
                    boardHeight = Integer.parseInt(row[1]);
                } catch(NumberFormatException e) {
                    throw new IllegalArgumentException("The given board height is not a number.");
                }

                gotBoardSpecifications = true;
                continue;
            }

            rowCounter++;

            if(columnCounter == 0)
                columnCounter = row.length;
            else if(columnCounter != row.length)
                throw new IllegalArgumentException("The rows of the sudoku do not all have the same size.");

            List<Integer> rowList = new ArrayList<>();

            for(int i = 0; i < row.length; i++) {
                String value = row[i];

                try {
                    rowList.add(Integer.parseInt(value));
                } catch(NumberFormatException e) {
                    throw new IllegalArgumentException("The value '" + value + "' at x = " + (i + 1)
                            + " | y = " + rowCounter + " is not a number.");
                }
            }

            pattern.add(rowList);
        }

        scanner.close();

        Sudoku sudoku = new Sudoku(boardWidth, boardHeight, columnCounter, rowCounter);

        readPattern(pattern, sudoku);

        return sudoku;
    }

    private static boolean isStringBlank(String s) {
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) != ' ')
                return false;
        }

        return true;
    }

    /**
     * Splits a row after every blank {@link String}.
     *
     * @param row The row to be split.
     *
     * @return The split row.
     */
    private static String[] splitRow(String row) {
        List<String> list = new ArrayList<>();

        for(String s : row.split(" ")) {
            if(!s.equals(""))
                list.add(s);
        }

        return list.toArray(new String[0]);
    }

    /**
     * Reads the given pattern and writes it to the provided {@link Sudoku}.
     * Checks the if the sudoku is allowed after reading.
     *
     * @param pattern The sudoku in pattern form.
     * @param s       The sudoku to be written.
     *
     * @throws IllegalArgumentException if the given width and the width of the 2D sudoku do not match
     * @throws IllegalArgumentException if the given height and the height of the 2D sudoku do not match
     */
    private static void readPattern(List<List<Integer>> pattern, Sudoku s) {
        int rowCounter = 0;

        for(List<Integer> row : pattern) {
            rowCounter++;

            int columnCounter = 0;

            for(int value : row) {
                columnCounter++;
                s.setFieldValueAt(columnCounter - 1, rowCounter - 1, value);
            }
        }

        checkRules(s);
    }

    /**
     * Checks if the sudoku is allowed.
     *
     * @param sudoku The sudoku to be checked.
     */
    private static void checkRules(Sudoku sudoku) {
        for(int y = 0; y < sudoku.getWidth() * sudoku.getBoardWidth(); y++)
            checkHorizontalRowRules(sudoku, y);

        for(int x = 0; x < sudoku.getWidth() * sudoku.getBoardWidth(); x++)
            checkVerticalRowRules(sudoku, x);

        for(int i = 0; i < sudoku.getBoardWidth() * sudoku.getBoardHeight(); i++)
            checkBoardRules(sudoku, i);
    }

    /**
     * Checks if a {@link Board} contains duplicates of possible values for a {@link Field} of a {@link Sudoku}.
     *
     * @param sudoku The sudoku of the concerned field.
     */
    private static void checkBoardRules(Sudoku sudoku, int boardIndex) {
        Board board = sudoku.getBoards().get(boardIndex);
        Set<Integer> values = new HashSet<>();
        int boardWidth = sudoku.getBoardWidth(),
            boardHeight = sudoku.getBoardHeight();

        for(int i = 0; i < boardWidth * boardHeight; i++) {
            Field field = board.getFields().get(i);

            if(field.hasValue()) {
                int value = field.getValue();

                if(values.contains(value))
                    throw new IllegalArgumentException("Sudoku contains duplicate value " + value + " at x = "
                            + ((boardIndex % boardWidth) * boardWidth + i % boardWidth + 1)
                            + " | y = " + (Math.floorDiv(boardIndex, boardHeight) * boardHeight
                            + Math.floorDiv(i, boardHeight) + 1) + ".");

                values.add(value);
            }
        }
    }

    /**
     * Checks if the xth vertical row contains duplicates of possible values for a {@link Field} of a {@link Sudoku}.
     *
     * @param sudoku The sudoku of the concerned field.
     * @param x      The xth vertical row, starting at 0.
     */
    private static void checkVerticalRowRules(Sudoku sudoku, int x) {
        Set<Integer> values = new HashSet<>();

        for(int i = 0; i < sudoku.getHeight() * sudoku.getBoardHeight(); i++) {
            Field field = sudoku.getFieldAt(x, i);

            if(field.hasValue()) {
                int value = field.getValue();

                if(values.contains(value))
                    throw new IllegalArgumentException("Sudoku contains duplicate value " + value + " at x = "
                            + (x + 1) + " | y = " + (i + 1) + ".");

                values.add(value);
            }
        }
    }

    /**
     * Checks if the yth horizontal row contains duplicates of possible values for a {@link Field} of a {@link Sudoku}.
     *
     * @param sudoku The sudoku of the concerned field.
     * @param y      The yth horizontal row, starting at 0.
     */
    private static void checkHorizontalRowRules(Sudoku sudoku, int y) {
        Set<Integer> values = new HashSet<>();

        for(int i = 0; i < sudoku.getWidth() * sudoku.getBoardWidth(); i++) {
            Field field = sudoku.getFieldAt(i, y);

            if(field.hasValue()) {
                int value = field.getValue();

                if(values.contains(value))
                    throw new IllegalArgumentException("Sudoku contains duplicate value " + value + " at x = "
                            + (i + 1) + " | y = " + (y + 1) + ".");

                values.add(value);
            }
        }
    }
}
