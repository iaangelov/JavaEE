/**
 * Created by Dimitar Panayotov on 14-Jun-15.
 */

function getMyTickets(){
    var data = "";
    $.ajax({
        url: "rest/ticket/mytickets",
        type: "GET",
        dataType: "json",
        success : function(tickets) {
            data = tickets;
        }
    });
    return data;
}

function getAllTickets(){
    $.ajax({
        url: "rest/ticket/mytickets",
        type: "GET",
        dataType: "json",
        success : function(tickets) {
                renderTickets(tickets);
            }
    });
}

function renderTickets(data){
    var tickets = data.ticket;
    for (var i = 0; i < tickets.length; i++) {
        renderRow(tickets[i]);
    }
}

function renderRow(rowData) {
    var row = $("<tr/>")
    var longDateFormat  = 'dd/MM/yyyy HH:mm:ss';
    var screeningDate = $.format.date(rowData.screening.screeningDate, longDateFormat);
    $("#tickets_table").append(row);
    row.append($("<td>" + rowData.screening.movie.name + "</td>"));
    row.append($("<td>" + rowData.screening.hall.hallNumber + "</td>"));
    row.append($("<td>" + screeningDate + "</td>"));
    row.append($("<td>" + rowData.seatNumber + "</td>"));

}
