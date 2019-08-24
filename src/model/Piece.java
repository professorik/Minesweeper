package model;

public class Piece {
    private boolean isRigged = false;
    private boolean isFlagged = false;

    public Piece(boolean isRigged) {
        this.isRigged = isRigged;
    }

    private void toggleFlag() {
        this.isFlagged = !this.isFlagged;
    }

    @Override
    public String toString() {
        return isRigged ? "1" : "0";
    }
}
