<!-- Put your Project 2 code here -->
<!-- You will see on this form a CSS construct called a modal. Don't let
this scare you, this is just some pretty presentation. They allow you to create
boxes that appear when clicking on a button. You do not have to use them if you
don't want to. -->
<%@page
    import="java.util.List"
    import="helpers.*"%>
    <%@ page import="javax.servlet.*"%>
<%=CategoriesHelper.modifyCategories(request)%>
<%
	List<CategoryWithCount> categories = CategoriesHelper
			.listCategories();
	//List<AnalyticsList> list = AnalyticsHelper.getList("C1");
	//AnalyticsHelper.buildAnalyticsHelper(2);
	String action = request.getParameter("action");	
 	String customer_or_states = request.getParameter("selectCol");
	String topk_or_alphabetical = request.getParameter("selectRow");
	//String cidString = request.getParameter("cid");
	//int cid = Integer.parseInt(cidString);
	//System.out.println(request.getParameter("cid"));
	
	/*
	// check actions chosen
	if (action.equals("run")){	
	
		// build states table if states filtering option is chosen
		if (customer_or_states.equals("States")){
		
		
		}
	
		// check for top k or alphabetical option 
		if (topk_or_alphabetical.equals("")){
		
		
		}
	}
	
	if (action.equals("next10")){
		AnalyticsHelper.pOffset+=10;
		AnalyticsHelper.buttonClicked = true;
	}
	
	if (action.equals("next20")){
		AnalyticsHelper.uOffset+=20;
		AnalyticsHelper.buttonClicked = true;
	}
	*/
	
	//AnalyticsHelper.getAnalyticsProductList();
%>
<div align="middle">
<form action="next10">
<input type="submit" name="next10" value="next10" style="float:right;">
</form>
</div>

<style>
table {
    border-collapse: collapse;
}

table, td, th {
    border: 1px solid black;
    border-color: #0000ff;
}
</style>

<table
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
        	for(int i = 0; i < 10; i++){
        	%>
            <th><B>product<%=i%> <br>($ &nbsp &nbsp)</br></B></th>
            <%
        	}
            %>
        </tr>
    </thead>
    <tbody>
    <%
    for(int i = 0; i < 20; i++){
    %>
    <tr align="center">
    <td><B>user<%=i%><br>($ &nbsp &nbsp)</td> 
    <%
    for(int m = 0; m < 10; m++){
    %>
    <td>$0</td>
    <%
    }
    }
    %>
    </tbody>
</table>


	<form action="next20" >
	<input type="submit" name="next20" value="next20" style="float:left;">	
	<br>
	</form>
<center>
	<form action="run">
	<input type="submit" name="Run" value="run" style="float:middle;">	
	</form>
  </center>  

