package shell;

import devices.PCI;
import devices.StaticV24;
import io.Console;
import io.Table;
import kernel.Kernel;
import kernel.memory.GC;
import kernel.memory.Memory;
import kernel.memory.MemoryMap;
import kernel.scheduler.Scheduler;
import kernel.scheduler.tasks.EditorTask;
import kernel.scheduler.tasks.TestTask;

public class CommandProcessor {
    private static final String testGraphics = "testgm";
    private static final String helloWorld = "Hello";
    private static final String cls = "cls";
    private static final String clear = "clear";
    private static final String memMap = "memmap";
    private static final String pciscan = "pciscan";
    private static final String tasktest = "tasktest";
    private static final String editor = "atto";
    private static final String gc = "gc";

    public String processCommand(String input) {
        boolean recognized = false;
        if(input.equals(cls) || input.equals(clear)) {
            Console.clear();
            recognized = true;
        }
        if(input.equals(testGraphics))
        {
            Kernel.testGraphicsMode();
            recognized = true;
        }
        if(input.equals(helloWorld)) {
            return "World!";
        }
        if(input.equals(memMap))
        {
            MemoryMap.printMemoryMap2();
            recognized = true;
        }
        if(input.equals(pciscan))
        {
            PCI.printScan();
            recognized = true;
        }
        if(input.equals("gm")) {
            // TODO: VESA DRIVER
            return "Not implemented yet.";
        }
        if(input.equals(tasktest)) {
            Scheduler.addTask(new TestTask());
            recognized = true;
        }
        if(input.equals(editor)) {
            Scheduler.addTask(new EditorTask());
            recognized = true;
        }
        if(input.equals(gc)) {
            GC.collect(true);
            recognized = true;
        }
        if(input.equals("ho")) {
            Console.print("Heap object count: ");
            Console.println(Memory.getHeapObjectCount());
            recognized = true;
        }
        if(input.equals("help")) {
            Table table = new Table();
            int rowIndex = table.addRow();
            table.getRow(rowIndex).addCell("Command");
            table.getRow(rowIndex).addCell("Explanation");
            rowIndex = table.addRow();
            table.getRow(rowIndex).addCell("testgm");
            table.getRow(rowIndex).addCell("Executes a short graphics mode test.");
            rowIndex = table.addRow();
            table.getRow(rowIndex).addCell("gm");
            table.getRow(rowIndex).addCell("Starts the graphics mode.");
            rowIndex = table.addRow();
            table.getRow(rowIndex).addCell("memmap");
            table.getRow(rowIndex).addCell("Prints a memory map.");
            rowIndex = table.addRow();
            table.getRow(rowIndex).addCell("pciscan");
            table.getRow(rowIndex).addCell("Scans the PCI bus for devices.");
            rowIndex = table.addRow();
            table.getRow(rowIndex).addCell("clear/cls");
            table.getRow(rowIndex).addCell("Clears the screen.");

            return table.toString(true);
        }
        if(input.equals("shutdown")) {
            Kernel.shutdownQEmu();
            return "";
        }

        if(recognized){
            return "";
        }
        else
        {
            return "I am inevitable!";
        }
    }
}
