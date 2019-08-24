package model;

public class GameClock {
    public GameClock() {
        this.startTimeMillis = System.currentTimeMillis();
    }

    public GameClock(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    private long startTimeMillis = 0;

    public long getElapsedTimeMillis () {
        long currentTimeMillis = System.currentTimeMillis();

        return currentTimeMillis - startTimeMillis;
    }
}
