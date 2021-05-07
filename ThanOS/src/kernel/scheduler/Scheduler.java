package kernel.scheduler;

import collections.ObjectList;
import io.Console;
import kernel.scheduler.tasks.KeyboardTask;

public class Scheduler {
    private static Task _currentTask;
    private static int _currentTaskIndex;
    private static Task _keyboardTask;
    private static boolean _isRunning = false;

    private static ObjectList _tasks;

    public static void initialize() {
        _tasks = new ObjectList();
        _keyboardTask = new KeyboardTask();
    }


    public static void start() {
        _isRunning = true;
        _currentTask = (Task) _tasks.elementAt(0);
    }


    public static void stop() {
        _isRunning = false;
    }


    public static void run() {
        // The keyboard task must run independently
        _keyboardTask.run();

        if (_isRunning) {
            if(_currentTask != null) {
                // Remove completed or frozen tasks
                if(_currentTask.state == TaskState.COMPLETED || _currentTask.state == TaskState.FROZEN) {
                    _tasks.removeAt(_currentTaskIndex);
                    return;
                }
                _currentTask.state = TaskState.RUNNING;
                _currentTask.run();
            }
            else {
                if(_tasks.getLength() > 0) {
                    _currentTaskIndex = 0;
                    _currentTask = (Task)_tasks.elementAt(_currentTaskIndex);
                }
            }

            // Go back to list start if last element is reached
            if(_currentTaskIndex >= _tasks.getLength() - 1) {
                _currentTaskIndex = 0;
                _currentTask = (Task)_tasks.elementAt(_currentTaskIndex);
            }
            else {
                _currentTaskIndex += 1;
                _currentTask = (Task)_tasks.elementAt(_currentTaskIndex);
            }
        }
    }


    public static void addTask(Task task) {
        if(_tasks.getLength() == 0) {
            _tasks.add((Object)task);
            _currentTaskIndex = 0;
            _currentTask = (Task)_tasks.elementAt(_currentTaskIndex);
        }
        else {
            _tasks.add((Object) task);
        }
    }


    public static void removeTask(int index) {
        Task task = (Task)_tasks.elementAt(index);
        if(task.state != TaskState.RUNNING) {
            _tasks.removeAt(index);
        }
    }


    public static Task getTask(Task task) {
        for (int i = 0; i < _tasks.getLength(); i++) {
            if(_tasks.elementAt(i) == task) {
                return (Task)_tasks.elementAt(i);
            }
        }
        return null;
    }
}
