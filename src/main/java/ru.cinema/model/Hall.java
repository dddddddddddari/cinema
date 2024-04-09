package ru.cinema.model;

import java.util.ArrayList;
import java.util.List;


public class Hall {
    private int id;
    private final int rows;
    private final int cells;

    private final int numOfSeats;
    private final List<Seat> seats = new ArrayList<>();

    public Hall(int id, int rows, int cells) {
        this.id = id;
        this.rows = rows;
        this.cells = cells;
        numOfSeats = rows * cells;
        for (int row = 1; row <= rows; row++) {
            for (int cell = 1; cell <= cells; cell++) {
                seats.add(new Seat(row, cell));
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRows() {
        return rows;
    }

    public int getCells() {
        return cells;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }
}
