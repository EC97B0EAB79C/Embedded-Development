<?php
define("ADDRESS","127.0.0.1");
define("USER","pi");
define("PASSWORD","peanut butter jelly time");

readfile("/home/pi/isOn.data");
echo " ";
readfile("/home/pi/temperature.data");
echo " ";
readfile("/home/pi/autoSetting.data");
?>