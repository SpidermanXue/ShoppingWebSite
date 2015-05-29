<%@page import="helpers.SignupHelper, org.json.simple.JSONObject"%>
<%
	JSONObject result = new JSONObject();
	String name = request.getParameter("username");
	int age = Integer.parseInt(request.getParameter("userage"));
	String role = request.getParameter("userrole");
	String state = request.getParameter("userstate");
	//System.out.println("joe:::"+ name + age + role + state);
//	System.out.println("checking username in jsp");
	boolean feedback = SignupHelper.checkUsername(request.getParameter("username"));
	result.put("result", feedback);
	if(feedback){
		result.put("signup", helpers.SignupHelper.signup(name, age, role, state));
	}
	//System.out.println("evan" + result);
	out.print(result); //sent to html not same java
	out.flush(); //buffer flush 
%>
