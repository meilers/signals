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
$personId = $json['personId'];
$placeId = $json['pid'];
$msg = $json['msg'];


$userId = mysql_escape_string($userId);
$personId = mysql_escape_string($personId);
$placeId = mysql_escape_string($placeId);
$msg = mysql_escape_string($msg);

$sql = "INSERT INTO users_pers_messages (userId, userPersMessagedId, placeId, message, messageTime) VALUES ($userId, $personId, $placeId, '$msg', UTC_TIMESTAMP())";

$query = mysql_query($sql);

if ($query)
{
    $sql = "SELECT username, name FROM users, places WHERE userId = $userId AND placeId = $placeId";
    $query = mysql_query($sql);
    
    if (mysql_num_rows($query) > 0)
    {
        $row=mysql_fetch_array($query);
    
        $APPLICATION_ID = "ujzhrnPvHlLj7oX5wrHdZguIjfLGKZuLhFfqFcnn";
        $REST_API_KEY = "wYEDKZywwm3rOWoeRrAtOoQRWrrNY3WnOCXTwJnI";

        $url = 'https://api.parse.com/1/push';
        $data = array(
            'channel' => 'user' . strval($personId),
            'type' => 'android',
            'data' => array(
                'alert' => $row['username'] . ' sent you a personalized message!',
                'title' => '@ ' . $row['name'],
                'username' => $row['username'],
                'messCode' => 3,
                'action' => 'MyAction'
            ),
        );
        $_data = json_encode($data);
        $headers = array(
            'X-Parse-Application-Id: ' . $APPLICATION_ID,
            'X-Parse-REST-API-Key: ' . $REST_API_KEY,
            'Content-Type: application/json',
            'Content-Length: ' . strlen($_data),
        );

        $curl = curl_init($url);
        curl_setopt($curl, CURLOPT_POST, 1);
        curl_setopt($curl, CURLOPT_POSTFIELDS, $_data);
        curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER,1);  
        //curl_exec($curl);

        echo json_encode(array( 'ok' => '1' ));	
    }
    else
        echo json_encode(array( 'ok' => '0' ));	
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
