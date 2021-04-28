package devices.net;

/**
 * helper class to define struct access to Intel E1000 Flash registers
 * Registers documentation: Intel ICH 10 Manual Section 22 Serial Peripheral Interface (SPI)
 * @author Ackermann
 *
 */
public class IntelE1000_Flash extends STRUCT {
  @SJC(offset=0x0000)
  public int BFPR;				// BIOS Flash Primary Region		
  @SJC(offset=0x0004)
	public short HSFSTS;				// Hardware Sequencing Flash Status	
  @SJC(offset=0x0006)
	public short HSFCTL;				// Hardware Sequencing Flash Control					
  @SJC(offset=0x0008)
	public int FADDR;				// Flash Address					
  @SJC(offset=0x000C)
	public int Reserved1;			// reserved				
  @SJC(offset=0x0010)
	public int FDATA0;				// Flash Data 0
  @SJC(offset=0x0014, count=60)
	public int [] FDATAN;				// Flash Data N					
  @SJC(offset=0x0050)
	public int FRACC;				// Flash Region Access Permissions					
  @SJC(offset=0x0054)
	public int FREG0;				// Flash Region	0						
  @SJC(offset=0x0058)
	public int FREG1;				// Flash Region 1
  @SJC(offset=0x005C)
	public int FREG2;				// ...
  @SJC(offset=0x0060)
	public int FREG3;
  @SJC(offset=0x0064)
	public int FREG4;
  @SJC(offset=0x0068) //@SJC(offset=0x0088) ??
	public int Reserved2;
  @SJC(offset=0x0074)
	public int FPR0;				// Flash Protected Range 0
  @SJC(offset=0x0078)
	public int FPR1;				// ...
  @SJC(offset=0x007C)
	public int FPR2;
  @SJC(offset=0x0080)
	public int FPR3;
  @SJC(offset=0x0084)
	public int FPR4;
  @SJC(offset=0x00B8)
	public int Reserved3;
  @SJC(offset=0x0090)
	public int SSF;				// Software Sequencing & Control Flash Status (combined!)
  @SJC(offset=0x0094)
	public short PREOP;
  @SJC(offset=0x0096)
	public short OPTYPE;
  @SJC(offset=0x0098, count=8)
	public int [] OPMENU;
  @SJC(offset=0x00A0)
	public int BBAR;
  @SJC(offset=0x00B0)
	public int FDOC;
  @SJC(offset=0x00B4)
	public int FDOD;
	public int Reserved4;
  @SJC(offset=0x00C0)
	public int AFC;
  @SJC(offset=0x00C4)
	public int LVSCC;
  @SJC(offset=0x00C8)
	public int UVSCC;
  @SJC(offset=0x00D0)
	public int FPB;
}
