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

$username = $json['username'];
$password = $json['password'];
$email = $json['email'];
$sex = $json['sex'];
$interestedIn = $json['interestedIn'];
$wantedMinAge = $json['wantedMinAge'];
$wantedMaxAge = $json['wantedMaxAge'];
$birthday = $json['birthday'];


// prevent sql injection
$username = mysql_real_escape_string($username);
$password = mysql_real_escape_string($password);
$email = mysql_real_escape_string($email);

$sex = mysql_real_escape_string($sex);
$interestedIn = mysql_real_escape_string($interestedIn);
$wantedMinAge = mysql_real_escape_string($wantedMinAge);
$wantedMaxAge = mysql_real_escape_string($wantedMaxAge);
$birthday = mysql_real_escape_string($birthday);

$accessToken = '' . rand() . time();

	$sql = "INSERT INTO users (username, password, accessToken, email, profilePicFilename, sex, interestedIn, wantedMinAge, wantedMaxAge, birthdate, end_vip, last_active) VALUES ('$username', '$password', '$accessToken', '$email', '', $sex, $interestedIn, $wantedMinAge, $wantedMaxAge, '$birthday', NOW(), NOW())";
		
$query = mysql_query($sql);
                
    if ($query)
    {
        $userId = mysql_insert_id();
		
		
        $json=array(
            'userId' => $userId, 
			'accessToken' => $accessToken
        );
            
        echo json_encode(array( 'r'  =>  $json, 'ok' => '1' ));


    }
    else {
		$sql = "SELECT userId FROM users WHERE username = '$username'";	
		
		$query = mysql_query($sql);
		
		if (mysql_num_rows($query) > 0)
        	echo json_encode(array( 'ok' => '0', 'error' => '1'));	
		else
			echo json_encode(array( 'ok' => '0', 'error' => '2'));	
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
