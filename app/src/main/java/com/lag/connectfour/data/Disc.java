package com.lag.connectfour.data;


public class Disc {

    private int column;
    private int row;
    private Player player;

    public Disc(int row, int column, Player player) {
        this.column = column;
        this.row = row;
        this.player = player;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
