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



$placeId = $_POST['placeId'];
$userId = $_POST['userId'];

$placeId = mysql_escape_string($placeId);
$userId = mysql_escape_string($userId);

  
$jsonPeopleBar  = array();


// PLACE
$sql = "SELECT latitude, longitude, message, website, starMaleId, starFemaleId
        FROM places WHERE placeId = $placeId";		
        
$query = mysql_query($sql);

if( mysql_num_rows($query) > 0 )
{
    $rowPlace=mysql_fetch_array($query);

	$starMaleId = $rowPlace['starMaleId'];
	$starFemaleId = $rowPlace['starFemaleId'];

	// USERS

	$sql = "SELECT u.userId, u.username, u.sex, u.interestedIn, u.wantedMinAge, u.wantedMaxAge, u.birthdate, u.lookingFor
	        FROM users AS u, (SELECT sex, interestedIn, wantedMinAge, wantedMaxAge, birthdate FROM users WHERE userId = $userId) AS me
	        WHERE u.placeId = $placeId

	        AND ( (me.interestedIn=u.sex AND u.interestedIn=me.sex) OR (me.interestedIn=3 AND u.interestedIn=me.sex) OR 
				(u.interestedIn=3 AND me.interestedIn=u.sex) OR (u.interestedIn=3 AND u.interestedIn=me.interestedIn)  OR u.userId = $starMaleId OR u.userId = $starFemaleId  )

			AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= u.wantedMinAge
			AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= u.wantedMaxAge
			AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= me.wantedMinAge
			AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= me.wantedMaxAge

			AND (u.userId) NOT IN
	            ( SELECT ub.userId FROM users_blocks as ub 
	              WHERE ub.userBlockedId = $userId )
	";		

	$query = mysql_query($sql);

	while ($rowPerson=mysql_fetch_array($query)) 
	{
		// CHECK IF STAR
	    $star = 0;
	    if( $starMaleId == $rowPerson['userId'] ) 
	        $star = 1;
		else if( $starFemaleId == $rowPerson['userId'] ) 
	        $star = 1;

	    $jsonPeopleBar[]=array(
	        'userId' => $rowPerson['userId'],
	        'username' => $rowPerson['username'],
			'sex' => $rowPerson['sex'],
			'interestedIn' => $rowPerson['interestedIn'],
	        'birthday' => $rowPerson['birthdate'],
	        'lookingFor' => $rowPerson['lookingFor'],
	        'star' => $star  
	    );
	}
	
	$json = array(  
		'peopleBar' => $jsonPeopleBar
 	);
	
	 echo json_encode(array( 'r'  =>  $json, 'ok' => '1'));
}
else
    echo json_encode(array( 'ok' => '0', 'error' => '1'));

mysql_close($con);


function getAge($fbDate) {
// Convert SQL date to individual Y/M/D variables
    list($Y,$m,$d) = explode("-",$fbDate);
    $age = date("Y") - $Y;
	
// If the birthday has not yet come this year
    if(date("md") < $m.$d ) {
		$age--;
	}

    return $age;
}


?>
