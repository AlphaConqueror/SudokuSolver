package de.alphaconqueror.sudokusolver.utils;

public class Field {

    private int value = 0;

    protected Field() {}

    protected Field(int value) {
        this.value = value;
    }

    public boolean hasValue() {
        return value != 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Field clone() {
        return new Field(value);
    }
}
