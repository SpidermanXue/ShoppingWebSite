 <%@ page import="javax.servlet.*"%>
 <%@page
    import="java.util.List"
    import="helpers.*"%>
   
    <% 
    if (AnalyticsHelper.buttonClicked == false)
    {	
    %>	
<div class="panel panel-default">
	<div class="panel-body">
		<div class="bottom-nav">
			<h4>Options</h4>
			<!-- Put your part 2 code here -->
			<select style="color: #00000F">
				<option selected>ALL</option>
				<%
					List<CategoryWithCount> categories = CategoriesHelper.listCategories();
					for (CategoryWithCount cwc : categories) {
				%>
				<option><%=cwc.getName()%></option>
				<!-- DROPDOWN TO GET THE NAME OF CATEGORY -->
				<%
					}
				%>
			</select>
			<form action="jsp/list-analytics.jsp" method="post" id="formAnalytics">
					<select name="selectCol" form="formAnalytics">
						<option selected>Customers</option>
						<option>States</option>
					</select> 
					<select name="selectRow" form="formAnalytics">
						<option selected>Top K</option>
						<option>Alphabetical</option>
					</select>
					<input type="submit">
						</form> 
	<%
    }
	%>	
		</div>
	</div>
</div>