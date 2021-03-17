package rte;

import java.lang.Object;

public class DynamicRuntime
{
	private static int _nextFreeAddress = 0;

	public static void initializeFreeAddresses() {
		if(_nextFreeAddress == 0) {
			_nextFreeAddress = (MAGIC.imageBase+MAGIC.rMem32(MAGIC.imageBase+4)+0xFFF)&~0xFFF;
		}
	}

	public static Object newInstance(int scalarSize, int relocEntries, SClassDesc type)
	{
		Object object;
		int startAddress, relocs;

		// 4 bytes per reloc required
		relocs = relocEntries << 2;

		// Align the scalars
		scalarSize = (scalarSize + 3) &~ 3;

		// Starting address of the new object
		startAddress = _nextFreeAddress;

		// Place the new object behind the previous one
		_nextFreeAddress += relocs + scalarSize;

		// Zero-initialize the allocated memory
		for (int i = startAddress; i < _nextFreeAddress; i += 4) {
			MAGIC.wMem32(i, 0);
		}
		// Set the object - If IntelliJ shows a type error here, ignore it
		object = MAGIC.cast2Obj(startAddress + relocs);
		object._r_relocEntries = relocEntries;
		object._r_scalarSize = scalarSize;
		object._r_type = type;

		return object;
	}

	public static SArray newArray(int length, int arrDim, int entrySize, int stdType, Object unitType)
	{
		while (true) ;
	}

	public static void newMultArray(SArray[] parent, int curLevel, int destLevel, int length, int arrDim, int entrySize, int stdType, Object unitType)
	{
		while (true) ;
	}

	public static boolean isInstance(Object o, SClassDesc dest, boolean asCast)
	{
		while (true) ;
	}

	public static SIntfMap isImplementation(Object o, SIntfDesc dest, boolean asCast)
	{
		while (true) ;
	}

	public static boolean isArray(SArray o, int stdType, Object unitType, int arrDim, boolean asCast)
	{
		while (true) ;
	}

	public static void checkArrayStore(Object dest, SArray newEntry)
	{
		while (true) ;
	}
}