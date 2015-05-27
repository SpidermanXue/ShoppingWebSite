function checkSignUpInfo(){
	var name = document.signup.name.value;
	var role = document.signup.role.selectedIndex;
	var age = document.getElementById("age").value;
	var state = document.signup.state.selectedIndex;
    var check = true;

	if(name == null || name == "" ){
		 document.getElementById("myname").style.display = "block";
		 check = false;
	}
	if(role == 0){
		 document.getElementById("myrole").style.display = "block";
		 check = false;
	}
	if(age == null || age == ""){
		 document.getElementById("myage").style.display = "block";
		 check = false;
	}
	if(state == 0){
		 document.getElementById("mystate").style.display = "block";
		 check = false;
	}
	return check;
	
}