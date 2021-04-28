package devices.net;

//import devices.PciDevice;

/**
 * Mandatory interface for all network interface card driver classes
 * @author gerhold
 *
 */
// TODO: IMPLEMENT MISSING STUFF
@SJC.IgnoreUnit
public abstract class NIC extends PciDevice {
  /**
   * standard PciDevice constructor
   */
  public NIC(int busIdx, int deviceIdx, int functionIdx, int bridge) {
    super(busIdx, deviceIdx, functionIdx, bridge);
  }
  
	/**
	 * returns MAC address of NIC
	 * @return MAC address
	 */
	public abstract long getMacAddress();
	
	/**
	 * send network packet which is composed of two memory areas
	 * Both areas can be used for packet data, but using only one is sufficient.
	 * @param headerAddr memory address of packet header, if header is available
	 * @param headerSize size of header memory area
	 * @param payloadAddr memory address of packet payload, if payload is available
	 * @param payloadSize size of payload memory area 
	 * @return number of bytes which have been sent or < 0 in case of error 
	 */
	public abstract int send(int headerAddr, int headerSize, int payloadAddr, int payloadSize);
	
  /**
   * encourages the NIC to free unused transfer descriptors
   */ 
	public abstract void cleanupTxDescriptors();
	
	/**
	* when multiple NICs are available exactly one NIC may be used to receive packets from the network
	* @param disable set to true, to disable receipt of any packets using this NIC.
	*/
	public abstract void disableRX(boolean disable);
}
