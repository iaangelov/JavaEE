var index = 0;
var currentMovie;
$(document).ready(function () {
	indexInit();
	$('.carousel').carousel();
	renderAllMovies();
	$("#carousel").on('slid.bs.carousel', function() {
		var currentIndex = $(this).find('.active').index();
		var currentId = "#" + (currentIndex);
		currentMovie = $(currentId).find("h1").text();
  });	
}); 

function enter() {
	
		localStorage.setItem("currentMovie", currentMovie);
		window.location="screenings.html";
}

function renderAllMovies() {

	$.ajax({
		url: "rest/movie",
		type: "GET",
		dataType: "json",
		statusCode: {
			200 : function(movies) {
				for(i = 0; i < movies.movie.length; i++) {
					getMovieInfo(movies.movie[i].name);
				}
			}
		}
	});
}

function getMovieInfo(movieName) {

	$.ajax({

		url: "http://www.omdbapi.com/?t=" + movieName,
		type: "GET",
		dataType: "json",
		statusCode: {
			200: function(movie) {
				renderMovie(movie);
			}
		}
	});
}

function renderMovie(movie) {


	var itemType = "item";
	if($("#div").children().size() == 0) {
		itemType = "item active";
	}
	if(index == 0) {
		currentMovie = movie.Title;
	}
	var renderMovie = "<div " + "id=" + index + " class=\"" + itemType + "\"><img id=\"poster\" align=\"left\" src=\"" + movie.Poster + "\" class=\"img-rounded\">" +
	"<div id=\"info\">" +
       "<h1>" + movie.Title + "</h1>" +
      "<p>" + movie.Runtime + " | " + movie.Genre + " | " + movie.Released + "(" + movie.Country + ")</p>" +
      "<p>Rating: " + movie.imdbRating + " from " + movie.imdbVotes + " users.</p>" +
      "<p>Plot: " + movie.Plot + "</p>" +
      "<p>Actors: " + movie.Actors + "</p>" +
      "<p>Director: " + movie.Director + "</p>" +
      "<p>Writer: " + movie.Writer + "</p>" +
      "</div>" +
      "<p id=\"enter\"><a onClick=\"enter()\" class=\"btn btn-lg btn-primary btn-sm\" href=\"#\" role=\"button\">Show Screenings</a></p></div>";
	$('#div').append(renderMovie);
	index++;
}


