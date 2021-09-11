<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
 <style>
.btn-logout {
  width: 100%;
  padding: 13px 20px !important;
  text-align: left !important;
}
</style>
</head>
<body>
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Library Management: Librarian</a>
    </div>
    <ul class="nav navbar-nav">

      <li class=""><a href="HomePageLibrarian.jsp">Home</a></li>
      
	  <li class="active"><a href="ShowBookLibrarian.jsp">Show All Books</a></li>
	  
	  <li class=""><a href="ShowBookRequests.jsp">Approve a Book Request</a></li>
	  
	  <li class=""><a href="ChangePasswordLibrarian.html">Change Password</a></li>
	  
	  <li class=""><a href="CalculateFines">Calculate Fines</a></li>
	  
	  
      <li> <form action = "LogoutLibrarian" method= "post">
<button class="btn btn-link btn-logout" type = "submit"  value = "logout">
Logout</button>
</form></li>
    </ul>

    <form class="navbar-form navbar-right" action="/action_page.php">
      <div class="input-group">
        <input type="text" class="form-control" placeholder="Search" name="search">
        <div class="input-group-btn">
          <button class="btn btn-default" type="submit">
            <i class="glyphicon glyphicon-search"></i>
          </button>
        </div>
      </div>
    </form>
  </div>
</nav>
<table class="table">
  <thead class="thead-dark">
    <tr>
      <th scope="col">Request ID</th>
      <th scope="col">Book ID</th>
      <th scope="col">User ID</th>
      <th scope="col">Request Status</th>
      <th scope="col">Action</th>
    </tr>


<%
try
{
	Class.forName("com.mysql.jdbc.Driver");
	System.out.println("Driver OK");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
    
    String sqlquery = "select * from book_requests where UPPER(request_status)='PENDING APPROVAL'";		    
  
    
    Statement st = con.createStatement();  
    ResultSet rs = st.executeQuery(sqlquery);
   
    while(rs.next())
    {
    %>
	<tbody>
    <tr>
     <td scope="col"><%=rs.getInt("request_id") %></td>
    <td scope="col"><%=rs.getString("book_id") %></td>
    <td scope="col"><%=rs.getString("user_id") %></td>
    <td scope="col"><%=rs.getString("request_status") %></td>
    <td scope="col"><a href="BookRequestApproval?requestId=<%=rs.getInt("request_id") %>&bookID=<%=rs.getString("book_id") %>&UserId=<%=rs.getString("user_id") %>">Approve &nbsp;</a><a href= "BookRequestDeny?requestId=<%=rs.getInt("request_id") %>&bookID=<%=rs.getString("book_id") %>&UserId=<%=rs.getString("user_id") %>">Deny</a></td>
    </tr>
	</tbody>
    <% 
    }
}
catch(SQLException e)
{
	out.println("Database Exception caught " + e.getMessage());
}
%>
</table>


</body>
</html>