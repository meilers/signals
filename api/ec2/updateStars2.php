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


    $sql = "UPDATE places SET starFemaleId=0, starMaleId=0";	
            
    $query = mysql_query($sql); 

	if(!$query)
		echo 'error in setting stars to 0';
	
    // GET VOTES ON MALES
    $sql = "UPDATE places INNER JOIN 
			(
				SELECT T.userVotedId, T.placeId
				FROM 
				(
					SELECT uvo.userVotedId, p.placeId, count(*) AS cnt 
			    	FROM users as u, users_votes as uvo, places as p 
			    	WHERE u.userId = uvo.userVotedId AND p.placeId = uvo.placeId 
					AND uvo.voteTime >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
					AND u.sex = 1
			    	GROUP BY userVotedId, placeId
				) T 
				JOIN 
				(
					SELECT t.placeId, Max(t.cnt) maxcnt
					FROM 
					(
						SELECT uvo.userVotedId, p.placeId, count(*) AS cnt 
			        	FROM users as u, users_votes as uvo, places as p 
				    	WHERE u.userId = uvo.userVotedId AND p.placeId = uvo.placeId 
						AND uvo.voteTime >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
						AND u.sex = 1
			        	GROUP BY userVotedId, placeId
					) t
					GROUP BY t.placeId
				) T2 
				ON T.placeId = T2.placeId AND T.cnt = T2.maxcnt AND T.cnt >= 1
				GROUP BY T.placeId
			)
			AS other_table ON places.placeId = other_table.placeId
			SET places.starMaleId = other_table.userVotedId";	
            
    $query = mysql_query($sql);  
            
	if(!$query)
		echo 'error in assigning male stars';
		
    // GET VOTES ON FEMALES
    $sql = "UPDATE places INNER JOIN 
			(
				SELECT T.userVotedId, T.placeId
				FROM 
				(
					SELECT uvo.userVotedId, p.placeId, count(*) AS cnt 
			    	FROM users as u, users_votes as uvo, places as p 
			    	WHERE u.userId = uvo.userVotedId AND p.placeId = uvo.placeId 
					AND uvo.voteTime >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
					AND u.sex = 2
			    	GROUP BY userVotedId, placeId
				) T 
				JOIN 
				(
					SELECT t.placeId, Max(t.cnt) maxcnt
					FROM 
					(
						SELECT uvo.userVotedId, p.placeId, count(*) AS cnt 
			        	FROM users as u, users_votes as uvo, places as p 
				    	WHERE u.userId = uvo.userVotedId AND p.placeId = uvo.placeId 
						AND uvo.voteTime >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
						AND u.sex = 2
			        	GROUP BY userVotedId, placeId
					) t
					GROUP BY t.placeId
				) T2 
				ON T.placeId = T2.placeId AND T.cnt = T2.maxcnt AND T.cnt >= 1
				GROUP BY T.placeId
			)
			AS other_table ON places.placeId = other_table.placeId
			SET places.starFemaleId = other_table.userVotedId";	
            
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
?>
