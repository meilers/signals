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

$placeId = $json['pid'];
$userId = $json['uid'];

$placeId = mysql_escape_string($placeId);
$userId = mysql_escape_string($userId);

// GET PEOPLE IN BAR YOU BLOCKED
$peopleBlocked = array();
$sql = "SELECT ub.userBlockedId FROM users_blocks as ub, users as u, users_places AS up
            WHERE u.userId = $userId AND up.userId = $userId AND ub.userId = $userId AND up.userId = ub.userId AND up.placeId = $placeId";	
        
$query = mysql_query($sql); 

while ($row=mysql_fetch_array($query)) 
{
    $peopleBlocked[$row['userBlockedId']] = 1;
}

// GET PEOPLE IN BAR
$sql = "SELECT u.userId, u.fid, u.username, u.sex, u.orientation, u.birthday, u.wristBlue, u.wristRed, u.wristYellow, u.wristGreen, u.superuser, up.enterTime
        FROM users AS u, users_places AS up, (SELECT sex, orientation FROM users WHERE userId = $userId) AS me
        WHERE u.userId = up.userId AND up.placeId = $placeId
        
        AND ( ((me.sex=1 AND u.sex=2 OR me.sex=2 AND u.sex=1) AND u.orientation = 1 AND me.orientation = 1)
                OR ((me.sex=1 AND u.sex=1 OR me.sex=2 AND u.sex=2) AND u.orientation = 2 AND me.orientation = 2) )
                
        AND (u.userId) NOT IN
            ( SELECT ub.userId FROM users_blocks as ub 
              WHERE ub.userBlockedId = $userId )";	
        
$query = mysql_query($sql);  
        
        
$json  = array();

while ($row=mysql_fetch_array($query)) 
{
	$age = getAge($row[birthday]);
    
    $isBlocked = 0;
    
    if( array_key_exists($row['userId'], $peopleBlocked) )
        $isBlocked = 1;
        
    $json[]=array(
        'uid' => $row['userId'],
        'fid' => $row['fid'],
        'un' => $row['username'],
        'sex' => $row['sex'],
        'o' => $row['orientation'],
        'age' => $age,
        'wr_b' => $row['wristBlue'],
        'wr_r' => $row['wristRed'],
        'wr_y' => $row['wristYellow'], 
        'wr_g' => $row['wristGreen'],
        'super' => $row['superuser'],
        'let' => time() - strtotime($row['enterTime']),
        'isBlocked' => $isBlocked   
    );

}
 
if($query)
    echo json_encode(array( 'r'  =>  $json, 'ok' => '1'));
else
    myError();


mysql_close($link);



function myError()
{
    echo json_encode(array( 'ok' => '0'));
}

function getAge($fbDate) 
{
    list($m,$d, $Y) = explode("/",$fbDate);
    $age = date("Y") - $Y;
	
    if(date("md") < $m.$d ) {
		$age--;
	}

    return $age;
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
}


function distance($lat1, $lon1, $lat2, $lon2, $unit) { 
  
  $theta = $lon1 - $lon2; 
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta)); 
  $dist = acos($dist); 
  $dist = rad2deg($dist); 
  $miles = $dist * 60 * 1.1515;
  $unit = strtoupper($unit);

  if ($unit == "K") {
    return ($miles * 1.609344); 
  } else if ($unit == "N") {
      return ($miles * 0.8684);
    } else {
        return $miles;
      }
}
?>
