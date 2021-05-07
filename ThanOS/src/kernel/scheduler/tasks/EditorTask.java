package kernel.scheduler.tasks;

import devices.KeyCode;
import devices.Keyboard;
import io.Console;
import kernel.scheduler.Scheduler;
import kernel.scheduler.Task;

public class EditorTask extends Task {

    public EditorTask() {
        blocking = true;
    }

    @Override
    public void run() {
        Scheduler.redirectKeyboardInput = true;
    }

    @Override
    public void takeKeyCode(int keyCode) {
        if(Keyboard.isPrintable(keyCode)) {
            char c = Keyboard.getChar(keyCode);
            Console.print(c);
        }
    }

    @Override
    public void setState(byte state) {
        this.state = state;
    }
}
