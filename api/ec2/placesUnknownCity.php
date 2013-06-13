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
$latitude = $json['lat'];
$longitude = $json['lng'];
$accuracy = floatval($json['gps_accuracy']);
$radius = floatval($json['gps_radius']);

$userId = mysql_escape_string($userId);
$latitude = mysql_escape_string($latitude);
$longitude = mysql_escape_string($longitude);
$accuracy = mysql_escape_string($accuracy);
$radius = mysql_escape_string($radius);

$radius = intval($radius);

if( $radius == 1 )
    $radius = 0.5;
else if( $radius == 2 )
    $radius = 2.0;
else if( $radius == 3 )
    $radius = 5.0;
    





                  
	$sql = "SELECT placeId, genre, name, address, latitude, longitude, SUM(a), SUM(b) FROM
			(
				SELECT p.placeId, p.genre, p.name, p.address, p.latitude, p.longitude, 0 as a, 0 as b
				FROM places AS p
            
				UNION ALL
			
	            SELECT p.placeId, p.genre, p.name, p.address, p.latitude, p.longitude, count(*) as a, 0 as b
				FROM places AS p, users as u,
					 (SELECT sex, wantedMinAge, wantedMaxAge, birthdate, interestedIn FROM users WHERE userId = $userId) AS me
					
				WHERE u.placeId != 0 AND p.placeId = u.placeId
            
	        	AND ( (me.interestedIn=u.sex AND u.interestedIn=me.sex) OR (me.interestedIn=3 AND u.interestedIn=me.sex) OR 
					(u.interestedIn=3 AND me.interestedIn=u.sex) OR (u.interestedIn=3 AND u.interestedIn=me.interestedIn) )
				
				AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= u.wantedMinAge
				AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= u.wantedMaxAge
				AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= me.wantedMinAge
				AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= me.wantedMaxAge
				
				AND (u.userId) NOT IN
	                ( SELECT ub.userId FROM users_blocks as ub 
	                  WHERE ub.userBlockedId = $userId )
	
				AND u.sex=1	
				GROUP BY p.placeId
				
				UNION ALL
			
	            SELECT p.placeId, p.genre, p.name, p.address, p.latitude, p.longitude, 0 as a, count(*) as b
				FROM places AS p, users as u, 
				     (SELECT sex, wantedMinAge, wantedMaxAge, birthdate, interestedIn FROM users WHERE userId = $userId) AS me
				WHERE u.placeId != 0 AND p.placeId = u.placeId 
            
	        	AND ( (me.interestedIn=u.sex AND u.interestedIn=me.sex) OR (me.interestedIn=3 AND u.interestedIn=me.sex) OR 
					(u.interestedIn=3 AND me.interestedIn=u.sex) OR (u.interestedIn=3 AND u.interestedIn=me.interestedIn) )
					
				AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= u.wantedMinAge
				AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= u.wantedMaxAge
				AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= me.wantedMinAge
				AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= me.wantedMaxAge
					
				AND (u.userId) NOT IN
	                ( SELECT ub.userId FROM users_blocks as ub 
	                  WHERE ub.userBlockedId = $userId )
	
        		AND u.sex=2
				GROUP BY p.placeId
			) AS result
			GROUP BY placeId";		


            
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
             
        if( $distance <= $radius+$accuracy || $radius==0 )
        {
            $json[]=array(
                'placeId' => $row['placeId'],
                'genre' => $row['genre'],
                'name' => $row['name'],
                'address' => $row['address'],
				'nbUsersMale' => $row['SUM(a)'],
				'nbUsersFemale' => $row['SUM(b)'],
                'lat' => $row['latitude'],
                'lng' => $row['longitude']
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

	echo json_encode(array( 'ok' => '0', 'error' => '1'));

}

mysql_close($con);




function myError()
{
    echo json_encode(array( 'ok' => '0'));
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
