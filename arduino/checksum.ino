#include <stdbool.h>
#include <stddef.h>

void checkSum(const uint8_t *data, const uint16_t size, uint8_t *res_crc) {

    *res_crc = 0;
    for(int i=0; i<=size; i++){
        *res_crc = *res_crc ^ data[i];
    }

}

void process_frame(uint8_t *buf) {

    uint8_t ser_type = buf[7];

    if(ser_type == 17) {

      uint8_t ts_tx_raw[12] = { 24,1,192,185,0,5,16,17,2,6,0,0 };

      uint8_t res_crc = 0;
      checkSum(ts_tx_raw, 10, &res_crc);
      ts_tx_raw[11] = res_crc;

      uint8_t out[100];
      uint8_t size = 12;
      HLDLC_frame(ts_tx_raw, out, &size);

      for(int i=0; i < size; i++) {
        Serial.write(out[i]);
      }

    } else if(ser_type == 3) {
      System.out.println("Housekeeping service");
    }
}
