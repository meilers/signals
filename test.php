 
<?php

$APPLICATION_ID = "ujzhrnPvHlLj7oX5wrHdZguIjfLGKZuLhFfqFcnn";
$REST_API_KEY = "wYEDKZywwm3rOWoeRrAtOoQRWrrNY3WnOCXTwJnI";

$url = 'https://api.parse.com/1/push';
$data = array(
    'channel' => '',
    'type' => 'android',
    'data' => array(
        'alert' => 'Test Push'
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

?>
