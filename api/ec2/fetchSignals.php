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
$nbNewFlirts = 0;
$nbNewMessages = 0;
$nbNewVotes = 0;
$nbNewVisits = 0;
   
$errorFlirts = 0;
$errorMessages = 0;
$errorVotes = 0;
$errorVisits = 0;
 

// Flirts
$sql = "SELECT *
		FROM users_flirts AS uf
        WHERE uf.userFlirtedId = $userId AND uf.seen = 0
		AND date(uf.flirtTime) between date_sub(now(),INTERVAL 1 WEEK) and now()
        AND (uf.userId) NOT IN
        ( SELECT ub.userId FROM users_blocks as ub 
          WHERE ub.userBlockedId = $userId )";

$query = mysql_query($sql);

if ($query)
	$nbNewFlirts = mysql_num_rows($query);
else
	$errorFlirts = 1;


// Msgs
$sql = "SELECT *
		FROM users_messages AS um
        WHERE um.userMessagedId = $userId AND um.seen = 0
		AND date(um.messageTime) between date_sub(now(),INTERVAL 1 WEEK) and now()
        AND (um.userId) NOT IN
        ( SELECT ub.userId FROM users_blocks as ub 
          WHERE ub.userBlockedId = $userId )";

$query = mysql_query($sql);


if ($query)
	$nbNewMsgs = mysql_num_rows($query);
else
	$errorMessages = 1;


// Votes
$sql = "SELECT *
		FROM users_votes AS uv
        WHERE uv.userVotedId = $userId AND uv.seen = 0
		AND date(uv.voteTime) between date_sub(now(),INTERVAL 1 WEEK) and now()
        AND (uv.userId) NOT IN
        ( SELECT ub.userId FROM users_blocks as ub 
          WHERE ub.userBlockedId = $userId )";

$query = mysql_query($sql);

if ($query)
	$nbNewVotes = mysql_num_rows($query);
else
	$errorVotes = 1;


// Visits
$sql = "SELECT *
		FROM users_visits AS uvi
        WHERE uvi.userVisitedId = $userId AND uvi.seen = 0
		AND date(uvi.visitTime) between date_sub(now(),INTERVAL 1 WEEK) and now()
        AND (uvi.userId) NOT IN
        ( SELECT ub.userId FROM users_blocks as ub 
          WHERE ub.userBlockedId = $userId )";

$query = mysql_query($sql);

if ($query)
	$nbNewVisits = mysql_num_rows($query);
else
	$errorVisits = 1;



if (!$errorFlirts && !$errorMessages && !$errorVotes && !$errorVisits )
{
	$newSignals=array(
        'nbNewFlirts' => $nbNewFlirts,
        'nbNewMsgs' => $nbNewMsgs,
		'nbNewVotes' => $nbNewVotes,
		'nbNewVisits' => $nbNewVisits
    );


	echo json_encode(array( 'r'  =>  $newSignals, 'ok' => '1' ));
}
else
{
	if ($errorFlirts)
		echo json_encode(array( 'ok' => '0', 'error' => 1 ));
	else if ($errorMessages)
		echo json_encode(array( 'ok' => '0', 'error' => 2 ));
	else if ($errorVotes)
		echo json_encode(array( 'ok' => '0', 'error' => 3 ));
	else if ($errorVisits)
		echo json_encode(array( 'ok' => '0', 'error' => 4 ));
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
