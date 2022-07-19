<?php
// program location
define("GETTEMP","/usr/bin/sudo /home/pi/CurrentTemp");
// Paspberry Pi SSH
define("ADDRESS","127.0.0.1");
define("USER","pi");
define("PASSWORD","YOUR PASSWORD");

// SSH connection
$sconnection=ssh2_connect(ADDRESS,22);
ssh2_auth_password($sconnection,USER,PASSWORD);

// run command
$commamd=GETTEMP;
$stream = ssh2_exec($sconnection, $commamd);
// get command output
stream_set_blocking($stream, true);
$stream_out = ssh2_fetch_stream($stream, SSH2_STREAM_STDIO);
echo stream_get_contents($stream_out);
?>