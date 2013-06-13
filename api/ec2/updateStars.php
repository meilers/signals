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

// reset votes
$sql = "UPDATE places SET starMaleId=0, starFemaleId=0";	

$query = mysql_query($sql);

	
    // GET VOTES FOR FEMALES
    $sql = "UPDATE places INNER JOIN 
			(
				SELECT T.voteId, T.placeId
				FROM 
				(
					SELECT voteId, placeId, count(*) AS cnt 
					FROM users as u
					WHERE voteId != 0 AND votedId
					AND voteTime >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
					GROUP BY voteId, placeId
				) T 
				JOIN 
				(
					SELECT t.placeId, Max(t.cnt) maxcnt
					FROM 
					(
						SELECT voteId, placeId, count(*) AS cnt 
						FROM users as u
						WHERE sex = 1 AND voteId != 0
						AND voteTime >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
						GROUP BY voteId, placeId
					) t
					GROUP BY t.placeId
				) T2 
				ON T.placeId = T2.placeId AND T.cnt = T2.maxcnt AND T.cnt >= 1
				GROUP BY T.placeId
			)
			AS other_table ON places.placeId = other_table.placeId
			SET places.starFemaleId = other_table.voteId";	
            
    $query = mysql_query($sql);  
            
	if(!$query)
		echo 'error in processing st male votes';
		
		
	// GET VOTES FOR FEMALES
    $sql = "UPDATE places INNER JOIN 
			(
				SELECT T.voteId, T.placeId
				FROM 
				(
					SELECT u.voteId, u.placeId, count(*) AS cnt 
					FROM users as u, users as other_user
					WHERE u.voteId != 0 AND u.voteId = other_user.userId AND other_user.sex = 2
					AND u.voteTime >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
					GROUP BY u.voteId, u.placeId
				) T 
				JOIN 
				(
					SELECT t.placeId, Max(t.cnt) maxcnt
					FROM 
					(
						SELECT u.voteId, u.placeId, count(*) AS cnt 
						FROM users as u, users as other_user
						WHERE u.voteId != 0 AND u.voteId = other_user.userId AND other_user.sex = 2
						AND u.voteTime >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
						GROUP BY u.voteId, u.placeId
					) t
					GROUP BY t.placeId
				) T2 
				ON T.placeId = T2.placeId AND T.cnt = T2.maxcnt AND T.cnt >= 1
				GROUP BY T.placeId
			)
			AS other_table ON places.placeId = other_table.placeId
			SET places.starFemaleId = other_table.voteId";	
            
    $query = mysql_query($sql);  
            
	if(!$query)
		echo 'error in processing gay male votes';
		
			
		
    // GET VOTES FOR MALES
    $sql = "UPDATE places INNER JOIN 
			(
				SELECT T.voteId, T.placeId
				FROM 
				(
					SELECT u.voteId, u.placeId, count(*) AS cnt 
					FROM users as u, users as other_user
					WHERE u.voteId != 0 AND u.voteId = other_user.userId AND other_user.sex = 1
					AND u.voteTime >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
					GROUP BY u.voteId, u.placeId
				) T 
				JOIN 
				(
					SELECT t.placeId, Max(t.cnt) maxcnt
					FROM 
					(
						SELECT u.voteId, u.placeId, count(*) AS cnt 
						FROM users as u, users as other_user
						WHERE u.voteId != 0 AND u.voteId = other_user.userId AND other_user.sex = 1
						AND u.voteTime >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
						GROUP BY u.voteId, u.placeId
					) t
					GROUP BY t.placeId
				) T2 
				ON T.placeId = T2.placeId AND T.cnt = T2.maxcnt AND T.cnt >= 1
				GROUP BY T.placeId
			)
			AS other_table ON places.placeId = other_table.placeId
			SET places.starMaleId = other_table.voteId";	

            
    $query = mysql_query($sql);  
            
	if(!$query)
		echo 'error in assigning female stars';
		
		
	$sql = "SELECT p.starMaleId, p.starFemaleId, p.placeId FROM places as p WHERE (p.starMaleId != 0 OR p.starFemaleId != 0)";
	
    $query = mysql_query($sql);  

    if ($query)
	{        
	    while ($row=mysql_fetch_array($query)) 
	    {
			$placeId = $row['placeId'];
				
			if($row['starMaleId'] != 0)
			{
				$userId = $row['starMaleId'];
				
				$sql = "INSERT INTO users_stars (userId, placeId, starTime) VALUES ($userId, $placeId, UTC_TIMESTAMP())";
				$query = mysql_query($sql);  
				
				if(!$query)
					echo 'error in assigning male star ' . strval($row['starMaleId']);
			}
			
			if($row['starFemaleId'] != 0)
			{
				$userId = $row['starFemaleId'];
				
				$sql = "INSERT INTO users_stars (userId, placeId, starTime) VALUES ($userId, $placeId, UTC_TIMESTAMP())";
				$query = mysql_query($sql);  
				
				if(!$query)
					echo 'error in assigning female star ' . strval($row['starFemaleId']);
			}
		}
	}	
	else
		echo 'error getting stars';
	
	
	// reset votes
	$sql = "UPDATE users SET voteId=0";	

    $query = mysql_query($sql); 

	if(!$query)
		echo 'error in setting stars to 0';
?>
