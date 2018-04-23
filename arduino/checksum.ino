#include <stdbool.h>
#include <stddef.h>

void checkSum(const uint8_t *data, const uint16_t size, uint8_t *res_crc) {

    *res_crc = 0;
    for(int i=0; i<=size; i++){
        *res_crc = *res_crc ^ data[i];
    }

}
