<!-- Put your Project 2 code here -->
<!-- You will see on this form a CSS construct called a modal. Don't let
this scare you, this is just some pretty presentation. They allow you to create
boxes that appear when clicking on a button. You do not have to use them if you
don't want to. -->
<%@page import="java.util.*" import="helpers.*" import="java.lang.*"
	import="javax.servlet.*"%>
<%
	// time stamp
	long startTime = System.currentTimeMillis();
%>

<%--Getting actions --%>
<%
	String action = request.getParameter("action");
%>

<%-- Getting the data from data base --%>
<%	
	if (action != null) {
		//System.out.println("action is: " + action);
		//choose a build combination

		//query all data
		List<AnalyticsProduct> products = AnalyticsHelper
				.getAnalyticsProductList();	
		System.out.println("products size: " + products.size());
		List<AnalyticsUser> users = AnalyticsHelper
				.getAnalyticsUserList();
		System.out.println("user size: " + users.size());
		AnalyticsHelper.buildUserProductDataMap();
%>

<%-- Handle button behavior --%>
<%
	if (action != null) {
			if (users.size() == 20) {
%>
<form action="analytics">
	<input type="hidden" name="action" value="next20" /> <input
		type="submit" name="next20" value="next20" style="float: left;">
</form>
<%
	}
			
%>
<form action="analytics">
	<input type="hidden" name="action" value="next10" /> 
	<% if (products.size() == 10) {
	%>
	<input type="submit" name="next10" value="next10" style="float: right;">
	<% 
		}else{
	%>
	<input type="submit" name="next10" value="next10" disabled style="float: right;">
	<br>
	<%
		}
	%>
</form>
<%
	}
%>

<%-- Table --%>
<style>
table {
	border-collapse: collapse;
}

table, td, th {
	border: 1px solid black;
	border-color: #0000ff;
}
</style>

<table height="100%" width="100%" border="0" cellspacing="0"
	cellpadding="0" class="table table-striped" align="center"
	style="margin-top: 4px; margin-left: 4px; margin-right: 4px; margin-bottom: 4px; overflow: scroll;">
	<thead>
		<!-- 
        The <thead> tag is used to group header content in an HTML table.
		<tfoot> tag is to group the foot of the table 
		<tbody> tag is to use as the main body part of a table
         -->

		<tr align="center">
			<th></th>
			<%
				for (AnalyticsProduct product : products) {
			%>
			<th><B><%=product.pname%> <br>($<%=product.sum%>)</br></B></th>
			<%
				}
			%>
		</tr>
	</thead>
	<tbody>
		<%
			for (AnalyticsUser user : users) {
		%>
		<tr align="center">
			<td><B><%=user.uname%><br>($<%=user.sum%>)</B></td>
			<%
				for (AnalyticsProduct product : products) {
			%>
			<td>$<%=AnalyticsHelper.getSum(user.uid, product.pid)%></td>
			<%
				}
					}
			%>
		
	</tbody>
</table>
<%
	}
%>
<%
	long endTime = System.currentTimeMillis();
	long runTime = endTime - startTime;
%>
Running Time:<%=runTime%>
ms.
<!-- <center>
	<form action="analytics" id="formAnalytics">
	<input type="hidden" name="action" value="run"/>
	<input type="submit" name="Run" value="run" style="float:middle;">	
	</form>
  </center>  -->

