//open create meeting jsp
$(function () {
    $('#menuCreateMeeting').click(function () {

        $('#pagecontainer').html('<img src="loading.gif" /> Now loading...');

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

    $('#websockettest').click(function () {
        $('#pagecontainer').load('websocket.jsp');
    });

});


//open invitations.jsp - pending invitations
$(function () {
    $('#menuPendingInvitations').click(function () {

        $('#pagecontainer').html('<img src="loading.gif" /> Now loading...');


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

// open meetings.jsp - list meetings / meetings overview
$(function () {
    $('#menuListMeetings').click(function () {

        $('#pagecontainer').html('<img src="loading.gif" /> Now loading...');
        $.ajax({
            url: 'openMeetings',
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


//Enter 'join meeting' menu
$(function () {
    $('#menuJoinMeeting').click(function () {

        $('#pagecontainer').html('<img src="loading.gif" /> Now loading...');
        $.ajax({
            url: 'listMyMeetings',
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


/*$('#formViewDetails').submit(function() { // catch the form's submit event
 $.ajax({ // create an AJAX call...
 data: $(this).serialize(), // get the form data
 type: $(this).attr('POST'), // GET or POST
 url: $(this).attr('openMeetingDetails'), // the file to call
 success: function(response) { // on success..
 $('#meetingsmaindiv').html(response); // update the DIV
 }
 });
 return false; // cancel original event to prevent form submitting
 });*/



/*$('#formViewDetails').submit(function() { // catch the form's submit event
 $.ajax({ // create an AJAX call...
 data: $(this).serialize(), // get the form data
 type: $(this).attr('POST'), // GET or POST
 url: $(this).attr('openMeetingDetails'), // the file to call
 success: function(response) { // on success..
 $('#meetingsmaindiv').html(response); // update the DIV
 }
 });
 return false; // cancel original event to prevent form submitting
 });*/




//open meetingdetails.jsp
/*$(function () {
 $('#viewDetails').click(function () {

        $('#pagecontainer').html('<img src="loading.gif" /> Now loading...');
        $.ajax({
            url: 'openMeetingDetails',
 data: $("#hiddenlabel"),
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
 });*/

//set datetimepicker preferenes
$(function () {
    $('.form_datetime').datetimepicker({
        format: "yyyy-MM-dd hh:ii",
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0

    });
});