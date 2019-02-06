
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
           <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <style type="text/css">
            .bodyclass{
                background-color: #333333;
            }
             .headerstyle{
        
                background-color:grey ;
              margin-top: 10px;
                   color: #33cc00;
                   font-family:monospace;
                   border-style: dotted;
                   border-width: 1px;
                
               
            }
            .header{
         position: relative;
         top:20px;
          margin-left: 5px; }
 

 .logostyle{
   
  width:70px;
margin-top: -60px;
margin-left: 1188px;
margin-right: 7px;
margin-bottom: 1px;
}
.loginSection{
    border:2px solid #33cc00;
padding:10px 40px; 
background:#dddddd;
width:400px;
border-radius:25px;
margin-left: 750px;
margin-top: 50px;
}
  #content {
    margin-top: 10%;
   
}
#container {
    margin: 0 auto;
   
    text-align: center;
}
input[type=text], input[type=password] {
    width: 250px;
    min-height: 25px;
    border-radius: 3px;
    border: 0;
    padding: 7px;
    font-family: Calibri;
    font-size: 20px;
    box-shadow: 0px 0px 3px 1px #aaa inset;
    box-sizing: border-box;
}
#sign_in, #sign_up {
    border:2px solid #33cc00;
    border-radius: 20px;
    margin: 0 auto;
    height: auto;
     width: 480px;
    padding: 10px;
    background-color: #dddddd;
    border: 0px;
    box-shadow: 0px 0px 15px 1px #33cc00;
}
#sign_up {
    display: none;
    height: auto;
}
        </style>
    </head>
    <body class="bodyclass">
    
        
        <header class="headerstyle">
            <h1 class="header" > Welcome to Cars Accident Detection And Notification System using Smartphone</h1>
          <left>        <div class="logostyle">
                  <img src="ambuflash3.gif" width="120" height="75" >
            </div>
    </left>
         
        </header>
        
        <img src="emergencyRecponder.png" style="margin-top: 10px;margin-left: 20px; float: left;" height="500" >
       
               <div style="
margin-left: 750px;
margin-top: 50px;">
        <section id="content">
    <div id="logo" style="margin-left: 0;"></div>
    <div id="container">
            <form id="sign_in" action="signIn.php" method="post">
            <table style="margin: 0 auto;">
                <tr>
                    <td align="center"> <span style="font-weight: bold;font-size: 2em; color: #33cc00;">
                            <img src="ic_launcher_1.png" style="alignment-adjust: auto;float: left"><p style="display: inline;margin-left: -60px;">Sgin In</p>
                        
                        </span>

                    </td>
                    <tr>
                        <td>
                            <input class="input" type="text" placeholder="Username" name="username" required="required" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input class="input" type="password" placeholder="Password" name="password" required="required" />
                            <br />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <br/>
                        </td>
                    </tr>
                    <tr align="right">
                        <td>
                            <input class="btn" type="submit" value="Sign In" name="submit_sign_in" >
                        </td>
                    </tr>
            </table>
        </form>
    </div>
    
      </section>
    </div>
     
    </body>
</html>

