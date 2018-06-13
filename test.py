import time
import serial
print (serial.__version__)
#3.4
ser = serial.Serial(
    port='/dev/tty.usbmodem14411',
    baudrate=9600,
    parity=serial.PARITY_NONE,
    stopbits=serial.STOPBITS_ONE,
    bytesize=serial.EIGHTBITS,
    timeout = None
    #timeout = 1
)

while 1:
    in_bin = ser.read(1)
    if in_bin == '~':
        print
        print "New address: "
    else:
        print " ," + in_bin.encode('hex'), 
    time.sleep(0.01)
