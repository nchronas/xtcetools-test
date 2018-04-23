#include <stdint.h>
#include <stddef.h>

#define HLDLC_START_FLAG        0x7E
#define HLDLC_CONTROL_FLAG      0x7D
#define HLDLC_STOP_FLAG         0x7C



void HLDLC_deframe(uint8_t *buf_in,
                              uint8_t *buf_out,
                              const uint16_t size_in,
                              uint16_t *size_out) {

  
    uint16_t cnt = 0;

  //  for(uint16_t i = 1; i < size_in; i++) {
  //          if(buf_in[i] == HLDLC_START_FLAG) {
  //              cnt = 0;

   // }

    for(uint16_t i = 0; i < size_in; i++) {
        if(buf_in[i] == HLDLC_START_FLAG) {
            cnt = 0;
        } else if(buf_in[i] == HLDLC_STOP_FLAG) {
            *size_out = cnt;
            return ;
        } else if(buf_in[i] == HLDLC_CONTROL_FLAG) {
            i++;

            if(buf_in[i] == 0x5E) {
              buf_out[cnt++] = 0x7E;
            } else if(buf_in[i] == 0x5D) {
              buf_out[cnt++] = 0x7D;
            } else if(buf_in[i] == 0x5C) {
              buf_out[cnt++] = 0x7C;
            } else {
              return ;
            }
        } else {
            buf_out[cnt++] = buf_in[i];
        }
    }
    return ;
}

//used for DMA
void HLDLC_frame(uint8_t *buf_in, uint8_t *buf_out, uint16_t *size) {

    uint16_t cnt = 2;

    for(uint16_t i = 0; i < *size; i++) {
        if(i == 0) {
            buf_out[0] = HLDLC_START_FLAG;
            buf_out[1] = buf_in[0];
        } else if(i == (*size) - 1) {
            if(buf_in[i] == HLDLC_START_FLAG) {
                buf_out[cnt++] = HLDLC_CONTROL_FLAG;
                buf_out[cnt++] = 0x5E;
            } else if(buf_in[i] == HLDLC_STOP_FLAG) {
                buf_out[cnt++] = HLDLC_CONTROL_FLAG;
                buf_out[cnt++] = 0x5C;
            } else if(buf_in[i] == HLDLC_CONTROL_FLAG) {
                buf_out[cnt++] = HLDLC_CONTROL_FLAG;
                buf_out[cnt++] = 0x5D;
            } else {
                buf_out[cnt++] = buf_in[i];
            }
            buf_out[cnt++] = HLDLC_STOP_FLAG;
            *size = cnt;
            return ;
        } else if(buf_in[i] == HLDLC_START_FLAG) {
            buf_out[cnt++] = HLDLC_CONTROL_FLAG;
            buf_out[cnt++] = 0x5E;
        } else if(buf_in[i] == HLDLC_STOP_FLAG) {
            buf_out[cnt++] = HLDLC_CONTROL_FLAG;
            buf_out[cnt++] = 0x5C;
        } else if(buf_in[i] == HLDLC_CONTROL_FLAG) {
            buf_out[cnt++] = HLDLC_CONTROL_FLAG;
            buf_out[cnt++] = 0x5D;
        } else {
            buf_out[cnt++] = buf_in[i];
        }

    }

    return ;
}
