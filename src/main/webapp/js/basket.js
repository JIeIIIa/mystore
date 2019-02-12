$('#buy').click(function () {
    var items =
        $('[data-vendor-code]').map(function () {
            return this.getAttribute('data-vendor-code');
        })
            .get().join(',');
    console.log('items=' + items);
    $.post("/mystore/shop/buyService", {'items': items})
        .done(function (data, status, xhr) {
            if (xhr.status === 201) {
                window.location.replace("/mystore/shop/success");
            } else {
                window.location.replace("/mystore/shop/failure");
            }
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            window.location.replace("/mystore/shop/failure");
        });
});