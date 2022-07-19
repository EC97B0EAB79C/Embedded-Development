<?php
// program location
define("ACAUTO","/usr/bin/sudo /home/pi/AcAutoOn");
// Paspberry Pi SSH
define("ADDRESS","127.0.0.1");
define("USER","pi");
define("PASSWORD","YOUR PASSWORD");

// run if all variables are present
if(!is_null($_GET["isEnabled"])&&!is_null($_GET["onTemp"])&&!is_null($_GET["targetTemp"])){
    
    // SSH connection
    $sconnection=ssh2_connect(ADDRESS,22);
    ssh2_auth_password($sconnection,USER,PASSWORD);
    
    // run command
    $commamd=ACAUTO." ".$_GET["isEnabled"]." ".$_GET["onTemp"]." ".$_GET["targetTemp"];
    $stdio_stream=ssh2_exec($sconnection,$commamd);
}
?>