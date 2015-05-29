<%@page import="helpers.SignupHelper, org.json.simple.JSONObject"%>
<%
	JSONObject result = new JSONObject();
	System.out.println("checking username in jsp");
	result.put("result", SignupHelper.checkUsername(request.getParameter("username")));
	System.out.println("evan" + result);
	out.print(result); //sent to html not same java
	out.flush(); //buffer flush 
%>
