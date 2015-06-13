//display login, register or logout links
function loginControls() {
	$.ajax({
		url : 'rest/user/authorized',
		type : "GET",
		dataType : "json",
		statusCode : {
			200 : function() {
				if (getCurrentUserType() === "Administrator") {
					$('<li><a href="adminPanel.html">Admin panel</a></li>')
							.prependTo("#navelems");
				}
				$(".navigation").css("display", "inline");
			},
			401 : function() {
				$(".login_register").css("display", "inline");
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
			$(".welcome-greeting").css("display", "inline");
			$("#user-holder").text(data);
		}
	});
}

function getCurrentUserType() {
	var type = "";
	$.ajax({
		url : 'rest/user/type',
		type : "GET",
		dataType : "text",
		success : function(data) {
			type = data;
		}
	});
	return type;
}

// show error and success messages on the index page
function showMessage(ElementID) {
	$(ElementID).css({
		'width' : '40%',
		'margin-top' : '4.5%',
		'text-align' : 'center',
		'margin-left' : '30%'
	});
	$(ElementID).show();
	localStorage.clear();
}

function indexInit() {
	getUserName();
	loginControls();
}

function register() {
	var username = $("#usernameRegister")[0].value;
	var password = $("#pwdRegister")[0].value;
	var email = $("#email")[0].value;

	if (!isPasswordValid(password)) {
		return;
	}

	var data = {
		user : {
			username : username,
			password : password,
			email : email
		}
	}

	$.ajax({
		url : 'rest/user',
		type : "POST",
		async : false,
		contentType : "application/json;charset=UTF-8",
		data : JSON.stringify(data)
	}).success(function(data) {
		localStorage.setItem('registerSuccess', 'true');
		return;
	}).fail(function(data) {
		localStorage.setItem('userTaken', 'true');
		return;
	});
}

function isPasswordValid(password) {
	var confirmPassword = $("#conf_pwdRegister")[0].value;

	if (password == "" || confirmPassword == "") {
		alert("Please fill both fields for password and try again.");
		return false;
	}

	if (password != confirmPassword) {
		alert("Two passwords do not match. Please correct the values and try again.");
		return false;
	}
	return true;
}

function login() {
	var username = $("#usernameLogin")[0].value;
	var password = $("#pwdLogin")[0].value;

	if (username == "" || password == "") {
		alert("Username and password should not be empty.");
		return;
	}

	var data = {
		user : {
			username : username,
			password : password
		}
	}

	$.ajax({
		url : 'rest/user/login',
		type : "POST",
		async : false,
		contentType : "application/json;charset=UTF-8",
		data : JSON.stringify(data),
		statusCode : {
			401 : function() {
				localStorage.setItem('loginFail', 'true');
				return;
			},
			200 : function() {
				localStorage.setItem('loginSuccess', 'true');
				window.location.replace("index.html");
				return;
			}
		}
	});
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
