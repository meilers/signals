

<?php

header("Content-type: application/json");

$services_json = json_decode(getenv("VCAP_SERVICES"),true);
$mysql_config = $services_json["mysql-5.1"][0]["credentials"];

$username = $mysql_config["Custom App-34204"];
$password = $mysql_config["TIKi1234"];
$hostname = $mysql_config["mysql-shared-02.phpfog.com"];
//$port = $mysql_config["port"];
$db = $mysql_config["signals_phpfogapp_com"];

//$link = mysql_connect("$hostname:$port", $username, $password);
$link = mysql_connect("$hostname", $username, $password);

if (!$link)
{
	echo "Failed to make connection.";
	exit;
}

$db_selected = mysql_select_db($db, $link);

if (!$db_selected)
{
	echo "Failed to select db.";
	exit;
}


// NE PAS ENLEVER, pour UNICODE
mysql_set_charset('utf8',$con);

$sql = "SELECT c.city, c.state, c.country FROM cities AS c ORDER BY c.city";

$query = mysql_query($sql);

$json  = array();

// If we find a match, create an array of data, json_encode it and echo it out
if (mysql_num_rows($query) > 0)
{
    while ($row=mysql_fetch_array($query)) 
    {
        $json[]=array(
			'c' => $row['city'],
            's' => $row['state'],
			'co' => $row['country']
        );			
    }
		
	echo json_encode(array( 'r'  =>  $json, 'ok' => '1' ));
}
else
{
	echo json_encode(array('ok' => '0'));	
}


?>
