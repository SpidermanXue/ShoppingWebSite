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
	return check;

}
//haven't finish here, don't know how to parse the response yet.
function checkUsername() {
	var xmlHttp;
	xmlHttp = new XMLHttpRequest();
	var username = document.getElementById("username").value;

	var responseHandler = function() {
		if (xmlHttp.status != 200){
			alert("HTTP status is " + xmlHttp.status + " instead of 200");
			return;
		}
		if (xmlHttp.readyState == 4) {
			var responseDoc = xmlHttp.responseText;
			var response = eval('('+responseDoc+')');
			document.write(responseDoc);
			document.write(response);
			//if(response.isValidUser){
				document.getElementById("errorUsername").style.display = "block";
			//}
		}
	}

	xmlHttp.onreadystatechange = responseHandler;
	xmlHttp.open("GET", "checkUsername.jsp?useranme="+username, true);
	xmlHttp.send(null);
}
