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
$rvId = $json['rvId'];
$placesSelected = json_decode($json['placesSelected'], true);
$timesSelected = json_decode($json['timesSelected'], true);

$userId = mysql_escape_string($userId);
$rvId = mysql_escape_string($rvId);
array_walk_recursive( $placesSelected, 'mysql_real_escape_string' );
array_walk_recursive( $timesSelected, 'mysql_real_escape_string' );

$places = array(0,0,0,0,0,0,0,0,0,0);

for ($i = 0; $i < count($placesSelected); ++$i) {
  	$places[$i] = $placesSelected[$i];
}

$sql = "UPDATE users_rendezvous_potential
		SET placeId0 = $places[0], 
		placeId1 = $places[1],
		placeId2 = $places[2],
		placeId3 = $places[3],
		placeId4 = $places[4],
		placeId5 = $places[5],
		placeId6 = $places[6],
		placeId7 = $places[7],
		placeId8 = $places[8],
		placeId9 = $places[9],
		time0 = $timesSelected[0],
		time1 = $timesSelected[1],
		time2 = $timesSelected[2],
		time3 = $timesSelected[3],
		time4 = $timesSelected[4],
		time5 = $timesSelected[5],
		time6 = $timesSelected[6],
		time7 = $timesSelected[7],
		time8 = $timesSelected[8],
		time9 = $timesSelected[9],
		userAnswered = 1
		WHERE id = $rvId
		";
		
$query = mysql_query($sql);
	
if ($query)
{
	echo json_encode(array( 'ok' => '1' ));
}
else {
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
