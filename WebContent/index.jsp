<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width">
<script type="text/javascript" src="jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="jquery.qrcode-0.12.0.js"></script>
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:400,600,300,700'
	rel='stylesheet' type='text/css'>
<link href="style.css" type="text/css" rel='stylesheet' />
<script type="text/javascript" src="main.js"></script>
<script type="text/javascript" src="bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
<title>MonKey :: Home</title>
</head>
<body  id="section_background">
<section>
	<div id="icons" align="center" style="vertical-align:top">
		<br><img src="ic_launcher-web.png" style="width: 40%"><br><br><br><br><br>
		<a href="https://play.google.com/store/apps/details?id=com.whatsapp&hl=it" class="button"><img style="width: 50%; margin-bottom: 8%" src="google_play_store_icon.png"/></a>
	</div>
	<div id="title" align="center" style="vertical-align:top">	
		<h1 style="font-size:80px; font-weight: bold; color: #ECECEC; text-shadow: -2px -2px 0 #000, 2px -2px 0 #000, -2px 2px 0 #000, 2px 2px 0 #000;">MonKey</h1>
		<h1 style="color:white">Say what you want.<br>Draw what you want.<br>Everywhere.</h1>
		<div id="start_chatting_button">
			<div id="prompt_name_container">
				<a href="chat.html" style="text-decoration:none"><input style="font-size: 22px; color: black; background: #ECECEC" type="button" value="START CHATTING NOW!" id="btn_join_as_guest"></a>
			</div>
		</div>
	</div>
	<div style="clear:both"></div>
</section>
<section class="container-fluid" align="center" style="display: inline-block; width: 100%; background-color: #ECECEC">
	<div class="row">
		<div class="col-md-4" id="step"  style="text-align: center;">
			<h1 style="font-weight: 600; color: #4444AE"><img src="images/1.png" style="vertical-align: sub">&nbsp;&nbsp;Create your gate</h1>
			<h1>Make your own QR code<br>choosing a word,<br> sentence or link to<br> reference a gate. <br><a href="qrcodegenerator.jsp" style="text-decoration:none; color: #5DB5E0; font-weight: 500">QR Code Generator</a>.</h1>
		</div>
		<div class="col-md-4" id="step"  style="text-align: center;">
			<h1 style="font-weight: 600; color: #4444AE"><img src="images/2.png" style="vertical-align: sub">&nbsp;&nbsp;Shape your gate</h1>
			<h1>Use the app to scan your QR.<br>You can now start to<br>create and post content.<br></h1>
		</div>
		<div class="col-md-4" id="step"  style="text-align: center;">
			<h1 style="font-weight: 600; color: #4444AE"><img src="images/3.png" style="vertical-align: sub">&nbsp;&nbsp;Share it with your friends</h1>
			<h1>Give your QR code name<br>to your friends!<br>They can join you instantly!</h1>
		</div>
	</div>
</section>
<section>
	<div id="footer" style="display: inline-block; float: left">
	    <a href="faq.jsp" style="text-decoration:none; color: white"><h1>FAQ</h1></a>
	</div>
	<div id="footer" style="display: inline-block;  float: right">
		<a href="contact.jsp" style="text-decoration:none; color: white"><h1>CONTACT</h1></a>
	</div>
	<div style="clear:both"></div>
</section>
</body>
</html>