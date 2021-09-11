<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">


<link rel="stylesheet" href="bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
<link href = "UserInfo.css" rel = "stylesheet">
<style>
.btn-logout {
  width: 100%;
  padding: 13px 20px !important;
  text-align: left !important;
}
</style>

<title>Librarian Home Page</title>
</head>
<body>

<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Library Management: Librarian</a>
    </div>
    <ul class="nav navbar-nav">

      <li class="active"><a href="HomePageLibrarian.jsp">Home</a></li>
      
	  <li class=""><a href="ShowBookLibrarian.jsp">Show All Books</a></li>
	  
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

<h1>Librarian Home Page</h1>
<%
String username = null;
Cookie[] cookies = request.getCookies();

if(cookies != null)
{
	for(Cookie c:cookies)
	{
		if(c.getName().equals("librarian"))
		{
			username = c.getValue();
		}
	}
	
}

if(username==null)
{
response.sendRedirect("loginLibrarian.html");
}
%>

<h2>Hello <%= username %>, Login Successful</h2>




</body>
</html>