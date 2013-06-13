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
$candidateId = $json['personId'];
$rvId = $json['rvId'];
$placeSelected = $json['placeSelected'];
$timeSelected = $json['timeSelected'];

$userId = mysql_escape_string($userId);
$candidateId = mysql_escape_string($candidateId);
$rvId = mysql_escape_string($rvId);
$placeSelected = mysql_escape_string($placeSelected);
$timeSelected = mysql_escape_string($timeSelected);

mysql_query("BEGIN");

$sql = "INSERT INTO users_rendezvous_confirmed (userId, candidateId, createTime, placeId, timeSlot) 
		VALUES ($userId, $candidateId, CURRENT_TIMESTAMP(), $placeSelected, $timeSelected )";
		
$result1 = mysql_query($sql);
$confirmedRvId = mysql_insert_id();

$sql = "DELETE FROM users_rendezvous_potential
		WHERE id=$rvId";
		
$result2 = mysql_query($sql);
	
if ( $result1 && $result2 )
{
	mysql_query("COMMIT"); 
	
	$sql = "SELECT urc.id, urc.candidateId, urc.createTime, urc.placeId, urc.timeSlot, 
			p.name, p.genre, p.address, p.latitude, p.longitude, 
			u.userId, u.username, u.sex, u.interestedIn, u.wantedMinAge, u.wantedMaxAge, u.birthdate, u.lookingFor
			FROM users_rendezvous_confirmed AS urc, places AS p, users AS u
			WHERE urc.id = $confirmedRvId AND urc.placeId = p.placeId AND urc.candidateId = u.userId";
	$query = mysql_query($sql);
	$row=mysql_fetch_array($query);
	
	$person = array(
	    'userId' => $row['userId'],
        'username' => $row['username'],
		'sex' => $row['sex'],
		'interestedIn' => $row['interestedIn'],
        'birthday' => $row['birthdate'],
        'lookingFor' => $row['lookingFor']
	);
	
	$place = array(
		'placeId' => $row['placeId'],
		'name' => $row['name'],
		'genre' => $row['genre'],
		'address' => $row['address'],
		'lat' => $row['latitude'],
		'lng' => $row['longitude']
	);
	
	$confirmedRendezvous=array(
		'id' => $confirmedRvId,
		'createTime' => $row['createTime'],
		'person' => $person,
		'place' => $place,
		'timeSlot' => $row['timeSlot']
    );


	echo json_encode(array( 'ok' => '1', 'r' => $confirmedRendezvous ));
}
else {
	mysql_query("ROLLBACK");
	
	echo json_encode(array( 'ok' => '0', 'error' => '1' ));	
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
}?>
