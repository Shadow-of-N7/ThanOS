package kernel.scheduler;

public abstract class Task {
    public byte state = 0;
    public boolean blocking = true;

    // Convention: At the end of run() a TaskState should be set.
    // If complete/frozen is not set at some point in time, the task will be executed forever.
    public abstract void run();
}
