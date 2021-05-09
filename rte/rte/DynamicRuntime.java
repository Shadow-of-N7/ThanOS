package rte;

import io.Console;
import kernel.memory.Memory;
import rte.SArray;
import rte.SClassDesc;
import rte.SIntfDesc;
import rte.SIntfMap;
import kernel.memory.EmptyObject;

import java.lang.Object;

public class DynamicRuntime
{
	// Was 0
	private static int _nextFreeAddress = 0;
	private static int _previousObjectAddress = 1;

	/**
	 * Memory size to add to the next object to be allocated.
	 */
	public static int sizeOffset = 0;

	/**
	 * Returns the next free address for basic mode.
	 * @return
	 */
	public static int getBasicNextAddress() {
		return _nextFreeAddress;
	}

	public static void initializeFreeAddresses() {
		if(_nextFreeAddress == 0) {
			_nextFreeAddress = (MAGIC.imageBase + MAGIC.rMem32(MAGIC.imageBase + 4));
		}
	}

	public static Object newInstance(int scalarSize, int relocEntries, SClassDesc type)
	{
		Object object;
		if(Memory.isAdvancedMode) {

			int startAddress, relocSize;

			// 4 bytes per reloc required
			relocSize = relocEntries << 2;

			// Align the scalars
			scalarSize = (scalarSize + 3) & ~3;
			// Apply object enlargement if emptyObject is too small
			scalarSize += sizeOffset;

			int totalSize = relocSize + scalarSize;
			// Starting address of the new object
			startAddress = Memory.getFreeAddress(totalSize);


			// Zero-initialize the allocated memory
			for (int i = startAddress; i < startAddress + totalSize; i += 4) {
				MAGIC.wMem32(i, 0);
			}

			int objectAddress = startAddress + relocSize;

			object = MAGIC.cast2Obj(objectAddress);
			MAGIC.assign(object._r_scalarSize, scalarSize);
			MAGIC.assign(object._r_relocEntries, relocEntries);
			MAGIC.assign(object._r_type, type);

			// Update the previous object
			if (Memory.getLastObject() != null) {
				Object lastObject = Memory.getLastObject();
				MAGIC.assign(lastObject._r_next, object);
			}
			Memory.updateLastObject(object);
		}
		else {
			int startAddress, relocSize;

			// 4 bytes per reloc required
			relocSize = relocEntries << 2;

			// Align the scalars
			scalarSize = (scalarSize + 3) &~ 3;

			// Starting address of the new object
			startAddress = _nextFreeAddress;

			// Place the new object behind the previous one
			_nextFreeAddress += relocSize + scalarSize;

			// Zero-initialize the allocated memory
			for (int i = startAddress; i < _nextFreeAddress; i += 4) {
				MAGIC.wMem32(i, 0);
			}

			int objectAddress = startAddress + relocSize;

			object = MAGIC.cast2Obj(objectAddress);
			MAGIC.assign(object._r_scalarSize, scalarSize);
			MAGIC.assign(object._r_relocEntries, relocEntries);
			MAGIC.assign(object._r_type, type);

			// Update the previous object
			if (_previousObjectAddress != -1)
			{
				Object lastObject = MAGIC.cast2Obj(_previousObjectAddress);
				MAGIC.assign(lastObject._r_next, object);
			}
			_previousObjectAddress = objectAddress;
		}
		// Reset the size offset after allocation
		sizeOffset = 0;
		return object;
	}

	public static Object newEmptyObject(int startAddress, int size) {
		SClassDesc type = (SClassDesc) MAGIC.clssDesc("EmptyObject");
		// Get the base size so we don't overwrite at the end
		int scalarSize = MAGIC.getInstScalarSize("EmptyObject");
		int relocEntries = MAGIC.getInstRelocEntries("EmptyObject");
		int relocSize = relocEntries << 2;
		int totalSize = relocSize + scalarSize;

		scalarSize = size - relocSize;
		scalarSize = (scalarSize + 3) &~ 3;

		for(int i = startAddress; i < startAddress + totalSize; i += 4) {
			MAGIC.wMem32(i, 0);
		}

		int objectAddress = startAddress + relocSize;

		Object object = MAGIC.cast2Obj(objectAddress);
		MAGIC.assign(object._r_scalarSize, scalarSize);
		MAGIC.assign(object._r_relocEntries, relocEntries);
		MAGIC.assign(object._r_type, type);
		return object;
	}

	public static SArray newArray(int length, int arrDim, int entrySize, int stdType,
								  SClassDesc unitType) { //unitType is not for sure of type SClassDesc
		int scS, rlE;
		SArray me;

		if (stdType==0 && unitType._r_type!=MAGIC.clssDesc("SClassDesc"))
			MAGIC.inline(0xCC); //check type of unitType, we don't support interface arrays
		scS=MAGIC.getInstScalarSize("SArray");
		rlE=MAGIC.getInstRelocEntries("SArray");
		if (arrDim>1 || entrySize<0) rlE+=length;
		else scS+=length*entrySize;
		me=(SArray)newInstance(scS, rlE, (SClassDesc) MAGIC.clssDesc("SArray"));
		MAGIC.assign(me.length, length);
		MAGIC.assign(me._r_dim, arrDim);
		MAGIC.assign(me._r_stdType, stdType);
		MAGIC.assign(me._r_unitType, (Object) unitType);
		return me;
	}

	public static void newMultArray(SArray[] parent, int curLevel, int destLevel,
									int length, int arrDim, int entrySize, int stdType, SClassDesc clssType) {
		int i;

		if (curLevel+1<destLevel) { //step down one level
			curLevel++;
			for (i=0; i<parent.length; i++) {
				newMultArray((SArray[])((Object)parent[i]), curLevel, destLevel,
						length, arrDim, entrySize, stdType, clssType);
			}
		}
		else { //create the new entries
			destLevel=arrDim-curLevel;
			for (i=0; i<parent.length; i++) {
				parent[i]=newArray(length, destLevel, entrySize, stdType, clssType);
			}
		}
	}

	public static boolean isInstance(Object o, SClassDesc dest, boolean asCast) {
		SClassDesc check;

		if (o==null) {
			if (asCast) return true; //null matches all
			return false; //null is not an instance
		}
		check=o._r_type;
		while (check!=null) {
			if (check==dest) return true;
			check=check.parent;
		}
		if (asCast) MAGIC.inline(0xCC);
		return false;
	}

	public static SIntfMap isImplementation(Object o, SIntfDesc dest, boolean asCast) {
		SIntfMap check;

		if (o==null) return null;
		check=o._r_type.implementations;
		while (check!=null) {
			if (check.owner==dest) return check;
			check=check.next;
		}
		if (asCast) MAGIC.inline(0xCC);
		return null;
	}

	public static boolean isArray(SArray o, int stdType, SClassDesc clssType, int arrDim, boolean asCast) {
		SClassDesc clss;

		//in fact o is of type "Object", _r_type has to be checked below - but this check is faster than "instanceof" and conversion
		if (o==null) {
			if (asCast) return true; //null matches all
			return false; //null is not an instance
		}
		if (o._r_type!=MAGIC.clssDesc("SArray")) { //will never match independently of arrDim
			if (asCast) MAGIC.inline(0xCC);
			return false;
		}
		if (clssType==MAGIC.clssDesc("SArray")) { //special test for arrays
			if (o._r_unitType==MAGIC.clssDesc("SArray")) arrDim--; //an array of SArrays, make next test to ">=" instead of ">"
			if (o._r_dim>arrDim) return true; //at least one level has to be left to have an object of type SArray
			if (asCast) MAGIC.inline(0xCC);
			return false;
		}
		//no specials, check arrDim and check for standard type
		if (o._r_stdType!=stdType || o._r_dim<arrDim) { //check standard types and array dimension
			if (asCast) MAGIC.inline(0xCC);
			return false;
		}
		if (stdType!=0) {
			if (o._r_dim==arrDim) return true; //array of standard-type matching
			if (asCast) MAGIC.inline(0xCC);
			return false;
		}
		//array of objects, make deep-check for class type (PicOS does not support interface arrays)
		if (o._r_unitType._r_type!=MAGIC.clssDesc("SClassDesc")) MAGIC.inline(0xCC);
		clss=(SClassDesc) o._r_unitType;
		while (clss!=null) {
			if (clss==clssType) return true;
			clss=clss.parent;
		}
		if (asCast) MAGIC.inline(0xCC);
		return false;
	}

	public static void checkArrayStore(SArray dest, SArray newEntry) {
		if (dest._r_dim>1) isArray(newEntry, dest._r_stdType, (SClassDesc) dest._r_unitType, dest._r_dim-1, true);
		else if (dest._r_unitType==null) MAGIC.inline(0xCC);
		else isInstance(newEntry, (SClassDesc) dest._r_unitType, true);
	}

	public static void nullException() {
		//Console.println();
		//Console.println("Null reference exception!");
		while (true);
	}
}