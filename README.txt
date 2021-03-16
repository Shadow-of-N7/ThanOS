To compile use the native compiler
  (Windows) compile.exe
  (Linux)   ./compile
or the Sun-Java compiler
  java -cp YOUR-CLASS-PATH ui.SC
and append options afterwards.

As parameters use
  (32 bit) rte.java hello.java -o boot
  (64 bit) rte.java hello.java -t amd64 -o boot -O "bootconf.txt#floppy64"
to compile the test environment and generate a floppy disk image.

To test the compiled floppy disk image, use
  (32 bit)        qemu-system-i386 -boot a -fda BOOT_FLP.IMG
  (32 and 64 bit) qemu-system-x86_64 -boot a -fda BOOT_FLP.IMG
if you have qemu installed. You should see "Hello World" in the left upper
corner after booting completed.

