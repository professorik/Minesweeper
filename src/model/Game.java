package model;

import java.util.Random;

public class Game {

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

    public Game(FieldSize size, GameLevel level) {
        this.level = level;
        this.field = generateField(size.getWidth(), size.getHeight(),level);
    }

    private static Piece[][] generateField(int width, int height, GameLevel level) {
        Piece[][] generatedField = new Piece[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                    generatedField[i][j] = new Piece(Math.random() >= 0.5);
            }
        }

        return generatedField;
    }

    public Game(int width, int height, GameLevel level) {
        if (width < 0 || height < 0) {
            throw new RuntimeException("Field width and height must be > 0!");
        }

        this.level = level;

        this.field = generateField(width, height, level);
    }
}