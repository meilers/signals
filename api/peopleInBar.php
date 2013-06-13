<?php

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

// Connect to the database(host, username, password)
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

$placeId = $_POST['pId'];
$userId = $_POST['uid'];
$sex = $_POST['s'];
$orientation = $_POST['o'];
$ageGroup = $_POST['ageG'];
$w_sex = $_POST['w_s'];
$w_orientation = $_POST['w_o'];
$w_ageGroup = $_POST['w_ageG'];
$w_wrist = $_POST['w_w'];

$placeId = mysql_escape_string($placeId);
$userId = mysql_escape_string($userId);
$sex = mysql_escape_string($sex);
$orientation = mysql_escape_string($orientation);
$ageGroup = mysql_escape_string($ageGroup);
$w_sex = mysql_escape_string($w_sex);
$w_orientation = mysql_escape_string($w_orientation);
$w_ageGroup = mysql_escape_string($w_ageGroup);
$w_wrist = mysql_escape_string($w_wrist);




	$sql = "SELECT u.userId, u.username, u.sex, u.orientation, u.picSquare, u.wristbands, u.birthday, c.city, c.country
			FROM users AS u, users_places AS up, cities AS c 
			WHERE u.userId = up.userId AND u.cityId = c.cityId AND up.placeId = $placeId
            AND (u.orientation = $w_orientation OR $w_orientation = 0) AND (u.w_orientation = $orientation OR u.w_orientation = 0)
            AND (u.sex = $w_sex OR $w_sex = 0) AND (u.w_sex = $sex OR u.w_sex = 0)
            AND (u.ageGroup = $w_ageGroup OR $w_ageGroup = 0) AND (u.w_ageGroup = $ageGroup OR u.w_ageGroup = 0)
			GROUP BY up.userId
			ORDER BY up.enterTime";		


$query = mysql_query($sql);

$json  = array();

// If we find a match, create an array of data, json_encode it and echo it out
if (mysql_num_rows($query) > 0)
{
    while ($row=mysql_fetch_array($query)) 
    {

        $json[]=array(
        	'c' => $row['city'],
        	'co' => $row['country'],
		    'id' => $row['userId'],
            's' => $row['userId'],
            'o' => $row['userId'],
	    	'nn' => $row['nickName'],
	    	'ps' => $row['picSquare'],
	      	'age' => (string)getAge($row['birthday']),
	    	'w' => explode(",", $row['wristbands'])
        );					


    }
	
	echo json_encode(array( 'r'  =>  $json, 'ok' => '1'));
}
else {
{
	echo json_encode(array( 'ok' => '0'));	
}
}

?>
