<?php

	$userId=$_POST['userId'];
	
	$filePath = '/opt/app/current/signalsS3/user_images/' . $userId . $_POST['filename'];
		
	$success = unlink($filePath);
	
	if( $success )
		echo json_encode(array( 'ok' => '1') );
	else
		echo json_encode(array( 'ok' => '0') );
	
?>

