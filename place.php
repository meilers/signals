<?php
define('TIMEZONE', 'America/Los_Angeles');  


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

function distance($lat1, $lon1, $lat2, $lon2, $unit) { 
  
  $theta = $lon1 - $lon2; 
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta)); 
  $dist = acos($dist); 
  $dist = rad2deg($dist); 
  $miles = $dist * 60 * 1.1515;
  $unit = strtoupper($unit);

  if ($unit == "K") {
    return ($miles * 1.609344); 
  } else if ($unit == "N") {
      return ($miles * 0.8684);
    } else {
        return $miles;
      }
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
date_default_timezone_set('America/Los_Angeles'); 	

$placeId = $_POST['pid'];
$userId = $_POST['uid'];

$placeId = mysql_escape_string($placeId);
$userId = mysql_escape_string($userId);



$recentUsersInPlace  = array();
$totalUsersInPlace = 0;
$averageAgeInPlace = 0.0;
$total_wr_blue = 0;
$total_wr_red = 0;
$total_wr_yellow = 0;
$total_wr_green = 0;


// USERS

$sql = "SELECT u.fid, u.birthday, u.wristBlue, u.wristRed, u.wristYellow, u.wristGreen, up.enterTime
        FROM users AS u, users_places AS up, (SELECT sex, orientation FROM users WHERE userId = $userId) AS me
        WHERE u.userId = up.userId AND up.placeId = $placeId
        
        AND ( ((me.sex=1 AND u.sex=2 OR me.sex=2 AND u.sex=1) AND u.orientation = 1 AND me.orientation = 1)
                OR ((me.sex=1 AND u.sex=1 OR me.sex=2 AND u.sex=2) AND u.orientation = 2 AND me.orientation = 2) )
                
        ORDER BY up.enterTime DESC";		

$query = mysql_query($sql);

while ($row=mysql_fetch_array($query)) 
{
    $averageAgeInPlace = $averageAgeInPlace + getAge($row['birthday']);
    
    if( $row['wristBlue'] )
        $total_wr_blue = $total_wr_blue + 1;
        
    if( $row['wristRed'])
        $total_wr_red = $total_wr_red + 1;
        
    if( $row['wristYellow'])
        $total_wr_yellow = $total_wr_yellow + 1;
        
    if( $row['wristGreen'] )
        $total_wr_green = $total_wr_green + 1;
        
    // Get recent checked-in people
    if( time() - strtotime($row['enterTime']." UTC") < 7200 )
        array_push($recentUsersInPlace, $row['fid']);
    
    $totalUsersInPlace = $totalUsersInPlace + 1;
}

$averageAgeInPlace = $averageAgeInPlace / $totalUsersInPlace;

    
// PLACE 

$sql = "SELECT latitude, longitude, message, website
        FROM places WHERE placeId = $placeId";		
        
$query = mysql_query($sql);


if( $query )
{
    $row=mysql_fetch_array($query);
    
    $distance = distance($latitude, $longitude, $row['latitude'], $row['longitude'], "k");
    
    $place=array(
            'd' => sprintf("%.2f", $distance),
            'msg' => $row['message'],
            'web' => $row['website']
    );	
    
    
    $json = array( 'p' => $place, 
                                'ru'  =>  $recentUsersInPlace, 
                                'tu' => $totalUsersInPlace,
                                't_wr_b'  =>  $total_wr_blue, 
                                't_wr_r' => $total_wr_red,                            
                                't_wr_y'  =>  $total_wr_yellow, 
                                't_wr_g' => $total_wr_green,                                
                                'aa'  =>  intval($averageAgeInPlace)
                                );

    echo json_encode(array( 'r'  =>  $json, 'ok' => '1'));
}
else
    echo json_encode(array( 'ok' => '0'));


?>
