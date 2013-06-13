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
$userId = mysql_escape_string($userId);


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
$sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, ul.flirtTime, ul.seen FROM users AS u, users_flirts AS ul, places AS p
    WHERE ul.userId = u.userId AND ul.userFlirtedId = $userId AND ul.placeId = p.placeId 
    AND (u.userId) NOT IN
    ( SELECT ub.userId FROM users_blocks as ub 
      WHERE ub.userBlockedId = $userId )";

$query = mysql_query($sql);
$flirtsIn  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
        // Check checked-in place
        $personId = $row['userId'];
        $sql = "SELECT up.placeId, p.name, p.genre, p.address FROM users_places as up, places as p WHERE up.userId = $personId AND up.placeId = p.placeId";
        $checkedInPlaceId = -1;
        $checkedInPlaceName = "";
        $checkedInPlaceGenre = -1;
        $checkedInPlaceAddress = "";
        $query2 = mysql_query($sql);
        $row2 = mysql_fetch_array($query2);
        if($row2) 
        {
            $checkedInPlaceId = $row2['placeId'];
            $checkedInPlaceName = $row2['name'];
            $checkedInPlaceGenre = $row2['genre'];
            $checkedInPlaceAddress = $row2['address'];
        }
    
        // Check if blocked
        $blocked = 0;
        if( array_key_exists($row['userId'], $peopleBlocked) )
            $blocked = 1;
        
            
        $flirtsIn[]=array(
            'uid' => $row['userId'],
            'fid' => $row['fid'],
            'un' => $row['username'],
            'n' => $row['name'],
            'flirtTime' => $row['flirtTime'],
            'blocked' => $blocked,
            'checkedInPlaceId' => $checkedInPlaceId,
            'checkedInPlaceName' => $checkedInPlaceName,
            'checkedInPlaceGenre' => $checkedInPlaceGenre,
            'checkedInPlaceAddress' => $checkedInPlaceAddress,
            'signalSeen' => $row['seen']
        );
    }
}
$query = mysql_query("UPDATE users_flirts SET seen = 1 WHERE userFlirtedId = $userId");

// Get users who you flirted
$sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, ul.flirtTime, ul.seen FROM users AS u, users_flirts AS ul, places AS p
    WHERE ul.userFlirtedId = u.userId AND ul.userId = $userId AND ul.placeId = p.placeId 
    AND (u.userId) NOT IN
    ( SELECT ub.userId FROM users_blocks as ub 
      WHERE ub.userBlockedId = $userId )";
        
$query = mysql_query($sql);
$flirtsOut  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
        // Check checked-in place
        $personId = $row['userId'];
        $sql = "SELECT up.placeId, p.name, p.genre, p.address FROM users_places as up, places as p WHERE up.userId = $personId AND up.placeId = p.placeId";
        $checkedInPlaceId = -1;
        $checkedInPlaceName = "";
        $checkedInPlaceGenre = -1;
        $checkedInPlaceAddress = "";
        $query2 = mysql_query($sql); 
        $row2 = mysql_fetch_array($query2);
        if($row2) 
        {
            $checkedInPlaceId = $row2['placeId'];
            $checkedInPlaceName = $row2['name'];
            $checkedInPlaceGenre = $row2['genre'];
            $checkedInPlaceAddress = $row2['address'];
        }
    
        // Check if blocked
        $blocked = 0;
        if( array_key_exists($row['userId'], $peopleBlocked) )
            $blocked = 1;
        
            
        $flirtsOut[]=array(
            'uid' => $row['userId'],
            'fid' => $row['fid'],
            'un' => $row['username'],
            'n' => $row['name'],
            'flirtTime' => $row['flirtTime'],
            'blocked' => $blocked,
            'checkedInPlaceId' => $checkedInPlaceId,
            'checkedInPlaceName' => $checkedInPlaceName,
            'checkedInPlaceGenre' => $checkedInPlaceGenre,
            'checkedInPlaceAddress' => $checkedInPlaceAddress,
            'signalSeen' => $row['seen']
        );
    }
}


/******* MESSAGES *******/

// Get users who messaged you and that haven't been blocked by you or that have blocked you
$sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, um.messageType, um.messageTime, um.seen FROM users as u, users_messages AS um, places AS p
        WHERE um.userId = u.userId AND um.userMessagedId = $userId AND um.placeId = p.placeId 
        AND (u.userId) NOT IN
        ( SELECT ub.userId FROM users_blocks as ub 
          WHERE ub.userBlockedId = $userId )";

$query = mysql_query($sql);
$messagesIn  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
        // Check checked-in place
        $personId = $row['userId'];
        $sql = "SELECT up.placeId, p.name, p.genre, p.address FROM users_places as up, places as p WHERE up.userId = $personId AND up.placeId = p.placeId";
        $checkedInPlaceId = -1;
        $checkedInPlaceName = "";
        $checkedInPlaceGenre = -1;
        $checkedInPlaceAddress = "";
        $query2 = mysql_query($sql); 
        $row2 = mysql_fetch_array($query2);
        if($row2) 
        {
            $checkedInPlaceId = $row2['placeId'];
            $checkedInPlaceName = $row2['name'];
            $checkedInPlaceGenre = $row2['genre'];
            $checkedInPlaceAddress = $row2['address'];
        }
    
        // Check if blocked   
        $blocked = 0;
        if( array_key_exists($row['userId'], $peopleBlocked) )
            $blocked = 1;
        
        $messagesIn[]=array(
            'uid' => $row['userId'],
            'fid' => $row['fid'],
            'un' => $row['username'],
            'n' => $row['name'],
            'msgType' => $row['messageType'],
            'msgTime' => $row['messageTime'],
            'blocked' => $blocked,
            'checkedInPlaceId' => $checkedInPlaceId,
            'checkedInPlaceName' => $checkedInPlaceName,
            'checkedInPlaceGenre' => $checkedInPlaceGenre,
            'checkedInPlaceAddress' => $checkedInPlaceAddress,
            'signalSeen' => $row['seen']

        );
    }
}
$query = mysql_query("UPDATE users_messages SET seen = 1 WHERE userMessagedId = $userId");

// Get users you messaged
$sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, um.messageType, um.messageTime, um.seen FROM users as u, users_messages AS um, places AS p
        WHERE um.userMessagedId = u.userId AND um.userId = $userId AND um.placeId = p.placeId 
        AND (u.userId) NOT IN
        ( SELECT ub.userId FROM users_blocks as ub 
          WHERE ub.userBlockedId = $userId )";

$query = mysql_query($sql);
$messagesOut  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
        // Check checked-in place
        $personId = $row['userId'];
        $sql = "SELECT up.placeId, p.name, p.genre, p.address FROM users_places as up, places as p WHERE up.userId = $personId AND up.placeId = p.placeId";
        $checkedInPlaceId = -1;
        $checkedInPlaceName = "";
        $checkedInPlaceGenre = -1;
        $checkedInPlaceAddress = "";
        $query2 = mysql_query($sql); 
        $row2 = mysql_fetch_array($query2);
        if($row2) 
        {
            $checkedInPlaceId = $row2['placeId'];
            $checkedInPlaceName = $row2['name'];
            $checkedInPlaceGenre = $row2['genre'];
            $checkedInPlaceAddress = $row2['address'];
        }
    
        // Check if blocked   
        $blocked = 0;
        if( array_key_exists($row['userId'], $peopleBlocked) )
            $blocked = 1;
        
        // Check if flirted recently
        $flirtedRecently = 0;
        if( array_key_exists($row['userId'], $peopleFlirtedRecently) )
            $flirtedRecently = 1;
            
            
        $messagesOut[]=array(
            'uid' => $row['userId'],
            'fid' => $row['fid'],
            'un' => $row['username'],
            'n' => $row['name'],
            'msgType' => $row['messageType'],
            'msgTime' => $row['messageTime'],
            'blocked' => $blocked,
            'flirtedRecently' => $flirtedRecently,
            'checkedInPlaceId' => $checkedInPlaceId,
            'checkedInPlaceName' => $checkedInPlaceName,
            'checkedInPlaceGenre' => $checkedInPlaceGenre,
            'checkedInPlaceAddress' => $checkedInPlaceAddress,
            'signalSeen' => $row['seen']
        );
    }
}


// Get users who messaged you and that haven't blocked you
$sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, um.message, um.messageTime, um.seen FROM users as u, users_pers_messages AS um, places AS p
    WHERE um.userId = u.userId AND um.userPersMessagedId = $userId AND um.placeId = p.placeId 
    AND (u.userId) NOT IN
    ( SELECT ub.userId FROM users_blocks as ub 
      WHERE ub.userBlockedId = $userId )";

$query = mysql_query($sql);
$persMessagesIn  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
        // Check checked-in place
        $personId = $row['userId'];
        $sql = "SELECT up.placeId, p.name, p.genre, p.address FROM users_places as up, places as p WHERE up.userId = $personId AND up.placeId = p.placeId";
        $checkedInPlaceId = -1;
        $checkedInPlaceName = "";
        $checkedInPlaceGenre = -1;
        $checkedInPlaceAddress = "";
        $query2 = mysql_query($sql); 
        $row2 = mysql_fetch_array($query2);
        if($row2) 
        {
            $checkedInPlaceId = $row2['placeId'];
            $checkedInPlaceName = $row2['name'];
            $checkedInPlaceGenre = $row2['genre'];
            $checkedInPlaceAddress = $row2['address'];
        }
    
        // Check if blocked    
        $blocked = 0;
        if( array_key_exists($row['userId'], $peopleBlocked) )
            $blocked = 1;
            
        $persMessagesIn[]=array(
            'uid' => $row['userId'],
            'fid' => $row['fid'],
            'un' => $row['username'],
            'n' => $row['name'],
            'msg' => $row['message'],
            'msgTime' => $row['messageTime'],
            'blocked' => $blocked,
            'checkedInPlaceId' => $checkedInPlaceId,
            'checkedInPlaceName' => $checkedInPlaceName,
            'checkedInPlaceGenre' => $checkedInPlaceGenre,
            'checkedInPlaceAddress' => $checkedInPlaceAddress,
            'signalSeen' => $row['seen']

        );
    }
}
$query = mysql_query("UPDATE users_pers_messages SET seen = 1 WHERE userPersMessagedId = $userId");

// Get users you pers messaged
$sql = "SELECT DISTINCT u.userId, u.fid, u.username, p.name, um.message, um.messageTime, um.seen FROM users as u, users_pers_messages AS um, places AS p
        WHERE um.userPersMessagedId = u.userId AND um.userId = $userId AND um.placeId = p.placeId 
        AND (u.userId) NOT IN
        ( SELECT ub.userId FROM users_blocks as ub 
          WHERE ub.userBlockedId = $userId )";

$query = mysql_query($sql);
$persMessagesOut  = array();

if ($query)
{
    while ($row=mysql_fetch_array($query)) 
    {
        // Check checked-in place
        $personId = $row['userId'];
        $sql = "SELECT up.placeId, p.name, p.genre, p.address FROM users_places as up, places as p WHERE up.userId = $personId AND up.placeId = p.placeId";
        $checkedInPlaceId = -1;
        $checkedInPlaceName = "";
        $checkedInPlaceGenre = -1;
        $checkedInPlaceAddress = "";
        $query2 = mysql_query($sql); 
        $row2 = mysql_fetch_array($query2);
        if($row2) 
        {
            $checkedInPlaceId = $row2['placeId'];
            $checkedInPlaceName = $row2['name'];
            $checkedInPlaceGenre = $row2['genre'];
            $checkedInPlaceAddress = $row2['address'];
        }
    
        // Check if blocked    
        $blocked = 0;
        if( array_key_exists($row['userId'], $peopleBlocked) )
            $blocked = 1;
            
        // Check if flirted recently
        $flirtedRecently = 0;
        if( array_key_exists($row['userId'], $peopleFlirtedRecently) )
            $flirtedRecently = 1;
            
        $persMessagesOut[]=array(
            'uid' => $row['userId'],
            'fid' => $row['fid'],
            'un' => $row['username'],
            'n' => $row['name'],
            'msg' => $row['message'],
            'msgTime' => $row['messageTime'],
            'blocked' => $blocked,
            'flirtedRecently' => $flirtedRecently,
            'checkedInPlaceId' => $checkedInPlaceId,
            'checkedInPlaceName' => $checkedInPlaceName,
            'checkedInPlaceGenre' => $checkedInPlaceGenre,
            'checkedInPlaceAddress' => $checkedInPlaceAddress,
            'signalSeen' => $row['seen']
        );
    }
}


$signals =array(
            'flirtsIn' => $flirtsIn,
            'flirtsOut' => $flirtsOut,
            'messagesIn' => $messagesIn,
            'messagesOut' => $messagesOut,
            'persMessagesIn' => $persMessagesIn,
            'persMessagesOut' => $persMessagesOut
        );

echo json_encode(array( 'r'  =>  $signals, 'ok' => '1' ));


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
