//display login, register or logout links
function loginControls() {
	$.ajax({
		url : 'rest/user/authorized',
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


function indexInit(){
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
		url : 'rest/user/authorized',
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