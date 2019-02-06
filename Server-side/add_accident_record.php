<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['largestAccel']) && isset($_POST['speed']) && isset($_POST['lat']) && isset($_POST['lon']) 
    && isset($_POST['phoneId']) && isset($_POST['showState']) && isset($_POST['vidUrl']))
 {
 
    $largestAccel = $_POST['largestAccel'];
    $speed = $_POST['speed'];
    $lat = $_POST['lat'];
    $lon = $_POST['lon'];
    $phoneId = $_POST['phoneId'];
     $showState = $_POST['showState'];
     $vidUrl= $_POST['vidUrl'];
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO accidentdb(largestAccel, speed, lat,lon ,phoneId ,showState ,vidUrl) VALUES('$largestAccel', '$speed', '$lat',
	'$lon','$phoneId' , '$showState' , '$vidUrl')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "report successfully added";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>