 <?php
header("Content-type: application/json");

require_once '/opt/app/AWSSDKforPHP/sdk.class.php';

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


// prevent sql injection
$userId = mysql_real_escape_string($userId);
$personId = mysql_real_escape_string($personId);



// GET PROFILE
$sql = "SELECT u.userId, u.placeId, p.name, p.genre, p.latitude, p.longitude, p.starMaleId, p.starFemaleId, u.username, u.placeId, u.sex, u.interestedIn, u.birthdate, 
				u.tonight, u.dontApproach, u.lookingFor, 
				u.selfSummary, u.hometown, u.occupation, u.education, u.music, u.drinks, u.interests, 
				u.tipComeAndSayHi, u.tipBuyMeADrink, u.tipInviteMeToDance, u.tipSurpriseMe, 
				u.dontUsePickupLines, u.dontCornerMe, u.dontBePersistent, u.dontBeDrunk, u.extraAdvice,
				(u.userId IN (SELECT userBlockedId FROM users_blocks WHERE userId = $userId)) AS blocked
		FROM users AS u
		LEFT JOIN places AS p
		ON u.placeId = p.placeId
		WHERE u.userId = $personId
		AND u.userId NOT IN
    	( 
			SELECT ub.userId 
			FROM users_blocks as ub 
      		WHERE ub.userBlockedId = $userId
		)";


$query = mysql_query($sql);

// If we find a match, create an array of data, json_encode it and echo it out
if (mysql_num_rows($query) > 0)
{
	$rowAbout=mysql_fetch_array($query);

    if($query)
    {
		// PHOTOS FROM S3
		$s3 = new AmazonS3();
		$bucket = 'signals';
		
	    $response = $s3->list_objects($bucket, array(
		    'prefix' => 'user_images/' . $personId . '/profilePhotos/'
		));

		$arr = $response->body->Contents;
		$profilePhotosFilenames = array();
		foreach($arr as $item)
		{
		   if(isset($item->Key))
		   {
				$str = $item->Key;
				$str = str_replace('user_images/' . $personId . '/profilePhotos/', '', $str);
		
				array_push($profilePhotosFilenames, $str);
		   }
		}
		
		$response = $s3->list_objects($bucket, array(
		    'prefix' => 'user_images/' . $personId . '/otherPhotos/'
		));

		$arr = $response->body->Contents;
		$otherPhotosFilenames = array();
		foreach($arr as $item)
		{
		   if(isset($item->Key))
		   {
				$str = $item->Key;
				$str = str_replace('user_images/' . $personId . '/otherPhotos/', '', $str);
		
				array_push($otherPhotosFilenames, $str);
		   }
		}
		
		
		// activity
		$sql = "SELECT uc.placeId, p.genre, p.name, COUNT(*) AS cnt
				FROM places AS p, users_checkins AS uc
				WHERE uc.userId = $personId AND uc.placeId = p.placeId
				GROUP BY uc.placeId ORDER BY cnt DESC LIMIT 3";
				
		$jsonTopFrequentedBars  = array();
		$query = mysql_query($sql);
		
		if ($query)
		{
		    while ($rowTopFrequentedBars=mysql_fetch_array($query)) 
		    {
				$jsonTopFrequentedBars[]=array(
	                'placeId' => $rowTopFrequentedBars['placeId'],
	                'genre' => $rowTopFrequentedBars['genre'],
	                'name' => $rowTopFrequentedBars['name']
	            );
			}
		}
		
		$sql = "SELECT us.placeId, p.genre, p.name, COUNT(*) AS cnt
				FROM places AS p, users_stars AS us
				WHERE us.userId = $personId AND us.placeId = p.placeId
				GROUP BY us.placeId ORDER BY cnt DESC";
				
		$jsonCollectedBarStars  = array();
		$query = mysql_query($sql);
		
		if ($query)
		{
		    while ($rowCollectedBarStars=mysql_fetch_array($query)) 
		    {
				$jsonCollectedBarStars[]=array(
	                'placeId' => $rowCollectedBarStars['placeId'],
	                'genre' => $rowCollectedBarStars['genre'],
	                'name' => $rowCollectedBarStars['name'],
					'starCount' => $rowCollectedBarStars['cnt']
	            );
			}
		}
		
				
		
		// flirted, can vote and voted
		$flirted = 0;
		$canBeVoted = 1;
		$voted = 0;
		
		if( $userId != $personId )
		{
			$sql = "SELECT placeId, voteId, (date(now() ) = date(voteTime) AND hour(now() ) = hour(voteTime)) AS cannotBeVoted FROM users WHERE userId = $userId";
			$query = mysql_query($sql);
		
			if ($query)
			{
				$rowVote=mysql_fetch_array($query);
			
				if( $rowVote['cannotBeVoted'] == 1)
					$canBeVoted=0;
					
				if( $rowVote['voteId'] == $rowAbout['userId'] && $canBeVoted == 0 )
					$voted=1;
			}
		
			// flirted
			$sql = "SELECT uf.userId
					FROM users_flirts AS uf 
					WHERE uf.userId = $userId AND uf.userFlirtedId = $personId AND uf.flirtTime >= DATE_SUB(NOW(),INTERVAL 12 HOUR)";
			$query = mysql_query($sql);
		
			if (mysql_num_rows($query) > 0)
			{
				$flirted = 1;
			}
			
			// mark as visited
			if( $rowAbout['placeId'] == $rowVote['placeId'] )
			{
				$placeId = $rowAbout['placeId'];

				$sql = "SELECT uv.userId
						FROM users_visits AS uv
						WHERE uv.userId = $userId AND uv.userVisitedId = $personId AND uv.visitTime >= DATE_SUB(NOW(),INTERVAL 12 HOUR)";
				$query = mysql_query($sql);

				if (mysql_num_rows($query) == 0)
				{
					$sql = "INSERT INTO users_visits (userId, userVisitedId, placeId, visitTime) VALUES ($userId, $personId, $placeId, CURRENT_TIMESTAMP())";
					$query = mysql_query($sql);
				}
			}
		}
		
		

		// CHECK IF STAR
	    $star = 0;
	    if( $rowAbout['starMaleId'] == $rowAbout['userId'] || $rowAbout['starFemaleId'] == $rowAbout['userId']  ) 
	        $star = 1;
		

		
		// FINAL RESULT		
        $json=array(
	
            'userId' => $rowAbout['userId'],
			'username' => $rowAbout['username'],

			// BASIC INFO
            'sex' => $rowAbout['sex'],
            'interestedIn' => $rowAbout['interestedIn'],
            'birthday' => $rowAbout['birthdate'],

			// PHOTOS
			'profilePhotosFilenames' => $profilePhotosFilenames,
			'otherPhotosFilenames' => $otherPhotosFilenames,
			
	
			// ABOUT
			'selfSummary' => $rowAbout['selfSummary'],
            'hometown' => $rowAbout['hometown'],
			'occupation' => $rowAbout['occupation'],
			'education' => $rowAbout['education'],
            'favoriteMusic' => $rowAbout['music'],
			'favoriteDrinks' => $rowAbout['drinks'],
			'interests' => $rowAbout['interests'],
			
			// ACTIVITY
            'placeId' => $rowAbout['placeId'],
			'name' => $rowAbout['name'],
			'genre' => $rowAbout['genre'],
			'lat' => $rowAbout['latitude'],
			'lng' => $rowAbout['longitude'],
			'topFrequentedBars' => $jsonTopFrequentedBars,
			'collectedBarStars' => $jsonCollectedBarStars,

			// APPROACH
            'tipComeAndSayHi' => $rowAbout['tipComeAndSayHi'],
			'tipBuyMeADrink' => $rowAbout['tipBuyMeADrink'],
			'tipInviteMeToDance' => $rowAbout['tipInviteMeToDance'],
            'tipSurpriseMe' => $rowAbout['tipSurpriseMe'],
          	'dontUsePickupLines' => $rowAbout['dontUsePickupLines'],
			'dontCornerMe' => $rowAbout['dontCornerMe'],
			'dontBePersistent' => $rowAbout['dontBePersistent'],
            'dontBeDrunk' => $rowAbout['dontBeDrunk'],
			'extraAdvice' => $rowAbout['extraAdvice'],
			
			// MORE
            'tonight' => $rowAbout['tonight'],
            'dontApproach' => $rowAbout['dontApproach'],
			'lookingFor' => $rowAbout['lookingFor'],
			
			
			// STATUS
			'flirted' => $flirted,
			'voted' => $voted,
			'blocked' => $rowAbout['blocked'],
			'canBeVoted' => $canBeVoted,
            'star' => $star


        );

        echo json_encode(array( 'r'  =>  $json, 'ok' => '1' ));
    }
	else	
        echo json_encode(array( 'ok' => '0', 'error' => '2' ));	
}
else
{
	echo json_encode(array( 'ok' => '0', 'error' => '1' ));	
}

mysql_close($con);


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
