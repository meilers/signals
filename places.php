 <?php
header("Content-type: application/json");


$services_json = json_decode(getenv("VCAP_SERVICES"),true);
$mysql_config = $services_json["mysql-5.1"][0]["credentials"];

$username = $mysql_config["username"];
$password = $mysql_config["password"];
$hostname = $mysql_config["hostname"];
$port = $mysql_config["port"];
$db = $mysql_config["name"];

$link = mysql_connect("$hostname:$port", $username, $password) OR die (mysqli_connect_error() );

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
mysql_set_charset('utf8',$link);


$jsonString = file_get_contents('php://input');
$jsonString = EncodeToUTF8($jsonString);
$json = json_decode($jsonString, true);

$userId = $json['uid'];
$city = $json['c'];
$state = $json['s'];
$country = $json['co'];
$latitude = $json['lat'];
$longitude = $json['lng'];
$radius = floatval($json['rad']);

$userId = mysql_escape_string($userId);
$city = mysql_escape_string($city);
$state = mysql_escape_string($state);
$country = mysql_escape_string($country);
$latitude = mysql_escape_string($latitude);
$longitude = mysql_escape_string($longitude);
$radius = mysql_escape_string($radius);

/*
$userId = 34;
$city = 'Montreal';
$state = 'Quebec';
$country = 'Canada';
$latitude = '45.520000';
$longitude = '-73.58';
$radius = 0;
*/

$radius = intval($radius);

if( $radius == 1 )
    $radius = 0.5;
else if( $radius == 2 )
    $radius = 2.0;
else if( $radius == 3 )
    $radius = 5.0;



$sql = "SELECT cityId FROM cities WHERE city = '$city' AND state = '$state' AND country = '$country'";	
		
$query = mysql_query($sql);
$row=mysql_fetch_array($query);
$cityId = $row['cityId'];
            
                  
	$sql = "SELECT placeId, genre, name, address, latitude, longitude, COUNT(placeId) FROM
			(
			SELECT p.placeId, p.genre, p.name, p.address, p.latitude, p.longitude
			FROM places AS p
			WHERE p.cityId = $cityId
            
			UNION ALL
			
            SELECT p.placeId, p.genre, p.name, p.address, p.latitude, p.longitude
			FROM places AS p, users_places as up, users as u, (SELECT sex, orientation FROM users WHERE userId = $userId) AS me
			WHERE p.cityId = $cityId AND p.placeId = up.placeId AND up.userId = u.userId 
            
            AND ( ((me.sex=1 AND u.sex=2 OR me.sex=2 AND u.sex=1) AND u.orientation = 1 AND me.orientation = 1)
                OR ((me.sex=1 AND u.sex=1 OR me.sex=2 AND u.sex=2) AND u.orientation = 2 AND me.orientation = 2) )
                
			) AS result
			GROUP BY placeId
			ORDER BY name";			


            
$query = mysql_query($sql);

$json  = array();

$totalUsers = 0;

// If we find a match, create an array of data, json_encode it and echo it out
if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
    	$nUsers = $row['COUNT(placeId)'] - 1;
		$totalUsers = $totalUsers + $nUsers;
		
        $distance = distance($latitude, $longitude, $row['latitude'], $row['longitude'], "k");
        
        if( $distance <= $radius || $radius==0 ) 
        {
            $json[]=array(
                'pid' => $row['placeId'],
                'g' => $row['genre'],
                'n' => $row['name'],
                'a' => $row['address'],
                'tu' => (string)$nUsers,
                'd' => sprintf("%.2f", $distance),
                'rat' => 5.0
            );
        }

    }
	
    $sql = "UPDATE users SET last_active = NOW() WHERE userId = $userId";
    $query = mysql_query($sql);
    
    if($query)
        echo json_encode(array( 'r'  =>  $json, 'nuc' => (string)$totalUsers, 'ok' => '1'));
    else
        myError();
}
else {

	myError();

}

mysql_close($link);




function myError()
{
    echo json_encode(array( 'ok2' => '0'));
}



function EncodeToUTF8( $sStr )
{
    try
    {
        mb_substitute_character("none");// So nonconvertible characters are stripped rather than replaced with a "?".
        if ( ($sStr = mb_convert_encoding($sStr, "UTF-8", mb_detect_encoding($sStr, "UTF-8, ISO-8859-1, GBK, JIS, eucjp-win, sjis-win, ASCII,Windows-1252", true))) === false ) throw new Exception();
    }
    catch (Exception $e){
        $sGood[] = 10; //nl
        $sGood[] = 13; //cr
        $sNewstr = "";

        for($iX = 32;$iX < 127;$iX++) $sGood[] = $iX;
            $iLen = mb_strlen($sStr);

        for($iX = 0;$iX < $iLen; $iX++)
        {
            if(in_array(ord($sStr[$iX]), $sGood))
            {
                $sNewstr .= $sStr[$iX];
            }
            else{
                $sNewstr .= "&#" . ord($sStr[$iX]) . ";";
            }
        }
        $sStr = $sNewstr;
    }

    return $sStr;
}


function distance($lat1, $lon1, $lat2, $lon2, $unit) { 
  
  $theta = $lon1 - $lon2; 
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta)); 
  $dist = acos($dist); 
  $dist = rad2deg($dist); 
  $miles = $dist * 60 * 1.1515;
  $unit = strtoupper($unit);

  if ($unit == "K") {
    return ($miles * 1.609344); 
  } else if ($unit == "N") {
      return ($miles * 0.8684);
    } else {
        return $miles;
      }
}

?>
