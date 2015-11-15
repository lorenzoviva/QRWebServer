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
<link href="bootstrap-3.3.5-dist/css/bootstrap.min.css" type="text/css"
	rel='stylesheet' />
<script type="text/javascript" src="main.js"></script>
<script>
	$(document).ready(
			function() {
				function close_accordion_section() {
					$('.accordion .accordion-section-title').removeClass(
							'active');
					$('.accordion .accordion-section-content').slideUp(300)
							.removeClass('open');
				}

				$('.accordion-section-title').click(
						function(e) {
							// Grab current anchor value
							var currentAttrValue = $(this).attr('href');

							if ($(e.target).is('.active')) {
								close_accordion_section();
							} else {
								close_accordion_section();

								// Add active class to section title
								$(this).addClass('active');
								// Open up the hidden content panel
								$('.accordion ' + currentAttrValue).slideDown(
										300).addClass('open');
							}

							e.preventDefault();
						});
			});
</script>
<title>MonKey :: FAQ</title>
</head>
<body align="center" style="background-color: #5DB5E0">
	<h1>Frequently Asked Questions</h1>
	<br><br>
	<div style="width:80%; display:inline-block" class="accordion">
		<div class="accordion-section">
			<a class="accordion-section-title" href="#accordion-1">What
				versions of QR codes are supported?</a>

			<div id="accordion-1" class="accordion-section-content">
				<p>All QR codes from version 2 on are supported.</p>
			</div>
			<!--end .accordion-section-content-->
		</div>
		<!--end .accordion-section-->


		<div class="accordion-section">
			<a class="accordion-section-title" href="#accordion-2">What can I
				do with the app?</a>

			<div id="accordion-2" class="accordion-section-content">
				<p>You can use the app to link your QR code with a drawing
					canvas, a chat or a web page.</p>
			</div>
			<!--end .accordion-section-content-->
		</div>
		<!--end .accordion-section-->

		<div class="accordion-section">
			<a class="accordion-section-title" href="#accordion-3">What can I
				do from the site?</a>

			<div id="accordion-3" class="accordion-section-content">
				<p>You can join or create a new chat, anonymously or by logging
					in.</p>
			</div>
			<!--end .accordion-section-content-->
		</div>
		<!--end .accordion-section-->

		<div class="accordion-section">
			<a class="accordion-section-title" href="#accordion-4">Do I need
				to create an account to use the app or the site?</a>

			<div id="accordion-4" class="accordion-section-content">
				<p>No, you can freely use the app and the website staying
					anonymous.</p>
			</div>
			<!--end .accordion-section-content-->
		</div>
		<!--end .accordion-section-->

		<div class="accordion-section">
			<a class="accordion-section-title" href="#accordion-5">Do I have
				to pay something to use your services?</a>

			<div id="accordion-5" class="accordion-section-content">
				<p>No, our services are completely free of any charge.</p>
			</div>
			<!--end .accordion-section-content-->
		</div>
		<!--end .accordion-section-->


	</div>
	<!--end .accordion-->

	<!-- <div id="accordion">
		<h3>
			<a href="#">What versions of QR codes are supported?</a>
		</h3>
		<div>All QR codes from version 2 on are supported.</div>
		<h3>
			<a href="#">What can I do with the app?</a>
		</h3>
		<div>You can use the app to link your QR code with a drawing
			canvas, a chat or a web page.</div>
		<h3>
			<a href="#">What can I do from the site?</a>
		</h3>
		<div>You can join or create a new chat, anonymously or by
			logging in.</div>
		<h3>
			<a href="#">Do I need to create an account to use the app or the
				site?</a>
		</h3>
		<div>No, you can freely use the app and the website staying
			anonymous.</div>
		<h3>
			<a href="#">Do I have to pay something to use your services?</a>
		</h3>
		<div>No, our services are completely free of any charge.</div>
	</div> -->
</body>
</html>