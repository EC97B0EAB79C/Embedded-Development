<?php
define("ACCONTROL","/usr/bin/sudo /home/pi/AcPulseGenerator");
define("IRCTL","/usr/bin/ir-ctl -d /dev/lirc0 --send=/home/pi/ac.pulse");
define("ADDRESS","127.0.0.1");
define("USER","pi");
define("PASSWORD","peanut butter jelly time");

if(!is_null($_GET["isOn"])&&!is_null($_GET["targetTemp"])){
    $sconnection=ssh2_connect(ADDRESS,22);
    ssh2_auth_password($sconnection,USER,PASSWORD);

    $commamd=ACCONTROL." ".$_GET["isOn"]." ".$_GET["targetTemp"]." > /home/pi/ac.pulse";
    $stream = ssh2_exec($sconnection, $commamd);
    stream_set_blocking($stream, true);
    $stream_out = ssh2_fetch_stream($stream, SSH2_STREAM_STDIO);
    echo stream_get_contents($stream_out);

    $commamd=IRCTL;
    $stream = ssh2_exec($sconnection, $commamd);
    stream_set_blocking($stream, true);
    $stream_out = ssh2_fetch_stream($stream, SSH2_STREAM_STDIO);
    echo stream_get_contents($stream_out);
}
?>