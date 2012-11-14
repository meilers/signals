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
date_default_timezone_set('America/New_York'); 	

$userId = $_POST['uid'];
 
$userId = mysql_escape_string($userId);



// Liked me
	$sql = "SELECT ul.userId, u.fid, u.username, u.sex, u.orientation, u.birthday,  u.picPosX, u.picPosY, u.wristbands, u.superuser
			FROM users AS u, users_likes AS ul
			WHERE ul.userLikedId = $userId AND ul.userId = u.userId
			ORDER BY ul.likeTime";		


$query = mysql_query($sql);

$json  = array();

// If we find a match, create an array of data, json_encode it and echo it out
if (mysql_num_rows($query) > 0)
{
    while ($row=mysql_fetch_array($query)) 
    {
        $usersLikedYou[] = array(
                'uid' => $row['userId'],
                'fid' => $row['fid'],
                'un' => $row['username'],
                'sex' => $row['sex'],
                'o' => $row['orientation'],
                'age' => (string)getAge($row['birthday']),
                'ppx' => $row['picPosX'],
                'ppy' => $row['picPosY'],
                'wr' => json_decode($row['wristbands']),
                'super' => $row['superuser']
            );		
    }

    // I liked
	$sql = "SELECT ul.userLikedId, u.fid, u.username, u.sex, u.orientation, u.birthday, u.picPosX, u.picPosY, u.wristbands, u.superuser
			FROM users AS u, users_likes AS ul
			WHERE ul.userId = $userId AND ul.userLikedId = u.userId
			ORDER BY ul.likeTime";	
          
    $query = mysql_query($sql);
        
    if (mysql_num_rows($query) > 0)
    {
        
        while ($row=mysql_fetch_array($query)) 
        {

            $usersYouLiked[]=array(
                'uid' => $row['userLikedId'],
                'fid' => $row['fid'],
                'un' => $row['username'],
                'sex' => $row['sex'],
                'o' => $row['orientation'],
                'age' => (string)getAge($row['birthday']),
                'ppx' => $row['picPosX'],
                'ppy' => $row['picPosY'],
                'wr' => json_decode($row['wristbands']),
                'super' => $row['superuser']
            );					
        }
        
        $json = array( 'uly' => $usersLikedYou, 'uyl' => $usersYouLiked );
        echo json_encode(array( 'r'  =>  $json, 'ok' => '1'));
    }
    else
        echo json_encode(array( 'ok' => '0'));	

}
else {
{
	echo json_encode(array( 'ok' => '0'));	
}
}

?>
