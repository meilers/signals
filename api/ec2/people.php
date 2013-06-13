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
$placeId = $json['placeId'];
$lat = $json['lat'];
$lon = $json['lng'];
$accuracy = $json['accuracy'];
$rad = $json['radius'];

$userId = mysql_escape_string($userId);
$lat = mysql_escape_string($lat);
$lon = mysql_escape_string($lon);
$placeId = mysql_escape_string($placeId);
$accuracy = mysql_escape_string($accuracy);
$rad = mysql_escape_string($rad);

/*
$userId = 64;
$lat = 45.520000;
$lon = -73.580000;
$placeId = 0;
$accuracy = 0;
$rad = 20;
*/

$jsonPeopleBar  = array();
$jsonPeoplePromoted  = array();

if( $placeId != 0 )
{
    // GET PEOPLE IN BAR
    $sql = "SELECT u.userId, u.username, u.sex, u.interestedIn, u.wantedMinAge, u.wantedMaxAge, u.birthdate, u.lookingFor, p.placeId, p.name, p.starMaleId, p.starFemaleId
            FROM users AS u, (SELECT sex, interestedIn, wantedMinAge, wantedMaxAge, birthdate FROM users WHERE userId = $userId) AS me, places as p
            WHERE u.placeId = $placeId AND p.placeId = $placeId
            
            AND ( (me.interestedIn=u.sex AND u.interestedIn=me.sex) OR (me.interestedIn=3 AND u.interestedIn=me.sex) OR 
				(u.interestedIn=3 AND me.interestedIn=u.sex) OR (u.interestedIn=3 AND u.interestedIn=me.interestedIn) OR (p.starMaleId=u.userId OR p.starFemaleId=u.userId) )
				
			AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= u.wantedMinAge
			AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= u.wantedMaxAge
			AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= me.wantedMinAge
			AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= me.wantedMaxAge
				
			AND (u.userId) NOT IN
                ( SELECT ub.userId FROM users_blocks as ub 
                  WHERE ub.userBlockedId = $userId )
			";	
            
    $query = mysql_query($sql);  
            
    if ($query)
	{        
	    $json  = array();

	    while ($row=mysql_fetch_array($query)) 
	    {
            
			// CHECK IF STAR
		    $star = 0;
		    if( $row['starMaleId'] == $row['userId'] ) 
		        $star = 1;
			else if( $row['starFemaleId'] == $row['userId'] ) 
		        $star = 1;
	
	        $jsonPeopleBar[]=array(
	            'userId' => $row['userId'],
	            'username' => $row['username'],
				'sex' => $row['sex'],
				'interestedIn' => $row['interestedIn'],
	            'birthday' => $row['birthdate'],
	            'lookingFor' => $row['lookingFor'],
	            'star' => $star,
				
				'placeId' => $row['placeId'],
				'name' => $row['name']
	        );
		}
    }
}


$json = array(
    'peopleBar' => $jsonPeopleBar,
    'peoplePromoted' => $jsonPeoplePromoted
);

echo json_encode(array( 'r'  =>  $json, 'ok' => '1' ));



mysql_close($con);


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
