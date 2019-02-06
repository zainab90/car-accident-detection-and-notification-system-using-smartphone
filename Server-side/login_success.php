<?php
session_start();
?>
<html>
<body>
Login Successful
<?php
// Echo session variables that were set on previous page
echo "Favorite color is " . $_SESSION["UN"] . ".<br>";
echo "Favorite animal is " . $_SESSION["PASS"] . "." . ".<br>";
echo "Favorite animal is " . $_SESSION["lat"] . "." . ".<br>";
echo "Favorite animal is " . $_SESSION["lon"] . "." . ".<br>";
$c=$_SESSION["UN"];
?>
<script>
var name='<?php echo($c) ?>';
var lat='<?php 
echo($_SESSION["lat"])

?>';
alert(name +" "+ lat);

</script>


</body>
</html>