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
$placeId = $json['placeId'];


$userId = mysql_escape_string($userId);
$personId = mysql_escape_string($personId);
$placeId = mysql_escape_string($placeId);


INSERT INTO users_visits (userId, userVisitedId, placeId, visitTime) VALUES ($userId, $personId, $placeId, CURRENT_TIMESTAMP())";

$query = mysql_query($sql);

if ($query)
{
    $sql = "SELECT u.username, p.name FROM users as u, places as p WHERE u.userId = $userId AND p.placeId = $placeId";
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
                'alert' => $row['username'] . ' visited your profile!',
                'title' => '@ ' . $row['name'],
                'username' => $row['username'],
                'messCode' => 1,
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
        curl_exec($curl);

        echo json_encode(array( 'ok' => '1' ));	
    }
    else
        echo json_encode(array( 'ok' => '0', 'error' => '2' ));	
}
else {

	echo json_encode(array( 'ok' => '0', 'error' => '1' ));	
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
}?>
