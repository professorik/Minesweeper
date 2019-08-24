package model;

public class Piece {
    private boolean isRigged;
    private boolean isFlagged = false;

    public Piece(boolean isRigged) {
        this.isRigged = isRigged;
    }

    private void toggleFlag() {
        this.isFlagged = !this.isFlagged;
    }
}
