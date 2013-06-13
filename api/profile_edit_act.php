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
$username = $json['un'];
$orientation = $json['o'];
$wristBlue = $json['wr_b'];
$wristRed = $json['wr_r'];
$wristYellow = $json['wr_y'];
$wristGreen = $json['wr_g'];

$userId = mysql_escape_string($userId);
$username = mysql_escape_string($username);
$orientation = mysql_escape_string($orientation);
$wristBlue = mysql_escape_string($wristBlue);
$wristRed = mysql_escape_string($wristRed);
$wristYellow = mysql_escape_string($wristYellow);
$wristGreen = mysql_escape_string($wristGreen);

$sql = "UPDATE users SET username = '$username', orientation = '$orientation', wristBlue = '$wristBlue', wristRed = '$wristRed', wristYellow = '$wristYellow', wristGreen= '$wristGreen' WHERE userId = $userId";
	
$query = mysql_query($sql);

if ($query)
{
	echo json_encode(array( 'ok' => '1' ));	

}
else {

	echo json_encode(array( 'ok' => '0' ));	
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
