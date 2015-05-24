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
	String customer_or_states = request.getParameter("selectCol");
	String topk_or_alphabetical = request.getParameter("selectRow");
	String next10 = request.getParameter("next10");
	String next20 = request.getParameter("next20");
%>

<%-- Getting the data from data base --%>
<%
	//System.out.println(customer_or_states);
	int cid = 0;
	if (request.getParameter("cid") != null) {
		cid = Integer.parseInt(request.getParameter("cid"));
	}
	System.out.println("action is: " + action);
	if (action != null && action.equals("run")) {
		System.out.println(customer_or_states);
		System.out.println(topk_or_alphabetical);

		//choose a build combination
		if (next10 != null) {
			AnalyticsHelper.buildTop10(AnalyticsHelper.pOffset, cid,
					topk_or_alphabetical.equals("Top K"));
		} else if (next20 != null) {
			AnalyticsHelper.buildTop20(AnalyticsHelper.uOffset, cid,
					topk_or_alphabetical.equals("Top K"),
					customer_or_states.equals("States"));
		} else {
			AnalyticsHelper.buildAnalyticsHelper(cid,
					topk_or_alphabetical.equals("Top K"),
					customer_or_states.equals("States"));
		}
		//query all data
		List<AnalyticsProduct> products = AnalyticsHelper
				.getAnalyticsProductList();		
		List<AnalyticsUser> users = AnalyticsHelper
				.getAnalyticsUserList(customer_or_states
						.equals("States"));
		System.out.println("Getting 200 rows");
		System.out.println("option: "+customer_or_states);
		AnalyticsHelper.buildUserProductDataMap(customer_or_states
				.equals("States"));
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
			if (products.size() == 10) {
%>
<form action="analytics">
	<input type="hidden" name="action" value="next10" /> <input
		type="submit" name="next10" value="next10" style="float: right;">
	<br>
</form>
<%
	}
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

