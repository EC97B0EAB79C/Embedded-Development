<?php
define("LEDTEST","/usr/bin/sudo /home/pi/ledtest");
define("ADDRESS","127.0.0.1");
define("USER","pi");
define("PASSWORD","peanut butter jelly time");

if(!is_null($_GET["num"])&&!is_null($_GET["stat"])){
    $sconnection=ssh2_connect(ADDRESS,22);
    ssh2_auth_password($sconnection,USER,PARRWORD);
    $commamd=LED." ".$_GET["num"]." ".$_GET["stat"];
    $stdio_stram=ssh2_exec($sconnection,$commamd);
}
?>