 <%@ page import="javax.servlet.*"%>
<div class="panel panel-default">
	<div class="panel-body">
		<div class="bottom-nav">
			<h4>Options</h4>
			<!-- Put your part 2 code here -->
			
					<form action="/jsp/list-analytics.jsp" method="post" id="formAnalytics">
						
				
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
		
		</div>
	</div>
</div>