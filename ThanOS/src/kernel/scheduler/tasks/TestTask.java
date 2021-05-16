package kernel.scheduler.tasks;

import io.Console;
import kernel.scheduler.Task;
import kernel.scheduler.TaskState;

public class TestTask extends Task {
    @Override
    public void run() {
        Console.clearLine();
        Console.println("Hello world task!");
        _t_state = TaskState.COMPLETED;
    }

    @Override
    public void takeKeyCode(int keyCode) {}

    @Override
    public void setState(byte state) {}
}
