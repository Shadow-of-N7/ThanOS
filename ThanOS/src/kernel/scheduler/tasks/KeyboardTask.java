package kernel.scheduler.tasks;

import devices.KeyCode;
import devices.Keyboard;
import kernel.scheduler.Task;
import kernel.scheduler.TaskState;
import shell.Thash;

public class KeyboardTask extends Task {
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
                    Thash.takeKeyCode(keyCode);
                    break;
            }
        }
        state = TaskState.PAUSED;
    }
}
