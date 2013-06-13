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


$city = $_POST['c'];
$country = $_POST['co'];


$city = mysql_escape_string($city);
$country = mysql_escape_string($country);


$sql = "SELECT u.userId FROM users AS u, cities AS c WHERE u.online=1 AND u.cityId = c.cityId AND c.city = '$city' AND c.country = '$country'";		

$result = mysql_query($sql);
    

if (mysql_num_rows($result) > 0)
{
    $num_users = mysql_num_rows($result);
	
    $sql = "SELECT p.placeId FROM places AS p, cities AS c WHERE p.cityId = c.cityId AND c.city = '$city' AND c.country = '$country'";		

    $result = mysql_query($sql);
    
    if (mysql_num_rows($result) > 0)
    {
        $num_places = mysql_num_rows($result);


        $json= array(
				'nuc' => $num_users,
	        	'npc' => $num_places
        );		
            
        echo json_encode(array( 'r'  =>  $json, 'ok' => '1'));
    }
    else
        echo json_encode(array( 'ok' => '0'));	
}
else {
	echo json_encode(array( 'ok' => '0'));	
}

?>
