function getMyTickets() {
	var data = "";
	$.ajax({
		url : "rest/ticket/mytickets",
		type : "GET",
		dataType : "json",
		success : function(tickets) {
			data = tickets;
		}
	});
	return data;
}

function reserveTicket(ticketId) {
	var link = $("#reserve");
	link.click(function() {
		$.ajax({
			url : 'rest/ticket/reserveTicket?ticketId=' + ticketId,
			type : "PUT",
			dataType : "json",
			success : function() {
				showMessage("#reserveSuccess");
				/*
				 * <div id="reserveSuccess" class="alert alert-success fade in"
				 * style="display: none"> <a href="#" class="close"
				 * data-dismiss="alert">&times;</a> <strong>Success!</strong>
				 * Your ticket will be reserved for 10 minutes! </div>
				 */
			}
		});
	});
}

function getAllTickets() {
	$.ajax({
		url : "rest/ticket/mytickets",
		type : "GET",
		dataType : "json",
		success : function(tickets) {
			renderTickets(tickets);
		}
	});
}

function renderTickets(data) {
	if (typeof data === 'undefined') {
		$("#div")
				.append(
						"<h1 style='text-align: center; vertical-align: middle; margin-top: 15%; color: white;'>You do not have any tickets yet.</h1>");
		$("#tickets_table").remove();
		return;
	}
	var tickets = data.ticket;
	for (var i = 0; i < tickets.length; i++) {
		renderRow(tickets[i]);
	}
}

function renderRow(rowData) {
	var row = $("<tr/>")
	var longDateFormat = 'dd/MM/yyyy HH:mm:ss';
	var screeningDate = $.format.date(rowData.screening.screeningDate,
			longDateFormat);
	var link = $("<button class=\"btn btn-lg btn-primary btn-sm\">Confirm</button>");
	$("#tickets_table").append(row);
	row.append($("<td>" + rowData.screening.movie.name + "</td>"));
	row.append($("<td>" + rowData.screening.hall.hallNumber + "</td>"));
	row.append($("<td>" + screeningDate + "</td>"));
	row.append($("<td>" + rowData.status + "</td>"));
	if(rowData.status == 'RESERVED'){
		row.append(link);
		link.click(function(){
	    	$.ajax({
	    		url: 'rest/ticket/confirm?id=' + rowData.id,
	    		type: 'PUT',
	    		dataType: 'json',
	    		async: false,
	    		statusCode: {
	    			200: function(){
		    			alert("Successfully confirmed ticket!");
		    			$("#tickets_table").empty();
		    			getAllTickets();
		    		}
	    		}
	    	});
	    });
	}
}
