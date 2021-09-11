<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Money Owed</title>
</head>
<body>

    
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

<h2> Total Money Owed </h2>
<% 
try{
	Class.forName("com.mysql.jdbc.Driver");
	System.out.println("Driver OK");
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
 	String sqlquery = "select fine_amount,user_id from library_user where username='"+username+"'";
    Statement st_count = con.createStatement();  
    ResultSet rs_count = st_count.executeQuery(sqlquery);
    String fine;
    if(rs_count.next())
    {
    	float fineAmount = rs_count.getFloat("fine_amount");
    	fine = "Your fine amount is "+fineAmount;
    }
    else
    {
    	fine = "No Pending Fine Amount";
    }
    	%>
    	<p><%=fine%>&nbsp;<a href="PayFine?userID=<%=rs_count.getInt("user_id")%>">Pay</a></p>
<% 
	}
catch(SQLException e)
{
	out.println("Database Exception caught " + e.getMessage());
}
%>
</body>
</html>