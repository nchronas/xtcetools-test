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
import java.util.Scanner;

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
    {
          //comPort = SerialPort.getCommPort("/dev/ttyUSB0");
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
                    System.out.printf("\nNew address: ");
                  } else if(newData[0]== HLDLC_CONTROL_FLAG) {
                    temp_counter++;
                  } else if(temp_counter > 0) {
                    if(newData[0] == 0x5e) {
                      System.out.printf(",7e ");  
                    } else if(newData[0] == 0x5d) {
                      System.out.print(",0x7d ");  
                    } else {
                      System.out.printf(",Error ");
                    }
                    temp_counter = 0;
                  } else {
                	System.out.printf(",%x ", newData[0]);  
                	System.out.printf(",%c ", newData[0]); 
                  }
                  

                }

              }
            });  
          
  		Timer t = new Timer();
  		t.scheduleAtFixedRate(new TimerTask() {

  			@Override
  		    public void run() {

                        byte[] tx_raw;
                        
                        // byte[] ts_obc_raw = { (byte)0x18, (byte)0x01, (byte)0xc0, (byte)0xb9, (byte)0x0, (byte)0x05, (byte)0x10, (byte)0x11, (byte)0x01, (byte)0x07, (byte)0x00, (byte)0x00 };
                        // tx_raw = Arrays.copyOf(ts_obc_raw, ts_obc_raw.length);
                        // System.out.println("Tx test service to OBC");
                        // ser_tx(tx_raw, (byte)1);
                        //
  		                  // byte[] ts_eps_raw = { (byte)0x18, (byte)0x02, (byte)0xc0, (byte)0xb9, (byte)0x0, (byte)0x05, (byte)0x10, (byte)0x11, (byte)0x01, (byte)0x07, (byte)0x00, (byte)0x00 };
                        // tx_raw = Arrays.copyOf(ts_eps_raw, ts_eps_raw.length);
                        // System.out.println("Tx test service to EPS");
                        // ser_tx(tx_raw, (byte)2);
                        //
	                      // byte[] ts_adb_raw = { (byte)0x18, (byte)0x03, (byte)0xc0, (byte)0xb9, (byte)0x0, (byte)0x05, (byte)0x10, (byte)0x11, (byte)0x01, (byte)0x07, (byte)0x00, (byte)0x00 };
                        // tx_raw = Arrays.copyOf(ts_adb_raw, ts_adb_raw.length);
                        // System.out.println("Tx test service to ADB");
                        // ser_tx(tx_raw, (byte)3);
                        //
                        // byte[] ts_adcs_raw = { (byte)0x18, (byte)0x05, (byte)0xc0, (byte)0xb9, (byte)0x0, (byte)0x05, (byte)0x10, (byte)0x11, (byte)0x01, (byte)0x07, (byte)0x00, (byte)0x00 };
                        // tx_raw = Arrays.copyOf(ts_adcs_raw, ts_adcs_raw.length);
                        // System.out.println("Tx test service to ADCS");
                        // ser_tx(tx_raw, (byte)5);
                        //
                        //
                        // byte[] hk_obc_raw = { (byte)0x18, (byte)0x01, (byte)0xc0, (byte)0xb9, (byte)0x0, (byte)0x06, (byte)0x10, (byte)0x03, (byte)0x15, (byte)0x07, (byte)0x01, (byte)0x00, (byte)0x00 };
                        // tx_raw = Arrays.copyOf(hk_obc_raw, hk_obc_raw.length);
                        // System.out.println("Tx Housekeeping service to OBC");
                        // ser_tx(tx_raw, (byte)1);
                        //
                        // byte[] hk_eps_raw = { (byte)0x18, (byte)0x02, (byte)0xc0, (byte)0xb9, (byte)0x0, (byte)0x06, (byte)0x10, (byte)0x03, (byte)0x15, (byte)0x07, (byte)0x01, (byte)0x00, (byte)0x00 };
                        // tx_raw = Arrays.copyOf(hk_eps_raw, hk_eps_raw.length);
                        // System.out.println("Tx Housekeeping service to EPS");
                        // ser_tx(tx_raw, (byte)2);
                        //
                        // byte[] hk_adb_raw = { (byte)0x18, (byte)0x03, (byte)0xc0, (byte)0xb9, (byte)0x0, (byte)0x06, (byte)0x10, (byte)0x03, (byte)0x15, (byte)0x07, (byte)0x01, (byte)0x00, (byte)0x00 };
                        // tx_raw = Arrays.copyOf(hk_adb_raw, hk_adb_raw.length);
                        // System.out.println("Tx Housekeeping service to ADB");
                        // ser_tx(tx_raw, (byte)3);
                        //
                        // byte[] hk_adcs_raw = { (byte)0x18, (byte)0x05, (byte)0xc0, (byte)0xb9, (byte)0x0, (byte)0x06, (byte)0x10, (byte)0x03, (byte)0x15, (byte)0x07, (byte)0x01, (byte)0x00, (byte)0x00 };
                        // tx_raw = Arrays.copyOf(hk_adcs_raw, hk_adcs_raw.length);
                        // System.out.println("Tx Housekeeping service to ADCS");
                        // ser_tx(tx_raw, (byte)5);
                        //
                        //
                        // byte[] fm_eps_off_raw = { (byte)0x18, (byte)0x02, (byte)0xc0, (byte)0xb9, (byte)0x0, (byte)0x07, (byte)0x10, (byte)0x08, (byte)0x01, (byte)0x07, (byte)0x00, (byte)0x04, (byte)0x00, (byte)0x00 };
                        // tx_raw = Arrays.copyOf(fm_eps_off_raw, fm_eps_off_raw.length);
                        // System.out.println("Tx FM service to EPS, closing v1");
                        // //ser_tx(tx_raw, (byte)2);
                        //
                        // byte[] fm_eps_on_raw = { (byte)0x18, (byte)0x02, (byte)0xc0, (byte)0xb9, (byte)0x0, (byte)0x07, (byte)0x10, (byte)0x08, (byte)0x01, (byte)0x07, (byte)0x01, (byte)0x04, (byte)0x00, (byte)0x00 };
                        // tx_raw = Arrays.copyOf(fm_eps_on_raw, fm_eps_on_raw.length);
                        // System.out.println("Tx FM service to EPS, opening v1");
                        // //ser_tx(tx_raw, (byte)2);

  		            }
  		        }, 1000, 10000);

  		Scanner scanner = new Scanner(System.in);
  		
        while(true) {
        	System.out.println("Tell me what to do");
        	String c = scanner.nextLine();
        	if(c.matches("r")) {
        		System.out.println("Sending ping");
        		byte[] ping_eps = { (byte)0x55, (byte)0x02, (byte)0x01, (byte)0x11, (byte)0x01, (byte)0x00, (byte)0x00};
        		byte[] tx_raw = Arrays.copyOf(ping_eps, ping_eps.length);
        		byte[] framed = HLDLC_frame(tx_raw);
        	    comPort.writeBytes(framed, (long)framed.length);
        	    String temp =Arrays.toString(framed);
        	    System.out.println("Tx: " + temp);

        	} else {
        		System.out.println("Wrong command");
        	}
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

    static void ser_tx(byte[] frame, byte dest) {

      frame[frame.length - 1] = (byte)checkSum(frame, frame.length - 2);
      byte[] pq_frame = pq_pack(frame, dest);
      //byte[] pq_frame = pq_pack(ts_tx_raw, 3);
      byte[] framed = HLDLC_frame(pq_frame);
      comPort.writeBytes(framed, (long)framed.length);
      String temp =Arrays.toString(framed);
      System.out.println("Tx: " + temp);

      try {
      Thread.sleep(3000);
      } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      }
    }


    static byte[] pq_unpack(byte[] frame) {
      byte[] res = Arrays.copyOf(frame, frame.length-1);
      System.out.println("PQ to " + frame[frame.length-1]);
      return res;
    }

    static byte[] pq_pack(byte[] frame, byte dest) {
      byte[] res = Arrays.copyOf(frame, frame.length+1);
      res[frame.length] = dest;

      return res;
    }

    static void process_frame(byte[] frame) {

    	if(frame.length <= 0) {
    		return;
    	}
      byte type = frame[0];
    	byte source_id = frame[1];
    	byte ser_type = frame[7];
    	byte ser_subtype = frame[8];

    	if(ser_type == 17 && ser_subtype == 2 && type == 8) {
    		System.out.println("Frame: " + Arrays.toString(frame));
    		System.out.println("Processed Test service response from " + source_id + " !!!!!!!!!!!!!");

    	} else if(ser_type == 3 && type == 8) {
    		System.out.println("Frame: " + byteArrayToHex(frame));
    		System.out.println("Processed Housekeeping service response from " + source_id + " !!!!!!!!!!!!!");

    	}

      if(ser_type == 17 && ser_subtype == 1 && type == 24) {
        source_id = frame[9];

        System.out.println("Frame: " + Arrays.toString(frame));
        System.out.println("Processed Test service request from " + source_id + " !!!!!!!!!!!!!");

      } else if(ser_type == 3 && type == 24) {
        source_id = frame[9];

        System.out.println("Frame: " + byteArrayToHex(frame));
        System.out.println("Processed Housekeeping service request from " + source_id + " !!!!!!!!!!!!!");

      }
    }

    static String byteArrayToHex(byte[] a) {

    	StringBuilder sb = new StringBuilder(a.length *3);
    	for(byte b: a) {
    		sb.append(String.format(" %02x", b));
    	}
    	return sb.toString();
    }

    static byte[] hdlc_deframe(byte[] framed) {

      int cnt = 0;
      byte[] res = new byte[100];

      for(int idx = 0; idx < framed.length; idx++) {

          if(idx == 0 && framed[idx] != HLDLC_START_FLAG) {  //HLDLC_START_FLAG
              System.out.println("Error in HLDC deframe 1" + "Frame: " + Arrays.toString(framed));
              //throw exeption
          }

          if(framed[idx] == (byte)HLDLC_START_FLAG) { //HLDLC_START_FLAG
            // do nothing, no need to copy

          } else if(framed[idx] == (byte)HLDLC_STOP_FLAG) { //HLDLC_STOP_FLAG
                  byte[] temp = new byte[cnt];
                  for(int i = 0; i < cnt; i++) {
                    temp[i] = res[i];
                  }
                  //System.out.println("HLDC deframe complete " + temp.length + " bytes." + new String(temp, 0));
                  return temp;

          } else if(framed[idx] == HLDLC_CONTROL_FLAG) { //HLDLC_CONTROL_FLAG
              idx++;
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
