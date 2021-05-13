package kernel.memory;

import collections.ObjectList;
import devices.StaticV24;

public class GC {
    private static ObjectList _rootSet = null;
    private static int _markCounter = 0;
    private static int _sweepCounter = 0;


    public static void initialize() {
        int currentObjectAddress = MAGIC.imageBase + 16;
        // GC starts from the root set, consisting of all objects within the SJC image.
        if(_rootSet == null) {
            _rootSet = new ObjectList();
        } else {
            _rootSet.clear();
        }

        while (currentObjectAddress < Memory.getSjcUpperAddress() && currentObjectAddress > MAGIC.imageBase) {
            Object currentObject = MAGIC.cast2Obj(currentObjectAddress);
            _rootSet.add(currentObject);
            currentObjectAddress = MAGIC.cast2Ref(currentObject._r_next);
        }
        _rootSet.add(Memory.getFirstHeapObject());
    }


    /**
     * Triggers a garbage collection run.
     * @param outputStats Whether to print a report.
     */
    public static void collect(boolean outputStats){
        initialize();
        if(outputStats) {
            int markCount = mark();
            StaticV24.print("Objects marked: ");
            StaticV24.println(markCount);
            int sweepCount = sweep();
            StaticV24.print("Objects sweeped: ");
            StaticV24.println(sweepCount);
        }
        else {
            mark();
            sweep();
        }
        Memory.printEmptyObjectInfo();
        Memory.mergeEmptyObjects();
        StaticV24.println("Merging empty objects if possible...");
        Memory.printEmptyObjectInfo();
    }


    private static int mark() {
        _markCounter = 0;
        for(int i = 0; i < _rootSet.getLength(); i++) {
            markObject(_rootSet.elementAt(i));
        }
        return _markCounter;
    }


    private static int sweep() {
        _sweepCounter = 0;
        Object heapObject = Memory.getFirstHeapObject();
        while(heapObject._r_next != null) {
            heapObject = heapObject._r_next;
            if(heapObject._a_marked) {
                heapObject._a_marked = false;
            }
            else {
                // TODO: Is this check really required?
                if(MAGIC.cast2Ref(heapObject) >= Memory.getSjcUpperAddress() && !(heapObject instanceof EmptyObject)) {
                    StaticV24.print("Sweeped: ");
                    StaticV24.printHex(MAGIC.cast2Ref(heapObject), 8);
                    StaticV24.println();
                    Memory.removeHeapObject(heapObject);
                    _sweepCounter++;
                }
            }
        }

        initialize();
        for(int i = 0; i < _rootSet.getLength(); i++) {
            unmarkObject(_rootSet.elementAt(i));
        }
        return _sweepCounter;
    }


    /**
     * Marks all reachable objects for deletion.
     * @param object
     * @return Amount of marked objects.
     */
    private static void markObject(Object object) { // TODO: FAILS AFTER SOME REPEATS
        if(!object._a_marked && !(object instanceof EmptyObject)) {
            StaticV24.print("Marked: ");
            StaticV24.printHex(MAGIC.cast2Ref(object), 8);
            StaticV24.println();
            object._a_marked = true;
            ++_markCounter;
            int address = MAGIC.cast2Ref(object);
            for(int i = 0; i < object._r_relocEntries; i++) {
                // Subtract additional 2 to skip next and type
                int nextAddress = MAGIC.rMem32(address - ((i + 2) * MAGIC.ptrSize));
                Object nextObject = MAGIC.cast2Obj(nextAddress);
                if(nextObject != null && nextAddress > Memory.getSjcUpperAddress()) {
                    markObject(object);
                }
            }
        }
    }


    private static void unmarkObject(Object object) {
        if(object._a_marked && !(object instanceof EmptyObject)) {
            object._a_marked = false;
            ++_markCounter;
            int address = MAGIC.cast2Ref(object);
            for(int i = 0; i < object._r_relocEntries; i++) {
                // Subtract additional 2 to skip next and type
                int nextAddress = MAGIC.rMem32(address - ((i + 2) * MAGIC.ptrSize));
                Object nextObject = MAGIC.cast2Obj(nextAddress);
                if(nextObject != null && nextAddress > Memory.getSjcUpperAddress()) {
                    unmarkObject(object);
                }
            }
        }
    }
}
