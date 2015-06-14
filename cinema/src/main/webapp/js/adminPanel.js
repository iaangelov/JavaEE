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
    remove.append(link);
    row.append(remove);
    link.click(function() {
        $.ajax({
            url: 'rest/movie/remove?movieId=' + rowData.id,
            type: "DELETE",
            dataType: "json",
            success : function () {
            	$("#movies_table").empty();
            	getMovies();
			}
		});
    });
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