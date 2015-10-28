// to keep the session id
var sessionId = '';

// name of the client
var name = '';

// socket connection url and port
var socket_url = '192.168.1.5';
var port = '8080';

$(document).ready(function() {

	$("#form_submit, #form_send_message").submit(function(e) {
		e.preventDefault();
		join("", "");
	});
});

var webSocket;
var loginid = 'a';
var jsonChat;
var jsonUser;
/**
 * Connecting to socket
 */
function join(idChat, userid) {
	if (idChat.trim().length <= 0) { // not from mobile
		if ($('#input_name').val().trim().length <= 0) { // empty box chat
			// name
			alert("you must insert the chat qr text");
		} else {
			idChat = $('#input_name').val().trim();
			// Checking person name

			if (userid.trim().length > 0) {
				$('#prompt_name_container').fadeOut(1000, function() {
					// opening socket connection
					openSocket(idChat, userid);
				});
			} else {
				$('#prompt_name_container').fadeOut(1000, function() {
					// opening socket connection
					openSocket(idChat, "anonymous");
				});
			}
		}
	} else {
		// Checking person name

		if (userid.trim().length > 0) {
			$('#prompt_name_container').fadeOut(1000, function() {
				// opening socket connection
				openSocket(idChat, userid);
			});
		} else {
			$('#prompt_name_container').fadeOut(1000, function() {
				// opening socket connection
				openSocket(idChat, "anonymous");
			});
		}
	}

	return false;
}

/**
 * Will open the socket connection
 */
function openSocket(idChat, idUser) {
	// Ensures only one connection is open at a time
	if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
		return;
	}

	// Create a new instance of the websocket
	webSocket = new WebSocket("ws://" + socket_url + ":" + port
			+ "/QRWebService/chat?chat=" + idChat + "&name=" + idUser);

	/**
	 * Binds functions to the listeners for the websocket.
	 */
	webSocket.onopen = function(event) {
		$('#message_container').fadeIn();

		if (event.data === undefined)
			return;

	};

	webSocket.onmessage = function(event) {

		// parsing the json data
		parseMessage(event.data);
	};

	webSocket.onclose = function(event) {

	};
}

/**
 * Sending the chat message to server
 */
function send() {
	var message = $('#input_message').val();

	if (message.trim().length > 0) {
		sendMessageToServer('message', message);
	} else {
		alert('Please enter message to send!');
	}

}
/*
 * Sending request for login
 */
function showAuthenticationQR() {
	if(loginid.length < 2){
		if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
			return;
		}
		webSocket = new WebSocket("ws://" + socket_url + ":" + port
				+ "/QRWebService/chat?loginid=" + loginid);

		/**
		 * Binds functions to the listeners for the websocket.
		 */
		webSocket.onopen = function(event) {

		};

		webSocket.onmessage = function(event) {
			parseMessage(event.data);
		};

		webSocket.onclose = function(event) {

		};
		loginid = getlogid();
		$("#qrcode").qrcode({
			text : loginid,
			fill: '#96be0e',
			width : 128,
			height : 128
		});
	}
	
	
}
function getlogid() {
	function s4() {
		return Math.floor((1 + Math.random()) * 0x10000).toString(16)
				.substring(1);
	}
	return 'authentication' + s4() + s4() + s4() + s4() + s4() + s4() + s4()
			+ s4() + s4() + s4() + s4() + s4() + s4();
}
/**
 * Closing the socket connection
 */
function closeSocket() {
	webSocket.close();

	$('#message_container').fadeOut(600, function() {
		$('#prompt_name_container').fadeIn();
		// clearing the name and session id
		sessionId = '';
		name = '';

		// clear the ul li messages
		$('#messages').html('');
		$('p.online_count').hide();
	});
}

/**
 * Parsing the json message. The type of message is identified by 'flag' node
 * value flag can be self, new, message, exit
 */
function parseMessage(message) {
	var jObj = $.parseJSON(message);

	// if the flag is 'self' message contains the session id
	if (jObj.flag == 'self') {

		sessionId = jObj.sessionId;
		var chatObj = $.parseJSON(jObj.chat);
		for (var i = 0; i < chatObj.messages.length; i++) {
			var from_name = 'You';
			// if (key.sender != null) {
			// from_name = jObj.name;
			// }
			var li = '<li><span class="name">' + from_name + '</span> '
					+ chatObj.messages[i].text + '</li>';

			// appending the chat message to list
			appendChatMessage(li);

		}
		;

	} else if (jObj.flag == 'new') {
		// if the flag is 'new', a client joined the chat room
		var new_name = 'You';

		// number of people online
		var online_count = jObj.onlineCount;

		$('p.online_count').html(
				'Hello, <span class="green">' + name + '</span>. <b>'
						+ online_count + '</b> people online right now')
				.fadeIn();

		if (jObj.sessionId != sessionId) {
			new_name = jObj.name;
		}

		var li = '<li class="new"><span class="name">' + new_name + '</span> '
				+ jObj.message + '</li>';
		$('#messages').append(li);

		$('#input_message').val('');

	} else if (jObj.flag == 'message') {
		// if the json flag is 'message', it means somebody sent the chat
		// message

		var from_name = 'You';

		if (jObj.sessionId != sessionId) {
			from_name = jObj.name.replace('&', ' ');
		}

		var li = '<li><span class="name">' + from_name + '</span> '
				+ jObj.message + '</li>';

		// appending the chat message to list
		appendChatMessage(li);

		if (jObj.sessionId == sessionId) {
			$('#input_message').val('');
		}

	} else if (jObj.flag == 'exit') {
		// if the json flag is 'exit', it means somebody left the chat room
		var li = '<li class="exit"><span class="name red">' + jObj.name
				+ '</span> ' + jObj.message + '</li>';

		var online_count = jObj.onlineCount;

		$('p.online_count').html(
				'Hello, <span class="green">' + name + '</span>. <b>'
						+ online_count + '</b> people online right now');

		appendChatMessage(li);
	} else if(jObj.flag == "auth") {
		if(jObj.success == "true"){
			jsonUser = $.parseJSON(jObj.user);
			$("#qrcode").html('successfully login :' + jsonUser.firstName);
		}else{
			$("#qrcode").html('Unable to login');
		}
		closeSocket();
	}
}

/**
 * Appending the chat message to list
 */
function appendChatMessage(li) {
	$('#messages').append(li);

	var ul = $("#messages"), last = ul.children().last();
	var wholeHeight = last.offset().top - ul.children().first().offset().top
			+ last.outerHeight() + parseFloat(ul.css("padding-top"))
			+ parseFloat(ul.css("padding-bottom"));

	// scrolling the list to bottom so that new message will be visible
	$('#messages').scrollTop(wholeHeight);
}

/**
 * Sending message to socket server message will be in json format
 */
function sendMessageToServer(flag, message) {
	var json = '{""}';

	// preparing json object
	var myObject = new Object();
	myObject.sessionId = sessionId;
	myObject.message = message;
	myObject.flag = flag;

	// converting json object to json string
	json = JSON.stringify(myObject);

	// sending message to server
	webSocket.send(json);
}