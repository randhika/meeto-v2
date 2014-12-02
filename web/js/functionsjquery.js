//open create meeting jsp
$(function () {
    $('#menuCreateMeeting').click(function () {
        $.ajax({
            url: 'openCreateMeeting',
            type: 'POST',

            dataType: 'html',
            success: function (html) {
                $('#pagecontainer').html(html);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert('An error occurred! ' + thrownError);
            }
        });
    });
});

$(function () {
    $('#menuPendingInvitations').click(function () {
        $.ajax({
            url: 'openInvitations',
            type: 'POST',

            dataType: 'html',
            success: function (html) {
                $('#pagecontainer').html(html);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert('An error occurred! ' + thrownError);
            }
        });
    });
});


//set datetimepicker preferenes
$(function () {
    $(".form_datetime").datetimepicker({
        format: "yyyy-MM-dd hh:ii",
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0

    });
});
/*
 $(function(){
 $('#submitMeetingBtn').click(function(){
 $.ajax({
 dataType: 'html',
 success: function (html) {
 $('#pagecontainer').html(html); //set result.jsp output to leftDiv
 }

 });
 });
 })*/



$(function () {

    $('#menuListMeetings').click(function () {
        $('#pagecontainer').load('pending_invitations.jsp');
    });

});

$(function () {
    $('#itemShares').click(function () {
        $('#pagecontainer').load('SharesServlet?optionshares=1');
    });

});

$(function () {
    $('#itemTransactions').click(function () {
        $.get("TransactionsServlet", function (data) {
            $('#pagecontainer').html(data.replace(/\n/g, "<br>"));

        }, "text");
    });
});

$(function () {
    $('#refreshCoins').click(function () {
        $.get("MoneyServlet", function (data) {
            $("#coins").text(data + "DEIcoins");
        }, "text");
    });
});

$(function () {
    $('#portfolio').click(function () {
        $('#pagecontainer').load('PortfolioServlet?');
    });
});

//mudar para hall of fame
$(function () {
    $('#itemHallofFame').click(function () {
        $('#pagecontainer').load('HallofFameServlet?');
    });
});

// root - comprar x ideia
$(function () {
    $('#buyallbtn').click(function () {

        alert("Entrou no jquery");

        var selected = $("#listideasroot").val().split("\t");

        alert("valor do selected:" + selected);

        alert("valor do selected[0]:" + selected[0]);

        $.get("RootServlet?ididearoot=" + selected[0], function (data) {
            alert("Chamou servlet e retornou alguma coisa");
            if ($.trim(data) == 'true') {
                alert("It's done Boss!");
                location.refresh();
            }
            else {
                alert("Ups...something went wrong..PLEASE DON'T HURT US!!");
                location.refresh();

            }
        }, "text");
    });

});


$(function () {
    $('#itemNotifications').click(function () {
        $('#pagecontainer').load('notifications.jsp');
    });

});














                      





