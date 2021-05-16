package kernel.scheduler.tasks;

import io.Console;
import io.Table;
import kernel.scheduler.Task;

public class NullTask extends Task {
    private boolean _isInitialized = false;
    Table table;

    @Override
    public void run() {
        Console.print(table.getRow(0).getCell(0).text);
    }

    @Override
    public void takeKeyCode(int keyCode) {

    }

    @Override
    public void setState(byte state) {

    }
}
