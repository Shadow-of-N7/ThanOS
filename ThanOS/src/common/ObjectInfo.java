package common;

import java.lang.*;

import kernel.scheduler.tasks.EditorTask;
import kernel.scheduler.tasks.KeyboardTask;
import kernel.scheduler.tasks.TestTask;
import rte.*;
import collections.*;
import devices.*;
import io.*;
import kernel.*;
import kernel.memory.*;
import kernel.scheduler.*;
import shell.*;
import util.*;

public class ObjectInfo
{
	public static String getName(Object o)
	{
		if (o == null)
			return "null";
		if (o instanceof String)
			return "String";
		if (o instanceof SIntfDesc)
			return "SIntfDesc";
		if (o instanceof SClassDesc)
			return "SClassDesc";
		if (o instanceof SIntfMap)
			return "SIntfMap";
		if (o instanceof SArray)
			return "SArray";
		if (o instanceof DynamicRuntime)
			return "DynamicRuntime";
		if (o instanceof SMthdBlock)
			return "SMthdBlock";
		if (o instanceof Kernel)
			return "Kernel";
		if (o instanceof Timer)
			return "Timer";
		if (o instanceof BlueScreen)
			return "BlueScreen";
		if (o instanceof BIOS)
			return "BIOS";
		if (o instanceof Interrupt)
			return "Interrupt";
		if (o instanceof MemoryMap)
			return "MemoryMap";
		if (o instanceof Memory)
			return "Memory";
		if (o instanceof GC)
			return "GC";
		if (o instanceof EmptyObject)
			return "EmptyObject";
		if (o instanceof MemoryBlock)
			return "MemoryBlock";
		if (o instanceof Task)
			return "Task";
		if (o instanceof Scheduler)
			return "Scheduler";
		if (o instanceof TaskState)
			return "TaskState";
		if (o instanceof EditorTask)
			return "EditorTask";
		if (o instanceof TestTask)
			return "TestTask";
		if (o instanceof KeyboardTask)
			return "KeyboardTask";
		if (o instanceof Table)
			return "Table";
		if (o instanceof Console)
			return "Console";
		if (o instanceof ObjectList)
			return "ObjectList";
		if (o instanceof CharList)
			return "CharList";
		if (o instanceof CharStack)
			return "CharStack";
		if (o instanceof StringBuilder)
			return "StringBuilder";
		if (o instanceof IntRingBuffer)
			return "IntRingBuffer";
		if (o instanceof MemoryBlockList)
			return "MemoryBlockList";
		if (o instanceof PCIScanList)
			return "PCIScanList";
		if (o instanceof StringConverter)
			return "StringConverter";
		if (o instanceof ObjectInfo)
			return "ObjectInfo";
		if (o instanceof KeyCode)
			return "KeyCode";
		if (o instanceof Device)
			return "Device";
		if (o instanceof PCI)
			return "PCI";
		if (o instanceof StaticV24)
			return "StaticV24";
		if (o instanceof Keyboard)
			return "Keyboard";
		if (o instanceof Thash)
			return "Thash";
		if (o instanceof CommandProcessor)
			return "CommandProcessor";
		if (o instanceof Object)
			return "Object";
		return "Unknown";
	}
}
