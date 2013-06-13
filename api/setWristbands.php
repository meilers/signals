<?php

// Connect to the database(host, username, password)
$con = mysql_connect($server = "mysql-shared-02.phpfog.com",$username = "Custom App-34204",$password = "TIKi1234");

if (!$con)
{
	echo "Failed to make connection.";
	exit;
}

$db = mysql_select_db("signals_phpfogapp_com");

if (!$db)
{
	echo "Failed to select db.";
	exit;
}

// NE PAS ENLEVER, pour UNICODE
mysql_set_charset('utf8',$con);
date_default_timezone_set('America/New_York'); 	

$userId = $_POST['uId'];
$wristbands = $_POST['w'];

$userId = mysql_escape_string($userId);
$wristbands = mysql_escape_string($wristbands);

$sql = "UPDATE users SET wristbands = '$wristbands' WHERE userId = $userId";
	
$query = mysql_query($sql);

if ($query)
{

		
	echo json_encode(array( 'ok' => '1' ));	

}
else {

	echo json_encode(array( 'ok' => '0' ));	
}


mysql_close($con);
?>
