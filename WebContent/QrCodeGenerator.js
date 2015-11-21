function updateQrCode() {

    var options = {
            render: 'image',
            ecLevel: 'H',
            minVersion: 4,

            fill: '#333333',
            background: '#ffffff',

            text: $('#text').val(),
            size: 400,
            radius: 50,
            quiet: 1,

            mode: 0,

//            mSize: parseInt($('#msize').val(), 10) * 0.01,
//            mPosX: parseInt($('#mposx').val(), 10) * 0.01,
//            mPosY: parseInt($('#mposy').val(), 10) * 0.01,
//
//            label: $('#label').val(),
//            fontname: $('#font').val(),
//            fontcolor: $('#fontcolor').val(),
//
//            image: $('#img-buffer')[0]
        };

    $('#qr').empty().qrcode(options);
}