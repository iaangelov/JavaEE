var currentMovie = localStorage["currentMovie"];
var currentHall;
var index = 0;
$(document).ready(function () {
	indexInit();
	$("#title").append(currentMovie);
	renderAllScreenings();
}); 



function renderAllScreenings() {

	$.ajax({
		url: "rest/screening/screeningsByMovieName?movieName=" + currentMovie,
		type: "GET",
		dataType: "json",
		statusCode: {
			200 : function(screenings) {
				for(i = 0; i < screenings.screening.length; i++) {
					console.log(screenings.screening[i].movie.name);
					renderScreening(screenings.screening[i]);
				}
			}
		}
	});
}

function enter(id) {
	
	console.log(id);
	var currentId = "#" + (id);
	currentHall = $(currentId).text();
	localStorage.setItem("currentHall", currentHall);
	window.location="hall.html";
}

function renderScreening(screening) {

	var screeningDate = new Date(Date.parse(screening.screeningDate));
	$("#screenings_body").append("<tr>" +
			"<td>" + screeningDate.toLocaleString() + "</td>" +
			"<td id=" + index + ">" + screening.hall.hallNumber + "</td>" +
			"<td>10.00лв</td>" +
			"<td><a id=" + index + " onClick=\"enter(this.id)\" class=\"btn btn-lg btn-primary btn-sm\" href=\"#\" role=\"button\">Reserve Seats</a></td>" +
			"</tr>");
	index++;
}



