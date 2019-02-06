<?php
header('Content-Type: text/event-stream');
header('Cache-Control: no-cache');
$con=mysqli_connect("localhost","root","","webservice");
$count=0;
$By_count=0;
$totalRows="";
$totalByst="@";
$lat_c=$_GET['centerLat'];
$lon_c=$_GET['centerLon'];
$list;
$centerName=$_GET['centerName'];
$index=0;
$center_temp =0.0;
$temp=0.0;
// Check connection
if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$result = mysqli_query($con,"SELECT * FROM user
WHERE center_lat != '$$lat_c'  and center_lon != '$lon_c' ");
while($row = mysqli_fetch_array($result)){
 $list[$index] = $row['center_lat'];
 $index++;   
}


$result = mysqli_query($con,"SELECT * FROM accidentdb");

while($row = mysqli_fetch_array($result)) {

if (strcasecmp($row['showState'],"true")==0)	
	
{
 $phone_lat=(Double)$row['lat'];
 $center_temp=abs($phone_lat-$lat_c);
 for ($i=0;$i < count($list);$i++)
 {
 if (abs($phone_lat-$list[$i]) > $temp )
 {$temp=abs($phone_lat-$list[$i]); }   
      
 }   
    
   if ($center_temp < $temp)
   {
    $larg_acc=$row['largestAccel'];
    $speed_ph=$row['speed'];
    $lat_ph=$row['lat'];
    $lon_ph=$row['lon'];
    $imgVid=$row['vidUrl'];
    $phoneId=$row['phoneId'];
    $time=$row['time'];
    $type=0;
    $center_name=$centerName;
    
    
    $insert="insert into showposts (LargestAcc,speed,lat,lon,ImgVidUrl,phoneId,time,postType,centername) 
 values('$larg_acc' , '$speed_ph' , '$lat_ph' , '$lon_ph', '$imgVid','$phoneId','$time' , '$type' , '$center_name')";
    
   if (!mysqli_query($con,$insert)) {
  die('Error: ' . mysqli_error($con));
} 
  
    
    
    
    
	$totalRows .= $row['phoneId'] . "," . $row['time'] ."," . $row['largestAccel'] . "," . $row['speed'] . "," . $row['lat'] . "," . 
	 $row['lon'] . "," . $row['vidUrl'] ."#";

mysqli_query($con,"UPDATE accidentdb SET showState='false' WHERE accidentId = " .$row['accidentId'] );	
	
  $count++;		
}	
}
}

$result = mysqli_query($con,"SELECT * FROM bystanderstb");

while($row = mysqli_fetch_array($result)) {

if (strcasecmp($row['showState'],"true")==0)	
	
{
	
   $phone_lat=(Double)$row['lat'];
 $center_temp=abs($phone_lat-$lat_c);
 for ($i=0;$i < count($list);$i++)
 {
 if (abs($phone_lat-$list[$i]) > $temp )
 {$temp=abs($phone_lat-$list[$i]); }   
      
 }   
    
   if ($center_temp < $temp)
   {
    $lat_ph=$row['lat'];
    $lon_ph=$row['lon'];
    $imgVid=$row['imgVidUrl'];
    $phoneId=$row['phoneId'];
    $time=$row['time'];
    $type=1;
    $center_name=$centerName;
    
    
    $insert="insert into showposts (lat,lon,ImgVidUrl,phoneId,time,postType,centername) 
 VALUES('$lat_ph', '$lon_ph', '$imgVid',
	'$phoneId','$time' , '$type' , '$center_name')";
    
   if (!mysqli_query($con,$insert)) {
  die('Error: ' . mysqli_error($con));
} 
   
   
    $totalByst .= $row['phoneId'] . "," . $row['time'] . "," . $row['lat'] . "," .  $row['lon'] . "," .  $row['reportType'] . "," . $row['imgVidUrl'] . "#";
	mysqli_query($con,"UPDATE bystanderstb SET showState='false' WHERE id = " .$row['id'] );	
  $By_count++;	
	}
}
}

if ($count > 0)
{
echo "data: $totalRows \n\n";
flush();	
}

if ($By_count > 0)
{
echo "data: $totalByst \n\n";
flush();	
}



mysqli_close($con);
?>