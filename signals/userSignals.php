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
$signalsIn = $json['signalsIn'];

$userId = mysql_escape_string($userId);
$signalsIn = mysql_escape_string($signalsIn);


// GET PEOPLE YOU BLOCKED
$peopleBlocked = array();
$sql = "SELECT ub.userBlockedId FROM users_blocks as ub, users as u
            WHERE u.userId = $userId AND ub.userId = u.userId";	
        
$query = mysql_query($sql); 

while ($row=mysql_fetch_array($query)) 
{
    $peopleBlocked[$row['userBlockedId']] = 1;
}



if( $signalsIn == 1 )
    // Get users who liked you and that haven't been blocked by you or that have blocked you
    $sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, ul.likeTime FROM users AS u, users_likes AS ul, places AS p
        WHERE ul.userId = u.userId AND ul.userLikedId = $userId AND ul.placeId = p.placeId 
        AND (u.userId) NOT IN
        ( SELECT ub.userId FROM users_blocks as ub 
          WHERE ub.userBlockedId = $userId )";
else
    // Get users who you liked
    $sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, ul.likeTime FROM users AS u, users_likes AS ul, places AS p
        WHERE ul.userLikedId = u.userId AND ul.userId = $userId AND ul.placeId = p.placeId 
        AND (u.userId) NOT IN
        ( SELECT ub.userId FROM users_blocks as ub 
          WHERE ub.userBlockedId = $userId )";
        
        
$query = mysql_query($sql);
$usersLiked  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
    
        $isBlocked = 0;
    
        if( array_key_exists($row['userId'], $peopleBlocked) )
            $isBlocked = 1;
        
        $usersLiked[]=array(
            'uid' => $row['userId'],
            'fid' => $row['fid'],
            'un' => $row['username'],
            'n' => $row['name'],
            'likeTime' => $row['likeTime'],
            'isBlocked' => $isBlocked  
        );
    }
}


if( $signalsIn == 1 )
// Get users who messaged you and that haven't been blocked by you or that have blocked you
    $sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, um.messageType, um.messageTime FROM users as u, users_messages AS um, places AS p
            WHERE um.userId = u.userId AND um.userMessagedId = $userId AND um.placeId = p.placeId 
            AND (u.userId) NOT IN
            ( SELECT ub.userId FROM users_blocks as ub 
              WHERE ub.userBlockedId = $userId )";
else
    $sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, um.messageType, um.messageTime FROM users as u, users_messages AS um, places AS p
            WHERE um.userMessagedId = u.userId AND um.userId = $userId AND um.placeId = p.placeId 
            AND (u.userId) NOT IN
            ( SELECT ub.userId FROM users_blocks as ub 
              WHERE ub.userBlockedId = $userId )";


$query = mysql_query($sql);
$usersMessaged  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
            
        $isBlocked = 0;
    
        if( array_key_exists($row['userId'], $peopleBlocked) )
            $isBlocked = 1;
            
        $usersMessaged[]=array(
            'uid' => $row['userId'],
            'fid' => $row['fid'],
            'un' => $row['username'],
            'n' => $row['name'],
            'msgType' => $row['messageType'],
            'msgTime' => $row['messageTime'],
            'isBlocked' => $isBlocked  
        );
    }
}


// Get users who messaged you and that haven't blocked you
if( $signalsIn == 1 )
    $sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, um.message, um.messageTime FROM users as u, users_pers_messages AS um, places AS p
            WHERE um.userId = u.userId AND um.userPersMessagedId = $userId AND um.placeId = p.placeId 
            AND (u.userId) NOT IN
            ( SELECT ub.userId FROM users_blocks as ub 
              WHERE ub.userBlockedId = $userId )";
else
    $sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, um.message, um.messageTime FROM users as u, users_pers_messages AS um, places AS p
            WHERE um.userPersMessagedId = u.userId AND um.userId = $userId AND um.placeId = p.placeId 
            AND (u.userId) NOT IN
            ( SELECT ub.userId FROM users_blocks as ub 
              WHERE ub.userBlockedId = $userId )";


$query = mysql_query($sql);
$usersPersMessaged  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
            
        $isBlocked = 0;
    
        if( array_key_exists($row['userId'], $peopleBlocked) )
            $isBlocked = 1;
            
        $usersPersMessaged[]=array(
            'uid' => $row['userId'],
            'fid' => $row['fid'],
            'un' => $row['username'],
            'n' => $row['name'],
            'msg' => $row['message'],
            'msgTime' => $row['messageTime'],
            'isBlocked' => $isBlocked  
        );
    }
}


$usersSignaled =array(
            'ul' => $usersLiked,
            'um' => $usersMessaged,
            'upm' => $usersPersMessaged
        );


echo json_encode(array( 'r'  =>  $usersSignaled, 'ok' => '1' ));


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
