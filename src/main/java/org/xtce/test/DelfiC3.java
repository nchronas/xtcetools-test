package org.xtce.test;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import org.xtce.toolkit.XTCEContainerContentEntry;
import org.xtce.toolkit.XTCEContainerContentModel;
import org.xtce.toolkit.XTCEContainerEntryValue;
import org.xtce.toolkit.XTCEDatabase;
import org.xtce.toolkit.XTCEDatabaseException;
import org.xtce.toolkit.XTCETMStream;

import com.fazecast.jSerialComm.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Example Delfi-C3 XTCE telemetry extractor
 *
 */
public class DelfiC3
{

  static byte[] temp_buffer = new byte[100];
  static int temp_counter = 0;

  static SerialPort comPort;
  
  static final int HLDLC_START_FLAG = 0x7E;
  static final int HLDLC_CONTROL_FLAG = 0x7D;
  static final int HLDLC_STOP_FLAG = 0x7C;
  

    public static void main(String[] args)
          comPort = SerialPort.getCommPort("/dev/ttyACM0");
          comPort.openPort();
          comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
              public void serialEvent(SerialPortEvent event)
              {


                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                  return;
                while(comPort.bytesAvailable() > 0) {

                  byte[] newData = new byte[1];
                  int numRead = comPort.readBytes(newData, 1);

                  if(newData[0]== HLDLC_START_FLAG) {
                    temp_buffer[0] = 0x7e;
                    temp_counter = 1;
                    System.out.println("Yo, 7E? " + (temp_buffer[0] ==(byte) 0x7e) );
                  } else if(newData[0]== HLDLC_STOP_FLAG) {

                    temp_buffer[temp_counter] = HLDLC_STOP_FLAG;
                    temp_counter++;
                    byte[] passingBy = Arrays.copyOf(temp_buffer, temp_counter);
                    temp_counter = 0;
                    System.out.println("Read " + passingBy.length + " bytes." + new String(passingBy, 0));

                    byte[] resp = hdlc_deframe(passingBy);
                    process_frame(resp);

                  } else if(temp_counter > 0) {
                    temp_buffer[temp_counter++] = newData[0];
                  }

                }

              }
            });
          
  		Timer t = new Timer();
  		t.scheduleAtFixedRate(new TimerTask() {

  			@Override
  		    public void run() {
  		                System.out.println("Tx test service");
  		                byte[] ts_tx_raw = { 24,1,(byte)192,(byte)185,0,5,16,17,1,6,0,0 };
                      ts_tx_raw[ts_tx_raw.length - 1] = (byte)checkSum(ts_tx_raw, ts_tx_raw.length - 2);
                      byte[] framed = HLDLC_frame(ts_tx_raw);
                      comPort.writeBytes(framed, (long)framed.length);
  		            }
  		        }, 1000, 1000);

          
        while(true) {
        }
    }

    static byte[] HLDLC_frame(byte[] buf_in) {

        byte[] temp = new byte[100];
        int size = buf_in.length;
        int cnt = 2;


        for(int i = 0; i < size; i++) {
            if(i == 0) {
                temp[0] = HLDLC_START_FLAG;
                temp[1] = buf_in[0];
            } else if(i == (size) - 1) {
                if(buf_in[i] == HLDLC_START_FLAG) {
                    temp[cnt++] = HLDLC_CONTROL_FLAG;
                    temp[cnt++] = 0x5E;
                } else if(buf_in[i] == HLDLC_STOP_FLAG) {
                    temp[cnt++] = HLDLC_CONTROL_FLAG;
                    temp[cnt++] = 0x5C;
                } else if(buf_in[i] == HLDLC_CONTROL_FLAG) {
                    temp[cnt++] = HLDLC_CONTROL_FLAG;
                    temp[cnt++] = 0x5D;
                } else {
                    temp[cnt++] = buf_in[i];
                }
                temp[cnt++] = HLDLC_STOP_FLAG;

                byte[] res = Arrays.copyOf(temp, cnt);
                return res;
            } else if(buf_in[i] == HLDLC_START_FLAG) {
                temp[cnt++] = HLDLC_CONTROL_FLAG;
                temp[cnt++] = 0x5E;
            } else if(buf_in[i] == HLDLC_STOP_FLAG) {
                temp[cnt++] = HLDLC_CONTROL_FLAG;
                temp[cnt++] = 0x5C;
            } else if(buf_in[i] == HLDLC_CONTROL_FLAG) {
                temp[cnt++] = HLDLC_CONTROL_FLAG;
                temp[cnt++] = 0x5D;
            } else {
                temp[cnt++] = buf_in[i];
            }

        }
        System.out.println("Error in HLDC frame");
        return temp;
    }

    static void process_frame(byte[] frame) {

    	byte ser_type = frame[7];

    	if(ser_type == 17) {
    		System.out.println("Processed Test service");
    	} else if(ser_type == 3) {
    		System.out.println("Processed Housekeeping service");
    	}
    }

    static byte[] hdlc_deframe(byte[] framed) {

      int cnt = 0;
      byte[] res = new byte[100];

      for(int idx = 0; idx < framed.length; idx++) {

          if(idx == 0 && framed[idx] != HLDLC_START_FLAG) {  //HLDLC_START_FLAG
              System.out.println("Error in HLDC deframe 1");
              //throw exeption
          }

          if(framed[idx] == (byte)HLDLC_START_FLAG) { //HLDLC_START_FLAG
            // do nothing, no need to copy

          } else if(framed[idx] == (byte)HLDLC_STOP_FLAG) { //HLDLC_STOP_FLAG
                  byte[] temp = new byte[cnt];
                  for(int i = 0; i < cnt; i++) {
                    temp[i] = res[i];
                  }
                  System.out.println("HLDC deframe complete " + temp.length + " bytes." + new String(temp, 0));
                  return temp;

          } else if(framed[idx] == HLDLC_CONTROL_FLAG) { //HLDLC_CONTROL_FLAG

              if(framed[idx] == 0x5E) {
                res[cnt++] = 0x7E;
              } else if(framed[idx] == 0x5D) {
                res[cnt++] = 0x7D;
              } else if(framed[idx] == 0x5C) {
                res[cnt++] = 0x7C;
              } else {
                System.out.println("Error in HLDC deframe 2");
                //throw exeption
              }
              idx++;
          } else {
              res[cnt++] = framed[idx];
          }
      }

      System.out.println("Error in HLDC deframe 3");
      //throw exeption
      return res;
    }

    static int checkSum(byte[] data, int size) {

        int res_crc = 0;
        for(int i=0; i<=size; i++) {
            res_crc = res_crc ^ data[i];
        }
        return res_crc;
    }


   
}
