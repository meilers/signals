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


/******* MESSAGES *******/

$sql = "SELECT DISTINCT u.userId, u.username, p.name, p.starMaleId, p.starFemaleId, um.message, um.messageTime, um.seen, u.placeId,
		SUM(um.userMessagedId = $userId AND um.seen = 0) as nbNewMessages,(um.userMessagedId = $userId) AS signalIn 
		FROM users as u, (SELECT * FROM users_messages ORDER BY messageTime DESC) AS um, (SELECT placeId FROM users WHERE userId = $userId) AS me, places AS p
    	WHERE ((um.userId = u.userId AND um.userMessagedId = $userId) OR (um.userMessagedId = u.userId AND um.userId = $userId)) 
		AND um.placeId = p.placeId AND um.placeId = me.placeId

		AND um.messageTime >= date_sub(now(),INTERVAL 12 HOUR)
        
		AND (um.userId) NOT IN
        ( SELECT ub.userId FROM users_blocks as ub 
          WHERE ub.userBlockedId = $userId ) 

		AND (um.userMessagedId) NOT IN
      ( SELECT ub.userId FROM users_blocks as ub 
        WHERE ub.userId = u.userId AND ub.userBlockedId = $userId)

		GROUP BY u.userId ORDER BY um.messageTime DESC";



$query = mysql_query($sql);
$messages  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
    	// CHECK IF STAR
	    $star = 0;
	    if( $row['starMaleId'] == $row['userId'] || $row['starFemaleId'] == $row['userId']  ) 
	        $star = 1;
	
	
        $messages[]=array(
            'userId' => $row['userId'],
            'username' => $row['username'],
			'star' => $star,
			
            'msg' => $row['message'],
            'msgTime' => $row['messageTime'],
			'signalIn' => $row['signalIn'],
			'signalSeen' => $row['seen'],

			'placeId' => $row['placeId'],
			'name' => $row['name'],
			
            'nbNewMsgs' => $row['nbNewMessages']
        );
    }
}
else
{
	$success = 0;
	$error = 1;
}
//$query = mysql_query("UPDATE users_messages SET seen = 1 WHERE userMessagedId = $userId");





if( $success )
	echo json_encode(array( 'r'  =>  $messages, 'ok' => '1' ));
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
