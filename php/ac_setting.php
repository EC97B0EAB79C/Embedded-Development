<?php
// Paspberry Pi SSH
define("ADDRESS","127.0.0.1");
define("USER","pi");
define("PASSWORD","YOUR PASSWORD");

// Print Data on Raspberry Pi
readfile("/home/pi/isOn.data");
echo " ";
readfile("/home/pi/temperature.data");
echo " ";
readfile("/home/pi/autoSetting.data");
?>