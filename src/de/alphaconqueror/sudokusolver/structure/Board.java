/*
 * @author: AlphaConqueror
 * Copyright (c) 2020
 * All rights reserved.
 */

package de.alphaconqueror.sudokusolver.structure;

import java.util.ArrayList;
import java.util.List;

public class Board {

    /**
     * The width and height of the board in amount of {@link Field}s.
     */
    private final int width, height;

    /**
     * A list of all {@link Field}s on the board.
     */
    private final List<Field> fields;

    protected Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.fields = new ArrayList<>();

        initFields();
    }

    private Board(int width, int height, List<Field> fields) {
        this.width = width;
        this.height = height;
        this.fields = fields;
    }

    /**
     * Initializes the board with {@link Field}s.
     */
    private void initFields() {
        for(int i = 0; i < width * height; i++)
            fields.add(new Field());
    }

    /**
     * Gets the {@link Field} at the coordinates relative to the board.
     *
     * @param x The coordinate of the field referring to the horizontal axis.
     * @param y The coordinate of the field referring to the vertical axis.
     *
     * @return The field at the coordinates relative to the board.
     *
     * @throws IndexOutOfBoundsException if the coordinates are out of bounds
     */
    public Field getField(int x, int y) {
        if(x < 0 || y < 0 || x > width - 1 || y > height - 1)
            throw new IndexOutOfBoundsException("The field at x = " + x + " | y = " + y + " is not available.");

        return fields.get(x + y * width);
    }

    /**
     * Sets the value of the {@link Field} at the coordinates relative to the board.
     *
     * @param x     The coordinate of the field referring to the horizontal axis.
     * @param y     The coordinate of the field referring to the vertical axis.
     * @param value The value to be set.
     *
     * @throws UnsupportedOperationException if the concerned field already has a value
     * @throws IndexOutOfBoundsException if the value is out of bounds
     */
    public void setFieldValue(int x, int y, int value) {
        Field field = getField(x, y);

        if(field.hasValue())
            throw new UnsupportedOperationException("The value of the field at x = " + x
                    + " | y = " + y + " can not be overwritten.");

        if(value != 0 && (value < 1 || value > width * height))
            throw new IndexOutOfBoundsException("The value '" + value + "' at x = " + (x + 1)
                    + " | y = " + (y + 1) + " is out of bounds [1," + (width * height) + "].");

        field.setValue(value);
    }

    /**
     * Gets a {@link List} of all {@link Field}s on the board.
     *
     * @return A list of all fields on the board.
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * Clones this board.
     *
     * @return A clone of this board.
     */
    public Board clone() {
        List<Field> fieldClones = new ArrayList<>();

        fields.forEach(field -> fieldClones.add(field.clone()));

        return new Board(width, height, fieldClones);
    }
}
