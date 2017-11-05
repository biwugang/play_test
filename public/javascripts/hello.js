if (window.console) {
    console.log("Welcome to your Play application's JavaScript!");
}
BWG = {};
BWG.ajax = function (url, method, data) {
    if (data) {
        return $.ajax({
            url: url
            , type: method
            , async: true
            , timeout: 5000
            , beforeSend: function (xhr) {}
            , success: function (data, textStatus, jqXHR) {}
            , error: function (xhr, textStatus) {}
            , complete: function () {}
        });
    }
    else {
        return $.ajax({
            url: url
            , type: method
            , async: true
            , timeout: 5000
            , dataType: 'json'
            , data: data
            , beforeSend: function (xhr) {}
            , success: function (data, textStatus, jqXHR) {}
            , error: function (xhr, textStatus) {}
            , complete: function () {}
        })
    }
};