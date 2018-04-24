#include <stdint.h>

void setup() {
  //Initialize serial and wait for port to open:
  Serial.begin(9600);

}

uint16_t size_out = 0;
uint8_t out[100];

uint8_t buf[100];
uint16_t b_cnt = 0;

void loop() {

  if(Serial.available() > 0) {
    uint8_t c = Serial.read();

    if(c == HLDLC_STOP_FLAG) {
      buf[b_cnt++] = c;

      size_out = 0;
      HLDLC_deframe(buf, out,b_cnt, &size_out);
      if(size_out > 0) {
        process_frame(out);
      }

      b_cnt = 0;
    } else if(b_cnt > 0 || c == HLDLC_START_FLAG) {
      buf[b_cnt++] = c;
    }
  }

}
