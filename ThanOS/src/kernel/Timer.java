package kernel;

import io.Console;

public class Timer {
    private static long _timerBaseCount = 0; // Counts timer interrupts
    private static long _elapsedTime = 0;

    /**
     * Called whenever a timer interrupt arrives.
     */
    @SJC.Inline
    public static void updateTimer() {
        ++_timerBaseCount;
    }


    /**
     * Returns the amount of timer interrupts since bootup.
     */
    @SJC.Inline
    public static long getUptimeReal() {
        return _timerBaseCount;
    }


    /**
     * Waits until the given amount of timer interrupts have arrived.
     * @param amount The time to wait.
     */
    @SJC.NoOptimization
    public static void waitReal(long amount) {
        while (_timerBaseCount < amount) {
            // This only exists to stop the compiler from optimizing the loop away
            ++_elapsedTime;
        }
    }
}
