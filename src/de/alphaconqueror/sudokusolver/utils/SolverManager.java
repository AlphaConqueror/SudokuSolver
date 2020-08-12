/*
 * @author: AlphaConqueror
 * Copyright (c) 2020
 * All rights reserved.
 */

package de.alphaconqueror.sudokusolver.utils;

import de.alphaconqueror.sudokusolver.structure.Board;
import de.alphaconqueror.sudokusolver.structure.Field;
import de.alphaconqueror.sudokusolver.structure.Sudoku;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SolverManager {

    /**
     * The width and height of every {@link Board} in amount of {@link Field}s.
     */
    private final int boardWidth, boardHeight;

    /**
     * The width and height of the {@link Sudoku} in amount of {@link Board}s.
     */
    private final int width, height;

    public SolverManager(Sudoku sudoku) {
        this.boardWidth = sudoku.getBoardWidth();
        this.boardHeight = sudoku.getBoardHeight();
        this.width = sudoku.getWidth();
        this.height = sudoku.getHeight();
    }

    /**
     * Solves the given {@link Sudoku}.
     *
     * @param sudoku The sudoku to be solved.
     *
     * @return The solved sudoku.
     */
    public Sudoku solveSudoku(Sudoku sudoku) {
        int solvedFields = 0;
        Set<Integer> unsolvedFields = getUnsolvedFields(sudoku);

        if(unsolvedFields.size() == 0)
            return sudoku;

        for(int fieldIndex : unsolvedFields) {
            int x = fieldIndex % (width * boardWidth),
                y = Math.floorDiv(fieldIndex, width * boardWidth);
            Set<Integer> possibilities = getPossibilities(sudoku, x, y);

            if(possibilities.size() == 1) {
                sudoku.setFieldValueAt(x, y, possibilities.iterator().next());
                solvedFields++;
            }
        }

        return solvedFields == 0 ? guessSolution(sudoku) : solveSudoku(sudoku);
    }

    /**
     * Guesses the next solving move of a {@link Sudoku} depending on the possibilities of each {@link Field}.
     *
     * @param sudoku The sudoku where the next move should be guessed.
     *
     * @return The sudoku with the next most probable move.
     *
     * @throws UnsupportedOperationException if the sudoku is not solvable
     */
    public Sudoku guessSolution(Sudoku sudoku) {
        int x = -1, y = -1;
        Set<Integer> possibilities = new HashSet<>();

        for(int fieldIndex : getUnsolvedFields(sudoku)) {
            int i = fieldIndex % (width * boardWidth),
                j = Math.floorDiv(fieldIndex, width * boardWidth);
            Set<Integer> pos = getPossibilities(sudoku, i, j);

            if(possibilities.isEmpty() || pos.size() < possibilities.size()) {
                possibilities = new HashSet<>(pos);
                x = i;
                y = j;
            }
        }

        for(int possibility : possibilities) {
            Sudoku clone = sudoku.clone();

            clone.setFieldValueAt(x, y, possibility);

            try {
                return solveSudoku(clone);
            } catch(Exception ignored) {}
        }

        throw new UnsupportedOperationException("Solving not possible.");
    }

    /**
     * Gets a {@link Set} containing the indexes of all unsolved {@link Field}s of a {@link Sudoku}.
     * The field indexes begin with 0 in the top left corner and end
     * with ((width * board width) * (height * board height) - 1) in the bottom right corner.
     *
     * @param sudoku The sudoku where the unsolved fields should be collected from.
     *
     * @return A set containing the indexes of all unsolved fields.
     */
    private Set<Integer> getUnsolvedFields(Sudoku sudoku) {
        Set<Integer> unsolvedFields = new HashSet<>();

        for(int y = 0; y < height * boardHeight; y++) {
            for(int x = 0; x < width * boardWidth; x++) {
                if(!sudoku.getFieldAt(x, y).hasValue())
                    unsolvedFields.add(x + y * width * boardWidth);
            }
        }

        return unsolvedFields;
    }

    /**
     * Gets a {@link Set} containing all possible values for a {@link Field} of a {@link Sudoku}.
     *
     * @param sudoku The sudoku of the concerned field.
     * @param x      The coordinate of the field referring to the horizontal axis.
     * @param y      The coordinate of the field referring to the vertical axis.
     *
     * @return A set containing all possible values for a field of the sudoku.
     */
    private Set<Integer> getPossibilities(Sudoku sudoku, int x, int y) {
        Field field = sudoku.getFieldAt(x, y);

        if(field.hasValue())
            return Collections.singleton(field.getValue());

        Set<Integer> possibilities = createPossibilitiesSet();

        return getRowPossibilities(sudoku, getBoardPossibilities(sudoku, possibilities, x, y), x, y);
    }

    /**
     * Gets {@link Set} containing the possible values for a {@link Field} within a {@link Board} of a {@link Sudoku}.
     *
     * @param sudoku        The sudoku of the concerned field.
     * @param possibilities The possible values of a field.
     * @param x             The coordinate of the field referring to the horizontal axis.
     * @param y             The coordinate of the field referring to the vertical axis.
     *
     * @return A set containing the possible values for a field within a board of a sudoku.
     */
    private Set<Integer> getBoardPossibilities(Sudoku sudoku, Set<Integer> possibilities, int x, int y) {
        Board board = sudoku.getBoardAt(x, y);

        for(int i = 0; i < boardWidth * boardHeight; i++) {
            Field field = board.getFields().get(i);

            if(field.hasValue())
                possibilities.remove(field.getValue());
        }

        return possibilities;
    }

    /**
     * Gets {@link Set} containing the possible values for a {@link Field} within both rows of a {@link Sudoku}.
     *
     * @param sudoku        The sudoku of the concerned field.
     * @param possibilities The possible values of a field.
     * @param x             The coordinate of the field referring to the horizontal axis.
     * @param y             The coordinate of the field referring to the vertical axis.
     *
     * @return A set containing the possible values for a field within both rows of a sudoku.
     */
    private Set<Integer> getRowPossibilities(Sudoku sudoku, Set<Integer> possibilities, int x, int y) {
        return getHorizontalRowPossibilities(sudoku, getVerticalRowPossibilities(sudoku, possibilities, x), y);
    }

    /**
     * Gets {@link Set} containing the possible values for a {@link Field} within the vertical row of a {@link Sudoku}.
     *
     * @param sudoku        The sudoku of the concerned field.
     * @param possibilities The possible values of a field.
     * @param x             The coordinate of the field referring to the horizontal axis.
     *
     * @return A set containing the possible values for a field within the vertical row of a sudoku.
     */
    private Set<Integer> getVerticalRowPossibilities(Sudoku sudoku, Set<Integer> possibilities, int x) {
        for(int i = 0; i < height * boardHeight; i++) {
            Field field = sudoku.getFieldAt(x, i);

            if(field.hasValue())
                possibilities.remove(field.getValue());
        }

        return possibilities;
    }

    /**
     * Gets {@link Set} containing the possible values for a {@link Field}
     * within the horizontal row of a {@link Sudoku}.
     *
     * @param sudoku        The sudoku of the concerned field.
     * @param possibilities The possible values of a field.
     * @param y             The coordinate of the field referring to the vertical axis.
     *
     * @return A set containing the possible values for a field within the horizontal row of a sudoku.
     */
    private Set<Integer> getHorizontalRowPossibilities(Sudoku sudoku, Set<Integer> possibilities, int y) {
        for(int i = 0; i < width * boardWidth; i++) {
            Field field = sudoku.getFieldAt(i, y);

            if(field.hasValue())
                possibilities.remove(field.getValue());
        }

        return possibilities;
    }

    /**
     * Creates a {@link Set} containing all possible values for a field.
     *
     * @return A set containing all possible values for a field.
     */
    private Set<Integer> createPossibilitiesSet() {
        Set<Integer> possibilities = new HashSet<>();

        for(int i = 1; i <= width * height; i++)
            possibilities.add(i);

        return possibilities;
    }
}
