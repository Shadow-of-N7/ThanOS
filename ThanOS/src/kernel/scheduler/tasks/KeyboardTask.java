package kernel.scheduler.tasks;

import devices.KeyCode;
import devices.Keyboard;
import kernel.scheduler.Scheduler;
import kernel.scheduler.Task;
import kernel.scheduler.TaskState;
import shell.Thash;

public class KeyboardTask extends Task {

    public KeyboardTask() {
        _t_blocking = false;
    }

    @Override
    public void run() {
        if(Keyboard.isNewKeyAvailable()) {
            int keyCode = Keyboard.getKeyCode();
            switch (keyCode) {
                case KeyCode.Escape:
                    if(Keyboard.State.IsCtrl && Keyboard.State.IsShift) {
                        MAGIC.inline(0xCC);
                    }

                default:
                    if(Scheduler.redirectKeyboardInput) {
                        // CTRL+C instantly completes the task
                        if(Keyboard.State.IsCtrl && (keyCode == KeyCode.C || keyCode == KeyCode.C + 32)) {
                            Scheduler.getCurrentTask().setState(TaskState.COMPLETED);
                        }
                        else {
                            Scheduler.getCurrentTask().takeKeyCode(keyCode);
                        }
                    }
                    else {
                        Thash.takeKeyCode(keyCode);
                    }
                    break;
            }
        }
        _t_state = TaskState.PAUSED;
    }

    @Override
    public void takeKeyCode(int keyCode) {}

    @Override
    public void setState(byte state) {}
}
