var count=0;

function updateQrCode() {
	
	count = count + 1;
	
	var options = {
		render : 'image',
		ecLevel : 'H',
		minVersion : 4,

		fill : '#333333',
		background : '#ffffff',

		text : $('#input_name').val(),
		size : 400,
		radius : 0.5,
		quiet : 1,

		mode : 4,

		mSize : 0.28,
		mPosX : 0.5,
		mPosY : 0.5,

		// label: $('#label').val(),
		// fontname: $('#font').val(),
		// fontcolor: $('#fontcolor').val(),

		image : $('#img-buffer')[0]
	};

	$('#qr').empty().qrcode(options);

	// Download link creation and management
	
	var link = document.createElement("a");
	link.setAttribute("id", "downloadlink")
	link.setAttribute("href", $('#qr').children()[0].getAttribute("src"));
	link.setAttribute("download", "MyQR.png");
	var node = document.createTextNode("");
	link.appendChild(node);
	var parent = document.getElementById("divdownload");
	if(count == 1) {
		parent.appendChild(link);
	} else {
		var elementToReplace = document.getElementById("downloadlink");
		parent.replaceChild(link, elementToReplace);
	}
	
	// Icon creation
	
	var icon = document.createElement("span");
	icon.setAttribute("class", "glyphicon glyphicon-save");
	icon.setAttribute("style", "color: white; top: 10px; font-size: 30px");
	var node2 = document.createTextNode("");
	icon.appendChild(node2);
	var linkparent = document.getElementById("downloadlink");
	linkparent.appendChild(icon);
	
}
$(function() {
	$('#input_name').keydown(function(event) {
		if (event.which == 13) {
			updateQrCode();
		}
	});
});