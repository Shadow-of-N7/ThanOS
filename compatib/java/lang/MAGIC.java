package java.lang;

@SJC.IgnoreUnit
public class MAGIC {
  /**
   * Zeigergröße der aktuellen Architektur (zum Beispiel 4 für IA32, 8 für AMD64).
   */
  public static int ptrSize;

  /**
   * Flag, welches die Verschiebbarkeit des Codes und der Skalare anzeigt (Code wurde mit der
   * Compiler-Option -m bzw. -M übersetzt).
   */
  public static boolean movable;

  /**
   * Flag, welches die Verschiebbarkeit des Codes und der Skalare anzeigt (Code wurde mit der
   * Compiler-Option -m bzw. -M übersetzt).
   */
  public static boolean indirScalars;

  /**
   * Flag, welches einen schlanken Objektaufbau anzeigt (Code wurde mit der Compiler-Option
   * -l übersetzt).
   */
  public static boolean streamline;

  /**
   * Flag, welches einen Laufzeitaufruf bei Referenzenzuweisung für alle Referenzen bzw. für
   * Referenzen im Heap anzeigt (Code wurde mit der Compiler-Option -c bzw. -h übersetzt).
   */
  public static boolean assignCall;

  /**
   * Flag, welches einen Laufzeitaufruf bei Referenzenzuweisung für alle Referenzen bzw. für
   * Referenzen im Heap anzeigt (Code wurde mit der Compiler-Option -c bzw. -h übersetzt).
   */
  public static boolean assignHeapCall;

  /**
   * Flag, welches einen Laufzeitaufruf im Falle einer Arraygrenzüberschreitung anzeigt (Code
   * wurde mit der Compiler-Option -b übersetzt).
   */
  public static boolean runtimeBoundException;

  /**
   * Flag, welches einen Laufzeittest und Laufzeitaufruf im Falle einer ungültigen
   * Nullzeigerverwendung anzeigt (Code wurde mit der Compiler-Option -n übersetzt).
   */
  public static boolean runtimeNullException;

  /**
   * Startadresse des aktuellen Speicherabbilds (Parameter der Compiler-Option -a ).
   */
  public static int imageBase;

  /**
   * Zieladresse des zu dekomprimierenden Speicherabbilds im Dekompressor (Parameter der
   * Compiler-Option -a für das komprimierte Abbild). Der Dekompressor muss sicherstellen,
   * dass das dekomprimierte Speicherabbild an der vorgesehenen Adresse (ggf. reloziert mit
   * Compiler-Option -Z , siehe unten die Felder relocation und comprRelocation ) abgelegt
   * wird.
   */
  public static int compressedImageBase;

  /**
   * Flag, welches aus den Klassendeskriptoren ausgelagerte Klassenvariablen (Code wurde mit
   * der Compiler-Option -e übersetzt) bzw. auch ausgelagerte Objekte (Code wurde mit der
   * Copmiler-Option -E übersetzt) anzeigt, siehe Kapitel 12.5.
   */
  public static boolean embedded;

  /**
   * Flag, welches aus den Klassendeskriptoren ausgelagerte Klassenvariablen (Code wurde mit
   * der Compiler-Option -e übersetzt) bzw. auch ausgelagerte Objekte (Code wurde mit der
   * Copmiler-Option -E übersetzt) anzeigt, siehe Kapitel 12.5.
   */
  public static boolean embConstRAM;

  /**
   * Eingestellte Relozierung (Compiler-Option -Z XXX , siehe Kapitel 5.1) des aktuellen
   * Speicherabbilds bzw. im Dekompressor des zu dekomprimierenden Speicherabbilds. Diese
   * Option ist nur für 64-Bit-Architekturen sinnvoll verwendbar, da eine Relozierung immer in
   * Gigabyte angegeben wird.
   */
  public static int relocation;

  /**
   * Eingestellte Relozierung (Compiler-Option -Z XXX , siehe Kapitel 5.1) des aktuellen
   * Speicherabbilds bzw. im Dekompressor des zu dekomprimierenden Speicherabbilds. Diese
   * Option ist nur für 64-Bit-Architekturen sinnvoll verwendbar, da eine Relozierung immer in
   * Gigabyte angegeben wird.
   */
  public static int comprRelocation;

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   */
  public static void inline(int p1) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   */
  public static void inline(int p1, int p2) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   */
  public static void inline(int p1, int p2, int p3) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   * @param p4
   */
  public static void inline(int p1, int p2, int p3, int p4) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   * @param p4
   * @param p5
   */
  public static void inline(int p1, int p2, int p3, int p4, int p5) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   * @param p4
   * @param p5
   * @param p6
   */
  public static void inline(int p1, int p2, int p3, int p4, int p5, int p6) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   */
  public static void inline16(int p1) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   */
  public static void inline16(int p1, int p2) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   */
  public static void inline16(int p1, int p2, int p3) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   * @param p4
   */
  public static void inline16(int p1, int p2, int p3, int p4) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   * @param p4
   * @param p5
   */
  public static void inline16(int p1, int p2, int p3, int p4, int p5) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   * @param p4
   * @param p5
   * @param p6
   */
  public static void inline16(int p1, int p2, int p3, int p4, int p5, int p6) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   */
  public static void inline32(int p1) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   */
  public static void inline32(int p1, int p2) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   */
  public static void inline32(int p1, int p2, int p3) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   * @param p4
   */
  public static void inline32(int p1, int p2, int p3, int p4) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   * @param p4
   * @param p5
   */
  public static void inline32(int p1, int p2, int p3, int p4, int p5) {}

  /**
   * Pseudo-Methoden zur direkten Angabe von Maschinenbefehlen, variable Anzahl von
   * Parametern ist möglich. Die Parameter müssen konstant sein, pro Parameter werden nur die
   * unteren 8 Bit bzw. die unteren 16 Bit bzw. die gesamten 32 Bit gewertet. Optional kann der
   * erste Parameter ein konstanter String sein, der bei Unterstützung durch die Architektur statt
   * der Opcodes als Inline-Assembler-Text verwendet wird. Wird ein String angegeben und die
   * Architektur unterstützt keinen Inline-Assembler-Text, wird der String ignoriert.
   * @param p1
   * @param p2
   * @param p3
   * @param p4
   * @param p5
   * @param p6
   */
  public static void inline32(int p1, int p2, int p3, int p4, int p5, int p6) {}

  /**
   * Pseudo-Methoden zum Einfügen von Variablenoffsets als Maschinencode. Diese Methoden
   * werden typischerweise verwendet, um in Inline-Code auf Variablen zugreifen zu können.
   * @param p1
   * @param p2
   */
  public static void inlineOffset(int p1, double p2) {}

  /**
   * Pseudo-Methoden zum Einfügen von Variablenoffsets als Maschinencode. Diese Methoden
   * werden typischerweise verwendet, um in Inline-Code auf Variablen zugreifen zu können.
   * @param p1
   * @param p2
   * @param p3
   */
  public static void inlineOffset(int p1, double p2, int p3) {}

  /**
   * Pseudo-Methoden zum Einfügen von Variablenoffsets als Maschinencode. Diese Methoden
   * werden typischerweise verwendet, um in Inline-Code auf Variablen zugreifen zu können.
   * @param p1
   * @param p2
   */
  public static void inlineOffset(int p1, Object p2) {}

  /**
   * Pseudo-Methoden zum Einfügen von Variablenoffsets als Maschinencode. Diese Methoden
   * werden typischerweise verwendet, um in Inline-Code auf Variablen zugreifen zu können.
   * @param p1
   * @param p2
   * @param p3
   */
  public static void inlineOffset(int p1, Object p2, int p3) {}

  /**
   * Pseudo-Methoden zum Einfügen eines Blocks von Maschinenbefehlen mit dem im
   * Parameter angegebenen Namen. Dieser muss als konstanter String vorliegen und darf keine
   * Dereferenzierung enthalten. Der Datenblock entstammt typischerweise aus einer binär
   * importierten Datei mit der Endung .bim und trägt den Namen der Datei ohne Pfad und
   * Dateiendung.
   * @param blockName
   */
  public static void inlineBlock(String blockName) {}

  /**
   * Pseudo-Methoden zum direkten Beschreiben des Hauptspeichers. Die Angabe der Adresse
   * muß in dem zur Architektur passenden Format oder als int vorliegen.
   * @param addr
   * @param v
   */
  public static void wMem64(int addr, long v) {}

  /**
   * Pseudo-Methoden zum direkten Beschreiben des Hauptspeichers. Die Angabe der Adresse
   * muß in dem zur Architektur passenden Format oder als int vorliegen.
   * @param addr
   * @return
   */
  public static long rMem64(int addr) { return 0L; }

  /**
   * Pseudo-Methoden zum direkten Beschreiben des Hauptspeichers. Die Angabe der Adresse
   * muß in dem zur Architektur passenden Format oder als int vorliegen.
   * @param addr
   * @param v
   */
  public static void wMem32(int addr, int v) {}

  /**
   * Pseudo-Methoden zum direkten Auslesen des Hauptspeichers. Die Angabe der Adresse muß
   * in dem zur Architektur passenden Format oder als in vorliegen.
   * @param addr
   * @return
   */
  public static int rMem32(int addr) { return 0; }

  /**
   * Pseudo-Methoden zum direkten Auslesen des Hauptspeichers. Die Angabe der Adresse muß
   * in dem zur Architektur passenden Format oder als in vorliegen.
   * @param addr
   * @return
   */
  public static int rMem32(long addr) { return 0; }

  /**
   * Pseudo-Methoden zum direkten Beschreiben des Hauptspeichers. Die Angabe der Adresse
   * muß in dem zur Architektur passenden Format oder als int vorliegen.
   * @param addr
   * @param v
   */
  public static void wMem16(int addr, short v) {}

  /**
   * Pseudo-Methoden zum direkten Auslesen des Hauptspeichers. Die Angabe der Adresse muß
   * in dem zur Architektur passenden Format oder als in vorliegen.
   * @param addr
   * @return
   */
  public static short rMem16(int addr) { return (short)0; }

  /**
   * Pseudo-Methoden zum direkten Beschreiben des Hauptspeichers. Die Angabe der Adresse
   * muß in dem zur Architektur passenden Format oder als int vorliegen.
   * @param addr
   * @param v
   */
  public static void wMem8(int addr, byte v) {}

  /**
   * Pseudo-Methoden zum direkten Auslesen des Hauptspeichers. Die Angabe der Adresse muß
   * in dem zur Architektur passenden Format oder als in vorliegen.
   * @param addr
   * @return
   */
  public static byte rMem8(int addr) { return (byte)0; }

  /**
   * Pseudo-Methoden zum direkten Beschreiben des I/O-Speichers. Die Angabe der Adresse
   * muß in dem zur Architektur passenden Format oder als int vorliegen. Unter Umständen
   * werden von manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @param v
   */
  public static void wIOs64(int addr, long v) {}

  /**
   * Pseudo-Methoden zum direkten Auslesen des I/O-Speichers. Die Angabe der Adresse muß
   * in dem zur Architektur passenden Format oder als int vorliegen. Unter Umständen werden
   * von manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @return
   */
  public static long rIOs64(int addr) { return 0l; }

  /**
   * Pseudo-Methoden zum direkten Beschreiben des I/O-Speichers. Die Angabe der Adresse
   * muß in dem zur Architektur passenden Format oder als int vorliegen. Unter Umständen
   * werden von manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @param v
   */
  public static void wIOs32(int addr, int v) {}

  /**
   * Pseudo-Methoden zum direkten Auslesen des I/O-Speichers. Die Angabe der Adresse muß
   * in dem zur Architektur passenden Format oder als int vorliegen. Unter Umständen werden
   * von manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @return
   */
  public static int rIOs32(int addr) { return 0; }

  /**
   * Pseudo-Methoden zum direkten Beschreiben des I/O-Speichers. Die Angabe der Adresse
   * muß in dem zur Architektur passenden Format oder als int vorliegen. Unter Umständen
   * werden von manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @param v
   */
  public static void wIOs16(int addr, short v) {}

  /**
   * Pseudo-Methoden zum direkten Auslesen des I/O-Speichers. Die Angabe der Adresse muß
   * in dem zur Architektur passenden Format oder als int vorliegen. Unter Umständen werden
   * von manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @return
   */
  public static short rIOs16(int addr) { return (short)0; }

  /**
   * Pseudo-Methoden zum direkten Beschreiben des I/O-Speichers. Die Angabe der Adresse
   * muß in dem zur Architektur passenden Format oder als int vorliegen. Unter Umständen
   * werden von manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @param v
   */
  public static void wIOs8(int addr, byte v) {}

  /**
   * Pseudo-Methoden zum direkten Auslesen des I/O-Speichers. Die Angabe der Adresse muß
   * in dem zur Architektur passenden Format oder als int vorliegen. Unter Umständen werden
   * von manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @return
   */
  public static byte rIOs8(int addr) { return (byte)0; }

  /**
   * Pseudo-Methoden zum direkten Auslesen des I/O-Speichers. Die Angabe der Adresse muß
   * in dem zur Architektur passenden Format oder als int vorliegen. Unter Umständen werden
   * von manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @param mode
   * @return
   */
  public static byte rIOs8(int addr, int mode) { return (byte)0; }

  /**
   * Pseudo-Methode zur Ermittlung der Adresse des referenzierten Objekts. Nicht zu
   * verwechseln mit der Methode addr(.) , die zur Ermittlung der Adresse der Variablen o statt
   * des dadurch referenzierten Objekts dient. Beispiele siehe auch unter MAGIC.addr() .
   * @param o
   * @return
   */
  public static int cast2Ref(Object o) { return 0; }

  /**
   * Pseudo-Methode zur Konvertierung einer Adresse in eine Objektreferenz. Die Adresse muss
   * auf das erste Skalare Feld des Objektes zeigen (siehe Kapitel 3.4 und MAGIC.addr() ).
   * @param addr
   * @return
   */
  public static Object cast2Obj(int addr) { return null; }

  /**
   * Pseudo-Methode zur Konvertierung einer Adresse in eine Objektreferenz. Die Adresse muss
   * auf das erste Skalare Feld des Objektes zeigen (siehe Kapitel 3.4 und MAGIC.addr() ).
   * @param addr
   * @return
   */
  public static Object cast2Obj(long addr) { return null; }

  /**
   * Pseudo-Methode zur Ermittlung der Adresse der im Parameter angegebenen Variablen.
   * Nicht zu verwechseln mit cast2Ref(.) , welches die Zieladresse des übergebenen Objektes
   * liefern würde. In einem 32-Bit System gelten mit der Objektreferenz o die Identitäten:
   * MAGIC.rMem32(MAGIC.addr(o)) <==> MAGIC.cast2Ref(o)
   * o <==> MAGIC.cast2Obj(MAGIC.cast2Ref(o))
   * @param i
   * @return
   */
  public static int addr(double i) { return 0; }

  /**
   * Pseudo-Methode zur Ermittlung der Adresse der im Parameter angegebenen Variablen.
   * Nicht zu verwechseln mit cast2Ref(.) , welches die Zieladresse des übergebenen Objektes
   * liefern würde. In einem 32-Bit System gelten mit der Objektreferenz o die Identitäten:
   * MAGIC.rMem32(MAGIC.addr(o)) <==> MAGIC.cast2Ref(o)
   * o <==> MAGIC.cast2Obj(MAGIC.cast2Ref(o))
   * @param o
   * @return
   */
  public static int addr(Object o) { return 0; }

  /**
   * Pseudo-Methode zur Ermittlung der Adresse des Deskriptors für die im Parameter
   * angegebenen Unit (Klasse oder Interface). Der Klassenname bzw. Interfacename muss zur
   * Einhaltung der Java-Kompatibilität in Form eines konstanten Strings vorliegen und darf
   * keine Dereferenzierung enthalten. Der ermittelte Deskriptor wird durch diesen Zugriff als zu
   * erzeugend markiert, existiert also unabhängig von sonstigen Erfordernissen.
   * @param clssName
   * @return
   */
  public static Object clssDesc(String clssName) { return null; } //return SClassDesc

  /**
   * Pseudo-Methode zur Ermittlung der Adresse des Deskriptors für die im Parameter
   * angegebenen Unit (Klasse oder Interface). Der Klassenname bzw. Interfacename muss zur
   * Einhaltung der Java-Kompatibilität in Form eines konstanten Strings vorliegen und darf
   * keine Dereferenzierung enthalten. Der ermittelte Deskriptor wird durch diesen Zugriff als zu
   * erzeugend markiert, existiert also unabhängig von sonstigen Erfordernissen.
   * @param clssName
   * @return
   */
  public static Object intfDesc(String clssName) { return null; } //returns SIntfDesc

  /**
   * Pseudo-Methode zur Ermittlung des Methodenoffsets der im zweiten Parameter
   * angegebenen Methode innerhalb der im ersten Parameter angegebenen Klasse. Beide
   * Parameter müssen zur Einhaltung der Java-Kompatibilität in Form von konstanten Strings
   * vorliegen und dürfen keine Dereferenzierung enthalten. Im Zusammenspiel mit clssDesc
   * können so zum Beispiel die Adressen von Methodenobjekten ermittelt werden.
   * @param clssName
   * @param mthdName
   * @return
   */
  public static int mthdOff(String clssName, String mthdName) { return 0; }

  /**
   * Pseudo-Methode zur Offset-Ermittlung des Codes innerhalb eines Methodenobjektes. Der
   * erste ausführbare Opcode befindet sich diese Anzahl an Bytes nach dem Ziel des
   * Methodenzeigers. Der Codestart einer Methode Cls.Mtd läßt sich wiefolgt bestimmen:
   * rMem32(cast2Ref(clssDesc("Cls"))+MAGIC.mthdOff("Cls", "Mtd"))+getCodeOff()
   * @return
   */
  public static int getCodeOff() { return 0; }

  /**
   * Pseudo-Methode zur Ermittlung der Größe des Skalarbereiches einer Instanz der im
   * Parameter angegebenen Klasse. Der Bezeichner muß zur Einhaltung der Java-Kompatibilität
   * in Form eines konstanten Strings vorliegen und darf keine Dereferenzierung enthalten.
   * @param clssName
   * @return
   */
  public static int getInstScalarSize(String clssName) { return 0; }

  /**
   * Pseudo-Methode zur Ermittlung der Anzahl der Referenzen einer Instanz der im Parameter
   * angegebenen Klasse. Die Größe des Referenzbereichs ergibt sich durch Multiplikation mit
   * der Zeigergröße. Der Bezeichner muß zur Einhaltung der Java-Kompatibilität in Form eines
   * konstanten Strings vorliegen und darf keine Dereferenzierung enthalten.
   * @param clssName
   * @return
   */
  public static int getInstRelocEntries(String clssName) { return 0; }

  /**
   * Pseudo-Methode zum Setzen oder Löschen (Auswahl durch den Parameter set ) der durch
   * mask angegebenen Bitmaske im Hauptspeicher. Die Angabe der Adresse muß in dem zur
   * Architektur passenden Format oder als int vorliegen.
   * @param addr
   * @param v
   * @param set
   */
  public static void bitMem8(int addr, byte v, boolean set) {}

  /**
   * Pseudo-Methode zum Setzen oder Löschen (Auswahl durch den Parameter set ) der durch
   * mask angegebenen Bitmaske im Hauptspeicher. Die Angabe der Adresse muß in dem zur
   * Architektur passenden Format oder als int vorliegen.
   * @param addr
   * @param v
   * @param set
   */
  public static void bitMem16(int addr, short v, boolean set) {}

  /**
   * Pseudo-Methode zum Setzen oder Löschen (Auswahl durch den Parameter set ) der durch
   * mask angegebenen Bitmaske im Hauptspeicher. Die Angabe der Adresse muß in dem zur
   * Architektur passenden Format oder als int vorliegen.
   * @param addr
   * @param v
   * @param set
   */
  public static void bitMem32(int addr, int v, boolean set) {}

  /**
   * Pseudo-Methode zum Setzen oder Löschen (Auswahl durch den Parameter set ) der durch
   * mask angegebenen Bitmaske im Hauptspeicher. Die Angabe der Adresse muß in dem zur
   * Architektur passenden Format oder als int vorliegen.
   * @param addr
   * @param v
   * @param set
   */
  public static void bitMem64(int addr, long v, boolean set) {}

  /**
   * Pseudo-Methode zum Setzen oder Löschen (Auswahl durch den Parameter set ) der durch
   * mask angegebenen Bitmaske im I/O-Speicher. Die Angabe der Adresse muß in dem zur
   * Architektur passenden Format oder als int vorliegen. Unter Umständen werden von
   * manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @param v
   * @param set
   */
  public static void bitIOs8(int addr, byte v, boolean set) {}

  /**
   * Pseudo-Methode zum Setzen oder Löschen (Auswahl durch den Parameter set ) der durch
   * mask angegebenen Bitmaske im I/O-Speicher. Die Angabe der Adresse muß in dem zur
   * Architektur passenden Format oder als int vorliegen. Unter Umständen werden von
   * manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @param v
   * @param set
   */
  public static void bitIOs16(int addr, short v, boolean set) {}

  /**
   * Pseudo-Methode zum Setzen oder Löschen (Auswahl durch den Parameter set ) der durch
   * mask angegebenen Bitmaske im I/O-Speicher. Die Angabe der Adresse muß in dem zur
   * Architektur passenden Format oder als int vorliegen. Unter Umständen werden von
   * manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @param v
   * @param set
   */
  public static void bitIOs32(int addr, int v, boolean set) {}

  /**
   * Pseudo-Methode zum Setzen oder Löschen (Auswahl durch den Parameter set ) der durch
   * mask angegebenen Bitmaske im I/O-Speicher. Die Angabe der Adresse muß in dem zur
   * Architektur passenden Format oder als int vorliegen. Unter Umständen werden von
   * manchen Architekturen nicht alle Bitbreiten unterstützt.
   * @param addr
   * @param v
   * @param set
   */
  public static void bitIOs64(int addr, long v, boolean set) {}

  /**
   * Pseudo-Methode zur Konvertierung einer Adresse zu einer Struct-Referenz. Die Angabe der
   * Adresse muss in dem zur Architektur passenden Format oder als int vorliegen (siehe auch
   * cast2Obj(.) ). Eine vorgelagerte Konvertierung auf einen anderen Struct-Typ wird ohne
   * Prüfung, also ohne Aufruf der Laufzeitumgebung durchgeführt.
   * @param addr
   * @return
   */
  public static Object cast2Struct(int addr) { return null; }

  /**
   * Pseudo-Methoden zur Ermittlung der RAM-Adresse für Klassenvariablen im Embedded-
   * Mode (Parameter der Compiler-Option -e ), des Initialisierungsspeichers sowie der Größe
   * des RAM-Bereichs für Klassenvariablen im Embedded-Mode, siehe Kapitel 12.5.
   * @return
   */
  public static int getRamAddr() { return 0; }

  /**
   * Pseudo-Methoden zur Ermittlung der RAM-Adresse für Klassenvariablen im Embedded-
   * Mode (Parameter der Compiler-Option -e ), des Initialisierungsspeichers sowie der Größe
   * des RAM-Bereichs für Klassenvariablen im Embedded-Mode, siehe Kapitel 12.5.
   * @return
   */
  public static int getRamSize() { return 0; }

  /**
   * Pseudo-Methoden zur Ermittlung der RAM-Adresse für Klassenvariablen im Embedded-
   * Mode (Parameter der Compiler-Option -e ), des Initialisierungsspeichers sowie der Größe
   * des RAM-Bereichs für Klassenvariablen im Embedded-Mode, siehe Kapitel 12.5.
   * @return
   */
  public static int getRamInitAddr() { return 0; }

  /**
   * Pseudo-Methode zur expliziten Zuweisung eines Objektes im Konstruktor als aktuelle
   * Instanz. Hauptanwendung sind Konstrkutoren von Klassen mit inline-Arrays, die in Kapitel
   * 8 genauer besprochen werden.
   * @param newThis
   */
  public static void useAsThis(Object newThis) {}

  /**
   * Pseudo-Methode zur Ermittlung des für konstante Objekte verbrauchten Speicherplatzes.
   * Dies ist beispielsweise im Embedded-Mode interessant, da die konstanten Objekte direkt
   * nach dem RAM-Init-Bereich abgelegt werden. Somit können sie für Tests mit Harvard-
   * Architekturen leicht ins RAM kopiert werden, um „dynamische“ Objekte zu testen.
   * @return
   */
  public static int getConstMemorySize() { return 0; }

  /**
   * Pseudo-Methode zum Einbau der in statischen Klassen-Initialisierungen vorgenommenen
   * Statements. Die Zielarchitektur muß dafür Methoden-Inlining unterstützen, da die
   * Statements der Zielmethoden anstelle des doStaticInit() -Aufrufs eingefügt werden.
   */
  public static void doStaticInit() {}

  /**
   * Die Pseudo-Methode liefert zu konstanten Strings entsprechend initialisierte
   * konstante Arrays, die aus den Zeichen des übergebenen Strings sowie optional einem
   * abschließenden 0-Zeichen bestehen. Auf diese Weise können sonst umständlich von Hand
   * initialisierte Arrays elegant angelegt werden, ohne zur Laufzeit Konvertierungsaufwand bei
   * der Umwandlung zu einem optional mit 0-terminierten Array zu erfordern.
   * @param s
   * @return
   */
  public static byte[] toByteArray(String s) { return null; }

  /**
   * Die Pseudo-Methode liefert zu konstanten Strings entsprechend initialisierte
   * konstante Arrays, die aus den Zeichen des übergebenen Strings sowie optional einem
   * abschließenden 0-Zeichen bestehen. Auf diese Weise können sonst umständlich von Hand
   * initialisierte Arrays elegant angelegt werden, ohne zur Laufzeit Konvertierungsaufwand bei
   * der Umwandlung zu einem optional mit 0-terminierten Array zu erfordern.
   * @param s
   * @return
   */
  public static char[] toCharArray(String s) { return null; }

  /**
   * Pseudo-Methode, die durch den konstanten String ersetzt wird, der durch stringName
   * benenant ist. Sollte das Objekt stringName nicht vom Übersetzungssystem gefunden
   * werden, ist das Resultat null . Inhaltlich ist diese Funktion vergleichbar mit dem Import von
   * Binärdaten als Byte-Array (siehe Kapitel 4.1), jedoch kann diese Funktion auch verwendet
   * werden, wenn unsicher ist, ob das Zielobjekt existiert (das Ergebnis ist dann null , die
   * Übersetzung ist dennoch möglich; bei Referenzierung eines Objekts wie in Kapitel 4.1 muss
   * das referenzierte Objekt vorhanden sein). Für weitere Informationen zum Import von
   * Dateien siehe auch Kapitel 4.2.
   * @param s
   * @return
   */
  public static String getNamedString(String s) { return null; }

  /**
   * Alle als Parameter übergebenen Parameter werden ignoriert (dennoch müssen die
   * angegebenen Parameter syntaktisch korrekt sein). Werden beispielsweise Variablen
   * ausschließlich in Inline-Code gelesen, zeigt SunJava eine Warnung an, was durch den für
   * SunJava als Auslesen interpretierten MAGIC.ignore(.) -Befehl verhindert werden kann.
   * @param dummy
   */
  public static void ignore(int dummy) {}

  /**
   * Alle als Parameter übergebenen Parameter werden ignoriert (dennoch müssen die
   * angegebenen Parameter syntaktisch korrekt sein). Werden beispielsweise Variablen
   * ausschließlich in Inline-Code gelesen, zeigt SunJava eine Warnung an, was durch den für
   * SunJava als Auslesen interpretierten MAGIC.ignore(.) -Befehl verhindert werden kann.
   * @param dummy
   */
  public static void ignore(long dummy) {}

  /**
   * Alle als Parameter übergebenen Parameter werden ignoriert (dennoch müssen die
   * angegebenen Parameter syntaktisch korrekt sein). Werden beispielsweise Variablen
   * ausschließlich in Inline-Code gelesen, zeigt SunJava eine Warnung an, was durch den für
   * SunJava als Auslesen interpretierten MAGIC.ignore(.) -Befehl verhindert werden kann.
   * @param o
   */
  public static void ignore(Object o) {}

  /**
   * Die Code-Erzeugung für den aktuellen Block wird gestoppt. Dies kann zum Beispiel
   * verwendet werden, wenn ein Inline-Block bereits einen Rückgabewert im für die aktuelle
   * Architektur passenden Primär-Register erzeugt, zur Erfüllung der Java-Quellcode-
   * Kompatibilität jedoch ein return -Statement erforderlich wäre.
   */
  public static void stopBlockCoding() {}

  /**
   * Die Variable value wird unabhängig von ihrem final -Flag beschrieben. Auf diese Weise ist
   * eine Java-konforme Initialisierung von z.B. neu angelegten Arrays möglich, auch wenn
   * SArray.length als final deklariert ist.
   * @param dest
   * @param value
   */
  public static void assign(int dest, int value) {}

  /**
   * Die Variable value wird unabhängig von ihrem final -Flag beschrieben. Auf diese Weise ist
   * eine Java-konforme Initialisierung von z.B. neu angelegten Arrays möglich, auch wenn
   * SArray.length als final deklariert ist.
   * @param dest
   * @param value
   */
  public static void assign(long dest, long value) {}

  /**
   * Die Variable value wird unabhängig von ihrem final -Flag beschrieben. Auf diese Weise ist
   * eine Java-konforme Initialisierung von z.B. neu angelegten Arrays möglich, auch wenn
   * SArray.length als final deklariert ist.
   * @param dest
   * @param value
   */
  public static void assign(Object dest, Object value) {}
}
