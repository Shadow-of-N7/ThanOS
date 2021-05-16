package kernel.scheduler;

public abstract class Task {
    // Convention: All variables shared by all tasks shall begin with _t_
    public byte _t_state = 0;
    public boolean _t_blocking = true;
    public boolean _t_fullScreen = false;

    // Convention: At the end of run() a TaskState should be set.
    // If complete/frozen is not set at some point in time, the task will be executed forever.
    public abstract void run();

    public abstract void takeKeyCode(int keyCode);

    public abstract void setState(byte state);
}
