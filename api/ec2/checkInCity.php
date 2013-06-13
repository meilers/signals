<?php
 
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

$uid = $_POST['uid'];
$city = $_POST['c'];
$state = $_POST['s'];
$country = $_POST['co'];


// prevent sql injection
$uid = mysql_real_escape_string($uid);
$city = mysql_real_escape_string($city);
$state = mysql_real_escape_string($state);
$country = mysql_real_escape_string($country);


$cityId = 1;
$hasPlaces = 0;
	
$sql = "SELECT c.cityId, c.hasPlaces FROM cities AS c WHERE c.city = '$city' AND c.state = '$state' AND c.country = '$country'";	
		
$query = mysql_query($sql);
		
if (mysql_num_rows($query) > 0)
{
    $row=mysql_fetch_array($query);
    
    $cityId = $row['cityId'];
    $hasPlaces = $row['hasPlaces'];
}
else
{
    $sql = "INSERT INTO cities_requested (userId, city, state, country) VALUES ($uid, '$city', '$state', '$country')";
    
    $query = mysql_query($sql);
        
        
    $sql = "INSERT INTO cities (city, state, country) VALUES ('$city', '$state', '$country')";
    
    $query = mysql_query($sql);
    
    if($query)
        $cityId = mysql_insert_id();

}
 
$sql = "UPDATE users SET cityId = $cityId, online = 1 WHERE userId = $uid";
        
$query = mysql_query($sql);
   
                
if ($query)
{
                    
            $json=array(
                'cid' => $cityId,
                'chp' => $hasPlaces
                         );
                
            echo json_encode(array( 'r'  =>  $json, 'ok' => '1' ));
}
else
{
    echo json_encode(array( 'ok' => '0' ));	
}



mysql_close($con);
?>
