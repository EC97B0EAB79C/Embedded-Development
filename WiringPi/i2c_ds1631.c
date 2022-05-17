#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<wiringPi.h>
#include<wiringPiI2C.h>
#include<fcntl.h>
#include<sys/ioctl.h>
#include<linux/i2c-dev.h>

int main(){
    int dev_fd;
    int data;
    dev_fd=wiringPiI2CSetup(0x48);
    if(dev_fd<0){
        printf("Error: I2C Setup\n");
        return -1;
    }

    wiringPiI2CWrite(dev_fd,0x51);
    usleep(5000);

    data=wiringPiI2CReadReg16(dev_fd,0xAA);
    printf("data=0x%x\n",data);

    wiringPiI2CWrite(dev_fd,0x22);

    float temp=0.f;

    if((data&(1<<0))!=0)temp+=1;
    if((data&(1<<1))!=0)temp+=2;
    if((data&(1<<2))!=0)temp+=4;
    if((data&(1<<3))!=0)temp+=8;
    if((data&(1<<4))!=0)temp+=16;
    if((data&(1<<5))!=0)temp+=32;
    if((data&(1<<6))!=0)temp+=64;

    if((data&(1<<12))!=0)temp+=1.f/(16);
    if((data&(1<<13))!=0)temp+=1.f/(8);
    if((data&(1<<14))!=0)temp+=1.f/(4);
    if((data&(1<<15))!=0)temp+=1.f/(2);

    if((data&(1<<7))==0){
        printf("float=%f\n",temp);
    }
    else{
        printf("float=%f\n",-temp);
    }
}