package kernel.memory;

import common.ObjectInfo;
import devices.StaticV24;

public class GC {
    private static int _markCounter = 0;
    private static int _sweepCounter = 0;


    public static void initialize() {
    }


    /**
     * Triggers a garbage collection run.
     * @param outputStats Whether to print a report.
     */
    public static void collect(boolean outputStats){
        int markCount = 0;
        Object imageObject = MAGIC.cast2Obj(MAGIC.imageBase + 16);
        while(isImageObject(imageObject)) {
            markCount += mark(imageObject);
            imageObject = imageObject._r_next;
        }


        if(outputStats) {
            //markCount += mark(Memory.getFirstHeapObject());
            StaticV24.print("Objects marked: ");
            StaticV24.println(markCount);
            int sweepCount = sweep();
            StaticV24.print("Objects swept: ");
            StaticV24.println(sweepCount);
        }
        else {
            //mark(Memory.getFirstHeapObject());
            sweep();
        }
        Memory.mergeEmptyObjects();
        Memory.printEmptyObjectInfo();
    }


    /**
     * Recursively marks all reachable objects for deletion.
     * @param object The object to mark. Usually initialized with the first heap object.
     * @return Amount of marked objects.
     */
    private static int mark(Object object) {
        if(!object._a_marked && !(object instanceof EmptyObject)) {
            StaticV24.println(ObjectInfo.getName(object));
            object._a_marked = true;
            ++_markCounter;
            int address = MAGIC.cast2Ref(object);
            for(int i = 0; i < object._r_relocEntries; i++) {
                // Subtract additional 2 to skip next and type - fetched by instRelocEntries
                int nextAddress = MAGIC.rMem32(address - ((i + MAGIC.getInstRelocEntries("Object")) * MAGIC.ptrSize));
                Object nextObject = MAGIC.cast2Obj(nextAddress);
                if(nextObject != null && nextAddress > Memory.getSjcUpperAddress()) {
                    mark(object);
                }
            }
        }
        return _markCounter;
    }


    private static int sweep() {
        _sweepCounter = 0;
        Object heapObject = Memory.getFirstHeapObject();
        while(heapObject != null) {
            if(heapObject._a_marked) {
                heapObject._a_marked = false;
                heapObject = heapObject._r_next;
            }
            else {
                if(MAGIC.cast2Ref(heapObject) > Memory.getSjcUpperAddress() && !(heapObject instanceof EmptyObject)) {
                    _sweepCounter++;
                    StaticV24.println(ObjectInfo.getName(heapObject));
                    heapObject = Memory.removeHeapObject(heapObject);
                }
                else {
                    heapObject._a_marked = false;
                    heapObject = heapObject._r_next;
                }
            }
        }

        return _sweepCounter;
    }


    /**
     * Determines whether the given object is part of the image.
     * @param object The object to check.
     * @return Whether the given object is part of the image.
     */
    private static boolean isImageObject(Object object)
    {
        if (object == null) {
            return false;
        }
        int objectAddress = MAGIC.cast2Ref(object);
        return objectAddress > MAGIC.imageBase && objectAddress < Memory.getSjcUpperAddress();
    }

}
