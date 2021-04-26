package devices;

import collections.PCIScanList;
import io.Console;

public class PCI {
    private final static int MAX_BUS_NUMBER = 255;
    private final static int MAX_DEVICE_NUM = 31;
    private final static int MAX_FUNCTION_NUM = 7;
    private final static int MAX_REGISTER_NUM = 63;

    public static PCIScanList scan() {
        PCIScanList entries = new PCIScanList();

        for(int busNum = 0; busNum <= MAX_BUS_NUMBER; busNum++) {
            for(int deviceNum = 0; deviceNum <= MAX_DEVICE_NUM; deviceNum++) {
                for(int functionNum = 0; functionNum <= MAX_FUNCTION_NUM; functionNum++) {
                    // Set highest bit as necessary
                    int address = 0x80000000;
                    address |= ((byte) busNum) << 16;
                    address |= deviceNum << 11;
                    // Leave function num at 0 for now
                    address |= functionNum << 8;

                    // save the base address, then insert the register into address then restore and repeat
                    int baseAddress = address;

                    // Contains basic data about the device.
                    // 0: Device ID, Vendor ID
                    // 1: Status, Instruction
                    // 2: Base class code, Subclass code, Interface, Revision
                    // 3: BIST, Header, Latency, CLG
                    int[] deviceInfo = new int[4];

                    for (int registerNum = 0; registerNum < 3; registerNum++) {
                        address |= registerNum << 2;

                        MAGIC.wIOs32(0x0CF8, address);
                        deviceInfo[registerNum] = MAGIC.rIOs32(0x0CFC);
                        // Restore the address for the next register.
                        address = baseAddress;
                    }
                    if (!(deviceInfo[0] == 0 || deviceInfo[0] == -1)) {
                        entries.add(new PCIScanRecord(deviceInfo, busNum, deviceNum, functionNum));
                    }
                }
            }
            //if(busNum > 32) break; // TODO: REMOVE THIS
        }
        return entries;
    }


    public static void printScan() {

        PCIScanList scanResult = scan();

        for(int i = 0; i < 5; i++) {

            PCIScanRecord deviceRecord = scanResult.elementAt(i);
            int[] deviceInfo = deviceRecord.DeviceInfo;

            // Don't show the device -> 0,-1 means invalid or not present

            Console.print("Found device: Bus ");
            Console.print(deviceRecord.BusNumber);
            Console.print(", Device ");
            Console.printHex(deviceRecord.DeviceNumber);
            Console.print(", Function ");
            Console.printHex(deviceRecord.FunctionNumber);
            Console.println();
            Console.print("  Type: ");
            int baseClass = deviceInfo[2] >> 24;
            switch (baseClass) {
                case 0x00:
                    Console.print("Old device");
                    break;
                case 0x01:
                    Console.print("Mass storage device");
                    break;
                case 0x02:
                    Console.print("Network controller");
                    break;
                case 0x03:
                    Console.print("Display controller");
                    break;
                case 0x04:
                    Console.print("Multimedia device");
                    break;
                case 0x05:
                    Console.print("Memory controller");
                    break;
                case 0x06:
                    Console.print("Bridge");
                    break;
                case 0x07:
                    Console.print("Communication controller");
                    break;
                case 0x08:
                    Console.print("System peripheral");
                    break;
                case 0x09:
                    Console.print("Input device");
                    break;
                case 0x0A:
                    Console.print("Docking station");
                    break;
                case 0x0B:
                    Console.print("Processor unit");
                    break;
                case 0x0C:
                    Console.print("Serial bus");
                    break;
                case 0x0D:
                    Console.print("Wireless communication device");
                    break;
                case 0x0E:
                    Console.print("Intelligent controller");
                    break;
                case 0x0F:
                    Console.print("Satellite communication");
                    break;
            }
            Console.print(" (");
            Console.print(baseClass);
            Console.println(")");
            Console.print("  Vendor ID:");
            Console.print(deviceInfo[0] & 0xFFFF);
            Console.print(", Device ID:");
            Console.println(deviceInfo[0] & 0xFFFF0000);
            Console.println();
        }
    }


    public static class PCIScanRecord {
        public int BusNumber;
        public int DeviceNumber;
        public int FunctionNumber;
        public int[] DeviceInfo;

        public PCIScanRecord(int[] deviceInfo, int busNumber, int deviceNumber, int functionNumber) {
            DeviceInfo = deviceInfo;
            BusNumber = busNumber;
            DeviceNumber = deviceNumber;
            FunctionNumber = functionNumber;
        }

        public PCIScanRecord() {}
    }
}
