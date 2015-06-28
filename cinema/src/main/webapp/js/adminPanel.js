function getMovies() {
	$.ajax({
		url : 'rest/movie',
		type : "GET",
		dataType : "json",
		success : function(data, textStatus, jqXHR) {
			drawTable(data);
		}
	});
}

function getUsers() {
	$.ajax({
		url: 'rest/admin/users/all',
		type: "GET",
		dataType: "json",
		success : function(data, textStatus, jqXHR) {
			drawUsersTable(data);
		}
	})
}

function drawUsersTable(data) {
	var users = data.user;
	for (var i = 0; i < users.length; i++) {
		drawUserRow(users[i]);
	}
}

function drawUserRow(rowData) {
	var row = $("<tr />")
	$("#users_table").append(row);
	row.append($("<td>" + rowData.id + "</td>"));
	row.append($("<td>" + rowData.username + "</td>"));
	row.append($("<td>" + rowData.email + "</td>"));
	row.append($("<td>" + rowData.userType + "</td>"));
    var remove = $("<td />");
    var link = $("<button class=\"btn btn-lg btn-primary btn-sm\">Remove</button>");
    remove.append(link);
    row.append(remove);
    link.click(function() {
        $.ajax({
            url: 'rest/admin/remove?id=' + rowData.id,
            type: "DELETE",
            success : function () {
            	$("#users_table").empty();
            	var row = $("<th>User name</th>" +
            			"<th>Email</th>" + 
            			"<th>User type</th>")
            	$("#users_table").prepend(row);
            	getUsers();
			}
		});
    });
}

function drawTable(data) {
	var movies = data.movie;
	for (var i = 0; i < movies.length; i++) {
		drawRow(movies[i]);
	}
}

function drawRow(rowData) {
	var row = $("<tr />")
	$("#movies_table").append(row);
	row.append($("<td>" + rowData.name + "</td>"));
    var remove = $("<td />");
    var link = $("<button class=\"btn btn-lg btn-primary btn-sm\">Remove</button>");
    var screenings = $("<button class=\"btn btn-lg btn-primary btn-sm\">Show Screenings</button>");
    remove.append(link);
    remove.append(screenings);
    row.append(remove);
    link.click(function() {
        $.ajax({
            url: 'rest/movie/remove?movieId=' + rowData.id,
            type: "DELETE",
            dataType: "json",
            success : function () {
            	$("#movies_table").empty();
            	var row = $("<th>Name</th>");
            	$("#movies_table").prepend(row);
            	getMovies();
			}
		});
    });
    screenings.click(function() {
    	localStorage.setItem("currentMovie",rowData.name);
    	window.location="screeningsManager.html";
    });
}

function scrBtnAct(currentMovie) {
	localStorage.setItem("currentMovie",currentMovie);
	window.location="screeningsManager.html";
}

function addMovie() {
	    var name = $("#movieName")[0].value;
	    var data = { movie : {
	    		name : name
    			}
	    }
	    
		$.ajax({
		 	url : 'rest/movie/add',
		    type: "POST",
		    contentType: "application/json;charset=UTF-8",
		    data: JSON.stringify(data)
		})
		.success(function(data) {
		    $("#admin_form").attr("action", "adminPanel.html");
		})
		.fail(function(data) {
		   alert("Something went wrong");

		})
		.always(function() {
                    $("#movie_form").submit();
        });
    }

function findTickets() {
	var names = $("#names")[0].value;
	$.ajax({
        url: 'rest/ticket/byUserNames/' + names,
        type: "GET",
        dataType: "json",
        success : function (data) {
        	$("#tickets_table").empty();
        	var row = $("<th>Screening</th>" +
			"<th>User</th>" +
			"<th>Seat number</th>" +
			"<th>Status</th>");
        	$("#tickets_table").prepend(row);
        	renderTickets(data);
		}
	});
}

function renderTickets(data) {
		var tickets = data.ticket;
		for (var i = 0; i < tickets.length; i++) {
			drawTicketRow(tickets[i]);
		}
	}

	function drawTicketRow(rowData) {
		var row = $("<tr />")
		$("#tickets_table").append(row);
		row.append($("<td>" + rowData.screening.hall.hallNumber +"," + rowData.screening.movie.name + "</td>"));
		row.append($("<td>" + rowData.user.username + "</td>"));
		row.append($("<td>" + rowData.seatNumber + "</td>"));
		row.append($("<td>" + rowData.status + "</td>"));
		var remove = $("<td />");
		var link = $("<button class=\"btn btn-lg btn-primary btn-sm\">Remove</button>");
	    remove.append(link);
	    row.append(remove);
	    link.click(function() {
	        $.ajax({
	            url: 'rest/ticket/remove?ticketId=' + rowData.id,
	            type: "DELETE",
	            dataType: "json",
	            success : function () {
	            	$("#tickets_table").empty();
	            	var row = $("<th>Screening</th>" +
	            			"<th>User</th>" +
	            			"<th>Seat number</th>" +
	            			"<th>Status</th>");
	            	$("#tickets_table").prepend(row);
	            	findTickets();
				}
			});
	    });
}