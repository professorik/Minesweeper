package model;

public class Game {

    public enum GameLevel {
        /**
         *
         */
        EASY {
            @Override
            public int getWidth() {
                return 3;
            }

            @Override
            public int getHeight() {
                return 3;
            }
        },

        /**
         *
         */
        MEDIUM {
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
        HARD {
            @Override
            public int getWidth() {
                return 30;
            }

            @Override
            public int getHeight() {
                return 30;
            }
        };

        public abstract int getWidth();
        public abstract int getHeight();
    }

    private GameLevel level;
    private GameClock clock = new GameClock();

    private int width;
    private int height;

    /**
     * Create new game with prepared sizes.
     * @param level
     * @param clock
     */
    public Game(GameLevel level, GameClock clock) {
        this.level = level;
        this.clock = clock;

        this.width = level.getWidth();
        this.height = level.getHeight();
    }

    public Game(GameLevel level, GameClock clock, int width, int height) {
        this.level = level;
        this.clock = clock;

        if (width < 0 || height < 0) {
            throw new RuntimeException("Field width and height must be > 0!");
        }
        this.width = width;
        this.height = height;
    }
}