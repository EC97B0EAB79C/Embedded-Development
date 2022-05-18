#include<stdio.h>
#include<stdlib.h>
#include<wiringPi.h>

// Set wiringPi Pin with connected switch number
int SW_PIN[]={0,7};

int main(int argc, char* argv[]){
    // check if needed arguments are present
    if(argc<2){
        printf("Usage: %s SW_Number\n",argv[0]);
        return -1;
    }

    // Get switch number from first argument
    int sw_num=atoi(argv[1]);
    int sw_val;

    // Check if switch number is in range
    if(sw_num>1){
        printf("Error: SW_Number must be specified fron 0 to 1.\n");
        return -1;
    }

    // Set up WiringPi
    if(wiringPiSetup()==-1){
        printf("Error: setup failed.\n");
        return -1;
    }

    // set swtich pin for output
    pinMode(SW_PIN[sw_num],INPUT);
    // read switch ON/OFF
    sw_val=digitalRead(SW_PIN[sw_num]);

    // print switch value
    printf("%d\n", sw_val);
    return 0;
}