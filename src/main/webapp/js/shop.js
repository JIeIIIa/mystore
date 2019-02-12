$(document).ready(function () {
    $.get("/mystore/shop/items")
        .done(function (data, status) {
            $('#products-table').html(data);
            console.log("Status: " + status);
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            $('#products-table').html('');
            console.log(jqXHR);
            console.log(textStatus);
            console.log(errorThrown);
            alert('Something went wrong:(')
        });
});

function doSubmit() {
    var items =
        $('input:checked').map(function () {
            return this.id;
        })
            .get().join(',');
    console.log('items=' + items);
    if (items === '') {
        alert('Please choose products');
        return false;
    }
    $('#items').val(items);
    return true;
}