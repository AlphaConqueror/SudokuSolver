package de.alphaconqueror.sudokusolver.utils;

import java.util.ArrayList;
import java.util.List;

public class Sudoku {

    private final int boardWidth, boardHeight, width, height;
    private final List<Board> boards;

    public Sudoku(int[][] sudoku, int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        checkBoardInput();

        this.width = calcWidth(sudoku);
        this.height = calcHeight(sudoku);

        checkSudokuInput();

        this.boards = new ArrayList<>();

        readSudoku(sudoku);
    }

    private Sudoku(int boardWidth, int boardHeight, int width, int height, List<Board> boards) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.width = width;
        this.height = height;

        this.boards = boards;
    }

    private int calcWidth(int[][] sudoku) {
        int columnCounter = 0;

        for(int[] rows : sudoku) {
            int c = 0;

            for(int ignored : rows)
                c++;

            if(columnCounter == 0)
                columnCounter = c;
            else if(columnCounter != c)
                throw new IllegalArgumentException("The rows of the sudoku do not all have the same size.");
        }

        if(columnCounter % boardWidth != 0)
            throw new IllegalArgumentException("The width has to be a multiple of the board width.");

        return Math.floorDiv(columnCounter, boardWidth);
    }

    private int calcHeight(int[][] sudoku) {
        int rowCounter = 0;

        for(int[] ignored : sudoku)
            rowCounter++;

        if(rowCounter % boardHeight != 0)
            throw new IllegalArgumentException("The height has to be a multiple of the board height.");

        return Math.floorDiv(rowCounter, boardHeight);
    }

    private void checkBoardInput() {
        if(boardWidth < 2)
            throw new IllegalArgumentException("The board width can not be smaller than 2.");
        if(boardHeight < 2)
            throw new IllegalArgumentException("The board height can not be smaller than 2.");
    }

    private void checkSudokuInput() {
        if(width < 2)
            throw new IllegalArgumentException("The width can not be smaller than 2.");
        if(height < 2)
            throw new IllegalArgumentException("The height can not be smaller than 2.");
    }

    private void initBoards() {
        for(int i = 0; i < width * height; i++)
            boards.add(new Board(boardWidth, boardHeight));
    }

    private void readSudoku(int[][] sudoku) {
        initBoards();

        int rowCounter = 0;

        for(int[] rows : sudoku) {
            rowCounter++;

            if(rowCounter > height * boardHeight)
                throw new IllegalArgumentException("The read height does not match given sudoku height.");

            int columnCounter = 0;

            for(int value : rows) {
                columnCounter++;

                if(columnCounter > width * boardWidth)
                    throw new IllegalArgumentException("The read width does not match given sudoku width.");

                setFieldValueAt(columnCounter - 1, rowCounter - 1, value);
            }
        }

    }

    public Board getBoard(int x, int y) {
        if(x < 0 || y < 0 || x > width - 1 || y > height - 1)
            throw new UnsupportedOperationException("The board at x = " + x + " | y = " + y + " is not available.");

        return boards.get(x + y * width);
    }

    public Board getBoardAt(int x, int y) {
        return getBoard(Math.floorDiv(x, boardWidth), Math.floorDiv(y, boardHeight));
    }

    public Field getFieldAt(int x, int y) {
        return getBoardAt(x, y).getField(x % boardWidth, y % boardHeight);
    }

    public Field getFieldAt(int index) {
        return getFieldAt(index % (width * boardWidth), Math.floorDiv(index, width * boardWidth));
    }

    public void setFieldValueAt(int x, int y, int value) {
        getBoardAt(x, y).setFieldValue(x % boardWidth, y % boardHeight, value);
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Sudoku clone() {
        List<Board> boardClones = new ArrayList<>();

        boards.forEach(board -> boardClones.add(board.clone()));

        return new Sudoku(boardWidth, boardHeight, width, height, boardClones);
    }
}
