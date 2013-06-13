<?php





	// UPLOAD IMAGE
	$baseThumbnail=$_POST['stringBase64Crop'];
	$baseOriginal=$_POST['stringBase64Original'];
	$userId=$_POST['userId'];
	
	mkdir('/opt/app/current/signalsS3/user_images/' . $userId);
	mkdir('/opt/app/current/signalsS3/user_images/' . $userId . '/profilePhotos');
	mkdir('/opt/app/current/signalsS3/user_images/' . $userId . '/otherPhotos');
	
	
   	$binaryThumbnail = base64_decode($baseThumbnail);
	$binaryOriginal = base64_decode($baseOriginal);
	
	header('Content-Type: bitmap; charset=utf-8');
	
	$imgFileStr = time() . '.jpg';
	
	// STORE ORIGINAL
	$imgFile = fopen('signalsS3/user_images/' . $userId . '/profilePhotos/' . $imgFileStr, 'wb');
	fwrite($imgFile, $binaryOriginal);
   	fclose($imgFile);
	
	
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
	
	echo json_encode(array( 'ok' => '1', 'r' => $imgFileStr ));
	
	
	function toASCII( $str )
	    {
	        return strtr(utf8_decode($str), 
	            utf8_decode('ŠŒŽšœžŸ¥µÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýÿ'),
	            'SOZsozYYuAAAAAAACEEEEIIIIDNOOOOOOUUUUYsaaaaaaaceeeeiiiionoooooouuuuyy');
	    }
?>

