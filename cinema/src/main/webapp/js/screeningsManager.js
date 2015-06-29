var currentMovie = localStorage["currentMovie"];
var currentHall;
var index = 0;
$(document).ready(function() {
	indexInit();
	$("#title").append(currentMovie);
	renderAllScreenings();
});

function addScreening() {
	var screeningDate = $("#screeningDate")[0].value;
	console.log(screeningDate);
	var sDate = new Date(Date.parse(screeningDate, "dd-MM-yyyy HH:mm z"));
	var hall = $("#screeningHall")[0].value;
	var price = $("#screeningPrice")[0].value;
	console.log(hall);
	console.log(price);
	console.log(sDate);
	var data = {
		"screening" : {
			"hall" : [ {
				"hallNumber" : hall,
				"screenings" : []
			} ],
			"movie" : [ {
				"name" : currentMovie
			} ],
			"screeningDate" : sDate
		}
	};

	$.ajax({
		url : 'rest/screening/add',
		type : "POST",
		contentType : "application/json;charset=UTF-8",
		data : JSON.stringify(data)
	}).success(function(data) {
		$("#admin_form").attr("action", "screeningsManager.html");
	}).fail(function(data) {
		console.log(data);
		alert("Something went wrong");
	}).always(function() {
		$("#screening_form").submit();
	});
}

function renderAllScreenings() {

	$.ajax({
		url : "rest/screening/screeningsByMovieName?movieName=" + currentMovie,
		type : "GET",
		dataType : "json",
		statusCode : {
			200 : function(screenings) {
				console.log(screenings);
				for (i = 0; i < screenings.screening.length; i++) {
					console.log(screenings.screening[i].movie.name);
					renderScreening(screenings.screening[i]);
				}
			}
		}
	});
}

function enter(id) {

	var currentId = "#" + (id);
	currentHall = $(currentId).text();
	localStorage.setItem("currentHall", currentHall);
	window.location = "hall.html";
}

function renderScreening(screening) {

	var screeningDate = new Date(screening.screeningDate);
	$("#screenings_body")
			.append(
					"<tr>"
							+ "<td>"
							+ screeningDate.toLocaleDateString()
							+ "</td>"
							+ "<td id="
							+ index
							+ ">"
							+ screening.hall.hallNumber
							+ "</td>"
							+ "<td>$10.00</td>"
							+ "<td><a id="
							+ index
							+ " onClick=\"removeScr("+ screening.id+")\" class=\"btn btn-lg btn-primary btn-sm\" href=\"#\" role=\"button\">Remove</a></td>"
							+ "</tr>");
	index++;
}

function removeScr(id) {
	$.ajax({
		url : 'rest/screening/remove?id=' + id,
		type : "DELETE",
		dataType : "json",
		success : function() {
			$("#screening_table").empty();
			// var row = $("<th>Name</th>");
			// $("#movies_table").prepend(row);
			location.reload();
		}
	});
}