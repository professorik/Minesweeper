package concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Timers {
    private static Map<Long, TimerThread> timers = new HashMap<>();
    private static interface TimerThread {
        public Runnable getAction();
        public double getDelay();
        public void start();
        public void stop();
        public boolean isRunning();
    }
    private static class TimeoutThread implements TimerThread {
        private boolean running;
        private Runnable action;
        private Thread thread;
        private double delay;

        @Override
        public double getDelay() {
            return delay;
        }

        @Override
        public Runnable getAction() {
            return action;
        }

        @Override
        public void start() {
            if (thread == null) {
                running = true;
                thread = new Thread(() -> {
                    try {
                        Thread.sleep(Math.round(this.getDelay() * 1000));
                        if (isRunning()) getAction().run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        }

        @Override
        public void stop() {
            running = false;
        }

        @Override
        public boolean isRunning() {
            return running;
        }

        public void setAction(Runnable action) {
            this.action = action;
        }

        public void setDelay(double delay) {
            this.delay = delay;
        }
    }

    private static class IntervalThread implements TimerThread {
        private boolean running;
        private Runnable action;
        private Thread thread;
        private double delay;

        @Override
        public double getDelay() {
            return delay;
        }

        @Override
        public Runnable getAction() {
            return action;
        }

        @Override
        public void start() {
            if (thread == null) {
                running = true;
                thread = new Thread(() -> {
                    try {
                        while (true) {
                            Thread.sleep(Math.round(this.getDelay() * 1000));
                            if (isRunning()) getAction().run(); else return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        }

        @Override
        public void stop() {
            running = false;
        }

        @Override
        public boolean isRunning() {
            return running;
        }

        public void setAction(Runnable action) {
            this.action = action;
        }

        public void setDelay(double delay) {
            this.delay = delay;
        }
    }

    public static long createTimeout(Runnable action, double delay) {
        TimeoutThread timerThread = new TimeoutThread();
        timerThread.setAction(action);
        timerThread.setDelay(delay);
        long id = System.nanoTime();
        timers.put(id, timerThread);
        timerThread.start();
        return id;
    }
    public static long createInterval(Runnable action, double delay) {
        IntervalThread timerThread = new IntervalThread();
        timerThread.setAction(action);
        timerThread.setDelay(delay);
        long id = System.nanoTime();
        timers.put(id, timerThread);
        timerThread.start();
        return id;
    }
    public static void stopTimer(long id) {
        TimerThread timerThread = Objects.requireNonNull(timers.get(id));
        timerThread.stop();
        timers.put(id, null);
    }
}
