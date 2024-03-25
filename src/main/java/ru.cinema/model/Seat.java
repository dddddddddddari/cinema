package ru.cinema.model;

/**
 * Класс описывающий ряд и место в зале.
 * @author Aleksandr Kuznetsov.
 * @version 1.0
 */
public class Seat {
    private int row;
    private int cell;
    private boolean visible;

    public Seat(int row, int cell) {
        this.row = row;
        this.cell = cell;
        this.visible = true;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
