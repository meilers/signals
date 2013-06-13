
<?php

header("Content-type: application/json");

$con = mysql_connect($server = "localhost",$username = "signals_db_user",$password = "Pritct123");

if (!$con)
{
	echo "Failed to make connection.";
	exit;
}

$db = mysql_select_db("signals_db");

if (!$db)
{
	echo "Failed to select db.";
	exit;
}


// NE PAS ENLEVER, pour UNICODE
mysql_set_charset('utf8',$link);

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
