<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Home Page</title>
</head>
<body>
<h1>Library Home Page for Administration</h1>

<%
String username = null;
Cookie[] cookies = request.getCookies();

if(cookies != null)
{
	for(Cookie c:cookies)
	{
		if(c.getName().equals("admin"))
		{
			username = c.getValue();
		}
	}
	
}

if(username==null)
{
response.sendRedirect("LoginAdmin.html");
}
%>

<h2>Hello <%= username %>, Login Successful</h2>

<br>

<form action = "LogoutAdmin" method= "post">
<input type = "submit" value = "logout">
</form>
<a href = "AddBook.html">Add Book to Library</a>
<br>
<a href = "ShowBook.jsp">Show Books in Library</a>
<br>
<a href = "librarianRequest.jsp">Librarian Requests</a>
<br>
<a href = "ChangePasswordAdmin.html">Change Password</a>
<br>
<a href= "SendReturnNotifications">Send Return Notifications</a>
</body>
</html>