<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
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
<title>Request a Book</title>

</head>
<body>

<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Library Management</a>
    </div>
    <ul class="nav navbar-nav">

      <li class=""><a href="HomePage.jsp">Home</a></li>
      
	  <li class=""><a href="ShowBook.jsp">Show All Books</a></li>
	  
	  <li class="active"><a href="UserBookRequest.jsp">Request a Book</a></li>
	  
	  <li class=""><a href="ReturnBook.jsp">Return Book</a></li>
	  
	  
      <li> <form action = "Logout" method= "post">
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
<%
String username = null;
Cookie[] cookies = request.getCookies();

if(cookies != null)
{
	for(Cookie c:cookies)
	{
		if(c.getName().equals("user"))
		{
			username = c.getValue();
		}
	}
	
}

%>
<h1>Available Books</h1>
<table class="table">
  <thead class="thead-dark">
    <tr>
      <th scope="col">Book ID</th>
      <th scope="col">Book Name</th>
      <th scope="col">Author Name</th>
      <th scope="col">Genre</th>
      <th scope="col">Book Status</th>
	  <th scope="col">Request Book</th>
    </tr>
    
    
<%
try
{
	Class.forName("com.mysql.jdbc.Driver");
	System.out.println("Driver OK");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
    
    String sqlquery = "select * from books where book_status = 'Available'";	 
    Statement st = con.createStatement();  
    ResultSet rs = st.executeQuery(sqlquery);
    
    while(rs.next())
    {
    %>
    <tr>
     <td><%=rs.getInt("book_id") %></td>
    <td><%=rs.getString("book_name") %></td>
    <td><%=rs.getString("book_author") %></td>
    <td><%=rs.getString("genre") %></td>
     <td><%=rs.getString("book_status") %></td>
    <td><a href= "UserBookRequest?BookId=<%=rs.getInt("book_id") %>&username=<%=username %>">Request</a></td>
    </tr>
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