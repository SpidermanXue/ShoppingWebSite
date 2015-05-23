<!-- Put your Project 2 code here -->
<!-- You will see on this form a CSS construct called a modal. Don't let
this scare you, this is just some pretty presentation. They allow you to create
boxes that appear when clicking on a button. You do not have to use them if you
don't want to. -->
<%@page
    import="java.util.*"
    import="helpers.*"
    import = "java.lang.*"
    import="javax.servlet.*"%>
<%=CategoriesHelper.modifyCategories(request)%>
<%
	List<CategoryWithCount> categories = CategoriesHelper
			.listCategories();	

	String action = request.getParameter("action");	
	String customer_or_states = request.getParameter("selectCol");
	String topk_or_alphabetical = request.getParameter("selectRow");
	int cid = 0;
	if (request.getParameter("cid")!=null){
		cid = Integer.parseInt(request.getParameter("cid"));
	}

%>

<% 
if (action !=null)
{
%>	

<form action="analytics" >
<input type="hidden" name="action" value="next20"/>
<input type="submit" name="next20" value="next20" style="float:left;">	
</form>
		
<form action="analytics">
<input type="hidden" name="action" value="next10"/>
<input type="submit" name="next10" value="next10" style="float:right;">
<br>
</form>
<%
}
%>
<style>
table {
    border-collapse: collapse;
}

table, td, th {
    border: 1px solid black;
    border-color: #0000ff;
}
</style>
<%	
	//System.out.println(customer_or_states);
	System.out.println("action is: " + action);
	if(action!=null && action.equals("run")){
		AnalyticsHelper.buildAnalyticsHelper(0, true, false);
		List<AnalyticsProduct> products = AnalyticsHelper.getAnalyticsProductList();
		System.out.println(products.isEmpty());
		List<AnalyticsUser> users = AnalyticsHelper.getAnalyticsUserList();
		List<List<Integer>> dataSet = AnalyticsHelper.getUserProductDataList(users.size(), products.size()); 
		/*
		// build states table if states filtering option is chosen
		if (customer_or_states.equals("States")){
		
		
		}

		// check for top k or alphabetical option 
		if (topk_or_alphabetical.equals("")){
			
		}
		*/
%>
<table  width="100%" border="0" cellspacing="0" cellpadding="0"
    class="table table-striped"
    align="center">
    <thead>
        <!-- 
        The <thead> tag is used to group header content in an HTML table.
		<tfoot> tag is to group the foot of the table 
		<tbody> tag is to use as the main body part of a table
         -->
         
        <tr align="center">
        	<th></th>
        	<%
        	
        	for(AnalyticsProduct product : products){
        	%>
            <th><B><%=product.pname%> <br>($<%= product.sum%>)</br></B></th>
            <%
        	}
            %>
        </tr>
    </thead>
    <tbody>
    <%
    for(int i = 0; i < users.size(); i++){
    	AnalyticsUser user = users.get(i);
    	List<Integer> rowSums = dataSet.get(i);
    %>
    <tr align="center">
    <td><B><%=user.uname %>><br>($<%=user.sum %>)</B></td> 
    <%
    for(Integer sum : rowSums){
    %>
    <td>$<%=sum%></td>
    <%
    }
    }
    %>
    </tbody>
</table>
<%
	}
%>

	
  <!-- <center>
	<form action="analytics" id="formAnalytics">
	<input type="hidden" name="action" value="run"/>
	<input type="submit" name="Run" value="run" style="float:middle;">	
	</form>
  </center>  -->

