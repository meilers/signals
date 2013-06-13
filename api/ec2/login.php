
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

// NE PAS ENLEVER, pour UNICODE
mysql_set_charset('utf8',$con);

$jsonString = file_get_contents('php://input');

$jsonString = EncodeToUTF8($jsonString);
$json = json_decode($jsonString, true);

$email = $json['email'];
$password = $json['password'];
$email = 'julia@hotmail.com';
$password = 'allanpoe';

// prevent sql injection
$email = mysql_real_escape_string($email);
$password = mysql_real_escape_string($password);

$sql = "SELECT u.userId, u.cityId, c.city, u.username, u.sex, u.interestedIn, u.wantedMinAge, u.wantedMaxAge, u.birthdate,  u.endVip
		FROM users AS u, cities AS c 
		WHERE u.email = '$email' AND u.password = '$password' AND u.cityId = c.cityId";

$query = mysql_query($sql);

// If we find a match, create an array of data, json_encode it and echo it out
if (mysql_num_rows($query) > 0)
{
	$row=mysql_fetch_array($query);
	$userId = $row['userId'];

	// CHECK IF VIP
    $vip = 0;
    
    if( $row['endVip'] > date('Y-m-d H:i:s') ) 
        $vip = 1;
      
	    
    // CREATE ACCESS TOKEN    
	
	$accessToken = '' . rand() . time();
		
    $query = mysql_query("UPDATE users SET accessToken = '$accessToken' WHERE userId = $userId");

    if($query)
    {
		$jsonCity = array(
			'id' => $row['cityId'],
			'city' => $row['city']
			
		);

        $json=array(
            'userId' => $userId,
			'city' => $jsonCity,
			'username' => $row['username'],
			'accessToken' => $accessToken,
            'sex' => $row['sex'],
            'interestedIn' => $row['interestedIn'],
            'wantedMinAge' => $row['wantedMinAge'],
            'wantedMaxAge' => $row['wantedMaxAge'],
            'birthday' => $row['birthdate'],
            'vip' => $vip
        );
        echo json_encode(array(  'ok' => '1', 'r'  =>  $json ));
    }
	else	
        echo json_encode(array( 'ok' => '0', 'error' => '2' ));	
}
else
{
	echo json_encode(array( 'ok' => '0', 'error' => '1' ));	
}

mysql_close($con);



function getAge($fbDate) {
// Convert SQL date to individual Y/M/D variables
    list($m,$d, $Y) = explode("/",$fbDate);
    $age = date("Y") - $Y;
	
// If the birthday has not yet come this year
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
        if ( ($sStr = mb_convert_encoding($sStr, "UTF-8", mb_detect_encoding($sStr, "UTF-8, ISO-8859-1, GBK, JIS, eucjp-win, sjis-win, ASCII,Windows-1252, GB2312", true))) === false ) throw new Exception();
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
