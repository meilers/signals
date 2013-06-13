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
$personId = $json['personId'];

$userId = mysql_escape_string($userId);
$personId = mysql_escape_string($personId);



$sql = "SELECT u.placeId, u.dontApproach, p.name, p.genre,
		IF($userId NOT IN ( SELECT ub.userId FROM users_blocks as ub 
    	WHERE ub.userId = $userId AND ub.userBlockedId = $personId),0,1) AS blocked
		FROM users AS u, places as p WHERE u.userId = $personId AND u.placeId = p.placeId";
$query = mysql_query($sql);

if( mysql_num_rows($query) > 0 )
{
	$rowPerson=mysql_fetch_array($query);
	

	/******* MESSAGES *******/

	// Get users who messaged you and that haven't been blocked by you or that have blocked you
	$sql = "SELECT DISTINCT p.name, um.message, um.messageTime, um.seen, 
			(um.userMessagedId = $userId) AS signalIn 
			FROM users_messages AS um, places AS p
	        WHERE ((um.userId = $personId AND um.userMessagedId = $userId) OR (um.userMessagedId = $personId AND um.userId = $userId)) 
	
			AND um.placeId = p.placeId
			
			AND (um.userId) NOT IN
	        ( SELECT ub.userId FROM users_blocks as ub 
	          WHERE ub.userBlockedId = $userId )
	
			AND (um.userMessagedId) NOT IN
	        ( SELECT ub.userId FROM users_blocks as ub 
	          WHERE ub.userId = $personId AND ub.userBlockedId = $userId)
	";

	$query = mysql_query($sql);
	$messages  = array();

	if ($query)
	{
	    while ($row=mysql_fetch_array($query)) 
	    {
	        $messages[]=array(
	            'name' => $row['name'],
	            'msg' => $row['message'],
	            'msgTime' => $row['messageTime'],
				'signalIn' => $row['signalIn'],
	            'signalSeen' => $row['seen']
	        );
	    }

		$json=array(
	        'placeId' => $rowPerson['placeId'],
			'name' => $rowPerson['name'],
			'genre' => $rowPerson['genre'],
			'dontApproach' => $rowPerson['dontApproach'],
			'blocked' => $rowPerson['blocked'],
	        'messages' => $messages
	    );
	
		echo json_encode(array( 'r'  =>  $json, 'ok' => '1' ));
	}
	else
		echo json_encode(array( 'ok' => '0', 'error' => '1' ));

	$query = mysql_query("UPDATE users_messages SET seen = 1 WHERE userMessagedId = $userId");
}
else
	echo json_encode(array( 'ok' => '0', 'error' => '0' ));


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
