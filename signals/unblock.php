 <?php
header("Content-type: application/json");

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
date_default_timezone_set('America/New_York'); 	

$jsonString = file_get_contents('php://input');
$jsonString = EncodeToUTF8($jsonString);
$json = json_decode($jsonString, true);

$userId = $json['uid'];
$personId = $json['personId'];

$userId = mysql_escape_string($userId);
$personId = mysql_escape_string($personId);

$sql = "DELETE FROM users_blocks WHERE userId = $userId AND userBlockedId = $personId";

$query = mysql_query($sql);

if ($query)
{
	echo json_encode(array( 'ok' => '1' ));	
}
else {

	echo json_encode(array( 'ok' => '0' ));	
}



mysql_close($con);
?>
