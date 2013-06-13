<?php





	// UPLOAD IMAGE
	$baseOriginal=$_POST['stringBase64Original'];
	$userId=$_POST['userId'];
	
	mkdir('/opt/app/current/signalsS3/user_images/' . $userId);
	mkdir('/opt/app/current/signalsS3/user_images/' . $userId . '/otherPhotos');
	
	
	$binaryOriginal = base64_decode($baseOriginal);
	
	header('Content-Type: bitmap; charset=utf-8');
	
	$imgFileStr = time() . '.jpg';
	
	// STORE ORIGINAL
	$imgFile = fopen('signalsS3/user_images/' . $userId . '/otherPhotos/' . $imgFileStr, 'wb');
	fwrite($imgFile, $binaryOriginal);
   	fclose($imgFile);
	
	

	
	echo json_encode(array( 'ok' => '1', 'r' => $imgFileStr ));
	
	
	function toASCII( $str )
	    {
	        return strtr(utf8_decode($str), 
	            utf8_decode('ŠŒŽšœžŸ¥µÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýÿ'),
	            'SOZsozYYuAAAAAAACEEEEIIIIDNOOOOOOUUUUYsaaaaaaaceeeeiiiionoooooouuuuyy');
	    }
?>

