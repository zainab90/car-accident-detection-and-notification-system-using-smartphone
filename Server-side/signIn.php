
<?php
// Start the session
session_start();
?>

<?php
$con=mysqli_connect("localhost","root","","webservice");
// Check connection
if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

// username and password sent from form 
// username and password sent from form 
$myusername=$_POST['username']; 
$mypassword=$_POST['password'];
$centerLat="";
$centerName="";
$centerLon=""; 
$count=0;
// To protect MySQL injection (more detail about MySQL injection)
$myusername = stripslashes($myusername);
$mypassword = stripslashes($mypassword);
$myusername = mysql_real_escape_string($myusername);
$mypassword = mysql_real_escape_string($mypassword);

$result = mysqli_query($con,"SELECT * FROM user
WHERE centerName='$myusername' and pass= '$mypassword' ");

while($row = mysqli_fetch_array($result)) {
  $centerLat=$row['center_lat'];
  $centerLon=$row['center_lon'];
 
  $count ++ ;
}
if ($count >0)
{
// Set session variables
$_SESSION["UN"] = $myusername;
$_SESSION["PASS"] = $mypassword;
 $_SESSION["lat"] = $centerLat;
 $_SESSION["lon"] = $centerLon;
  
    header("location:AccidentReports.php");}
else
{header("location:login_error.php");}




?>