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
</head>
<body>
	<div id="header">
		<h1>QRBoard Chat Registration</h1>
	</div>
	<div id="prompt_name_container" class="box_shadow">
		<form id="form_signUp" method="post">
			<p style="margin-bottom: 0; margin-top: 10px;">First name</p>
			<input type="text" id="input_first_name" autocomplete="off" />
			<p style="margin-bottom: 0; margin-top: 10px;">Last name</p>
			<input type="text" id="input_last_name" autocomplete="off" /><br>
			<p style="margin-bottom: 0; margin-top: 10px;">Username</p>
			<input type="text" id="input_username" autocomplete="off" /><br>
			<p style="margin-bottom: 0; margin-top: 10px;">Password</p>
			<input type="text" id="input_password" autocomplete="off" /><br>
			<input type="submit" value="REGISTER" id="btn_join_as_guest" />
		</form>
	</div>
</body>
</html>