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
	AnalyticsHelper.buildAnalyticsHelper(2);
/* 	String selectedCol = request.getParameter("selectCol");
	String selectedRow = request.getParameter("selectRow");
	System.out.println("selected column is "+ selectedCol);
	System.out.println("selected row is "+ selectedRow); */
	AnalyticsHelper.getAnalyticsProductList();
%>
<div align="middle">
<form action="">
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
    <td> user<%=i%><br>($ &nbsp &nbsp)</td> 
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
<div class="panel panel-default">
    <div class="panel-body">

	<form action="">
	<input type="submit" name="next20" value="next20" style="float:left;">	
	</form>
        <button> RUN</button>
        <div
            class="modal fade"
            id="newCategory"
            aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button
                            type="button"
                            class="close"
                            data-dismiss="modal"
                            aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4
                            class="modal-title"
                            id="myModalLabel">New Category</h4>
                    </div>
                    <div class="modal-body">


                        <!-- Insert New Category Form -->
                        <form
                            action="categories"
                            method="post">
                            <div>
                                Name : <input
                                    type="text"
                                    name="name"
                                    id="name"
                                    value=""
                                    size="40">
                            </div>
                            <div>
                                Description :
                                <textarea
                                    name="description"
                                    id="description"
                                    cols=40
                                    rows=6></textarea>
                            </div>
                            <input
                                type="text"
                                name="action"
                                id="action"
                                value="insert"
                                style="display: none"> <input
                                type="submit"
                                value="Insert">
                        </form>



                    </div>
                </div>
            </div>
        </div>
    </div>
</div>