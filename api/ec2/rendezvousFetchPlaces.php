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
$personId = $json['personId'];
$cityId = $json['cityId'];
$rvId = $json['rvId'];
$placesNotWantedIds = json_decode($json['placesSelected'], true);

$userId = mysql_escape_string($userId);
$personId = mysql_escape_string($personId);
$cityId = mysql_escape_string($cityId);
$rvId = mysql_escape_string($rvId);
array_walk_recursive( $placesNotWantedIds, 'mysql_real_escape_string' );

if( sizeof($placesNotWantedIds)>0)
	$placesNotWantedIds = implode(',',$placesNotWantedIds);  
else
	$placesNotWantedIds = 0;

$sql = "SELECT placeId, name, genre, address, latitude, longitude FROM
		(
			(
			SELECT u.placeId, p.name, p.genre, p.address, p.latitude, p.longitude, 0, 1 AS priority
			FROM users AS u, places AS p
			WHERE u.userId = $userId AND u.placeId != 0 AND u.placeId = p.placeId
			)
			UNION
			(
			SELECT u.placeId, p.name, p.genre, p.address, p.latitude, p.longitude, 0, 2 AS priority
			FROM users AS u, places AS p
			WHERE u.userId = $personId AND u.placeId != 0 AND u.placeId = p.placeId
			)
			UNION
			(	
			SELECT uc.placeId, p.name, p.genre, p.address, p.latitude, p.longitude, COUNT(*) AS cnt, 3 AS priority
			FROM places AS p, users_checkins AS uc
			WHERE uc.userId = $userId AND uc.placeId = p.placeId AND p.cityId = $cityId AND p.genre != 1
			GROUP BY uc.placeId ORDER BY cnt DESC LIMIT 3
			)
			UNION
			(
			SELECT uc.placeId, p.name, p.genre, p.address, p.latitude, p.longitude, COUNT(*) AS cnt, 4 AS priority
			FROM places AS p, users_checkins AS uc
			WHERE uc.userId = $personId AND uc.placeId = p.placeId AND p.cityId = $cityId AND p.genre != 1
			GROUP BY uc.placeId ORDER BY cnt DESC LIMIT 3
			)
			UNION
			(
			SELECT p.placeId, p.name, p.genre, p.address, p.latitude, p.longitude, 0, 5 AS priority
			FROM places AS p
			WHERE p.cityId = $cityId
			ORDER BY p.genre DESC
			)	
		) AS result
		WHERE placeId NOT IN ($placesNotWantedIds)
		GROUP BY placeId
		ORDER BY priority, genre DESC
		LIMIT 10
		";
		
$query = mysql_query($sql);

$places = array();
$itr = 0;

while ($rowPlace=mysql_fetch_array($query)) 
{
	$places[$itr] = array(
		'placeId' => $rowPlace['placeId'],
		'name' => $rowPlace['name'],
		'genre' => $rowPlace['genre'],
		'address' => $rowPlace['address'],
		'lat' => $rowPlace['latitude'],
		'lng' => $rowPlace['longitude']
	);
	
	++$itr;
}
	
	/*
$placeId0 = $places[0]['placeId'];
$placeId1 = $places[1]['placeId'];
$placeId2 = $places[2]['placeId'];
$placeId3 = $places[3]['placeId'];
$placeId4 = $places[4]['placeId'];
$placeId5 = $places[5]['placeId'];
$placeId6 = $places[6]['placeId'];
$placeId7 = $places[7]['placeId'];
$placeId8 = $places[8]['placeId'];
$placeId9 = $places[9]['placeId'];

$sql = "UPDATE users_rendezvous_potential
		SET placeId0 = $placeId0, placeId1 = $placeId1, placeId2 = $placeId2, placeId3 = $placeId3, placeId4 = $placeId4, placeId5 = $placeId5, placeId6 = $placeId6, placeId7 = $placeId7, placeId8 = $placeId8, placeId9 = $placeId9 
		WHERE id = $rvId";

$query = mysql_query($sql);
*/

echo json_encode(array( 'ok' => '1', 'r' => $places ));
	
mysql_close($link);





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
}?>
