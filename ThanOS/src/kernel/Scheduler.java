package kernel;

import collections.ObjectList;

public class Scheduler {
    private Object _currentTask;
    private Object _keyboardTask;

    private ObjectList _tasks;

    public void initialize() {
        _tasks = new ObjectList();
    }


    public static class Task {

    }
}
