package kernel.scheduler;

public class TaskState {
    public static final byte READY = 0;
    public static final byte RUNNING = 1;
    public static final byte COMPLETED = 2;
    public static final byte PAUSED = 3; // Only applies to periodically running tasks
    public static final byte FROZEN = 4;
}
