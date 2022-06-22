/**********************************************************
AC Auto manager
usage:
    AcPulseGenerator [ENABLE/DISABLE] [Auto On Temperature(16-31)] [AC Target Temperature]
        :creates setting file
    AcPulseGenerator
        :creates IR pulse sequence
to send pulse(use ir-ctl):
    sudo ./AcPulseGenerator > ac.pulse && ir-ctl -d /dev/lirc0 --send=./ac.pulse
**********************************************************/

#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<wiringPi.h>
#include<wiringPiI2C.h>
#include<fcntl.h>
#include<sys/ioctl.h>
#include<linux/i2c-dev.h>

float getCurrentTemp();

#define byte unsigned char
#define T 425
#define PIN_NO 3

// Default byte sequence
byte sequence[] = { 35,203,38,1,0,0,24,0,54,64,0,0,0,0,0,0,0,0 };

void sendLeaderPulse();
void sendEndPulse();
byte sendBytePulse(byte data);
byte sendOnOff(int isOn);
byte sendTemperature(byte p0);

int main(int argc, char* argv[]){
    FILE* temp;
    
    int isEnabled;
    int autoOnTemp;
    int targetTemp;
    if(argc>3){
        isEnabled=atoi(argv[1]);
        autoOnTemp=atoi(argv[2]);
        targetTemp=atoi(argv[3]);
        
        temp = fopen("autoSetting.data","w");
        fprintf(temp,"%d %d %d",isEnabled,autoOnTemp,targetTemp);
        fclose(temp);
        return 0;
    }
    else{
        if(temp=fopen("autoSetting.data","r")){
            fscanf(temp,"%d %d %d",&isEnabled,&autoOnTemp,&targetTemp);
            //fprintf(stdout,"%d %d %d",isEnabled,autoOnTemp,targetTemp);
            fclose(temp);
        }
        else{
            fprintf(stderr,"Setting does not exist\n");
            return 0;
        }
        
    }
    
    float temperature = getCurrentTemp();
    //fprintf(stdout,"%f\n",temperature);

    /*
        create IR sequence 
        if current temperature is above setting temperature
        and is enabled
    */
    if(temperature>autoOnTemp && isEnabled){
        
        // parity byte the sum of every byte of sequence
        byte parityByte = 0;

        for (int j = 0; j < 2; j++) {
            sendLeaderPulse();
            for (int i = 0; i < 18; i++) {
                // ON/OFF byte
                if (i == 5) {
                    parityByte += sendOnOff(1);
                }
                // temperature byte
                else if (i == 7) {
                    parityByte += sendTemperature(targetTemp);
                }
                // parity byte
                else if (i == 17) {
                    sendBytePulse(parityByte);
                }
                // default byte
                else {
                    parityByte += sendBytePulse(sequence[i]);
                }
            }
            sendEndPulse();
        }
    }
    // create empty pulse elsewise
    else{
        sendLeaderPulse();
        sendEndPulse();
    }

}

// send pulse indicating start of transmission
void sendLeaderPulse() {
    printf("pulse %d\n", T * 8);
    printf("space %d\n", T * 4);
}

// send pulse indicating end of transmission
void sendEndPulse() {
    printf("pulse %d\n", T * 1);
    printf("space %d\n", T * 30);
}

// transfer byte to pulse and transmit
byte sendBytePulse(byte data) {
    for (int i = 0; i < 8; i++) {
        if ((data >> i) & 1) {
            printf("pulse %d\n", T * 1);
            printf("space %d\n", T * 3);
        }
        else {
            printf("pulse %d\n", T * 1);
            printf("space %d\n", T * 1);
        }
    }
    return data;
}

byte sendOnOff(int isOn) {
    byte data;
    if (isOn) {
        data = 32;
    }
    else {
        data = 0;
    }
    sendBytePulse(data);
    return data;
}

byte sendTemperature(byte p0) {
    byte data = p0 - 16;
    sendBytePulse(data);
    return data;
}

// get current temperature from I2C
float getCurrentTemp(){
    int dev_fd;
    int data;

    // Set up wiringPi I2C
    dev_fd=wiringPiI2CSetup(0x48);
    if(dev_fd<0){
        printf("Error: I2C Setup\n");
        return -1;
    }

    
    float temperature=0;
    for(int i=0;i<10;i++){
        // Start
        wiringPiI2CWrite(dev_fd,0x51);
        usleep(5000);

        // Read
        data=wiringPiI2CReadReg16(dev_fd,0xAA);

        // Stop
        wiringPiI2CWrite(dev_fd,0x22);

        // change   
        temperature=0;
        if((data&(1<<0))!=0)temperature+=1;
        if((data&(1<<1))!=0)temperature+=2;
        if((data&(1<<2))!=0)temperature+=4;
        if((data&(1<<3))!=0)temperature+=8;
        if((data&(1<<4))!=0)temperature+=16;
        if((data&(1<<5))!=0)temperature+=32;
        if((data&(1<<6))!=0)temperature+=64;

        if((data&(1<<12))!=0)temperature+=1.f/(16);
        if((data&(1<<13))!=0)temperature+=1.f/(8);
        if((data&(1<<14))!=0)temperature+=1.f/(4);
        if((data&(1<<15))!=0)temperature+=1.f/(2);

        if((data&(1<<7))==0){
            
        }
        else{
            temperature=-temperature;
        }

        if(temperature>-50)
            break;
    }

    return temperature;
}