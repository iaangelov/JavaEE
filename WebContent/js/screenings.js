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
				for(i = 0; i < screenings.length; i++) {
					console.log(screenings[i].movie.name);
					renderScreening(screenings[i]);
				}
			}
		}
	});
}

function enter(id) {
	var ids = id.split("s");
	var currentId = "#" + (ids[0]);
	var screeningId = "#" + (ids[1]);
	currentHall = $(currentId).text();
	localStorage.setItem("currentHall", currentHall);
	localStorage.setItem("currentScreening", ids[1]);
	window.location="halls.html";
}

function renderScreening(screening) {
	var scrIdd = screening.id;
	var screeningDate = new Date(Date.parse(screening.screeningDate));
	$("#screenings_body").append("<tr>" +
			"<td>" + screeningDate.toLocaleString() + "</td>" +
			"<td id=" + index + ">" + screening.hall.hallNumber + "</td>" +
			"<td>10.00euro</td>" +
			"<td><a id=" + index + "s" + scrIdd + " onClick=\"enter(this.id, this.scrId)\" class=\"btn btn-lg btn-primary btn-sm\" href=\"#\" role=\"button\">Reserve Seats</a></td>" +
			"</tr>");
	index++;
}



