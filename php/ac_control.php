<?php
// program location
define("ACCONTROL","/usr/bin/sudo /home/pi/AcPulseGenerator");              // AcPulseGenerator
define("IRCTL","/usr/bin/ir-ctl -d /dev/lirc0 --send=/home/pi/ac.pulse");   // LIRC
// Paspberry Pi SSH
define("ADDRESS","127.0.0.1");
define("USER","pi");
define("PASSWORD","YOUR PASSWORD");

// run if all variables are present
if(!is_null($_GET["isOn"])&&!is_null($_GET["targetTemp"])){

    // SSH connection
    $sconnection=ssh2_connect(ADDRESS,22);
    ssh2_auth_password($sconnection,USER,PASSWORD);

    // run command (AcPulseGenerator)
    $commamd=ACCONTROL." ".$_GET["isOn"]." ".$_GET["targetTemp"];
    $stream = ssh2_exec($sconnection, $commamd);
    // get command output
    stream_set_blocking($stream, true);
    $stream_out = ssh2_fetch_stream($stream, SSH2_STREAM_STDIO);
    echo stream_get_contents($stream_out);

    // run command (LIRC)
    $commamd=IRCTL;
    $stream = ssh2_exec($sconnection, $commamd);
    // get command output
    stream_set_blocking($stream, true);
    $stream_out = ssh2_fetch_stream($stream, SSH2_STREAM_STDIO);
    echo stream_get_contents($stream_out);
}
?>