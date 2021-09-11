<%@ page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Librarian Requests</title>
</head>
<body>
<h1>New Librarian Requests</h1>
<table>
    <tr>
    <th>Librarian ID</th>
    <th>Librarian First Name</th>
    <th>Librarian Last Name</th>
    <th>Contract Expiration</th>
    <th>Status</th>
    <th>Take Action</th>
    </tr>

<%
try
{
	Class.forName("com.mysql.jdbc.Driver");
	System.out.println("Driver OK");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
    
    String sqlquery = "select * from librarian where UPPER(status)='PENDING APPROVAL'";		    
  
    
    Statement st = con.createStatement();  
    ResultSet rs = st.executeQuery(sqlquery);
   
    while(rs.next())
    {
    %>
    <tr>
     <td><%=rs.getInt("librarian_id") %></td>
    <td><%=rs.getString("first_name") %></td>
    <td><%=rs.getString("last_name") %></td>
    <td><%=rs.getDate("contract_expiration") %></td>
     <td><%=rs.getString("status") %></td>
    <td><a href= "Approval?librarianId=<%=rs.getInt("librarian_id") %>">Approve &nbsp;</a><a href= "Deny?librarianId=<%=rs.getInt("librarian_id") %>">Deny</a></td>
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