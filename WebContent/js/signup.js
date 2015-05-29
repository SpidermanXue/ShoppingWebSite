function checkSignUpInfo() {
	var name = document.signup.name.value;
	var role = document.signup.role.selectedIndex;
	var age = document.getElementById("age").value;
	var state = document.signup.state.selectedIndex;
	var check = true;

	if (name == null || name == "") {
		document.getElementById("myname").style.display = "block";
		check = false;
	}
	if (role == 0) {
		document.getElementById("myrole").style.display = "block";
		check = false;
	}
	if (age == null || age == "") {
		document.getElementById("myage").style.display = "block";
		check = false;
	}
	if (state == 0) {
		document.getElementById("mystate").style.display = "block";
		check = false;
	}

	// not proceeding if got error.
	if (!check)
		return check;

	console.log("Checking username in js");
	console.log(name);
	if (name) {

		// for checking user name
		var xmlHttp;
		xmlHttp = new XMLHttpRequest();

		var responseHandler = function() {
//			if (xmlHttp.status != 200) {
//				alert("HTTP status is " + xmlHttp.status + " instead of 200");
//				// return;
//			}
			if (xmlHttp.readyState == 4) {
				var responseDoc = xmlHttp.responseText;
				var response = eval('(' + responseDoc + ')');
				console.log(responseDoc);
				console.log(response.result);
				if (!response.result) {
					document.getElementById("errorUsername").style.display = "block";
				}
			}
		}
		
		xmlHttp.onreadystatechange = responseHandler;
		xmlHttp.open("GET", "./jsp/checkUsername.jsp?username=" + name, true);
		xmlHttp.send(null);
	}

	return false;
}

// haven't finish here, don't know how to parse the response yet.
function checkUsername(name) {

}
