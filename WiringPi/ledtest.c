#include<stdio.h>
#include<stdlib.h>
#include<wiringPi.h>

// Set wiringPi Pin with connected LED number
int LED_PIN[]={12,13,14,11,10};

int main(int argc,char*argv[]){
	// check if needed arguments are present
	if(argc<3){
		printf("Usage: %s LED_Number ON(1)/OFF(0)\n", argv[0]);
		return -1;
	}

	// Get LED number from first argument
	int led_num=atoi(argv[1]);
	// Get ON/OFF from secont argument
	int on_off=atoi(argv[2]);

	// Check if LED number is in range
	if(led_num>4){
		printf("Error: LED_Number must be specified from 0 to 4.\n");
		return -1;
	}
	// Chech if LED ON/OFF is set correctly
	if(!(on_off==0||on_off==1)){
		printf("Error: ON/OFF must be specified to 0 or 1.\n");
		return -1;
	}

	// Set up WiringPi
	if(wiringPiSetup()==-1){
		printf("Error: setup failed.\n");
		return -1;
	}

	// set LED pin for output
	pinMode(LED_PIN[led_num],OUTPUT);
	// set LED ON/OFF
	digitalWrite(LED_PIN[led_num],on_off);
	return 0;
}
