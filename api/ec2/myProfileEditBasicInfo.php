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
$username = $json['username'];
$sex = $json['sex'];
$interestedIn = $json['interestedIn'];
$wantedMinAge = $json['wantedMinAge'];
$wantedMaxAge = $json['wantedMaxAge'];
$lookingFor = $json['lookingFor'];
$birthday = $json['birthday'];

$userId = mysql_escape_string($userId);
$username = mysql_real_escape_string($username);
$sex = mysql_real_escape_string($sex);
$interestedIn = mysql_real_escape_string($interestedIn);
$wantedMinAge = mysql_real_escape_string($wantedMinAge);
$wantedMaxAge = mysql_real_escape_string($wantedMaxAge);
$lookingFor = mysql_real_escape_string($lookingFor);
$birthday = mysql_real_escape_string($birthday);


$sql = "UPDATE users SET username = '$username', sex = $sex, interestedIn = $interestedIn, wantedMinAge = $wantedMinAge, wantedMaxAge = $wantedMaxAge, lookingFor = $lookingFor, birthdate = '$birthday'
		WHERE userId = $userId";

$query = mysql_query($sql);

if ($query)
{
	echo json_encode(array( 'ok' => '1' ));
}
else {
	echo json_encode(array( 'ok' => '0', 'error' => '1' ));	
}

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
}

?>
