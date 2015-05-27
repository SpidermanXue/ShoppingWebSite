<%@page import="helpers.SignupHelper, org.json.simple.JSONObject"%>
<%
	JSONObject result = new JSONObject();
	System.out.println("checking username in jsp");
	result.put("result", SignupHelper.checkUsername(request.getParameter("username")));
	out.print(result);
	out.flush();
%>
