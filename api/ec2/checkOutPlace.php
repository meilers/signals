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

$userId = mysql_escape_string($userId);
$sql = "UPDATE users SET placeId=0 WHERE userId = $userId";

	
$query = mysql_query($sql);

if ($query)
{
	$sql = "DELETE FROM users_votes
	WHERE userVotedId = $userId AND	voteTime BETWEEN DATE_FORMAT(NOW(), '%Y-%m-%d %H:00:00') 
		             AND DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s')";
		
	$query = mysql_query($sql);
	
	$sql = "UPDATE users SET voteId=0 WHERE voteId = $userId";
		
	$query = mysql_query($sql);
	
	
	echo json_encode(array( 'ok' => '1' ));	

}
else {

	echo json_encode(array( 'ok' => '0' ));	
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


mysql_close($con);
?>
