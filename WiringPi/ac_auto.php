<?php
define("LEDTEST","/usr/bin/sudo /home/pi/AcAutoOn");
define("ADDRESS","127.0.0.1");
define("USER","pi");
define("PASSWORD","peanut butter jelly time");

if(!is_null($_GET["isEnabled"])&&!is_null($_GET["onTemp"])&&!is_null($_GET["targetTemp"])){
    $sconnection=ssh2_connect(ADDRESS,22);
    ssh2_auth_password($sconnection,USER,PASSWORD);
    $commamd=LEDTEST." ".$_GET["isEnabled"]." ".$_GET["onTemp"]." ".$_GET["targetTemp"];
    $stdio_stream=ssh2_exec($sconnection,$commamd);
}
?>