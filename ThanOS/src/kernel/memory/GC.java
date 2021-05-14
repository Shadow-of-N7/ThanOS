package kernel.memory;

import common.ObjectInfo;
import devices.StaticV24;
import io.Console;

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
        _markCounter = 0;
        int markCount = 0;
        Object imageObject = MAGIC.cast2Obj(MAGIC.imageBase + 16);
        while(Memory.isImageObject(imageObject)) {
            markCount += mark(imageObject);
            imageObject = imageObject._r_next;
            StaticV24.println("Image object and descendants done");
        }



        if(outputStats) {
            //markCount += mark(Memory.getFirstHeapObject());
            StaticV24.print("Objects marked: ");
            StaticV24.println(markCount);
            StaticV24.println("Starting sweep...");
            int sweepCount = sweep();
            StaticV24.print("Objects swept: ");
            StaticV24.println(sweepCount);
        }
        else {
            //mark(Memory.getFirstHeapObject());
            sweep();
        }
        unmarkImageObjects();
        Memory.mergeEmptyObjects();
        //Memory.printEmptyObjectInfo();
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
                int oType = MAGIC.rMem32(nextAddress - 4);
                if(nextObject != null && nextAddress > Memory.getSjcUpperAddress() && oType > MAGIC.imageBase) {
                    mark(nextObject);
                }
            }
        }
        return _markCounter;
    }


    /**
     * Recursively marks all reachable objects for deletion.
     * @param object The object to mark. Usually initialized with the first heap object.
     * @return Amount of marked objects.
     */
    private static int unmark(Object object) {
        if(object._a_marked && !(object instanceof EmptyObject)) {
            //StaticV24.println(ObjectInfo.getName(object));
            //StaticV24.println(_markCounter);
            object._a_marked = false;
            ++_markCounter;
            int address = MAGIC.cast2Ref(object);
            for(int i = 0; i < object._r_relocEntries; i++) {
                // Subtract additional 2 to skip next and type - fetched by instRelocEntries
                int nextAddress = MAGIC.rMem32(address - ((i + MAGIC.getInstRelocEntries("Object")) * MAGIC.ptrSize));
                Object nextObject = MAGIC.cast2Obj(nextAddress);
                int oType = MAGIC.rMem32(nextAddress - 4);
                if(nextObject != null && nextAddress > Memory.getSjcUpperAddress() && oType > MAGIC.imageBase) {
                    unmark(nextObject);
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
                StaticV24.print('u');
                StaticV24.print(':');
                StaticV24.println(ObjectInfo.getName(heapObject));
                heapObject = heapObject._r_next;
            }
            else {
                if(MAGIC.cast2Ref(heapObject) > Memory.getSjcUpperAddress() && !(heapObject instanceof EmptyObject)) {
                    _sweepCounter++;
                    StaticV24.print('s');
                    StaticV24.print(':');
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


    private static void unmarkImageObjects() {
        Object imageObject = MAGIC.cast2Obj(MAGIC.imageBase + 16);
        while(imageObject != null) {
            unmark(imageObject);
            imageObject = imageObject._r_next;
        }
    }

}
