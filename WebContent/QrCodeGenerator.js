function updateQrCode() {

    var options = {
            render: 'image',
            ecLevel: 'H',
            minVersion: 4,

            fill: '#333333',
            background: '#ffffff',

            text: $('#input_name').val(),
            size: 400,
            radius: 0.5,
            quiet: 1,

            mode: 4,

            mSize: 0.28,
            mPosX: 0.5,
            mPosY: 0.5,

//            label: $('#label').val(),
//            fontname: $('#font').val(),
//            fontcolor: $('#fontcolor').val(),

            image: $('#img-buffer')[0]
        };
    
    $('#qr').empty().qrcode(options);

}