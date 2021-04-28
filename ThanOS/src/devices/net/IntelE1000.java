package devices.net;

import kernel.Kernel;
import devices.Network;
import devices.PciBus;

/**
 * Intel 8654xxx Gigabit Ethernet NIC Driver Based on implementation by Alexander Weggerle.
 * Ported to Green64 by Thilo Schmitt. Ported to Rainbow by Steffen Gerhold.
 * Further porting by Stefan Frenz
 * 
 * Tested and developed with "82540EM-A / 8086h / 100E / Desktop" chipset (rev. 2)
 * 
 * @author gerhold
 *         2009-11-12, gerhold, modified some code to be compatible with the new
 *         sun-java-style compiler auto-conversion of basic types
 */
public class IntelE1000 extends NIC {
  private static final int CTRL_RST = 0x04000000; // Global reset
  private static final int TXBUF_ELEMCOUNT = 256;
  private static final int TXBUF_ELEMSIZE_IN_LONG = 2;
  private static final int TXBUF_ELEMSIZE_IN_BYTE = TXBUF_ELEMSIZE_IN_LONG * 8;
  private static final int RXBUF_ELEMCOUNT = 256;
  private static final int RXBUF_ELEMSIZE_IN_LONG = 2;
  private static final int RXBUF_ELEMSIZE_IN_BYTE = RXBUF_ELEMSIZE_IN_LONG * 8;
  private static final int RXBUF_ELEMBUFFER_SIZE = 1518;
  private static final int TIPG_COPPER = 10 | 10 << 10 | 10 << 20; // set the default Inter Packet Gap to 10. Suggested value for copper network
  private static final long FIRST_DESC_OF_PACKET = (1l << 36);
  private static final long RXDESC_STATUS = 0xFF00000000l;
  // bandwidth vs. latency
  // better, when bandwidth is more important than latency
  // static final int RX_TIMER_DELAY = 10; // in 1.024us steps
  // static final int RX_ABS_TIMER_DELAY = 100; // in 1.024us steps
  // better, when latency is more important than bandwidth (also possibly huge
  // CPU performance hit)
  // for every incoming packet an interrupt is generated
  private static final int RX_TIMER_DELAY = 0; // in 1.024us steps
  private static final int RX_ABS_TIMER_DELAY = 0; // in 1.024us steps
  private static final int MAX_PACKET_SIZE = RXBUF_ELEMBUFFER_SIZE;
  private long macAddress;
  private int physMmioBaseAddress, logMmioBaseAddress, physFlashBaseAddress, logFlashBaseAddress;
  private int deviceId;
  private IntelE1000_MMIO mmio;
  private IntelE1000_Flash flash;
  private long[] txRingBuffer = null;
  private long[] rxRingBuffer = null;
  private byte[][] rxBuffers = null;
  private boolean dropRX = false;
  //private long packSendCount = 0l;
  private int EEPROM_READ_DONE = 0x10;
  private int EEPROM_READ_OFFSET_SHIFT = 8;
  /*
   * NVM related variables (for ICH 10 integrated MAC 8257x with device id 0x10DE)
   */
  private int sector_base_addr;
  //private int sector_end_addr;
  private int gfpreg;
  private int flash_base_addr;
  //private int flash_bank_size;
  private static final int FLASH_GFPREG_BASE_MASK = 0x1FFF;
  private static final int FLASH_SECTOR_ADDR_SHIFT = 12;
  private static final int ICH_FLASH_LINEAR_ADDR_MASK = 0x00FFFFFF;
  private static final int ICH_FLASH_CYCLE_REPEAT_COUNT = 10;
  private static final int ICH_FLASH_READ_COMMAND_TIMEOUT = 500;
  private static final short FDBCMASK = (short) 0x3F00;
  private static final short FCYCLEMASK = (short) 0x6;
  private static final short FDONE = (short) 0x1;
  private static final short FCERR = (short) 0x2;
  private static final short AEL = (short) 0x4;
  private static final short SCIP = (short) 0x10;
  private static final short FDV = (short) 0x4000;

  /**
   * Constructor; initializes internal parameters
   * 
   * @param busIdx
   *          corresponding PCI bus index
   * @param deviceIdx
   *          corresponding PCI device index
   * @param functionIdx
   *          corresponding PCI function index
   * @param bridge
   *          1, if device is to be accessed in bridged mode, 0 otherwise
   */
  public IntelE1000(int busIdx, int deviceIdx, int functionIdx, int bridge) {
    super(busIdx, deviceIdx, functionIdx, bridge);
    this.deviceId = readConfig32(PciBus.REG_VENDOR_DEVICE) >>> 16;
    init();
  }

  /**
   * initializes the device
   * 
   * @return OK, if initialization succeeded, < 0 otherwise
   */
  public boolean init() {
    enablePciIoAccess();
    enablePciMemoryAccess();
    enablePciBusmasterAccess();
    physMmioBaseAddress = readConfig32(PciBus.REG_BASEADDR0); // change this here if 64 bit address base is supported
    //logMmioBaseAddress = Kernel.vmem.reserveDeviceMMIORange(physMmioBaseAddress, 128l * 1024l); // 128 KB register space, according to manual
    //if (logMmioBaseAddress == MemoryConstants.INVALID_ADDR) return false;
    logMmioBaseAddress = physMmioBaseAddress; //we have 1:1 mapping always, change if virtual memory does something else
    mmio = (IntelE1000_MMIO) MAGIC.cast2Struct(logMmioBaseAddress);
    if (deviceId == 0x10DE) {
//      EEPROM_AVAILABLE = false;
      physFlashBaseAddress = readConfig32(PciBus.REG_BASEADDR1); // change this here if 64 bit address base is supported
      //logFlashBaseAddress = Kernel.vmem.reserveDeviceMMIORange(physFlashBaseAddress, 4 * 1024); // 4 KB register space, according to manual
      //if (logFlashBaseAddress == MemoryConstants.INVALID_ADDR) return false;
      logFlashBaseAddress = physFlashBaseAddress; //we have 1:1 mapping always, change if virtual memory does something else
      flash = (IntelE1000_Flash) MAGIC.cast2Struct(logFlashBaseAddress);
      init_nvm();
    }
    else if (deviceId == 0x107C) {
//      EEPROM_READ_START = 0x1;
      EEPROM_READ_DONE = 0x2;
      EEPROM_READ_OFFSET_SHIFT = 2;
    }
    Kernel.ints.replaceIRQHandler(readConfig8(PciBus.REG_INT_LINE), this);
    resetController();
    return true;
  }

  /**
   * interrupt handler
   */
  public void handleIRQ(int no) {
    int icr;
    icr = mmio.icr;
    // link status changed
    if ((icr & 4) != 0) {
//      Kernel.log.print("[IntelE1000] link status changed, link is now ");
//      if ((mmio.status & 2) == 0) Kernel.log.println("down");
//      else Kernel.log.println("up");
    }
    if ((icr & 0x80) != 0) receive();
    //did something? (icr == 0) ? IRQ_NOT_HANDLED : IRQ_HANDLED;
  }

  /**
   * Queries status information from the NIC
   * 
   * @param status
   *          status object to be filled by NIV driver
   * @return reference to specified status object, if successful, null otherwise
   */
  /*public NicStatus getStatus(NicStatus status) {
    int nicStat;
    if (status == null) return null;
    nicStat = mmio.status;
    status.isfullDuplex = (nicStat & 0x01) != 0;
    status.isLinkUp = (nicStat & 0x02) != 0;
    status.isTxPaused = (nicStat & 0x08) != 0;
    status.speedInMBit = SPEEDLEVEL[(nicStat >>> 6) & 0x3];
    return status;
  }*/

  /**
   * returns MAC address of NIC
   * 
   * @return MAC address
   */
  public long getMacAddress() {
    return macAddress;
  }

  /**
   * send network packet which is composed of two memory areas Both areas can be
   * used for packet data, but using only one is sufficient.
   * 
   * @param headerAddr
   *          memory address of packet header, if header is available
   * @param headerSize
   *          size of header memory area
   * @param payloadAddr
   *          memory address of packet payload, if payload is available
   * @param payloadSize
   *          size of payload memory area
   * @return number of bytes which have been sent or < 0 in case of error
   */
  public int send(int headerAddr, int headerSize, int payloadAddr, int payloadSize) {
    Kernel.ints.cli();
    int result = 0;
    if (headerAddr != 0l && headerSize != 0l) result = send(headerAddr, headerSize, false);
    result += send(payloadAddr, payloadSize, true);
    Kernel.ints.sti();
    return result;
  }

  /**
   * schedule memory area which is part of a network packet for transmission
   * 
   * @param packetAddr
   *          start address of data
   * @param packetSize
   *          size of data
   * @param lastPart
   *          true, if this part is the last one of a packet, false otherwise
   * @return number of bytes which have been scheduled for transmission
   */
  private int send(int packetAddr, int packetSize, boolean lastPart) {
    int remPacketSize = (int) packetSize;
    if (packetAddr == 0l) return 0;
    if (remPacketSize > MAX_PACKET_SIZE || remPacketSize <= 0) return 0;
    Kernel.ints.cli();
    //long physAddr = Kernel.vmem.logToPhys(packetAddr);
    long physAddr = packetAddr; //we have 1:1 mapping always, change if virtual memory does something else
    if ((physAddr & 0xFFFl) != 0l) {
      int bytesToSend = 4096 - (int) (physAddr & 0xFFFl);
      if (bytesToSend > remPacketSize) bytesToSend = remPacketSize;
      addDescriptor(physAddr, bytesToSend, true, (lastPart && bytesToSend == remPacketSize));
      remPacketSize -= bytesToSend;
      packetAddr = (packetAddr + 0xFFF) & ~0xFFF; // adjust address to start of next page
    }
    while (remPacketSize > 4096) {
      //addDescriptor(Kernel.vmem.logToPhys(packetAddr), 4096, remPacketSize == (int) packetSize, false);
      addDescriptor(packetAddr, 4096, remPacketSize == (int) packetSize, false); //we have 1:1 mapping always, change if virtual memory does something else
      remPacketSize -= 4096;
      packetAddr += 4096l;
    }
    if (remPacketSize > 0) {
      //addDescriptor(Kernel.vmem.logToPhys(packetAddr), remPacketSize, remPacketSize == (int) packetSize, lastPart);
      addDescriptor(packetAddr, remPacketSize, remPacketSize == (int) packetSize, lastPart); //we have 1:1 mapping always, change if virtual memory does something else
      remPacketSize = 0;
    }
    Kernel.ints.sti();
    return (int) packetSize - remPacketSize;
  }

  /**
   * adds a transmit descriptor to the descriptor list
   * 
   * @param physAddr
   *          physical address of memory area to be transmitted
   * @param fragmentSize
   *          size of memory area to be transmitted
   * @param firstPacket
   *          true, if this descriptor is the first of a packet, false otherwise
   * @param lastPacket
   *          true, if this descriptor is the last of a packet, false otherwise
   */
  private void addDescriptor(long physAddr, int fragmentSize, boolean firstPacket, boolean lastPacket) {
    int tdtIdx = mmio.tdt;
    long param;
    //packSendCount++;
    // wait until next descriptor is free ("descriptor done")
    while ((txRingBuffer[tdtIdx * 2 + 1] & (1l << 32)) == 0l) /*busy-wait*/;
    cleanupSingleTxDescriptor(tdtIdx);
    param = lastPacket ? 0x0B000000l : 0x08000000l;
    // reserved bit, now means "first packet"
    if (firstPacket) param |= FIRST_DESC_OF_PACKET;
    param |= ((long) fragmentSize & 0xFFFFl);
    param |= 1l << 27; // report "descriptor done"
    txRingBuffer[tdtIdx * 2 + 1] = param;
    txRingBuffer[tdtIdx * 2] = physAddr;
    tdtIdx = (tdtIdx + 1) % TXBUF_ELEMCOUNT;
    mmio.tdt = tdtIdx;
  }

  // ------- non-public methods
  /**
   * reads an entry from the EEPROM
   * 
   * @param offset
   *          offset of entry to read
   * @return value of EEPROM at specified offset
   */
  private int readEeprom(int offset) {
    long timeout;
    int data = -1;
    mmio.eerd = ((offset & 0xFF) << EEPROM_READ_OFFSET_SHIFT) | 1; // start access
    timeout = System.getMicroSeconds() + 1000l;
    while (((data = mmio.eerd) & EEPROM_READ_DONE) == 0) {
      if (System.getMicroSeconds() > timeout) {
//        System.log.print("[IntelE1000] eeprom access failed (0x");
//        System.log.printHex(offset, 8);
//        System.log.print(")");
        return -1; // access failed
      }
    }
    return (data >>> 16);
  }

  /**
   * resets the controller and enables the interrupts in the end
   */
  private void resetController() {
    int i;
    mmio.imc = 0xFFFFFFFF; // disable all interrupts
    mmio.ctrl |= CTRL_RST; // global reset
    uSleep(2l);
    while ((mmio.ctrl & CTRL_RST) != 0)
      ; // wait for global reset to complete
    if ((mmio.status & 0x02) == 0) {
      // no link, probably inside virtual box
      mmio.ctrl |= 0x40;
    }
    mmio.vet = 0; // disable VLAN
    // read mac address stored in eeprom
    if (deviceId == 0x10DE) {
      macAddress = (((long) read_nvm(0, 2) & 0xFFFFl))
          | (((long) read_nvm(2, 2) & 0xFFFFl) << 16)
          | (((long) read_nvm(4, 2) & 0xFFFFl) << 32);
    }
    else {
      macAddress = (((long) readEeprom(0) & 0xFFFFl))
          | (((long) readEeprom(1) & 0xFFFFl) << 16)
          | (((long) readEeprom(2) & 0xFFFFl) << 32);
    }
    if (macAddress == 0xFFFFFFFFFFFFl) {
      // HACK: virtualbox does not implement mmio.eerd --> readEeprom will fail.
      // Just use a random MAC address.
      macAddress = 0x221100;
      macAddress |= (System.getTime() & 0xFFFFFFl) << 24;
//      System.log.print("e1000: either read_nvm or readEeprom failed. Using semi-random mac: ");
//      System.log.printMAC(macAddress);
//      System.log.println();
    }
    // SimplePrint.print("[IntelE1000] mac: ");//SimplePrint.printHex(macAddress);//SimplePrint.println("");
    //ipAddress = 0xC0A86E63; //NetworkTools.buildIpAddr(192, 168, 110, 99);
//    System.log.print("[IntelE1000] read mac address from eeprom: ");
//    System.log.printMAC(getMacAddress());
//    System.log.println();
    // clear multicast address table
    for (i = 0; i < 128; i++)
      mmio.mta[i] = 0;
    // clear all receive address filter entries but the first
    for (i = 1; i < 16; i++) {
      mmio.recvAddr[i].low = 0;
      mmio.recvAddr[i].high = 0;
    }
    // store own mac address in first entry of receive address filter
    mmio.recvAddr[0].low = (int) (macAddress);
    mmio.recvAddr[0].high = (int) (macAddress >>> 32) | 0x80000000; // mark address entry valid
    prepareBuffers();
    initTxRingBuffer();
    initRxRingBuffer();
    mmio.ims = 0x000000D4; // enabled interrupts: RXT0, RXO, RXDMT0, LSC
    //adjust macAddress byte order
    macAddress=((macAddress&0xFFl)<<40)
      |(((macAddress>>>8)&0xFFl)<<32)
      |(((macAddress>>>16)&0xFFl)<<24)
      |(((macAddress>>>24)&0xFFl)<<16)
      |(((macAddress>>>32)&0xFFl)<<8)
      |((macAddress>>>40)&0xFFl);
  }
  
  /**
   * allocates the buffers, if necessary and initializes them
   */
  private void prepareBuffers() {
    if (rxRingBuffer == null) {
      //TODO do we need this? Memory.alignArrayData(16l);
      rxRingBuffer = new long[RXBUF_ELEMCOUNT * RXBUF_ELEMSIZE_IN_LONG];
    }
    if (txRingBuffer == null) {
      //TODO do we need this? Memory.alignArrayData(16l);
      txRingBuffer = new long[TXBUF_ELEMCOUNT * TXBUF_ELEMSIZE_IN_LONG];
    }
    if (rxBuffers == null) {
      //TODO do we need this? Memory.alignArrayData(16l);
      rxBuffers = new byte[RXBUF_ELEMCOUNT][RXBUF_ELEMBUFFER_SIZE];
    }
  }

  /**
   * initializes the transmit buffer
   */
  private void initTxRingBuffer() {
    int i, tctl;
    long txRingBufferPhysAddr;
    for (i = 0; i < TXBUF_ELEMCOUNT; i++) {
      txRingBuffer[2 * i] = 0l;
      txRingBuffer[2 * i + 1] = 1l << 32; // must set "descriptor done" (mark as available)
    }
    //txRingBufferPhysAddr = Kernel.vmem.logToPhys(MAGIC.addr(txRingBuffer[0]));
    txRingBufferPhysAddr = MAGIC.addr(txRingBuffer[0]); //we have 1:1 mapping always, TODO if virtual memory does something else
    // specify tx descriptor buffer base address
    mmio.tdbah = (int) (txRingBufferPhysAddr >>> 32);
    mmio.tdbal = (int) txRingBufferPhysAddr;
    // specify tx descriptor buffer length in byte
    mmio.tdlen = TXBUF_ELEMCOUNT * TXBUF_ELEMSIZE_IN_BYTE;
    // set both head pointer and tail pointer to 0
    mmio.tdh = 0;
    mmio.tdt = 0;
    // set the inter packet gap
    mmio.tipg = TIPG_COPPER;
    // send immediately
    mmio.tadv = 0;
    mmio.tidv = 1;
    mmio.itr = 0; // disable interrupt throttling
    // enable DMA prefetch
    mmio.txdmac = 0;
    // configure transmit control
    tctl = mmio.tctl; // enable, pad short packets, retransmit on late collision, recommended threshold, standard
    tctl &= 0xFFFFF00F; // mask out collision threshold
    tctl |= 0x00000002 | // enable,
        0x00000008 | // pad short packets
        0x01000000 | // retransmit on late collision
        0x000000F0 | // recommended collision threshold (16 retries)
        0x00040000; // recommended collision distance (64 byte for full duplex)
    mmio.tctl = tctl;
  }

  /**
   * initializes the receive buffer
   */
  private void initRxRingBuffer() {
    long rxRingBufferPhysAddr;
    
    for (int i=0; i<rxBuffers.length; i++) {
      //rxRingBuffer[2 * i] = Kernel.vmem.logToPhys(MAGIC.addr(buf[0]));
      rxRingBuffer[2 * i] = MAGIC.addr(rxBuffers[i][0]); //we have 1:1 mapping always, change if virtual memory does something else
      rxRingBuffer[2 * i + 1] = 0l;
    }
    // disable receive while setting up the descriptors
    mmio.rctl &= ~2;
    // set receive delay timer and absolute timer
    mmio.rdtr = RX_TIMER_DELAY;
    mmio.radv = RX_ABS_TIMER_DELAY;
    // deactivate DMA burst
    mmio.rxdctl = 0;
    // set interrupt throttling rate
    mmio.itr = 0;
    // specify rx descriptor buffer base address
    //rxRingBufferPhysAddr = Kernel.vmem.logToPhys(MAGIC.addr(rxRingBuffer[0]));
    rxRingBufferPhysAddr = MAGIC.addr(rxRingBuffer[0]); //we have 1:1 mapping always, change if virtual memory does something else
    mmio.rdbah = (int) (rxRingBufferPhysAddr >>> 32);
    mmio.rdbal = (int) rxRingBufferPhysAddr;
    // specify rx descriptor buffer length in byte
    mmio.rdlen = RXBUF_ELEMCOUNT * RXBUF_ELEMSIZE_IN_BYTE;
    // set head and tail pointer
    mmio.rdh = 0;
    mmio.rdt = RXBUF_ELEMCOUNT - 1;
    // enable rx logic, allow long packets, no loop back, broadcast accept mode,
    // rdtms/2
    // receiver buffer size 16K
    mmio.rctl |= 0x2018022;
  }
  
  static final long DONE_AND_FIRST = FIRST_DESC_OF_PACKET | 0x100000000l; // DD && First Packet
  /**
   * checks all transmit descriptors and frees all unused ones
   */
  public void cleanupTxDescriptors() {
    Kernel.ints.cli();
    int firstIdx = mmio.tdt, lastIdx;
    lastIdx = mmio.tdh - 1;
    if (lastIdx < 0) lastIdx = TXBUF_ELEMCOUNT - 1;
    while (firstIdx != lastIdx) {
      cleanupSingleTxDescriptor(firstIdx);
      firstIdx = (firstIdx + 1) % TXBUF_ELEMCOUNT;
    }
    Kernel.ints.sti();
  }
  
  /**
   * check specified transmit descriptor and frees it if it is not used anymore
   * 
   * @param descIdx
   *          descriptor index in transmit descriptor ring
   */
  private void cleanupSingleTxDescriptor(int descIdx) {
    if (descIdx < 0 || descIdx >= TXBUF_ELEMCOUNT) return;
    long status = txRingBuffer[descIdx * 2 + 1];
    if ((status & DONE_AND_FIRST) == DONE_AND_FIRST) {
      Network.markTXBufferAsUnused(txRingBuffer[descIdx * 2]);
      txRingBuffer[descIdx * 2 + 1] &= ~FIRST_DESC_OF_PACKET;
      txRingBuffer[descIdx * 2] = 0l;
    }
  }

  /**
   * called by interrupt handler if packet(s) have been received scans the
   * receive descriptor ring and calls Kernel.basicProto.dissect() for each
   * received packet
   */
  private void receive() {
    int descIdx = mmio.rdt, headIdx = mmio.rdh;
    int count;
    while ((descIdx + 1) % RXBUF_ELEMCOUNT != headIdx) {
      descIdx = (descIdx + 1) % RXBUF_ELEMCOUNT;
      if ((rxRingBuffer[2 * descIdx + 1] & RXDESC_STATUS) != 0l) {
        count = (int) rxRingBuffer[2 * descIdx + 1] & 0xFFFF;
        rxRingBuffer[2 * descIdx + 1] &= ~RXDESC_STATUS; // clear status to zero
        if (dropRX == false) PacketNetwork.dissect((int)MAGIC.addr(rxBuffers[descIdx][0]), count - 4); //deliver
        // mmio.rdt = descIdx;
      }
      mmio.rdt = descIdx;
    }
    // in case of abort, these lines are not executed
    // the tail pointer will then be updated when the next packet comes in
    mmio.rdt = descIdx;
  }
  
  private void uSleep(long usec) {
    usec+=System.getTime();
    while (System.getTime()<usec) /*wait*/;
  }

  /**
   * This function should be called right after mapping the flash-region AND it
   * must be called before trying to read the MAC-Address.
   */
  private int init_nvm() {
    // Can't read flash registers if the register set isn't mapped.
    if (physFlashBaseAddress == 0l) {
      // SimplePrint.println("NVM init failed! Flash register is not mapped!");
      return -1;
    }
    gfpreg = flash.BFPR;
    // SimplePrint.print("GPFREG: ");//SimplePrint.printHex(gfpreg);
    // sector_X_addr is a "sector"-aligned address (4096 bytes)
    // Add 1 to sector_end_addr since this sector is included in
    // the overall size.
    sector_base_addr = gfpreg & FLASH_GFPREG_BASE_MASK;
    //sector_end_addr = ((gfpreg >> 16) & FLASH_GFPREG_BASE_MASK) + 1;
    // SimplePrint.print(" base_addr: ");//SimplePrint.printHex(sector_base_addr);
    // SimplePrint.print(" end_addr: ");//SimplePrint.printHex(sector_end_addr);
    // flash_base_addr is byte-aligned
    flash_base_addr = sector_base_addr << FLASH_SECTOR_ADDR_SHIFT;
    // SimplePrint.print(" base_addr: ");//SimplePrint.printHex(sector_base_addr);
    // find total size of the NVM, then cut in half since the total
    // size represents two separate NVM banks.
    //flash_bank_size = (sector_end_addr - sector_base_addr) << FLASH_SECTOR_ADDR_SHIFT;
    //flash_bank_size /= 2;
    // Adjust to word count
    //flash_bank_size /= 2;
    return 0;
  }

  /**
   * basically works like the readEeprom with the added ability to read one or
   * two bytes at a time.
   */
  int read_nvm(int offset, int size) {
    // union ich8_hws_flash_status hsfsts;
    // union ich8_hws_flash_ctrl hsflctl;
    int flash_linear_addr;
    int flash_data = 0;
    int ret_val = -1;
    int count = 0;
    if (size < 1 || size > 2 || offset > ICH_FLASH_LINEAR_ADDR_MASK) {
      return -1;
    }
    flash_linear_addr = (ICH_FLASH_LINEAR_ADDR_MASK & offset) + flash_base_addr;
    do {
      uSleep(1l);
      // Steps
      ret_val = nvm_flash_cycle_init();
      if (ret_val != 0) break;
      // 0b/1b corresponds to 1 or 2 byte size, respectively.
      flash.HSFCTL = (short) (((flash.HSFCTL & (~FDBCMASK)) & (~FCYCLEMASK)) | (short) ((size - 1) << 8));
      flash.FADDR = flash_linear_addr;
      // ew32flash(ICH_FLASH_FADDR, flash_linear_addr);
      ret_val = nvm_flash_cycle();
      // Check if FCERR is set to 1, if set to 1, clear it
      // and try the whole sequence a few more times, else
      // read in (shift in) the Flash Data0, the order is
      // least significant byte first msb to lsb
      if (ret_val == 0) {
        if (size == 1) {
          flash_data = (flash.FDATA0 & 0x000000FF);
        }
        else if (size == 2) {
          flash_data = (flash.FDATA0 & 0x0000FFFF);
        }
        return flash_data;
        // break;
      }
      else {
        // If we've gotten here, then things are probably
        // completely hosed, but if the error condition is
        // detected, it won't hurt to give it another try...
        // ICH_FLASH_CYCLE_REPEAT_COUNT times.
        if ((flash.HSFSTS & FCERR) != (short) 0) {
          // Repeat for some time before giving up.
          continue;
        }
        else if ((flash.HSFSTS & FDONE) == (short) 0) {
          // SimplePrint.println("read nvm: Timeout error - flash cycle did not complete.");
          break;
        }
      }
    } while (count++ < ICH_FLASH_CYCLE_REPEAT_COUNT);
    return -1;
  }

  /**
   * Starts the flash read or write cycle and should only be used by the
   * read_nvm-function
   */
  int nvm_flash_cycle() {
    int i = 0;
    // Start a cycle by writing 1 in Flash Cycle Go in Hw Flash Control
    flash.HSFCTL |= (short) 0x1;
    // wait till FDONE bit is set to 1
    do {
      if ((flash.HSFSTS & FDONE) == (short) 1) break;
      uSleep(1l);
    } while (i++ < ICH_FLASH_READ_COMMAND_TIMEOUT);
    if ((flash.HSFSTS & FDONE) == (short) 1
        && (flash.HSFSTS & FCERR) == (short) 0) return 0;
    return -1;
  }

  /**
   * Readies the flash for read/write-cycles and should only be used by the
   * read_nvm-function
   */
  int nvm_flash_cycle_init() {
    int ret_val = -1;
    int i = 0;
    /* Check if the flash descriptor is valid */
    if ((flash.HSFSTS & FDV) == (short) 0) {
      // SimplePrint.println("nfci: Flash descriptor is not valid!");
      return -1;
    }
    /* Clear FCERR and DAEL in hw status by writing 1 */
    flash.HSFSTS |= (short) (FCERR | AEL);
    /*
     * Either we should have a hardware SPI cycle in progress bit to check
     * against, in order to start a new cycle or FDONE bit should be changed in
     * the hardware so that it is 1 after hardware reset, which can then be used
     * as an indication whether a cycle is in progress or has been completed.
     */
    if ((flash.HSFSTS & SCIP) == (short) 0) {
      /*
       * There is no cycle running at present, so we can start a cycle Begin by
       * setting Flash Cycle Done.
       */
      flash.HSFSTS |= FDONE;
      ret_val = 0;
    }
    else {
      /*
       * otherwise poll for sometime so the current cycle has a chance to end
       * before giving up.
       */
      for (i = 0; i < ICH_FLASH_READ_COMMAND_TIMEOUT; i++) {
        if ((flash.HSFSTS & SCIP) == (short) 0) {
          ret_val = 0;
          break;
        }
        uSleep(1l);
      }
      if (ret_val == 0) {
        /*
         * Successful in waiting for previous cycle to timeout, now set the
         * Flash Cycle Done.
         */
        flash.HSFSTS |= FDONE;
      }
      else {
        // SimplePrint.println("nfci: Flash controller busy, cannot get access!");
      }
    }
    return ret_val;
  }

  public void disableRX(boolean disable) {
    dropRX = disable;
  }
}
