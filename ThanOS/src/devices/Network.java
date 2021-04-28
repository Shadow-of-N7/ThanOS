package devices;

import devices.net.NIC;
import io.Console;

// TODO: IMPLEMENT REMAINING METHOD, UNIGNORE
@SJC.IgnoreUnit
public class Network {
    public static final int TXBUFFERS = 256;
    public static final int TXBUFFERSIZE = 1518;

    public static NIC nic;
    private static byte[][] tempTXBuffers;
    private static boolean[] tempTXBufferUsed;

    public static void init() {
        if (nic == null) {
            //scan pci bus, handlers will be inserted autonomously
            PciBus.scanAndInitDevices();
            if (nic != null) {
                tempTXBuffers = new byte[TXBUFFERS][TXBUFFERSIZE];
                tempTXBufferUsed = new boolean[TXBUFFERS];
            }
        }
        if (nic != null) {
            Console.print("nic has mac: ");
            Console.println(nic.getMacAddress());
        } else Console.print("no network card found");
    }

    public static void markTXBufferAsUnused(long addr) {
        if (nic == null) return;
        for (int i = 0; i < tempTXBufferUsed.length; i++) {
            if ((int) MAGIC.addr(tempTXBuffers[i][0]) == (int) addr) {
                tempTXBufferUsed[i] = false;
                return;
            }
        }
    }

    public static byte[] getTempTXBuffer() {
        if (nic == null) return null;
        byte[] res;
        if ((res = Network.doGetTempTXBuffer()) != null) return res;
        nic.cleanupTxDescriptors();
        return Network.doGetTempTXBuffer();
    }

    private static byte[] doGetTempTXBuffer() {
        for (int i = 0; i < tempTXBufferUsed.length; i++) {
            if (!tempTXBufferUsed[i]) {
                tempTXBufferUsed[i] = true;
                return tempTXBuffers[i];
            }
        }
        return null;
    }
}
