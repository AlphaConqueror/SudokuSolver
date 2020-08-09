package de.alphaconqueror.sudokusolver;

import de.alphaconqueror.sudokusolver.utils.Board;
import de.alphaconqueror.sudokusolver.utils.Field;
import de.alphaconqueror.sudokusolver.utils.Sudoku;

import java.util.*;

public class SolverManager {

    private final int boardWidth, boardHeight, width, height;
    private final boolean DEBUG = true;

    public SolverManager(Sudoku sudoku) {
        this.boardWidth = sudoku.getBoardWidth();
        this.boardHeight = sudoku.getBoardHeight();
        this.width = sudoku.getWidth();
        this.height = sudoku.getHeight();
    }

    public Sudoku solveSudokuFirstDegree(Sudoku sudoku, String generation) {
        int solvedFields = 0;
        List<Integer> unsolvedFields = getUnsolvedFields(sudoku);

        if(unsolvedFields.size() == 0)
            return sudoku;

        for(int fieldIndex : unsolvedFields) {
            int x = fieldIndex % (width * boardWidth),
                y = Math.floorDiv(fieldIndex, width * boardWidth);
            Set<Integer> possibilities = getPossibilities(sudoku, x, y);

            switch(possibilities.size()) {
                case 0: throw new UnsupportedOperationException("An error occurred while trying to solve the sudoku. Generation = " + generation);
                case 1:
                    sudoku.setFieldValueAt(x, y, possibilities.iterator().next());
                    solvedFields++;
                    break;
                default:
                    break;
            }
        }

        if(solvedFields == 0) {
            if(DEBUG)
                System.out.println("Continue second degree solving:");
        }

        return solvedFields == 0 ? solveSudokuSecondDegree(sudoku, generation) : solveSudokuFirstDegree(sudoku, generation);
    }

    public Sudoku solveSudokuSecondDegree(Sudoku sudoku, String generation) {
        int g = 1;

        for(int fieldIndex : getUnsolvedFields(sudoku)) {
            int x = fieldIndex % (width * boardWidth),
                y = Math.floorDiv(fieldIndex, width * boardWidth);

            for(int possibility : getPossibilities(sudoku, x, y)) {
                Sudoku clone = sudoku.clone();

                clone.setFieldValueAt(x, y, possibility);

                if(DEBUG)
                    System.out.println("Checking sudoku with value = " + possibility + " at x = " + x + " | y = " + y + ". Generation = " + generation + "." + g);

                g++;

                try {
                    return solveSudokuFirstDegree(clone, generation + "." + g);
                } catch(Exception e) {
                    if(DEBUG)
                        System.out.println("Caught exception. Generation = " + generation + "." + g);
                }
            }
        }

        throw new UnsupportedOperationException("Solving not possible.");
    }

    private List<Integer> getUnsolvedFields(Sudoku sudoku) {
        List<Integer> unsolvedFields = new ArrayList<>();

        for(int y = 0; y < height * boardHeight; y++) {
            for(int x = 0; x < width * boardWidth; x++) {
                if(!sudoku.getFieldAt(x, y).hasValue())
                    unsolvedFields.add(x + y * width * boardWidth);
            }
        }

        return unsolvedFields;
    }

    private Set<Integer> getPossibilities(Sudoku sudoku, int x, int y) {
        Field field = sudoku.getFieldAt(x, y);

        if(field.hasValue())
            return new HashSet<>(Collections.singletonList(field.getValue()));

        Set<Integer> possibilities = createPossibilitiesSet();

        return getRowPossibilities(sudoku, getBoardPossibilities(sudoku, possibilities, x, y), x, y);
    }

    private Set<Integer> getBoardPossibilities(Sudoku sudoku, Set<Integer> possibilities, int x, int y) {
        Board board = sudoku.getBoardAt(x, y);

        for(int i = 0; i < boardWidth * boardHeight; i++) {
            Field field = board.getFields().get(i);

            if(field.hasValue())
                possibilities.remove(field.getValue());
        }

        return possibilities;
    }

    private Set<Integer> getRowPossibilities(Sudoku sudoku, Set<Integer> possibilities, int x, int y) {
        return getHorizontalRowPossibilities(sudoku, getVerticalRowPossibilities(sudoku, possibilities, x), y);
    }

    private Set<Integer> getVerticalRowPossibilities(Sudoku sudoku, Set<Integer> possibilities, int x) {
        for(int i = 0; i < height * boardHeight; i++) {
            Field field = sudoku.getFieldAt(x, i);

            if(field.hasValue())
                possibilities.remove(field.getValue());
        }

        return possibilities;
    }

    private Set<Integer> getHorizontalRowPossibilities(Sudoku sudoku, Set<Integer> possibilities, int y) {
        for(int i = 0; i < width * boardWidth; i++) {
            Field field = sudoku.getFieldAt(i, y);

            if(field.hasValue())
                possibilities.remove(field.getValue());
        }

        return possibilities;
    }

    private Set<Integer> createPossibilitiesSet() {
        Set<Integer> possibilities = new HashSet<>();

        for(int i = 1; i <= width * height; i++)
            possibilities.add(i);

        return possibilities;
    }
}
