package kernel.scheduler.tasks;

import devices.KeyCode;
import devices.Keyboard;
import io.Console;
import kernel.scheduler.Scheduler;
import kernel.scheduler.Task;

public class EditorTask extends Task {
    private boolean _isInitialized = false;

    public EditorTask() {
        _t_blocking = true;
        _t_fullScreen = true;
    }

    @Override
    public void run() {
        Scheduler.redirectKeyboardInput = true;
        if(!_isInitialized) {
            Console.clear();
            Console.setCaret(0, 0);
            _isInitialized = true;
        }
    }

    @Override
    public void takeKeyCode(int keyCode) {
        if(Keyboard.isPrintable(keyCode)) {
            char c = Keyboard.getChar(keyCode);
            Console.print(c);
        }
        else
        {
            switch (keyCode) {
                case KeyCode.Backspace:
                    if (Console.getCaretX() > 0) {
                        Console.setCaret(Console.getCaretX() - 1, Console.getCaretY());
                        Console.deleteChar();
                    }
                    break;
                case KeyCode.ArrowLeft:
                    if (Console.getCaretX() > 0) {
                        Console.setCaret(Console.getCaretX() - 1, Console.getCaretY());
                    }
                    break;
                case KeyCode.ArrowRight:
                    if (Console.getCaretX() < Console.SCREEN_WIDTH) {
                        Console.setCaret(Console.getCaretX() + 1, Console.getCaretY());
                    }
                    break;
            }
        }
    }

    @Override
    public void setState(byte state) {
        this._t_state = state;
    }
}
