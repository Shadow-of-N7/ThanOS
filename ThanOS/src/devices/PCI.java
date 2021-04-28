package devices;

import collections.PCIScanList;
import io.Console;
import io.Table;
import util.StringConverter;

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

        Table table = new Table();
        table.addColumn();
        table.getColumn(0).addCell("Bus");
        table.getColumn(0).addCell("Device");
        table.getColumn(0).addCell("Function");
        table.getColumn(0).addCell("Type");
        table.getColumn(0).addCell("Vendor ID");
        table.getColumn(0).addCell("Device ID");

        for(int i = 0; i < scanResult.getLength(); i++) {

            PCIScanRecord deviceRecord = scanResult.elementAt(i);
            int[] deviceInfo = deviceRecord.DeviceInfo;

            // Don't show the device -> 0,-1 means invalid or not present

            table.addColumn();
            table.getColumn(i + 1).addCell(StringConverter.toString(deviceRecord.BusNumber));
            table.getColumn(i + 1).addCell(StringConverter.toString(deviceRecord.DeviceNumber));
            table.getColumn(i + 1).addCell(StringConverter.toString(deviceRecord.FunctionNumber));
            int baseClass = deviceInfo[2] >> 24;
            String type = "";
            switch (baseClass) {
                case 0x00:
                    type = "Old device";
                    break;
                case 0x01:
                    type = "Mass storage device";
                    break;
                case 0x02:
                    type = "Network controller";
                    break;
                case 0x03:
                    type = "Display controller";
                    break;
                case 0x04:
                    type = "Multimedia device";
                    break;
                case 0x05:
                    type = "Memory controller";
                    break;
                case 0x06:
                    type = "Bridge";
                    break;
                case 0x07:
                    type = "Communication controller";
                    break;
                case 0x08:
                    type = "System peripheral";
                    break;
                case 0x09:
                    type = "Input device";
                    break;
                case 0x0A:
                    type = "Docking station";
                    break;
                case 0x0B:
                    type = "Processor unit";
                    break;
                case 0x0C:
                    type = "Serial bus";
                    break;
                case 0x0D:
                    type = "Wireless communication device";
                    break;
                case 0x0E:
                    type = "Intelligent controller";
                    break;
                case 0x0F:
                    type = "Satellite communication";
                    break;
            }
            table.getColumn(i + 1).addCell(type);
            table.getColumn(i + 1).addCell(StringConverter.toString(deviceInfo[0] & 0xFFFF));
            table.getColumn(i + 1).addCell(StringConverter.toString(deviceInfo[0] & 0xFFFF0000));
        }
        table.print();
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
