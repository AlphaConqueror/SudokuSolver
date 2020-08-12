/*
 * @author: AlphaConqueror
 * Copyright (c) 2020
 * All rights reserved.
 */

package de.alphaconqueror.sudokusolver.structure;

import java.util.ArrayList;
import java.util.List;

public class Sudoku {

    /**
     * The width and height of every {@link Board} in amount of {@link Field}s.
     */
    private final int boardWidth, boardHeight;

    /**
     * The width and height of the sudoku in amount of {@link Board}s.
     */
    private final int width, height;

    /**
     * A {@link List} of all {@link Board}s on the sudoku.
     */
    private final List<Board> boards;

    /** Note: Width and height of the sudoku in amount of fields. */
    public Sudoku(int boardWidth, int boardHeight, int width, int height) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        checkBoardInput();

        this.width = Math.floorDiv(width, boardWidth);
        this.height = Math.floorDiv(height, boardHeight);

        checkSudokuInput();

        this.boards = new ArrayList<>();

        initBoards();
    }

    private Sudoku(int boardWidth, int boardHeight, int width, int height, List<Board> boards) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.width = width;
        this.height = height;
        this.boards = boards;
    }

    /**
     * Checks if board width and height are allowed.
     *
     * @throws IllegalArgumentException if the board width or height is smaller than 2.
     */
    private void checkBoardInput() {
        if(boardWidth < 2)
            throw new IllegalArgumentException("The board width can not be smaller than 2.");
        if(boardHeight < 2)
            throw new IllegalArgumentException("The board height can not be smaller than 2.");
    }

    /**
     * Checks if width and height are allowed.
     *
     * @throws IllegalArgumentException if the width or height is smaller than 2.
     */
    private void checkSudokuInput() {
        if(width < 2)
            throw new IllegalArgumentException("The width can not be smaller than 2.");
        if(height < 2)
            throw new IllegalArgumentException("The height can not be smaller than 2.");

        if(width % boardWidth != 0)
            throw new IllegalArgumentException("The width has to be a multiple of the board width.");

        if(height % boardHeight != 0)
            throw new IllegalArgumentException("The height has to be a multiple of the board height.");
    }

    /**
     * Initializes the sudoku with {@link Board}s.
     */
    private void initBoards() {
        for(int i = 0; i < width * height; i++)
            boards.add(new Board(boardWidth, boardHeight));
    }

    /**
     * Gets the {@link Board} at the coordinates relative to the boards of the sudoku.
     *
     * @param x The coordinate of the board referring to the horizontal axis.
     * @param y The coordinate of the board referring to the vertical axis.
     *
     * @return The board at the coordinates relative to the boards of the sudoku.
     *
     * @usage {@code getBoard([0, board width - 1], [0, board height - 1])}
     */
    public Board getBoard(int x, int y) {
        if(x < 0 || y < 0 || x > width - 1 || y > height - 1)
            throw new IndexOutOfBoundsException("The board at x = " + x + " | y = " + y + " is not available.");

        return boards.get(x + y * width);
    }

    /**
     * Gets the {@link Board} at the coordinates relative to {@link Field}s of the sudoku.
     *
     * @param x The coordinate of the board referring to the horizontal axis.
     * @param y The coordinate of the board referring to the vertical axis.
     *
     * @return The board at the coordinates relative to the fields of the sudoku.
     *
     * @usage {@code getBoardAt([0, width * board width - 1], [0, height * board height - 1])}
     */
    public Board getBoardAt(int x, int y) {
        return getBoard(Math.floorDiv(x, boardWidth), Math.floorDiv(y, boardHeight));
    }

    /**
     * Gets the {@link Field} at the coordinates relative to fields of the sudoku.
     *
     * @param x The coordinate of the field referring to the horizontal axis.
     * @param y The coordinate of the field referring to the vertical axis.
     *
     * @return The field at the coordinates relative to the fields of the sudoku.
     *
     * @usage {@code getFieldAt([0, width * board width - 1], [0, height * board height - 1])}
     */
    public Field getFieldAt(int x, int y) {
        return getBoardAt(x, y).getField(x % boardWidth, y % boardHeight);
    }

    /**
     * Sets the value of the {@link Field} at the coordinates relative to fields of the sudoku.
     *
     * @param x     The coordinate of the field referring to the horizontal axis.
     * @param y     The coordinate of the field referring to the vertical axis.
     * @param value The value to be set.
     *
     * @usage {@code setFieldValueAt([0, width * board width - 1], [0, height * board height - 1], value)}
     */
    public void setFieldValueAt(int x, int y, int value) {
        getBoardAt(x, y).setFieldValue(x % boardWidth, y % boardHeight, value);
    }

    /**
     * Gets the width of the {@link Board}s in amount of {@link Field}s.
     *
     * @return The width of the boards in amount of fields.
     */
    public int getBoardWidth() {
        return boardWidth;
    }

    /**
     * Gets the height of the {@link Board}s in amount of {@link Field}s.
     *
     * @return The height of the boards in amount of fields.
     */
    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * Gets the width of the sudoku in amount of {@link Board}s.
     *
     * @return The width of the sudoku in amount of boards.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the sudoku in amount of {@link Board}s.
     *
     * @return The height of the sudoku in amount of boards.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets a {@link List} of all {@link Board}s on the sudoku.
     *
     * @return A list of all boards on the sudoku.
     */
    public List<Board> getBoards() {
        return boards;
    }

    /**
     * Clones this sudoku.
     *
     * @return A clone of this sudoku.
     */
    public Sudoku clone() {
        List<Board> boardClones = new ArrayList<>();

        boards.forEach(board -> boardClones.add(board.clone()));

        return new Sudoku(boardWidth, boardHeight, width, height, boardClones);
    }
}
