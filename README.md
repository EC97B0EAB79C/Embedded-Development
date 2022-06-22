# Embedded Development

This repository is a collection of codes written for the class "Practical development for Embedded and IoT systems(0ALD502)."

## Android

1. All Android Project code is written in Kotlin instead of JAVA.
2. [View Binding](https://developer.android.com/topic/libraries/view-binding) will be used instead of using findViewById method
3. Planned to replace deprecated funtions
    1. List of replaced functions could be found [here](./Android/replaced.md)

### Project List
1. [Orientation Sample](./Android/OrientationSample)
1. [Location Sample](./Android/LocationSample)
1. [Intent Sample](./Android/IntentSample)
    1. [Fragment Sample](./Android/FragmentSample)
1. [Web Sample](./Android/WebSample)
1. [Camera Sample](./Android/CameraSample)
1. [Touch Sample](./Android/TouchSample)
1. [Raspberry Pi LED Sample](./Android/RaspberryPiLEDSample)
1. [The ULTRA DELUXE Alarm](./Android/TheULTRADELUXEAlarm/)

## WiringPi
Interaction with Raspberry Pi's GPIO(General Purpose I/O) with WiringPi
### Project List
1. [ledtest](./WiringPi/ledtest.c):
C program for switching LED ON/OFF on Raspberry Pi
1. [swtest](./WiringPi/swtest.c):
C program for reading switch ON/OFF on Raspberry Pi
1. [i2c_ds1631](./WiringPi/i2c_ds1631.c):
C program for i2c temperature sensor on Raspberry Pi
1. [ledtest php](./WiringPi/ledtest.php):
php program for running ledtest program using url
1. [AcPulseGenerator](./WiringPi/AcPulseGenerator.cpp)
C program for making pulse sequence for AC remote

## Disclaimer
**Please do not use programs in this repository *as-is* for purposes like assignments.**