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
	AnalyticsHelper ah = new AnalyticsHelper(2);
	String selectedCol = request.getParameter("selectCol");
	String selectedRow = request.getParameter("selectRow");
	System.out.println("selected column is "+ selectedCol);
	System.out.println("selected row is "+ selectedRow);
	ah.getTitleList();
	ah.getAnalyticDataList(65663);
%>
<div align="right">
<select style="color:#00000F">
	<option selected>ALL</option>
  <%
  	for (CategoryWithCount cwc : categories){
  %>
  <option><%=cwc.getName()%></option>  <!-- DROPDOWN TO GET THE NAME OF CATEGORY -->
  <%
  	}
  %>
</select>
</div>
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
            <th width="20%"><B>Category Name</B></th>
            <th width="60%"><B>Category Description</B></th>
            <th
                width="20%"
                colspan="2"><B>Operations</B></th>
        </tr>
    </thead>
    <tbody>
        <%
        	for (CategoryWithCount cwc : categories) {
        %>
        <tr>

            <td><%=cwc.getName()%></td>
            <td><%=cwc.getDescription()%></td>
            <td>
                <%
                	if (cwc.getCount() == 0) {
                %> <!-- Delete Category Form -->
                <form
                    action="categories"
                    method="post">
                    <input
                        type="text"
                        name="action"
                        id="action"
                        value="delete"
                        style="display: none"> <input
                        type="text"
                        name="id"
                        id="id"
                        value="<%=cwc.getId()%>"
                        style="display: none"> <input
                        type="submit"
                        value="Delete">
                </form> <%
 	}
 %>
                <button
                    data-toggle="modal"
                    data-target="#category-<%=cwc.getId()%>">Update</button>
            </td>

            <div
                class="modal fade"
                id="category-<%=cwc.getId()%>"
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
                                id="myModalLabel">Update Category</h4>
                        </div>
                        <div class="modal-body">

                            <!-- Update Category Form -->
                            <form
                                action="categories"
                                method="post">
                                <div>
                                    Name : <input
                                        type="text"
                                        name="name"
                                        id="name"
                                        value="<%=cwc.getName()%>"
                                        size="40">
                                </div>
                                <div>
                                    Description :
                                    <textarea
                                        name="description"
                                        id="description"
                                        cols=40
                                        rows=6><%=cwc.getDescription()%></textarea>
                                </div>
                                <input
                                    type="text"
                                    name="id"
                                    id="id"
                                    value="<%=cwc.getId()%>"
                                    style="display: none"> <input
                                    type="text"
                                    name="action"
                                    id="action"
                                    value="update"
                                    style="display: none"> <input
                                    type="submit"
                                    value="Update">
                            </form>


                        </div>
                    </div>
                </div>
            </div>
        </tr>
        <%
        	}
        %>
    </tbody>
</table>
<div class="panel panel-default">
    <div class="panel-body">

        <button
            data-toggle="modal"
            data-target="#newCategory">Insert</button>

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