package kernel.memory;

public class MMU {
    public static void setCR3(int addr) {
        MAGIC.inline(0x8B, 0x45); MAGIC.inlineOffset(1, addr); //mov eax,[ebp+8]
        MAGIC.inline(0x0F, 0x22, 0xD8); //mov cr3,eax
    }


    public static void enableVirtualMemory() {
        MAGIC.inline(0x0F, 0x20, 0xC0); //mov eax,cr0
        MAGIC.inline(0x0D, 0x00, 0x00, 0x01, 0x80); //or eax,0x80010000
        MAGIC.inline(0x0F, 0x22, 0xC0); //mov cr0,eax
    }


    public static int getCR2() {
        int cr2=0;
        MAGIC.inline(0x0F, 0x20, 0xD0); //mov e/rax,cr2
        MAGIC.inline(0x89, 0x45); MAGIC.inlineOffset(1, cr2); //mov [ebp-4],eax
        return cr2;
    }


    private static void buildPageDirectory() {
        int[] directory = new int[1024];

        // Set lower bits - Writable and Present
        for(int i = 0; i < directory.length; i++) {
            directory[i] = 0x3;
        }
    }
}
