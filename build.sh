#!/bin/bash

start=false
while getopts ":s" option; do
  case $option in
    s) start=true ;;
    ?) echo "error: option -$OPTARG is not implemented"; exit ;;
  esac
done
echo $start

./compile rte/rte/*.java rte/java/**/*.java ThanOS/src/**/*.java -o boot

if [ $? -eq 1 ]
  then
    exit
fi

rm -R build/*
mv BOOT_FLP.IMG build/BOOT_FLP.IMG
mv syminfo.txt build/syminfo.txt


case $start in
  (true)    qemu-system-i386 -boot a -fda build/BOOT_FLP.IMG;;
  (false)   ;;
esac
