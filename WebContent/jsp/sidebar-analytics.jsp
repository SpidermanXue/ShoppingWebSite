 <%@ page import="javax.servlet.*"%>
 <%@page
    import="java.util.List"
    import="helpers.*"%>
   
    <% 
    AnalyticsHelper.changeSelection(request);
    if (AnalyticsHelper.rowOffset <20 || AnalyticsHelper.colOffset <10)
    {	
    %>	
<div class="panel panel-default">
	<div class="panel-body">
		<div class="bottom-nav">
			<h4>Options</h4>
			<!-- Put your part 2 code here -->
			<form action="analytics" method="post" id="formAnalytics">
				<select name = "cid" style="color: #00000F" form="formAnalytics">
				<option value = "<%=0 %>" selected>ALL</option>
				<%
					List<CategoryWithCount> categories = CategoriesHelper.listCategories();
					for (CategoryWithCount cwc : categories) {
						//System.out.println("id is " + cwc.getId());
				%>
				<option value = "<%=(int)cwc.getId() %>" ><%=cwc.getName()%></option>
				<!-- DROPDOWN TO GET THE NAME OF CATEGORY -->
				<%
					}
				%>
				</select>		
			
				<select name="selectCol" form="formAnalytics">
					<option selected>Customers</option>
					<option>States</option>
				</select> 
				<select name="selectRow" form="formAnalytics">
					<option selected>Top K</option>
					<option>Alphabetical</option>
				</select>
			<input type="hidden" name="action" value="run" form="formAnalytics"/>
			<input type="submit" name = "Run" value = "Run" form="formAnalytics">
			</form> 
	<%
    }
	%>	
		</div>
	</div>
</div>