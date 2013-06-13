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
$personId = $json['personId'];

$userId = mysql_escape_string($userId);
$personId = mysql_escape_string($personId);


$sql = "SELECT me.cityId, IF((me.cityId = u.cityId),0,1) AS a,
		IF((u.userId) NOT IN
	    	( SELECT ub.userId FROM users_blocks AS ub 
	      	WHERE ub.userBlockedId = $userId ),0,1) AS b,
		IF((u.userId IN (SELECT userId FROM users_invitations WHERE candidateId=$userId AND requestTime >= DATE_SUB(NOW(),INTERVAL 24 HOUR))),1,0) AS invitationReceived
		FROM users AS me, users as u
		WHERE me.userId = $userId AND u.userId = $personId";
$query = mysql_query($sql);

if (mysql_num_rows($query) > 0)
{
	$row=mysql_fetch_array($query);
	$cityId = $row['cityId'];
	
	if( $row['a'] == 1)
		echo json_encode(array( 'ok' => '0', 'error' => '3' ));	
	else if( $row['b'] == 1)
		echo json_encode(array( 'ok' => '0', 'error' => '4' ));	
	else
	{
		$sql = "INSERT INTO users_invitations (userId, candidateId, cityId, requestTime) VALUES ($userId, $personId, $cityId, CURRENT_TIMESTAMP())";
		$query = mysql_query($sql);

		if ($query)
		{
			$potentialRendezvous = NULL;
			
			if( $row['invitationReceived'] == 1 )
			{
				$sql = "SELECT placeId, name, genre, address, latitude, longitude FROM
						(
							(
							SELECT u.placeId, p.name, p.genre, p.address, p.latitude, p.longitude, 0, 1 AS priority
							FROM users AS u, places AS p
							WHERE u.userId = $userId AND u.placeId != 0 AND u.placeId = p.placeId
							)
							UNION
							(
							SELECT u.placeId, p.name, p.genre, p.address, p.latitude, p.longitude, 0, 2 AS priority
							FROM users AS u, places AS p
							WHERE u.userId = $personId AND u.placeId != 0 AND u.placeId = p.placeId
							)
							UNION
							(	
							SELECT uc.placeId, p.name, p.genre, p.address, p.latitude, p.longitude, COUNT(*) AS cnt, 3 AS priority
							FROM places AS p, users_checkins AS uc
							WHERE uc.userId = $userId AND uc.placeId = p.placeId AND p.cityId = $cityId AND p.genre != 1
							GROUP BY uc.placeId ORDER BY cnt DESC LIMIT 3
							)
							UNION
							(
							SELECT uc.placeId, p.name, p.genre, p.address, p.latitude, p.longitude, COUNT(*) AS cnt, 4 AS priority
							FROM places AS p, users_checkins AS uc
							WHERE uc.userId = $personId AND uc.placeId = p.placeId AND p.cityId = $cityId AND p.genre != 1
							GROUP BY uc.placeId ORDER BY cnt DESC LIMIT 3
							)
							UNION
							(
							SELECT p.placeId, p.name, p.genre, p.address, p.latitude, p.longitude, 0, 5 AS priority
							FROM places AS p
							WHERE p.cityId = $cityId AND p.genre != 1
							ORDER BY RAND()
							)	
						) AS result
						GROUP BY placeId
						ORDER BY priority
						LIMIT 10
						";
						
				$query = mysql_query($sql);
				$place = array(
					'placeId' => 0,
					'name' => 0,
					'genre' => 0,
					'address' => 0,
					'lat' => 0,
					'lng' => 0
				);
				
				$places = array($place, $place, $place, $place, $place, $place, $place, $place, $place, $place);
				$itr = 0;
				
				while ($rowPlace=mysql_fetch_array($query)) 
				{
					$places[$itr] = array(
						'placeId' => $rowPlace['placeId'],
						'name' => $rowPlace['name'],
						'genre' => $rowPlace['genre'],
						'address' => $rowPlace['address'],
						'lat' => $rowPlace['latitude'],
						'lng' => $rowPlace['longitude']
					);
					
					++$itr;
				}
					
				$placeId0 = $places[0]['placeId'];
				$placeId1 = $places[1]['placeId'];
				$placeId2 = $places[2]['placeId'];
				$placeId3 = $places[3]['placeId'];
				$placeId4 = $places[4]['placeId'];
				$placeId5 = $places[5]['placeId'];
				$placeId6 = $places[6]['placeId'];
				$placeId7 = $places[7]['placeId'];
				$placeId8 = $places[8]['placeId'];
				$placeId9 = $places[9]['placeId'];
				
				$sql = "INSERT INTO users_rendezvous_potential (userId, candidateId, placeId0, placeId1, placeId2, placeId3, placeId4, placeId5, placeId6, placeId7, placeId8, placeId9, createTime) 
						VALUES ($userId, $personId, $placeId0, $placeId1, $placeId2, $placeId3, $placeId4, $placeId5, $placeId6, $placeId7, $placeId8, $placeId9, CURRENT_TIMESTAMP() )";
				
				$query = mysql_query($sql);
				$id = mysql_insert_id();
				
				$sql = "SELECT createTime FROM users_rendezvous_potential WHERE id = $id";
				$query = mysql_query($sql);
				$row=mysql_fetch_array($query);
				
				$potentialRendezvous=array(
					'id' => $id,
					'createTime' => $row['createTime'],
					'places' => $places
		        );
		
				echo json_encode(array( 'ok' => '1', 'r' => $potentialRendezvous ));
			}
			else
				echo json_encode(array( 'ok' => '1' ));
			/*
		    $sql = "SELECT u.username, p.name FROM users as u, places as p WHERE u.userId = $userId AND p.placeId = $placeId";
		    $query = mysql_query($sql);
   
		    if (mysql_num_rows($query) > 0)
		    {
		        $row=mysql_fetch_array($query);
   
		        $APPLICATION_ID = "ujzhrnPvHlLj7oX5wrHdZguIjfLGKZuLhFfqFcnn";
		        $REST_API_KEY = "wYEDKZywwm3rOWoeRrAtOoQRWrrNY3WnOCXTwJnI";

		        $url = 'https://api.parse.com/1/push';
		        $data = array(
		            'channel' => 'user' . strval($personId),
		            'type' => 'android',
		            'data' => array(
		                'alert' => $row['username'] . ' flirted with you!',
		                'title' => '@ ' . $row['name'],
		                'username' => $row['username'],
		                'messCode' => 1,
		                'action' => 'MyAction'
		            ),
		        );
		        $_data = json_encode($data);
		        $headers = array(
		            'X-Parse-Application-Id: ' . $APPLICATION_ID,
		            'X-Parse-REST-API-Key: ' . $REST_API_KEY,
		            'Content-Type: application/json',
		            'Content-Length: ' . strlen($_data),
		        );

		        $curl = curl_init($url);
		        curl_setopt($curl, CURLOPT_POST, 1);
		        curl_setopt($curl, CURLOPT_POSTFIELDS, $_data);
		        curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
		        curl_setopt($curl, CURLOPT_RETURNTRANSFER,1);  
		        curl_exec($curl);
		    }
		*/	
		}
		else
			echo json_encode(array( 'ok' => '0', 'error' => '6' ));	
	}
}
else
	echo json_encode(array( 'ok' => '0', 'error' => '2' ));	
	
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
}?>
