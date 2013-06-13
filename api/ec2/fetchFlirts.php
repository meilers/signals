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

$success = 1;
$error = 0;


/******* BLOCKS *******/
$peopleBlocked = array();
$sql = "SELECT ub.userBlockedId FROM users_blocks as ub, users as u
            WHERE u.userId = $userId AND ub.userId = u.userId";	
        
$query = mysql_query($sql); 

while ($row=mysql_fetch_array($query)) 
{
    $peopleBlocked[$row['userBlockedId']] = 1;
}


/******* FLIRTS *******/
    
// Get users who flirted you and that haven't been blocked by you or that have blocked you
$sql = "SELECT DISTINCT u.userId, u.username, p.placeId, p.name, p.starMaleId, p.starFemaleId,  uf.flirtTime, uf.seen
		FROM users AS u, (SELECT placeId FROM users WHERE userId = $userId) AS me, users_flirts AS uf, places AS p
	    WHERE uf.userId = u.userId AND uf.userFlirtedId = $userId
		AND uf.placeId = p.placeId AND uf.placeId = me.placeId
		AND uf.flirtTime >= date_sub(now(),INTERVAL 12 HOUR)
	    AND (u.userId) NOT IN
	    ( SELECT ub.userId FROM users_blocks as ub 
	      WHERE ub.userBlockedId = $userId ) ORDER BY uf.flirtTime DESC";

$query = mysql_query($sql);
$flirts  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
        // CHECK IF STAR
	    $star = 0;
	    if( $row['starMaleId'] == $row['userId'] || $row['starFemaleId'] == $row['userId']  ) 
	        $star = 1;
	
	    
        $flirts[]=array(
            'userId' => $row['userId'],
            'username' => $row['username'],
			'star' => $star,
			'placeId' => $row['placeId'],
            'name' => $row['name'],
            'flirtTime' => $row['flirtTime'],
            'signalSeen' => $row['seen']
        );
    }
}
else
{
	$success = 0;
	$error = 1;
}
$query = mysql_query("UPDATE users_flirts SET seen = 1 WHERE userFlirtedId = $userId");





if( $success )
	echo json_encode(array( 'r'  =>  $flirts, 'ok' => '1' ));
else
	echo json_encode(array( 'ok' => '0', 'error' => $error ));

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
