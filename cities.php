

<?php

header("Content-type: application/json");

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
