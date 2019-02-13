rem ***************************************************************************************
rem * COM port can be adjusted by changing the -P<COMPORT> switch
rem * Firmware to be uploaded can be adjusted by changing the hex file name -Uflash:<Firmware File Name>:i
rem * Baud rate can be adjusted by changing the -b<Baud Rate> switch
rem ***************************************************************************************
@echo Be sure to adjust for your correct COM port
@echo Uploading fimrware...

avrdude -patmega328p -carduino -PCOM3 -b57600 -D -Uflash:w:firmware/DIYinoPrimeV1.hex:i
@echo Upload attempt is complete.
pause
