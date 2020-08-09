package de.alphaconqueror.sudokusolver.utils;

import java.util.*;

public class Board {

    private final int width, height;
    private final List<Field> fields;

    protected Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.fields = new ArrayList<>();

        initFields();
    }

    protected Board(int width, int height, List<Field> fields) {
        this.width = width;
        this.height = height;
        this.fields = fields;
    }

    private void initFields() {
        for(int i = 0; i < width * height; i++)
            fields.add(new Field());
    }

    public Field getField(int x, int y) {
        if(x < 0 || y < 0 || x > width - 1 || y > height - 1)
            throw new UnsupportedOperationException("The field at x = " + x + " | y = " + y + " is not available.");

        return fields.get(x + y * width);
    }

    public void setFieldValue(int x, int y, int value) {
        Field field = getField(x, y);

        if(field.hasValue())
            throw new UnsupportedOperationException("The value of the field at x = " + x + " | y = " + y + " can not be overwritten.");

        if(value != 0 && (value < 1 || value > width * height))
            throw new UnsupportedOperationException("The value '" + value + "' is out of bounds [1," + (width * height) + "].");

        field.setValue(value);
    }

    public List<Field> getFields() {
        return fields;
    }

    public Board clone() {
        List<Field> fieldClones = new ArrayList<>();

        fields.forEach(field -> fieldClones.add(field.clone()));

        return new Board(width, height, fieldClones);
    }
}
