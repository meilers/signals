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


/******* VOTES *******/
    
// Get users who voteed you and that haven't been blocked by you or that have blocked you
$sql = "SELECT DISTINCT u.userId, u.username, p.placeId, p.name, p.starMaleId, p.starFemaleId, uvo.voteTime, uvo.seen
		FROM users AS u, (SELECT placeId FROM users WHERE userId = $userId) AS me, users_votes AS uvo, places AS p
	    WHERE uvo.userId = u.userId AND uvo.userVotedId = $userId
		AND uvo.placeId = p.placeId AND uvo.placeId = me.placeId
		AND uvo.voteTime >= date_sub(now(),INTERVAL 12 HOUR)
	    AND (u.userId) NOT IN
	    ( SELECT ub.userId FROM users_blocks as ub 
	      WHERE ub.userBlockedId = $userId ) ORDER BY uvo.voteTime DESC";

$query = mysql_query($sql);
$votes  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
        // CHECK IF STAR
	    $star = 0;
	    if( $row['starMaleId'] == $row['userId'] || $row['starFemaleId'] == $row['userId']  ) 
	        $star = 1;
	
	    
        $votes[]=array(
            'userId' => $row['userId'],
            'username' => $row['username'],
			'star' => $star,
			'placeId' => $row['placeId'],
            'name' => $row['name'],
            'voteTime' => $row['voteTime'],
            'signalSeen' => $row['seen']
        );
    }
}
else
{
	$success = 0;
	$error = 1;
}
$query = mysql_query("UPDATE users_votes SET seen = 1 WHERE userVotedId = $userId");



if( $success )
	echo json_encode(array( 'r'  =>  $votes, 'ok' => '1' ));
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
