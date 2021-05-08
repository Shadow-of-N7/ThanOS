#!/bin/bash

start=false
while getopts ":s" option; do
  case $option in
    s) start=true ;;
    ?) echo "error: option -$OPTARG is not implemented"; exit ;;
  esac
done
echo $start

codefiles=$(find . -name "*.java")

#./compile rte/rte/*.java rte/java/**/*.java ThanOS/src/**/*.java ThanOS/src/kernel/memory/*.java -n -o boot
./compile $codefiles -n -o boot

if [ $? -ne 0 ]
  then
    exit
fi

rm -R build
mkdir build
mv BOOT_FLP.IMG build/BOOT_FLP.IMG
mv syminfo.txt build/syminfo.txt


case $start in
  (true)    qemu-system-i386 -serial stdio -boot a -fda build/BOOT_FLP.IMG;;
  (false)   ;;
esac
