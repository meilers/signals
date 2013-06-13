
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

$fid = $json['fid'];
$email = $json['email'];
$sex = $json['sex'];
$birthday = $json['b'];

$profilePicsUrls = $json['ppurls'];
$profilePicsUrls = json_encode($profilePicsUrls);

$hometown = $json['ht'];

$education = $json['ed'];
$education = json_encode($education);

$work = $json['wo'];
$work = json_encode($work);

$likesMusic = $json['lmusic'];
$likesMusic = json_encode($likesMusic);
$likesMovies = $json['lmovies'];
$likesMovies = json_encode($likesMovies);
$likesTv = $json['ltv'];
$likesTv = json_encode($likesTv);
$likesBooks = $json['lbooks'];
$likesBooks = json_encode($likesBooks);
$likesOther = $json['lother'];
$likesOther = json_encode($likesOther);

$languages = $json['lan'];
$languages = json_encode($languages);

$aboutMe = $json['aboutme'];
$quotations = $json['quotes'];

// prevent sql injection
$fid = mysql_real_escape_string($fid);
$sex = mysql_real_escape_string($sex);
$birthday = mysql_real_escape_string($birthday);
$profilePicsUrls = mysql_real_escape_string($profilePicsUrls);
$hometown = mysql_real_escape_string($hometown);
$education = mysql_real_escape_string($education);
$work = mysql_real_escape_string($work);
$likesMusic = mysql_real_escape_string($likesMusic);
$likesMovies = mysql_real_escape_string($likesMovies);
$likesTv = mysql_real_escape_string($likesTv);
$likesBooks = mysql_real_escape_string($likesBooks);
$likesOther = mysql_real_escape_string($likesOther);
$languages = mysql_real_escape_string($languages);
$aboutMe = mysql_real_escape_string($aboutMe);
$quotations = mysql_real_escape_string($quotations);

$sql = "SELECT u.userId, u.fid, u.username, u.orientation, u.tonight, u.wristBlue, u.wristRed, u.wristYellow, u.wristGreen, u.superuser, u.end_superuser FROM users AS u WHERE u.fid = $fid";

$query = mysql_query($sql);

// If we find a match, create an array of data, json_encode it and echo it out
if (mysql_num_rows($query) > 0)
{
	$row=mysql_fetch_array($query);
	$userId = $row['userId'];
	$age = getAge($birthday);

    $superuser = $row['superuser'];
    
    if( $row['end_superuser'] < date('Y-m-d H:i:s') ) 
        $superuser = 0;


    $query = mysql_query("UPDATE users SET email = '$email', sex = '$sex', birthday = '$birthday', profilePicsUrls = '$profilePicsUrls', hometown = '$hometown', work = '$work', education = '$education', music = '$likesMusic', movies = '$likesMovies', television = '$likesTv', books = '$likesBooks', other = '$likesOther', languages = '$languages', quotations = '$quotations', aboutMe = '$aboutMe', superuser = $superuser WHERE userId = $userId");

    if($query)
    {

        $json=array(
            'uid' => $row['userId'],
            'un' => $row['username'],
            'age' => $age,
            'ageG' => $ageGroup,
            'o' => $row['orientation'],
            'wr_b' => $row['wristBlue'],
            'wr_r' => $row['wristRed'],
            'wr_y' => $row['wristYellow'], 
            'wr_g' => $row['wristGreen'],
            'super' => $superuser,
            'tonight' => $row['tonight']
        );
        echo json_encode(array( 'r'  =>  $json, 'ok' => '1' ));
    }
	else	
        echo json_encode(array( 'ok' => '0' ));	
}
// create new account
else
{
	echo json_encode(array( 'ok' => '0' ));	
}

mysql_close($link);



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
