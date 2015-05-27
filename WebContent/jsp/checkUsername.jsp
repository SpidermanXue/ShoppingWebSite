<%@page import="helpers.SignupHelper"%>
<%
	SignupHelper.checkUsername(request.getParameter("username"));
%>
