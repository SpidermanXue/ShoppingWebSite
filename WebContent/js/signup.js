function checkSignUpInfo() {
	var name = document.signup.name.value;
	var rol = document.signup.role.selectedIndex;
	var role = document.signup.role.value;
	var age = document.getElementById("age").value;
	var sta = document.signup.state.selectedIndex;
	var state = document.signup.state.value;
	var check = true;

	if (name == null || name == "") {
		document.getElementById("myname").style.display = "block";
		check = false;
	}
	if (rol == 0) {
		document.getElementById("myrole").style.display = "block";
		check = false;
	}
	if (age == null || age == "") {
		document.getElementById("myage").style.display = "block";
		check = false;
	}
	if (sta == 0) {
		document.getElementById("mystate").style.display = "block";
		check = false;
	}

	// not proceeding if got error.
	if (!check)
		return check;
	
	
	
	var params = new Array();
	params.push("username=" + name);
	params.push("userrole=" + role);
	params.push("userage=" + age);
	params.push("userstate=" + state);
	var url = "./jsp/checkUsername.jsp?" + params.join("&");
	
	console.log("Checking username in js");
	console.log(url);
	if (name) {

		// for checking user name
		var xmlHttp;
		xmlHttp = new XMLHttpRequest();

		var responseHandler = function() {

			if (xmlHttp.readyState == 4) {
				var responseDoc = xmlHttp.responseText;
				var response = eval('(' + responseDoc + ')');
			 	console.log(responseDoc);
			 	//console.log(response.result);
				if (!response.result) {
					document.getElementById("errorUsername").style.display = "block";
					check = false;
				}
				if (response.result && response.signup){
					document.getElementById("success").style.display = "block";
					checkk = false;
				}
				
			}
		}
		
		xmlHttp.onreadystatechange = responseHandler;
		xmlHttp.open("GET", url, true);
		xmlHttp.send(null);
	}

	return false;
}

// haven't finish here, don't know how to parse the response yet.
function checkUsername(name) {

}
