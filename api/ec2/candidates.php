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

$userId = mysql_real_escape_string($userId);
$jsonPeople = array();
$userId = 72;
	
// GET PROFILE
$sql = "SELECT u.userId, u.placeId, p.name, p.genre, p.latitude, p.longitude, p.starMaleId, p.starFemaleId, u.username, u.placeId, u.sex, u.interestedIn, u.birthdate, 
				u.tonight, u.dontApproach, u.lookingFor, 
				u.aboutMe, u.occupation, u.education, u.interests, u.activities, u.travel, u.music, u.drinks, u.myPerfectNightOut, 
				u.tipComeAndSayHi, u.tipBuyMeADrink, u.tipInviteMeToDance, u.tipMakeMeLaugh, u.tipMeetMyFriends, u.tipSurpriseMe, 
				u.dontExpectAnything, u.dontBePersistent, u.dontBeDrunk, u.personalAdvice,
				IF((u.userId IN (SELECT userId FROM users_invitations WHERE candidateId=$userId AND requestTime >= DATE_SUB(NOW(),INTERVAL 24 HOUR))),1,0) AS invitationReceived
				
		FROM (SELECT cityId, sex, interestedIn, wantedMinAge, wantedMaxAge, birthdate FROM users WHERE userId = $userId) AS me, users AS u
		LEFT JOIN places AS p
		ON u.placeId = p.placeId
		WHERE u.cityId = me.cityId
		
		AND ( (me.interestedIn=u.sex AND u.interestedIn=me.sex) OR (me.interestedIn=3 AND u.interestedIn=me.sex) OR 
			(u.interestedIn=3 AND me.interestedIn=u.sex) OR (u.interestedIn=3 AND u.interestedIn=me.interestedIn)  )
			
		AND u.userId != $userId
			
		AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= u.wantedMinAge
		AND YEAR(CURDATE()) - YEAR(me.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(me.birthdate), '-', DAY(me.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= u.wantedMaxAge
		AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) >= me.wantedMinAge
		AND YEAR(CURDATE()) - YEAR(u.birthdate) - IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(u.birthdate), '-', DAY(u.birthdate)) ,'%Y-%c-%e') > CURDATE(), 1, 0) <= me.wantedMaxAge
			
		AND u.userId NOT IN
    	( 
			SELECT ub.userId 
			FROM users_blocks as ub 
      		WHERE ub.userBlockedId = $userId
		)
	
		AND u.userId NOT IN
    	( 
			SELECT ub.userBlockedId 
			FROM users_blocks as ub 
      		WHERE ub.userId = $userId
		)
		
		AND u.userId NOT IN
    	( 
			SELECT ulmr.candidateId
			FROM users_invitations AS ulmr
			WHERE ulmr.userId = $userId AND ulmr.candidateId = u.userId AND ulmr.requestTime >= DATE_SUB(NOW(),INTERVAL 24 HOUR)
		)
		
		AND u.userId NOT IN
    	( 
			SELECT unt.userNoThanksId
			FROM users_no_thanks AS unt
			WHERE unt.userId = $userId AND unt.userNoThanksId = u.userId
		)
		
		ORDER BY invitationReceived LIMIT 500
		";


$candidatesQuery = mysql_query($sql);

// If we find a match, create an array of data, json_encode it and echo it out
if (mysql_num_rows($candidatesQuery) > 0)
{

    while ($rowAbout=mysql_fetch_array($candidatesQuery)) 
    {
		$personId = $rowAbout['userId'];
	
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
				GROUP BY p.placeId ORDER BY cnt DESC";
			
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
	
	
		// CHECK IF STAR
	    $star = 0;
	    if( $rowAbout['starMaleId'] == $rowAbout['userId'] || $rowAbout['starFemaleId'] == $rowAbout['userId']  ) 
	        $star = 1;




			
	
		// mark as visited
		if( $userId != $personId )
		{
				
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
	
	
	


	
		// FINAL RESULT		
        $jsonPeople[]=array(

            'userId' => $rowAbout['userId'],
			'username' => $rowAbout['username'],

			// BASIC INFO
            'sex' => $rowAbout['sex'],
            'interestedIn' => $rowAbout['interestedIn'],
            'birthday' => $rowAbout['birthdate'],

			// PHOTOS
			'profilePhotosFilenames' => $profilePhotosFilenames,
		

			// ABOUT
			'aboutMe' => $rowAbout['aboutMe'],
			'occupation' => $rowAbout['occupation'],
			'education' => $rowAbout['education'],
			'interests' => $rowAbout['interests'],
            'activities' => $rowAbout['activities'],
			'travel' => $rowAbout['travel'],
			'music' => $rowAbout['music'],
			'drinks' => $rowAbout['drinks'],
			'myPerfectNightOut' => $rowAbout['myPerfectNightOut'],
		
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
			'tipMakeMeLaugh' => $rowAbout['tipMakeMeLaugh'],
            'tipMeetMyFriends' => $rowAbout['tipMeetMyFriends'],
            'tipSurpriseMe' => $rowAbout['tipSurpriseMe'],
          	'dontExpectAnything' => $rowAbout['dontExpectAnything'],
			'dontBePersistent' => $rowAbout['dontBePersistent'],
            'dontBeDrunk' => $rowAbout['dontBeDrunk'],
			'personalAdvice' => $rowAbout['personalAdvice'],
		
			// MORE
            'tonight' => $rowAbout['tonight'],
            'dontApproach' => $rowAbout['dontApproach'],
			'lookingFor' => $rowAbout['lookingFor'],
		
		
			// STATUS
			'invitationReceived' => $rowAbout['invitationReceived'],
            'star' => $star
        );
    }
}

// potential invitations
$sql = "SELECT id, u.userId, candidateId, u.username, u.sex, u.interestedIn, u.wantedMinAge, u.wantedMaxAge, u.birthdate, u.lookingFor,
		placeId0, 
		placeId1,
		placeId2, 
		placeId3,
		placeId4, 
		placeId5, 
		placeId6, 
		placeId7, 
		placeId8, 
		placeId9, 
		time0,
		time1,
		time2,
		time3,
		time4,
		time5,
		time6,
		time7,
		time8,
		time9,
		IF((upr.userId = $userId),1,0) AS firstAnswer,
		userAnswered,
		seenByUser,
		seenByCandidate,
		createTime
		FROM users_rendezvous_potential AS upr, users AS u
		WHERE ( (upr.userId = $userId AND candidateId = u.userId) OR (candidateId = $userId AND upr.userId = u.userId) )
		AND createTime >= date_sub(now(),INTERVAL 24 HOUR)
		AND ( (upr.userId = $userId AND userAnswered=0) OR (upr.userId != $userId AND userAnswered=1) )
		ORDER BY createTime DESC
		";
$query = mysql_query($sql);

$jsonPotentialRendezvous = array();

while ($rowPotentialRendezvous=mysql_fetch_array($query)) 
{
	$firstAnswer = $rowPotentialRendezvous['firstAnswer'];
	$userAnswered = $rowPotentialRendezvous['userAnswered'];
	$candidateAnswered = $rowPotentialRendezvous['candidateAnswered'];
	
	
	
	$places = array();

	$person = array(
	    'userId' => $rowPotentialRendezvous['userId'],
        'username' => $rowPotentialRendezvous['username'],
		'sex' => $rowPotentialRendezvous['sex'],
		'interestedIn' => $rowPotentialRendezvous['interestedIn'],
        'birthday' => $rowPotentialRendezvous['birthdate'],
        'lookingFor' => $rowPotentialRendezvous['lookingFor']
	);

	if( $userAnswered==1 )
	{
		$placeIds = array();
		
		if( $rowPotentialRendezvous['placeId0'] != 0 )	
			array_push( $placeIds, $rowPotentialRendezvous['placeId0'] );
			
		if( $rowPotentialRendezvous['placeId1'] != 0 )	
			array_push( $placeIds, $rowPotentialRendezvous['placeId1'] );
				
		if( $rowPotentialRendezvous['placeId2'] != 0 )	
			array_push( $placeIds, $rowPotentialRendezvous['placeId2'] );
			
		if( $rowPotentialRendezvous['placeId3'] != 0 )	
			array_push( $placeIds, $rowPotentialRendezvous['placeId3'] );
			
		if( $rowPotentialRendezvous['placeId4'] != 0 )	
			array_push( $placeIds, $rowPotentialRendezvous['placeId4'] );
			
		if( $rowPotentialRendezvous['placeId5'] != 0 )	
			array_push( $placeIds, $rowPotentialRendezvous['placeId5'] );	
			
		if( $rowPotentialRendezvous['placeId6'] != 0 )	
			array_push( $placeIds, $rowPotentialRendezvous['placeId6'] );	
			
		if( $rowPotentialRendezvous['placeId7'] != 0 )	
			array_push( $placeIds, $rowPotentialRendezvous['placeId7'] );
			
		if( $rowPotentialRendezvous['placeId8'] != 0 )	
			array_push( $placeIds, $rowPotentialRendezvous['placeId8'] );	
			
		if( $rowPotentialRendezvous['placeId9'] != 0 )	
			array_push( $placeIds, $rowPotentialRendezvous['placeId9'] );
		
		if( sizeof($placeIds)>0)
			$placeIds = implode(',',$placeIds);  
		else
			$placeIds = 0;
		
		$sqlPlaces = "SELECT placeId, name, genre, address, latitude, longitude
				FROM places
				WHERE placeId IN ($placeIds)";
						
		$queryPlaces = mysql_query($sqlPlaces);

		while ($rowPlaces=mysql_fetch_array($queryPlaces)) 
		{
			$places[] = array(
				'placeId' => $rowPlaces['placeId'],
				'name' => $rowPlaces['name'],
				'genre' => $rowPlaces['genre'],
				'address' => $rowPlaces['address'],
				'lat' => $rowPlaces['latitude'],
				'lng' => $rowPlaces['longitude']
			);
		}

	}
	
	// TIMES
	$times = array();
	
	if( $firstAnswer==1 || ($firstAnswer==0 && $rowPotentialRendezvous['time0']==1) )	
		$times[0] = 1;
	else
		$times[0] = 0;
	
	if( $firstAnswer==1 || ($firstAnswer==0 && $rowPotentialRendezvous['time1']==1) )	
		$times[1] = 1;
	else
		$times[1] = 0;
			
	if( $firstAnswer==1 || ($firstAnswer==0 && $rowPotentialRendezvous['time2']==1) )	
		$times[2] = 1;
	else
		$times[2] = 0;

	if( $firstAnswer==1 || ($firstAnswer==0 && $rowPotentialRendezvous['time3']==1) )	
		$times[3] = 1;
	else
		$times[3] = 0;

	if( $firstAnswer==1 || ($firstAnswer==0 && $rowPotentialRendezvous['time4']==1) )	
		$times[4] = 1;
	else
		$times[4] = 0;
	
	if( $firstAnswer==1 || ($firstAnswer==0 && $rowPotentialRendezvous['time5']==1) )	
		$times[5] = 1;
	else
		$times[5] = 0;

	if( $firstAnswer==1 || ($firstAnswer==0 && $rowPotentialRendezvous['time6']==1) )	
		$times[6] = 1;
	else
		$times[6] = 0;

	if( $firstAnswer==1 || ($firstAnswer==0 && $rowPotentialRendezvous['time7']==1) )	
		$times[7] = 1;
	else
		$times[7] = 0;
	
	if( $firstAnswer==1 || ($firstAnswer==0 && $rowPotentialRendezvous['time8']==1) )	
		$times[8] = 1;
	else
		$times[8] = 0;

	if( $firstAnswer==1 || ($firstAnswer==0 && $rowPotentialRendezvous['time9']==1) )	
		$times[9] = 1;
	else
		$times[9] = 0;
	
	$seen = 0;
	
	if( $rowPotentialRendezvous['candidateId'] = $userId )
		$seen = $rowPotentialRendezvous['seenByCandidate'];
	else
		$seen = $rowPotentialRendezvous['seenByUser'];
		
	$jsonPotentialRendezvous[]=array(
		'id' => $rowPotentialRendezvous['id'],
		'createTime' => $rowPotentialRendezvous['createTime'],
		'person' => $person,
        'places' => $places,
		'times' => $times,
		'firstAnswer' => $rowPotentialRendezvous['firstAnswer'],
		'seen' => $seen
    );
}


// CONFIRMED
$sql = "SELECT urc.id, urc.createTime, urc.placeId, urc.timeSlot, urc.cancelled, urc.cancelReason, urc.seenByUser, urc.seenByCandidate,
		p.name, p.genre, p.address, p.latitude, p.longitude, 
		u.userId, u.username, u.sex, u.interestedIn, u.wantedMinAge, u.wantedMaxAge, u.birthdate, u.lookingFor
		FROM users_rendezvous_confirmed AS urc, places AS p, users AS u
		WHERE ( (urc.userId = $userId AND urc.candidateId = u.userId) OR (urc.candidateId = $userId AND urc.userId = u.userId) )
		AND urc.placeId = p.placeId
		AND createTime >= date_sub(now(),INTERVAL 24 HOUR)
		ORDER BY createTime DESC";
$query = mysql_query($sql);

$jsonConfirmedRendezvous = array();

while ($rowConfirmedRendezvous=mysql_fetch_array($query)) 
{
	
	$person = array(
	    'userId' => $rowConfirmedRendezvous['userId'],
        'username' => $rowConfirmedRendezvous['username'],
		'sex' => $rowConfirmedRendezvous['sex'],
		'interestedIn' => $rowConfirmedRendezvous['interestedIn'],
        'birthday' => $rowConfirmedRendezvous['birthdate'],
        'lookingFor' => $rowConfirmedRendezvous['lookingFor']
	);
	
	$place = array(
		'placeId' => $rowConfirmedRendezvous['placeId'],
		'name' => $rowConfirmedRendezvous['name'],
		'genre' => $rowConfirmedRendezvous['genre'],
		'address' => $rowConfirmedRendezvous['address'],
		'lat' => $rowConfirmedRendezvous['latitude'],
		'lng' => $rowConfirmedRendezvous['longitude']
	);
	
	
	$seen = 0;
	
	if( $rowConfirmedRendezvous['userId'] = $userId )
		$seen = $rowConfirmedRendezvous['seenByCandidate'];
	else
		$seen = $rowConfirmedRendezvous['seenByUser'];
		
		
	$jsonConfirmedRendezvous[]=array(
		'id' => $rowConfirmedRendezvous['id'],
		'createTime' => $rowConfirmedRendezvous['createTime'],
		'person' => $person,
		'place' => $place,
		'timeSlot' => $rowConfirmedRendezvous['timeSlot'],
		'cancelled' => $rowConfirmedRendezvous['cancelled'],
		'cancelReason' => $rowConfirmedRendezvous['cancelReason'],
		'seen' => $seen
    );
	
}

$json = array(
	'candidates' => $jsonPeople,
	'potentialRendezvous' => $jsonPotentialRendezvous,
	'confirmedRendezvous' => $jsonConfirmedRendezvous
);

echo json_encode(array( 'r'  =>  $json, 'ok' => '1' ));

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
