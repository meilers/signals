<?php
function getAge($fbDate) {
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


$con = mysql_connect($server = "mysql-shared-02.phpfog.com",$username = "Custom App-34204",$password = "TIKi1234");

if (!$con)
{
	echo "Failed to make connection.";
	exit;
}
$db = mysql_select_db("signals_phpfogapp_com");

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

$userId = $json['uid'];
$personId = $json['personId'];

// prevent sql injection
$userId = mysql_real_escape_string($userId);
$personId = mysql_real_escape_string($personId);

$isLiked = 0;
$isBlocked = 0;

// Check if liked person recently
$sql = "SELECT userId FROM users_likes WHERE userId = $userId AND userLikedId = $personId AND likeTime > DATE_SUB(UTC_TIMESTAMP, INTERVAL 12 HOUR)";
$query = mysql_query($sql);
if (mysql_num_rows($query) > 0)
    $isLiked = 1;

// Checked if blocked person
$sql = "SELECT userId FROM users_blocks WHERE userId = $userId AND userBlockedId = $personId";
$query = mysql_query($sql);
if (mysql_num_rows($query) > 0)
    $isBlocked = 1;
    
    
// Get profile
$sql = "SELECT u.profilePicsUrls, u.tonight, u.hometown, u.languages, u.education, u.work, u.music, u.movies, u.television, u.books, u.other, u.aboutMe, u.quotations, u.superuser FROM users AS u WHERE u.userId = $personId";

$query = mysql_query($sql);

// If we find a match, create an array of data, json_encode it and echo it out
if (mysql_num_rows($query) > 0)
{
	$row=mysql_fetch_array($query);
	$age = getAge($row['birthday']);

    $json=array(

        'ppurls' => json_decode($row['profilePicsUrls']),

        'tonight' => $row['tonight'],
        'ht' => $row['hometown'],
        'lan' => json_decode($row['languages']),
        'ed' => json_decode($row['education']),
        'wo' => json_decode($row['work']), 
        'lmusic' => json_decode($row['music']),
        'lmovies' => json_decode($row['movies']),
        'ltv' => json_decode($row['television']),
        'lbooks' => json_decode($row['books']),
        'lother' => json_decode($row['other']),
        'aboutme' => $row['aboutMe'],
        'quotes' => $row['quotations'],
        'super' => $row['superuser'],
        'isLiked' => $isLiked,
        'isBlocked' => $isBlocked
    );
        
    echo json_encode(array( 'r'  =>  $json, 'ok' => '1' ));

}
else
{
	echo json_encode(array( 'ok' => '0' ));	
}

mysql_close($con);
?>
