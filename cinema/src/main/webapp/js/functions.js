//display login, register or logout links
function loginControls() {
	$.ajax({
		url : 'rest/user/authorised',
		type : "GET",
		dataType : "json",
		statusCode : {
			200 : function() {
				$(".login_register").hide();
				isUserAuthenticated = true;
			},
			401 : function() {
				$('.logout').hide();
			}
		}
	});
}

//fetch movies from DB and draw table on index page
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

// get user for welcoming message
function getUserName() {
	$.ajax({
		url : 'rest/user/current',
		type : "GET",
		dataType : "text"
	}).always(function(data) {
		if (typeof data != 'undefined') {
			$("#user-holder").text(data);
		} else {
			$(".welcome-greeting").hide();
		}
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
	row.append($("<td>" + rowData.year + "</td>"));
	row.append($("<td>" + rowData.genre + "</td>"));
}

function indexInit(){
	getMovies();
	getUserName();
	loginControls();
}

function logout() {
	$.ajax({
		url : 'rest/user/logout',
		type : "GET",
		dataType : "text"
	}).always(function(data) {
		window.location.replace("index.html");
	});
}

// used to redirect already authorized users who should not access certain pages
function redirectAuthorised() {
	$.ajax({
		url : 'rest/user/authorised',
		type : "GET",
		dataType : "json",
		statusCode : {
			200 : function() {
				window.location.replace("index.html")
			},
			401 : function() {
			}
		}
	});
}