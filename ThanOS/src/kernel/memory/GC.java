package kernel.memory;

import collections.ObjectList;

public class GC {
    private static ObjectList _rootSet;
    private static int _sjcImageSize;
    private static int _sjcImageUpperAddress;


    public static void initialize() {
        int currentObjectAddress = MAGIC.imageBase + 16;
        _sjcImageSize = MAGIC.rMem32(MAGIC.imageBase + 4);
        _sjcImageUpperAddress = MAGIC.imageBase + _sjcImageSize;
        // GC starts from the root set, consisting of all objects within the SJC image.
        _rootSet = new ObjectList();

        while (currentObjectAddress < _sjcImageUpperAddress && currentObjectAddress > MAGIC.imageBase) {
            Object currentObject = MAGIC.cast2Obj(currentObjectAddress);
            _rootSet.add(currentObject);
            currentObjectAddress = MAGIC.cast2Ref(currentObject._r_next);
        }
    }


    public static void collect(){
        mark();
        //sweep();
    }


    private static void mark() {
        for(int i = 0; i < _rootSet.getLength(); i++) {
            markObject(_rootSet.elementAt(i));
        }
    }


    private static void sweep() {
        Object heapObject = Memory.getFirstHeapObject();
        while(heapObject != null) {
            if(heapObject._a_marked)
                heapObject._a_marked = false;
            else
                Memory.removeHeapObject(heapObject);

            heapObject = heapObject._r_next;
        }
    }


    private static void markObject(Object object) {
        if(!(object instanceof EmptyObject)) {
            object._a_marked = true;
            int address = MAGIC.cast2Ref(object);
            for(int i = 0; i < object._r_relocEntries; i++) {
                // Subtract additional 2 to skip next and type
                int nextAddress = MAGIC.rMem32(address - ((i + 2) * MAGIC.ptrSize));
                Object nextObject = MAGIC.cast2Obj(nextAddress);
                if(nextObject != null && nextAddress > _sjcImageUpperAddress) {
                    markObject(object);
                }
            }
        }
    }
}
