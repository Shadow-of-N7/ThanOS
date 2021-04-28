package devices.net;

/**
 * helper class to define struct access to Intel E1000 MMIO registers
 * @author gerhold
 *
 */
public class IntelE1000_MMIO extends STRUCT {
	public int ctrl;				// device control 							@ 0000h
	public int dummy0004;			//											@ 0004h
	public int status;				// device status							@ 0008h
	public int dummy000C;			//											@ 000Ch
	public int eecd;				// eeprom control & data					@ 0010h	
	public int eerd;				// eeprom read data							@ 0014h
	public int ctrlExt;			// extended device control					@ 0018h
	public int fla;				// flash access								@ 001Ch
	public int mdic;				// mdi control								@ 0020h
	public int dummy0024;			//											@ 0024h	
	public int fcal;				// flow control address low					@ 0028h
	public int fcah;				// flow control address high				@ 002Ch
	public int fct;				// flow control type						@ 0030h
	public int dummy0034;			//											@ 0034h
	@SJC(offset=0x0038)
	public int vet;				// vlan ether type							@ 0038h	
  @SJC(offset=0x00C0)
	public int icr;				// interrupt cause read						@ 00C0h
  @SJC(offset=0x00C4)
	public int itr;				// interrupt throttling						@ 00C4h
  @SJC(offset=0x00C8)
	public int ics;				// interrupt cause set						@ 00C8h	
  @SJC(offset=0x00D0)
	public int ims;				// interrupt mask set						@ 00D0h	
  @SJC(offset=0x00D8)
	public int imc;				// interrupt mask clear						@ 00D8h	
  @SJC(offset=0x0100)
	public int rctl;				// receive control							@ 0100h	
  @SJC(offset=0x0170)
	public int fcttv;				// flow control transmit timer value		@ 0170h	
  @SJC(offset=0x0178)
	public int txcw;				// transmit configuration word				@ 0178h	
  @SJC(offset=0x0180)
	public int rxcw;				// receive configuration word				@ 0180h	
  @SJC(offset=0x0400)
	public int tctl;				// transmit control							@ 0400h	
  @SJC(offset=0x0410)
	public int tipg;				// transmit inter packet gap				@ 0410h
  @SJC(offset=0x0E00)
	public int ledCtl;				// led control								@ 0E00h	
  @SJC(offset=0x1000)
	public int pba;				// packet buffer allocation					@ 1000h	
  @SJC(offset=0x2800)
	public int rdbal;				// rx descriptor base address low			@ 2800h
  @SJC(offset=0x2804)
	public int rdbah;				// rx descriptor base address high			@ 2804h
  @SJC(offset=0x2808)
	public int rdlen;				// rx descriptor length						@ 2808h	
  @SJC(offset=0x2810)
	public int rdh;				// rx descriptor head						@ 2810h
  @SJC(offset=0x2818)
	public int rdt;				// rx descriptor tail						@ 2818h	
  @SJC(offset=0x2820)
	public int rdtr;				// rx delay time register					@ 2820h	
  @SJC(offset=0x2828)
	public int rxdctl;				// rx descriptor control					@ 2828h	
  @SJC(offset=0x282C)
	public int radv;				// rx interrupt absolute delay timer		@ 282Ch
  @SJC(offset=0x3000)
	public int txdmac;				// tx dma control							@ 3000h
  @SJC(offset=0x3800)
	public int tdbal;				// tx descriptor base address low			@ 3800h
  @SJC(offset=0x3804)
	public int tdbah;				// tx descriptor base address high			@ 3804h
  @SJC(offset=0x3808)
	public int tdlen;				// tx descriptor length						@ 3808h	
  @SJC(offset=0x3810)
	public int tdh;				// tx descriptor head						@ 3810h
  @SJC(offset=0x3818)
	public int tdt;				// tx descriptor tail						@ 3818h	
  @SJC(offset=0x3820)
	public int tidv;				// tx interrupt delay value					@ 3820h	
  @SJC(offset=0x3828)
	public int txdctl;				// tx descriptor control					@ 3828h
  @SJC(offset=0x382C)
	public int tadv;				// transmit absolute delay value			@ 382Ch
  @SJC(offset=0x4000)
	public int crcerrs;			// crc error count							@ 4000h
  @SJC(offset=0x400C)
	public int rxerrc;				// rx error count							@ 400Ch
  @SJC(offset=0x4010)
	public int mpc;				// missed packets count						@ 4010h
  @SJC(offset=0x4028)
	public int colc;				// collision count							@ 4028h
  @SJC(offset=0x4074)
	public int gprc;				// good packets received count				@ 4074h
  @SJC(offset=0x4080)
	public int gptc;				// good packets transmitted count			@ 4080h,
  @SJC(offset=0x4088)
	public int gorcl;				// good octets received count low			@ 4088h
  @SJC(offset=0x408C)
	public int gorch;				// good octets received count high			@ 408Ch
  @SJC(offset=0x4090)
	public int gotcl;				// good octets transmitted count low		@ 4090h
  @SJC(offset=0x4094)
	public int gotch;				// good octets transmitted count high		@ 4094h
	
	public int dummy;
	
	@SJC(offset=0x5200, count=128)
	int[] mta;			// multicast table array					@ 5200h - 53FCh, 128 entries
	@SJC(offset=0x5400, count=16)
	IntelE1000_MMIO_RA[] recvAddr;	// receive address				@ 5400h, 16 entries of 8 byte each
}
