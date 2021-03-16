#!/bin/bash

start=false
while getopts ":s" option; do
  case $option in
    b) start=true ;;
    ?) echo "error: option -$OPTARG is not implemented"; exit ;;
  esac
done

./compile rte/rte.java ThanOS/src/**/*.java -o boot
rm -R build/*
mv BOOT_FLP.IMG build/BOOT_FLP.IMG
mv syminfo.txt build/syminfo.txt

if [ start ]
  then
    echo "Starting"
    qemu-system-i386 -boot a -fda build/BOOT_FLP.IMG
fi
