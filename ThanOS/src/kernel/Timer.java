package kernel;

public class Timer {
    public static long timerBaseCount = 0; // Counts timer interrupts

    /**
     * Called whenever a timer interrupt arrives.
     */
    public static void updateTimer() {
        ++timerBaseCount;
    }


    /**
     * Returns the amount of timer interrupts since bootup.
     */
    public static long getUptimeReal() {
        return timerBaseCount;
    }


    /**
     * Waits until the given amount of timer interrupts have arrived.
     * @param amount The time to wait.
     */
    public static void waitReal(long amount) {
        while (timerBaseCount < amount) {}
    }
}
