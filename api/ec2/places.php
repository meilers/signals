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
mysql_set_charset('utf8',$con);



$jsonString = file_get_contents('php://input');
$jsonString = EncodeToUTF8($jsonString);
$json = json_decode($jsonString, true);

$userId = $json['userId'];
$city = $json['city'];
$state = $json['state'];
$country = $json['country'];
$lat = $json['lat'];
$lon = $json['lng'];
$rad = $json['radius'];
$accuracy = floatval($json['accuracy'])/1000;

$userId = mysql_escape_string($userId);
$city = mysql_escape_string($city);
$state = mysql_escape_string($state);
$country = mysql_escape_string($country);
$lat = mysql_escape_string($lat);
$lon = mysql_escape_string($lon);
$radius = mysql_escape_string($rad);
$accuracy = mysql_escape_string($accuracy);


$R = 6371;  	// earth's radius, km

// first-cut bounding box (in degrees)
$maxLat = $lat + rad2deg(($rad+$accuracy)/$R);
$minLat = $lat - rad2deg(($rad+$accuracy)/$R);
// compensate for degrees longitude getting smaller with increasing latitude
$maxLon = $lon + rad2deg(($rad+$accuracy)/$R/cos(deg2rad($lat)));
$minLon = $lon - rad2deg(($rad+$accuracy)/$R/cos(deg2rad($lat)));





$nbUsersMale = 0;
$nbUsersFemale = 0;



if( $city != "" && $state != ""  && $country != "" )
{
                  
	$sql = "SELECT p.placeId, p.genre, p.name, p.address, latitude, longitude, SUM(IF(u.sex=1,1,0)) as a, SUM(IF(u.sex=2,1,0)) as b
				FROM 
					cities as c
				INNER JOIN
					places AS p 
					ON p.cityId = c.cityId AND c.city = '$city' AND c.state = '$state' AND c.country = '$country' 
					AND p.latitude > $minLat AND p.latitude < $maxLat AND p.longitude > $minLon AND p.longitude < $maxLon
				INNER JOIN 
					(SELECT sex, wantedMinAge, wantedMaxAge, birthdate, interestedIn FROM users WHERE userId = $userId) AS me
				LEFT JOIN
					users as u
					ON u.placeId != 0 AND p.placeId = u.placeId

		        	AND ( (me.interestedIn=u.sex AND u.interestedIn=me.sex) OR (me.interestedIn=3 AND u.interestedIn=me.sex) OR 
						(u.interestedIn=3 AND me.interestedIn=u.sex) OR (u.interestedIn=3 AND u.interestedIn=me.interestedIn) )

					AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= u.wantedMinAge
					AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= u.wantedMaxAge
					AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= me.wantedMinAge
					AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= me.wantedMaxAge

					AND (u.userId) NOT IN
		                ( SELECT ub.userId FROM users_blocks as ub 
		                  WHERE ub.userBlockedId = $userId )

				GROUP BY p.placeId";			
}
else
{
	$sql = "SELECT p.placeId, p.genre, p.name, p.address, latitude, longitude, SUM(IF(u.sex=1,1,0)) as a, SUM(IF(u.sex=2,1,0)) as b
				FROM 
					places AS p 
				INNER JOIN 
					(SELECT sex, wantedMinAge, wantedMaxAge, birthdate, interestedIn FROM users WHERE userId = $userId) AS me
					ON p.latitude > $minLat AND p.latitude < $maxLat AND p.longitude > $minLon AND p.longitude < $maxLon
				LEFT JOIN
					users as u
					ON u.placeId != 0 AND p.placeId = u.placeId

		        	AND ( (me.interestedIn=u.sex AND u.interestedIn=me.sex) OR (me.interestedIn=3 AND u.interestedIn=me.sex) OR 
						(u.interestedIn=3 AND me.interestedIn=u.sex) OR (u.interestedIn=3 AND u.interestedIn=me.interestedIn) )

					AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= u.wantedMinAge
					AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= u.wantedMaxAge
					AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= me.wantedMinAge
					AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= me.wantedMaxAge

					AND (u.userId) NOT IN
		                ( SELECT ub.userId FROM users_blocks as ub 
		                  WHERE ub.userBlockedId = $userId )

				GROUP BY p.placeId";	
}
            
$query = mysql_query($sql);

$json  = array();

$totalUsers = 0;

// If we find a match, create an array of data, json_encode it and echo it out
    while ($row=mysql_fetch_array($query)) 
    {
    	$nUsers = $row['COUNT(placeId)'] - 1;
		$totalUsers = $totalUsers + $nUsers;
        
		
        $distance = distance($latitude, $longitude, $row['latitude'], $row['longitude'], "k");
        
        if( $distance <= $radius+$accuracy || $radius==0 ) 
        {        
            $json[]=array(
                'placeId' => $row['placeId'],
                'genre' => $row['genre'],
                'name' => $row['name'],
                'address' => $row['address'],
				'nbUsersMale' => $row['a'],
				'nbUsersFemale' => $row['b'],
                'lat' => $row['latitude'],
                'lng' => $row['longitude']
            );
        }
    }
        
	echo json_encode(array( 'r'  =>  $json, 'ok' => '1'));


mysql_close($con);




function myError()
{
    echo json_encode(array( 'ok' => '0', 'error' => '1'));
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
