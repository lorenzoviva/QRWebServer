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
<script type="text/javascript" src="QrCodeGenerator.js"></script>
<script type="text/javascript" src="main.js"></script>
<title>MonKey :: QR Code Generator</title>
</head>
<body>
<input type="text" id="text" autocomplete="off">
	<div id="prompt_name_container">
		<input style="font-size: 22px; color: black; background: #ECECEC" type="button" value="GENERATE QR CODE" id="btn_join_as_guest">
	</div>
<br>
<div id="qr">
	
</div>
</body>
</html>