#include <stdint.h>

void setup() {
  //Initialize serial and wait for port to open:
  Serial.begin(9600);

}

int cnt=0;
uint8_t msg[100];
uint16_t siz = 0;
uint8_t out[100];

void loop() {

  sprintf(msg,"Hello %d\n", cnt);
  siz = strlen(msg);
  HLDLC_frame(msg, out, &siz);

  for(int i=0; i < siz; i++) {
    Serial.write(out[i]);
  }
  //Serial.println(cnt++);
  cnt++;
  delay(1000);
}
