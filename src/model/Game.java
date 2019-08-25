package model;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    public String fieldString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Piece[] pieces :
                field) {
            stringBuilder.append(Arrays.toString(pieces)).append("\n");
        }

        return stringBuilder.toString();
    }

    public enum FieldSize {
        /**
         *
         */
        EASY {
            @Override
            public int getWidth() {
                return 9;
            }

            @Override
            public int getHeight() {
                return 9;
            }
        },

        /**
         *
         */
        MEDIUM {
            @Override
            public int getWidth() {
                return 30;
            }

            @Override
            public int getHeight() {
                return 30;
            }
        },

        /**
         *
         */
        HARD {
            @Override
            public int getWidth() {
                return 30;
            }

            @Override
            public int getHeight() {
                return 45;
            }
        };

        public abstract int getWidth();

        public abstract int getHeight();
    }

    public enum GameLevel {
        EASY {
            @Override
            public float getBombPercent() {
                return 0.1234f;
            }
        },
        MEDIUM {
            @Override
            public float getBombPercent() {
                return 0.246f;
            }
        },
        HARD {
            @Override
            public float getBombPercent() {
                return 0.34f;
            }
        };

        public abstract float getBombPercent();
    }

    private GameLevel level;
    private Piece[][] field;
    private int width;
    private int height;

    public Game(FieldSize size, GameLevel level) {
        this.level = level;
        this.height = size.getHeight();
        this.width = size.getWidth();
        this.field = generateField(width, height, level);
    }

    public GameLevel getLevel() {
        return level;
    }

    private static Piece[][] generateField(int width, int height, GameLevel level) {
        Piece[][] generatedField = new Piece[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                generatedField[i][j] = new Piece(false);
            }
        }

        for (int i = 0; i < level.getBombPercent() * width * height; i++) {
            int x = ThreadLocalRandom.current().nextInt(0, width);
            int y = ThreadLocalRandom.current().nextInt(0, height);

            generatedField[x][y] = new Piece(true);
        }

        return generatedField;
    }

    public Piece[][] getField() {
        return field;
    }

    public Game(int width, int height, GameLevel level) {
        if (width < 0 || height < 0) {
            throw new RuntimeException("Field width and height must be > 0!");
        }

        this.level = level;

        this.height = height;
        this.width = width;
        this.field = generateField(width, height, level);
    }

    public int getSurroundingBombCount (int posX, int posY) {
        int count = 0;

        if (posX < 0 || posX > width - 1 || posY < 0 || posY > height - 1) {
            throw new RuntimeException("Invalid coordinates!");
        }

        //upper row
        if (posX > 0 && posY > 0) {
            if (isPieceRigged(posX - 1, posY - 1)) {
                count++;
            }
        }

        if (posY > 0) {
            if (isPieceRigged(posX, posY - 1)) {
                count++;
            }
        }

        if (posX < width - 1 && posY > 0) {
            if (isPieceRigged(posX + 1, posY - 1)) {
                count++;
            }
        }
        //this row
        if (posX > 0) {
            if (isPieceRigged(posX - 1, posY)) {
                count++;
            }
        }

        if (posX < width - 1) {
            if (isPieceRigged(posX + 1, posY)) {
                count++;
            }
        }
        //next row
        if (posX > 0 && posY < height - 1) {
            if (isPieceRigged(posX - 1, posY + 1)) {
                count++;
            }
        }

        if (posY < height - 1) {
            if (isPieceRigged(posX, posY + 1)) {
                count++;
            }
        }

        if (posX < width - 1 && posY < height - 1) {
            if (isPieceRigged(posX + 1, posY + 1)) {
                count++;
            }
        }
        return count;
    }

    public void revealPiece(int posX, int posY) {
        getPiece(posX, posY).reveal();
    }

    private Piece getPiece(int posX, int posY) {
        return field[posX][posY];
    }

    public boolean isPieceRigged(int posX, int posY) {
        return getPiece(posX, posY).isRigged();
    }
}