package kernel.scheduler.tasks;

import io.Console;
import kernel.scheduler.Task;
import kernel.scheduler.TaskState;

public class TestTask extends Task {
    @Override
    public void run() {
        Console.clearLine();
        Console.println("Hello world task!");
        state = TaskState.COMPLETED;
    }
}
