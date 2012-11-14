<?php
function getAge($fbDate) {
    list($m,$d, $Y) = explode("/",$fbDate);
    $age = date("Y") - $Y;
	
    if(date("md") < $m.$d ) {
		$age--;
	}

    return $age;
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

$userId = $_POST['uid'];

// prevent sql injection
$userId = mysql_real_escape_string($userId);
$userId = 12;



$sql = "SELECT u.fid, u.username, u.picPosX, u.picPosY, u.profilePicsUrls, u.sex, u.orientation, u.ageGroup, u.birthday, u.hometown, u.languages, u.education, u.work, u.bars, u.music, u.movies, u.television, u.books, u.other, u.aboutMe, u.quotations, u.wristbands, u.w_sex, u.w_orientation, u.w_ageGroup, u.w_wrist, u.superuser, u.end_superuser, u.online FROM users AS u WHERE u.userId = $userId";

$query = mysql_query($sql);

// If we find a match, create an array of data, json_encode it and echo it out
if (mysql_num_rows($query) > 0)
{
	$row=mysql_fetch_array($query);
	$userId = $row['userId'];
	$ageGroup = 1;
	$age = getAge($row['birthday']);
	
	if($age > 25)
		$ageGroup = 2;
	if($age > 35)
		$ageGroup = 3;
	if($age > 50)
		$ageGroup = 4;

    $superuser = $row['superuser'];
    
    if( $row['end_superuser'] < date('Y-m-d H:i:s') ) 
        $superuser = 0;

    if( $superuser != $row['superuser'] || $ageGroup != $row['ageGroup'] )
        $query = mysql_query("UPDATE users SET ageGroup = $ageGroup, superuser = '$superuser' WHERE userId = $userId");

    if($query)
    {
        $json=array(
            'fid' => $row['fid'],
            'un' => $row['username'],
            'ppx' => $row['picPosX'],
            'ppx' => $row['picPosX'],
            'ppurls' => json_decode($row['profilePicsUrls']),
            'age' => $age,
            'sex' => $row['sex'],
            'o' => $row['orientation'],

            'ht' => $row['music'],
            'lan' => $row['movies'],
            'edu' => $row['television'],
            'work' => $row['books'],
            'lbars' => $row['bars'],  
            'lmusic' => $row['music'],
            'lmovies' => $row['movies'],
            'ltv' => $row['television'],
            'lbooks' => $row['books'],
            'lother' => $row['other'],
            'aboutme' => $row['aboutMe'],
            'quotes' => $row['quotations'],
            
            'w' => json_decode($row['wristbands']),
            'w_sex' => $row['w_sex'],
            'w_o' => $row['w_orientation'],
            'w_ageG' => $row['w_ageGroup'],
            'w_w' => json_decode($row['w_wrist']),
            'super' => $superuser,
            'online' => $online
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

mysql_close($con);
?>
