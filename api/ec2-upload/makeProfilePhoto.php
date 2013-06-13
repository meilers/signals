<?php

	// UPLOAD IMAGE
	$baseThumbnail=$_POST['stringBase64Crop'];
	$userId=$_POST['userId'];
	
	mkdir('/opt/app/current/signalsS3/user_images/' . $userId);
	
	
   	$binaryThumbnail = base64_decode($baseThumbnail);
	
	header('Content-Type: bitmap; charset=utf-8');
	
	
	// STORE SQUARE
	$imgFile = fopen('signalsS3/user_images/' . $userId . '/profilePhotoSquare.jpg', 'wb');
	fwrite($imgFile, $binaryThumbnail);
   	fclose($imgFile);

	/*
	// STORE THUMBNAIL
	require_once 'phar://imagine.phar';

	//var_dump(interface_exists('Imagine\Image\ImageInterface'));

	$imagine = new Imagine\Gd\Imagine();
	// or
	//$imagine = new Imagine\Imagick\Imagine();
	// or
	//$imagine = new Imagine\Gmagick\Imagine();

	$size    = new Imagine\Image\Box(200, 200);

	$mode    = Imagine\Image\ImageInterface::THUMBNAIL_INSET;
	// or
	//$mode    = Imagine\Image\ImageInterface::THUMBNAIL_OUTBOUND;
	
	$imagine->open('signalsS3/user_images/' . $userId . '/profilePhotoSquare.jpg')
	    ->thumbnail($size, $mode)
	    ->save('signalsS3/user_images/' . $userId . '/profilePhotoThumb.jpg')
	;
	*/
	
	echo json_encode(array( 'ok' => '1' ));
	
	
	function toASCII( $str )
	    {
	        return strtr(utf8_decode($str), 
	            utf8_decode('ŠŒŽšœžŸ¥µÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýÿ'),
	            'SOZsozYYuAAAAAAACEEEEIIIIDNOOOOOOUUUUYsaaaaaaaceeeeiiiionoooooouuuuyy');
	    }
?>

