<?php
session_start();
?>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
  <script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
             <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> 
  <script src="jquery.panzoom.js"></script>
    <script src="jquery.mousewheel.js"></script>
    <script type="text/javascript" src="jQueryRotate.js"></script>
  <script>
    var i=0;
</script>
        <style type="text/css">
       .headerstyle{
        
                  background-color:grey;
              margin-top: 10px;
                   color: #33cc00;
                   border-style: dotted;
                   border-width: 1px;
            }
  .logostyle{
   
  width:70px;
margin-top: -70px;
margin-left: 1170px;
margin-right: 7px;
margin-bottom: 1px;
}  

     
      .header{
         position: relative;
         top:20px;
         margin-left: 16px;
         font-family: sans-serif;
         font-weight: bold;
     }
         
   
.alertstyle{
    width:75px;
margin-top: -73px;
margin-left: 1235px;
margin-right: 20px;
margin-bottom: 3px;
    
}
.showpost{
 position: relative;
        margin-top: 10px;
         margin-left: 230px;
          margin-right:30px;
      
         background-color:#ffffff;
         border-style: dotted;
         border-color: #33cc00;
         border-width: 1px;
         background-color:#CCEBCC;
          padding: 10px;
          margin-bottom: 10px;
  
}
.tableStyle{
border-collapse: separate;
 border-spacing: 5px;
 table-layout: auto;
   padding: 10px;
   margin-left: 70px;
   float: left;
  
}
.accidentGellary{
   font-family: serif;float:left;right: 10px; top:10px; font-weight: bold;
}
.accidentCaption{
    background-image: url(smartphone.png);
    background-repeat:no-repeat;
    background-position:left;
   
}
  .left{
                width: 23px;
                height: 260px;
                background-image:url("arrow_left.png");
                background-repeat: no-repeat;
                background-color:#666666;
                background-position: center;
               float: left;
            }
            
                      .right{
                width: 23px;
                height: 260px;
                background-image:url("arrow_right.png");
                background-repeat: no-repeat;
                  background-color:#666666;
                background-position: center;
            float:left;
                margin-right: 20px;
            }
            .loctionByBystander{
                width: 200px;
                height: 24px;
                border-style: dotted;
                border-width: 1px;
           
            }
            .account{
                position: fixed;
       margin-top: 10px;
         margin-left: 10px;
        height: 800px;
          width: 236px;
            float: left;
     color: #CCEBCC;
        }
        
  .play_pause
{
position:absolute;
z-index:1;
left:650px;
top :480px;
}      
  .acc_txt{margin-left: 90px; margin-top: 5px; font-family: serif;font-weight: bold; font-size: large;}
  .newImg{margin-top: -30px; margin-right: 10px;width: 48px;height: 48px;
  -webkit-animation: pulse 5s ;
	animation: pulse 5s ;
  
  }
  .acc_time{text-align: left;font-family: sans-serif;font-weight: bold; margin-left: 60px;}
  .mapHolder{border-style: solid;border-width: 2px;margin-left: 80px;text-align: center;
             border-color: #33cc00; position: fixed;}
  .imgHolder{border: 3px solid #33cc00; width: 250px;height: 150px;margin-left: 490px; margin-top: 10px;}
  .vidHolder{border: 3px solid #33cc00; width: 450px;height: 336px; margin-left: 490px;margin-top: 10px;}
  .parag{margin-left: 50px;}
  
        a{
    text-decoration: none;
    color: #2E9AFE;
  cursor: pointer;
        }
   
  @-webkit-keyframes pulse {
 0% {
 -webkit-transform: scale(1, 1);
}
 50% {
 -webkit-transform: scale(1.1, 1.1);
}
 100% {
 -webkit-transform: scale(1, 1);
}}


 @keyframes pulse {
 0% {
 transform: scale(1, 1);
}
 50% {
 transform: scale(1.1, 1.1);
}
 100% {
transform: scale(1, 1);
};
}       
        

section { text-align: center; margin: 50px 0; }
      .panzoom-parent { border: 2px solid #333; }
      .panzoom-parent .panzoom { border: 2px dashed #666; }
      .buttons { margin: 40px 0 0; }
      #lightbox {
    position:fixed; /* keeps the lightbox window in the current viewport */
    top:0; 
    left:0; 
    width:100%; 
    height:100%; 
    background: rgba(0,0,0,.8);
    text-align:center;
}

#rotate , #close{
    margin-right: 5px;
      margin-top: 10px;
}
               .scrollToTop{
                   z-index: 20;
	width:38px; 
	height:38px;
	padding:10px; 
	text-align:center; 
	background: whiteSmoke;
	font-weight: bold;
	color: #444;
	text-decoration: none;
	position:fixed;
	right:5px;
        bottom: 70px;
	display:none;
	background: url('top.png') no-repeat 0px 20px;
        
}
.scrollToTop:hover{
	text-decoration: none;
       background: url('top.png') no-repeat 0px 20px;
        opacity: 0.4;
    filter: alpha(opacity=40);
}      
        </style>
         <script >

function stopAlert(){
document.getElementById('audio').pause();
document.getElementById('alertPic').src='campane-bell-icon.png';

}
</script>
<script type="text/javascript">

function myFunction(var1,var2){
    alert("clcik");
     location.href='Toanotherdirection.php?lat='+var1+'&lon='+var2;
    return false;
}
</script>


<script type="text/javascript">
var centerLat;
centerLat='<?php echo($_SESSION["lat"])?>';
centerLon='<?php echo($_SESSION["lon"])?>';
var centerName='<?php echo($_SESSION["UN"])?>';            
       
</script> 
        
  
</head>
<body style="background-color: #333333" >
     
     <header class="headerstyle">
         <h1 class="header" >Cars Accidents Reports</h1>
         <audio controls hidden="true" id="audio">
             <source src="Official Portal 2 Website - Music.MP3" type="audio/ogg" >
  <source src="Official Portal 2 Website - Music.mp3" type="audio/mpeg">
Your browser does not support the audio element.
</audio>
          <div class="alertstyle">
              <img  id="alertPic" src="campane-bell-icon.png" style="" width=75" height="75" onclick="stopAlert()">
         </div>
        </header>
        
        <div class="account">
            <img src="cust_service.png"  style="float:left;">
            <pre style="text-align: center">
              
                </pre>
             <p>
             
                </p>
                <p style="text-align: center">
                
                </p>
                
            <center>
                <img src="emergency_emergency_room_hospital.png" width="245" height="300" style="margin-left: 0px;">
            </center>
        </div>
    <div style="z-index: 10">
       <div id="res" >
    <a  href="#" class="scrollToTop" title="back to the top"></a>
    </div>
       </div>
    
    <?php 
  date_default_timezone_set('Asia/Baghdad');
$curentDate=date("Y-m-d H:i:s");

$pieces=explode(" ",$curentDate);
//echo($pieces[0]). "<br>";// date
$date=explode("-",$pieces[0]);
$year=$date[0];
$month=$date[1];
$day=$date[2];
$myusername=$_SESSION["UN"];
//echo($year . "<br>" .$month. "<br>" . $day. "<br>");
//echo($pieces[1]);//time.
  $con=mysqli_connect("localhost","root","","webservice");
  if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
   $result = mysqli_query($con,"SELECT * FROM showposts  WHERE centername='$myusername' ORDER BY postId DESC ");

while($row = mysqli_fetch_array($result)) {
 $time=$row['time'];
$accident_mon=explode("-",$time);
 $accident_mon[0];//year;
 $accident_mon[1];//month;
 $accident_mon[2];//day;
 if (strcmp($accident_mon[1],$month)==0)
 {
$acc_day=explode(" ",$accident_mon[2]); 
 
 if (strcmp($acc_day[0],$day)==0)
 {
   $lat=$row['lat'];
   $lon=$row['lon'];
  $vidSrc=$row['ImgVidUrl'];
  if ($row['postType']==0)
  {
   
   $acc=$row['LargestAcc'];
   $speed=$row['speed'];
   

     echo("<script>
 
var showPost=document.createElement('div');
   showPost.className='showpost';
   
   var accidentCaption=document.createElement('div');
   accidentCaption.className='accidentcaption';
   
   var accisentPost=document.createElement('p');
   var acc_txt=document.createTextNode('Accident happened at '+' '+$year);
   accisentPost.className='acc_txt';
   accisentPost.appendChild(acc_txt);
   accidentCaption.appendChild(accisentPost);  
    showPost.appendChild(accidentCaption);
   
   var mapHolder=document.createElement('div');
lat= $lat;       
  lon=$lon ; 
  mapHolder.align='center';
  mapHolder.style.height='270px';
  mapHolder.style.width='840px';
  mapHolder.className='mapHolder';
  latlon=new google.maps.LatLng(lat, lon);
 var myOptions={
  center:latlon,zoom:14,
  mapTypeId:google.maps.MapTypeId.ROADMAP,
  mapTypeControl:false,
  navigationControlOptions:{style:google.maps.NavigationControlStyle.SMALL}
  };
  var map=new google.maps.Map(mapHolder,myOptions);
  var marker=new google.maps.Marker({position:latlon,map:map,title:'You are here!'});
showPost.appendChild(mapHolder);
  

var link=document.createElement('a');
link.onclick = function() {myFunction(lat,lon)};
var link_txt=document.createTextNode('view in full screen');
link.appendChild(link_txt);
showPost.appendChild(link);

var accisentPost=document.createElement('pre');
   var acc_txt=document.createTextNode('Accident Information                                                                 Accident Imge/video');
   accisentPost.className='acc_txt';
   accisentPost.appendChild(acc_txt);
   showPost.appendChild(accisentPost);

 var table = document.createElement('table');
 table.className='tableStyle';
 
 tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Acceleration Force') );
tr.cells[1].appendChild( document.createTextNode($acc) );
table.appendChild(tr);

 tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Speed in which accident is happen') );
tr.cells[1].appendChild( document.createTextNode($speed) );
table.appendChild(tr);
 
 
 
 tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Location Latitude') );
tr.cells[1].appendChild( document.createTextNode($lat) );
table.appendChild(tr);

tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Location Longitude') );
tr.cells[1].appendChild( document.createTextNode($lon) );
table.appendChild(tr); 


showPost.appendChild(table);

 

 </script>");
 
 if ($vidSrc !== null)
 {
    
    echo("
    <script>
    
 var videoHolder=document.createElement('div');
videoHolder.className='vidHolder';
var videoSrc=document.createElement('video');
 videoSrc.style.width='450px';
 videoSrc.controls='true';
 var vid_src=document.createElement('source');
 vid_src.src='$vidSrc';
 vid_src.type='video/mp4';
 
 
videoSrc.appendChild(vid_src);
videoHolder.appendChild(videoSrc);
showPost.appendChild(videoHolder);
    
    
    
    </script>
    
    
    
    ");
    
    
 }
 
 echo("
 <script>
  document.body.appendChild(showPost);
 
 </script>
 
 ");
 }
  
  else
  {
  
 echo("<script>
 
var showPost=document.createElement('div');
   showPost.className='showpost';
   
   var accidentCaption=document.createElement('div');
   accidentCaption.className='accidentcaption';
   
   var accisentPost=document.createElement('p');
   var acc_txt=document.createTextNode('Accident happened at '+' '+$year);
   accisentPost.className='acc_txt';
   accisentPost.appendChild(acc_txt);
   accidentCaption.appendChild(accisentPost);
   
    showPost.appendChild(accidentCaption);
   
   var mapHolder=document.createElement('div');
lat= $lat;       
  lon=$lon ; 
  mapHolder.align='center';
  mapHolder.style.height='270px';
  mapHolder.style.width='840px';
  mapHolder.className='mapHolder';
  latlon=new google.maps.LatLng(lat, lon);
 var myOptions={
  center:latlon,zoom:14,
  mapTypeId:google.maps.MapTypeId.ROADMAP,
  mapTypeControl:false,
  navigationControlOptions:{style:google.maps.NavigationControlStyle.SMALL}
  };
  var map=new google.maps.Map(mapHolder,myOptions);
  var marker=new google.maps.Marker({position:latlon,map:map,title:'You are here!'});
showPost.appendChild(mapHolder);
  

var link=document.createElement('a');
link.onclick = function() {myFunction(lat,lon)};
var link_txt=document.createTextNode('view in full screen');
link.appendChild(link_txt);
showPost.appendChild(link);

var accisentPost=document.createElement('pre');
   var acc_txt=document.createTextNode('Accident Information                                                                 Accident Imge/video');
   accisentPost.className='acc_txt';
   accisentPost.appendChild(acc_txt);
   showPost.appendChild(accisentPost);

 var table = document.createElement('table');
 table.className='tableStyle';
 tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Location Latitude') );
tr.cells[1].appendChild( document.createTextNode($lat) );
table.appendChild(tr);

tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Location Longitude') );
tr.cells[1].appendChild( document.createTextNode($lon) );
table.appendChild(tr); 


showPost.appendChild(table);


</script>");

if (strcasecmp('mp4',strrchr($row['ImgVidUrl'],'mp4'))==0)
{
    echo("<script>
    var videoHolder=document.createElement('div');
videoHolder.className='vidHolder';
var videoSrc=document.createElement('video');
 videoSrc.style.width='450px';
 videoSrc.controls='true';
 var vid_src=document.createElement('source');
 vid_src.src='$vidSrc';
 vid_src.type='video/mp4';
 
 
videoSrc.appendChild(vid_src);
videoHolder.appendChild(videoSrc);
showPost.appendChild(videoHolder);
    </script>");
}
else
{
    echo("<script>
  var imgholder=document.createElement('div');
imgholder.className='imgHolder';
var accident_img_vid=document.createElement('img');
accident_img_vid.src='$vidSrc';
accident_img_vid.className='lightbox_trigger';
accident_img_vid.style.width='250px';
accident_img_vid.style.height='150px';
imgholder.appendChild(accident_img_vid);
showPost.appendChild(imgholder);
    </script>");
}

echo("<script>
document.body.appendChild(showPost);
 </script>");


 
 
   
  
   
 }
   } 
 }      
}
mysqli_close($con); 

 


  ?>  
  

  <script >

var source;
var arr;
var cars=[] ;
var index=0;
var totalRows;
var temp;
var update="no";
var name;
var driverpost;
var bystandersPost;
var tem;
;
if(typeof(EventSource)!=="undefined")
{     
 source=new EventSource("trySelect.php?centerLat="+centerLat+"&centerLon="+centerLon+"&centerName="+centerName);      
source.addEventListener("message", function(event) {
totalRows=event.data;
document.getElementById('alertPic').src='alarm_on (1).gif';
document.getElementById('audio').play();
document.getElementById('audio').loop=true;
sessionStorage.setItem(i,totalRows);
if (totalRows !== tem){
  var statrting=totalRows.substr(0,1);
  tem=totalRows;
    if (statrting==="@")
    {
      
        bystandersPost=totalRows.slice(1);
        var row= bystandersPost.split("#");
 for (var j=0;j<row.length-1;j++ )
      {// document.write(row[j]+"<br>");
       var temp=row[j].split(",");
       // now get element of each row
       
    var showPost=document.createElement('div');
   showPost.className='showpost';
   
   var accidentCaption=document.createElement('div');
   accidentCaption.className='accidentcaption';
   
   var accisentPost=document.createElement('p');
   var acc_txt=document.createTextNode('Accident happened at '+" "+temp[1]);
   accisentPost.className='acc_txt';
   accisentPost.appendChild(acc_txt);
   accidentCaption.appendChild(accisentPost);
   
  var newImgHolder=document.createElement('div');
    newImgHolder.className='newImg';
   var newImg=document.createElement('img');
   newImg.src='new.png';
   newImg.align='right';
   newImgHolder.appendChild(newImg);
   accidentCaption.appendChild(newImgHolder);
    showPost.appendChild(accidentCaption);
   
   
   var mapHolder=document.createElement("div");
lat= temp[2];       
  lon=temp[3] ; 
   mapHolder.align='center';
  mapHolder.style.height='270px';
  mapHolder.style.width='840px';
  mapHolder.className='mapHolder';
  latlon=new google.maps.LatLng(lat, lon);
 var myOptions={
  center:latlon,zoom:14,
  mapTypeId:google.maps.MapTypeId.ROADMAP,
  mapTypeControl:false,
  navigationControlOptions:{style:google.maps.NavigationControlStyle.SMALL}
  };
  var map=new google.maps.Map(mapHolder,myOptions);
  var marker=new google.maps.Marker({position:latlon,map:map,title:"You are here!"});
showPost.appendChild(mapHolder);

var link=document.createElement('a');
link.onclick = function() {myFunction(lat,lon)};
var link_txt=document.createTextNode('view in full screen');
link.appendChild(link_txt);
showPost.appendChild(link);

var accisentPost=document.createElement('pre');
   var acc_txt=document.createTextNode('Accident Information                                                                 Accident Imge/video');
   accisentPost.className='acc_txt';
   accisentPost.appendChild(acc_txt);
   showPost.appendChild(accisentPost);

 var table = document.createElement('table');
 table.className='tableStyle';
 tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Location Latitude') );
tr.cells[1].appendChild( document.createTextNode(temp[2]) );
table.appendChild(tr);

tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Location Longitude') );
tr.cells[1].appendChild( document.createTextNode(temp[3]) );
 sessionStorage.setItem("B"+j+3,temp[3]);
table.appendChild(tr); 


showPost.appendChild(table);


if (temp[4]==="0")

{var imgholder=document.createElement('div');
imgholder.className='imgHolder';
var accident_img_vid=document.createElement('img');
accident_img_vid.src=temp[5];
accident_img_vid.className='lightbox_trigger';
accident_img_vid.style.width='250px';
accident_img_vid.style.height='150px';
imgholder.appendChild(accident_img_vid);
showPost.appendChild(imgholder);






}

else
{
    
    var videoHolder=document.createElement('div');
videoHolder.className='vidHolder';
var videoSrc=document.createElement('video');
videoSrc.style.width='450px';
 videoSrc.controls='true';
 var vid_src=document.createElement('source');
 vid_src.src=temp[5];
 vid_src.type='video/mp4';
videoSrc.appendChild(vid_src);
videoHolder.appendChild(videoSrc);
showPost.appendChild(videoHolder);
    
    
}




document.getElementById('res').insertBefore(showPost,document.getElementById('res').childNodes[0]);
    }
        
    }
     else
     {
      driverpost=totalRows; 
       var row= driverpost.split("#");
 for (var j=0;j<row.length-1;j++ )
      {// document.write(row[j]+"<br>");
       var temp=row[j].split(",");
   var showPost=document.createElement('div');
   showPost.className='showpost';
   
   var accidentCaption=document.createElement('div');
   accidentCaption.className='accidentcaption';
   
   var accisentPost=document.createElement('p');
   var acc_txt=document.createTextNode('Accident happened at'+" "+ temp[1]);
   accisentPost.className='acc_txt';
   accisentPost.appendChild(acc_txt);
   accidentCaption.appendChild(accisentPost);
   
  var newImgHolder=document.createElement('div');
    newImgHolder.className='newImg';
   var newImg=document.createElement('img');
   newImg.src='new.png';
   newImg.align='right';
   newImgHolder.appendChild(newImg);
   accidentCaption.appendChild(newImgHolder);
  showPost.appendChild(accidentCaption);
   
   var mapHolder=document.createElement("div");
lat= temp[4];       
  lon=temp[5] ; 
  mapHolder.style.height='270px';
  mapHolder.style.width='840px';
  mapHolder.className='mapHolder';
  latlon=new google.maps.LatLng(lat, lon);
 var myOptions={
  center:latlon,zoom:14,
  mapTypeId:google.maps.MapTypeId.ROADMAP,
  mapTypeControl:false,
  navigationControlOptions:{style:google.maps.NavigationControlStyle.SMALL}
  };
  var map=new google.maps.Map(mapHolder,myOptions);
  var marker=new google.maps.Marker({position:latlon,map:map,title:"You are here!"});
showPost.appendChild(mapHolder);
   
   var link=document.createElement('a');
link.onclick = function() {myFunction(lat,lon)};
var link_txt=document.createTextNode('view in full screen');
link.appendChild(link_txt);
showPost.appendChild(link);


var accisentPost=document.createElement('pre');
   var acc_txt=document.createTextNode('Accident Information                                                                 Accident Imge/video');
   accisentPost.className='acc_txt';
   accisentPost.appendChild(acc_txt);
   showPost.appendChild(accisentPost);
   
   
   
   
 var table = document.createElement('table');
 table.className='tableStyle';
 tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Acceleration Force') );
tr.cells[1].appendChild( document.createTextNode(temp[2]) );
table.appendChild(tr);

 tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Speed in which accident is happen') );
tr.cells[1].appendChild( document.createTextNode(temp[3]) );
table.appendChild(tr);

 tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Location Latitude') );
tr.cells[1].appendChild( document.createTextNode(temp[4]) );
table.appendChild(tr);

tr = document.createElement('tr');
tr.appendChild( document.createElement('th') );
tr.appendChild( document.createElement('td') );
tr.cells[0].appendChild( document.createTextNode('Location Longitude') );
tr.cells[1].appendChild( document.createTextNode(temp[5]) );
table.appendChild(tr); 


showPost.appendChild(table);

if (temp[6] !== null)
{
    var videoHolder=document.createElement('div');
videoHolder.className='vidHolder';
var videoSrc=document.createElement('video');
videoSrc.style.width='450px';
 videoSrc.controls='true';
 var vid_src=document.createElement('source');
 vid_src.src=temp[6];
 vid_src.type='video/mp4';
 
 
videoSrc.appendChild(vid_src);
videoHolder.appendChild(videoSrc);
showPost.appendChild(videoHolder);
}

document.getElementById('res').insertBefore(showPost,document.getElementById('res').childNodes[0]);
       
    }
          
  }
  
 
}

}, false);

}
else
{
document.getElementById("result").innerHTML="Sorry, your browser does not support server-sent events...";
}

</script>

 <script>
            
        $(document).ready(function(){
	
	$('.lightbox_trigger').click(function(e) {
		
		//prevent default action (hyperlink)
		e.preventDefault();
		
		//Get clicked link href
		var image_href = $(this).attr("src");
       var angle=90;
              
		
		/* 	
		If the lightbox window HTML already exists in document, 
		change the img src to to match the href of whatever link was clicked
		
		If the lightbox window HTML doesn't exists, create it and insert it.
		(This will only happen the first time around)
		*/
		
		if ($('#lightbox').length > 0) { // #lightbox exists
	       
                    
                        
              	//place href as img src value
			$('.panzoom').html('<img id="lightbox_img" src="' + image_href + '" width="500" height="500"/>');
                           
                       
	 
                 
			//show lightbox window - you could use .show('fast') for a transition
			$('#lightbox').show();
                        $('#rotate').click(function(){
					
                                          $("#lightbox_img").rotate({angle:angle});
                                          angle=angle+90;
				});
                                 $('#close').click(function(){
					//alert("close");
                                          $("#lightbox").hide();
				});
		}
		
		else { //#lightbox does not exist - create and insert (runs 1st time only)
			
			//create HTML markup for lightbox window
			var lightbox = 
                                '<div id="lightbox">' +
				'<img id="close" src="close-icon.png"  align="right"/>' +
                                '<img  id="rotate" src="Rotate_left_arrow.png"  align="right" alt=""/>' +
                                 ' <section id="content">' +
  
      '<div class="parent">' +
        '<div class="panzoom">' +
           '<img id ="lightbox_img" src="' + image_href +'" width="500" height="500">' +
        '</div>' +
      '</div>' +
     ' <div class="buttons">'+ 
        '<button class="zoom-in">Zoom In</button>' +
        '<button class="zoom-out">Zoom Out</button>' +
        '<input type="range" class="zoom-range">' +
        '<button class="reset">Reset</button>' +
      '</div>' +
      '</section>'+  '</div>' ;
    	//insert lightbox HTML into page
			$('body').append(lightbox);
          var $section = $('section');
          $section.find('.panzoom').panzoom({
            $zoomIn: $section.find(".zoom-in"),
            $zoomOut: $section.find(".zoom-out"),
            $zoomRange: $section.find(".zoom-range"),
            $reset: $section.find(".reset")
          });                    
           
                           
        $('#rotate').click(function(){
					
                                          $("#lightbox_img").rotate({angle:angle});
                                           angle=angle+90;
				});
                                 $('#close').click(function(){
					//alert("close");
                                          $("#lightbox").hide();
				});
		}
		
	});
	
	//Check to see if the window is top if not then display button
	$(window).scroll(function(){
		if ($(this).scrollTop() > 150) {
			$('.scrollToTop').fadeIn();
		} else {
			$('.scrollToTop').fadeOut();
		}
	});
	
	//Click event to scroll to top
	$('.scrollToTop').click(function(){
		$('html, body').animate({scrollTop : 0},800);
		return false;
	});
});
</script> 


 
</body>
</html>