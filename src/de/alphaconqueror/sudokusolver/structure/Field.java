/*
 * @author: AlphaConqueror
 * Copyright (c) 2020
 * All rights reserved.
 */

package de.alphaconqueror.sudokusolver.structure;

public class Field {

    /**
     * The value of the field, 0 by default.
     */
    private int value;

    protected Field() {
        this.value = 0;
    }

    protected Field(int value) {
        this.value = value;
    }

    /**
     * Checks if the field has a value.
     *
     * @return true, if the field has a value, false, if otherwise.
     */
    public boolean hasValue() {
        return value != 0;
    }

    /**
     * Gets the value of the field.
     *
     * @return The value of the field.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the field.
     *
     * @param value The field value to be set.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Clones this field.
     *
     * @return A clone of this field.
     */
    public Field clone() {
        return new Field(value);
    }
}
