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

$tonight = $json['tonight'];
$aboutMe = $json['aboutMe'];
$occupation = $json['occupation'];
$education = $json['education'];
$interests = $json['interests'];
$travel = $json['travel'];
$activities = $json['activities'];
$music = $json['music'];
$drinks = $json['drinks'];
$myPerfectNightOut = $json['myPerfectNightOut'];

$dontApproach = $json['dontApproach'];
$tipComeAndSayHi = $json['tipComeAndSayHi'];
$tipBuyMeADrink = $json['tipBuyMeADrink'];
$tipInviteMeToDance = $json['tipInviteMeToDance'];
$tipMakeMeLaugh = $json['tipMakeMeLaugh'];
$tipMeetMyFriends = $json['tipMeetMyFriends'];
$tipSurpriseMe = $json['tipSurpriseMe'];
$dontBeDrunk = $json['dontBeDrunk'];
$dontExpectAnything = $json['dontExpectAnything'];
$dontBePersistent = $json['dontBePersistent'];


$tonight = mysql_escape_string($tonight);
$aboutMe = mysql_escape_string($aboutMe);
$aboutMe = mysql_escape_string($aboutMe);
$occupation = mysql_escape_string($occupation);
$education = mysql_escape_string($education);
$interests = mysql_escape_string($interests);
$activities = mysql_escape_string($activities);
$travel = mysql_escape_string($travel);
$music = mysql_escape_string($music);
$drinks = mysql_escape_string($drinks);
$myPerfectNightOut = mysql_escape_string($myPerfectNightOut);

$dontApproach = mysql_escape_string($dontApproach);
$tipComeAndSayHi = mysql_escape_string($tipComeAndSayHi);
$tipBuyMeADrink = mysql_escape_string($tipBuyMeADrink);
$tipInviteMeToDance = mysql_escape_string($tipInviteMeToDance);
$tipSurpriseMe = mysql_escape_string($tipSurpriseMe);
$dontExpectAnything = mysql_escape_string($dontExpectAnything);
$dontCornerMe = mysql_escape_string($dontCornerMe);
$dontBePersistent = mysql_escape_string($dontBePersistent);
$dontBeDrunk = mysql_escape_string($dontBeDrunk);
$personalAdvice = mysql_escape_string($personalAdvice);



$sql = "UPDATE users SET tonight = '$tonight', aboutMe = '$aboutMe', occupation = '$occupation',
		education = '$education', interests = '$interests', activities = '$activities', 
		travel = '$travel', music = '$music', drinks = '$drinks', 
		myPerfectNightOut = '$myPerfectNightOut', 
		dontApproach = $dontApproach, tipComeAndSayHi = $tipComeAndSayHi, tipBuyMeADrink = $tipBuyMeADrink,
		tipInviteMeToDance = $tipInviteMeToDance, tipMakeMeLaugh = $tipMakeMeLaugh, tipMeetMyFriends = $tipMeetMyFriends, tipSurpriseMe = $tipSurpriseMe, 
		dontExpectAnything = $dontExpectAnything, dontBePersistent = $dontBePersistent, dontBeDrunk = $dontBeDrunk, 
		personalAdvice = '$personalAdvice'
		WHERE userId = $userId";

$query = mysql_query($sql);

if ($query)
{
	echo json_encode(array( 'ok' => '1' ));
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
}

?>
