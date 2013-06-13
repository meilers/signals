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

$placeId = $json['placeId'];
$userId = $json['userId'];


$placeId = mysql_escape_string($placeId);
$userId = mysql_escape_string($userId);



$sql = "UPDATE users SET placeId = $placeId, placeEnterTime = UTC_TIMESTAMP() WHERE userId = $userId";

$query = mysql_query($sql);

if ($query)
{
	// LOG CHECKIN
	$sql = "SELECT uc.userId
			FROM users_checkins AS uc
			WHERE uc.userId = $userId AND uc.checkinTime >= DATE_SUB(NOW(),INTERVAL 12 HOUR)";
	$query = mysql_query($sql);

	if (mysql_num_rows($query) == 0)
	{
		$sql = "INSERT INTO users_checkins (userId, placeId, checkinTime) VALUES ($userId, $placeId, CURRENT_TIMESTAMP())";
		$query = mysql_query($sql);
	}
	
	echo json_encode(array( 'ok' => '1' ));
}
else {
	echo json_encode(array( 'ok' => '0', 'error' => '1' ));	
}

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

?>
