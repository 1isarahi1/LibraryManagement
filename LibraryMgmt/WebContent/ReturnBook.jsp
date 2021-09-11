<%@ page import="java.sql.*"%>

<%@ page import="java.io.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Base64"%>
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
   

<style>

body {
  font-family: Arial, Helvetica, sans-serif;
}

.flip-card {
  background-color: transparent;
  width: 300px;
  height: 300px;
  perspective: 1000px;
}

.flip-card-inner {
  position: relative;
  width: 100%;
  height: 100%;
  text-align: center;
  transition: transform 0.6s;
  transform-style: preserve-3d;
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
}

.flip-card:hover .flip-card-inner {
  transform: rotateY(180deg);
}

.flip-card-front, .flip-card-back {
  position: absolute;
  width: 100%;
  height: 100%;
  -webkit-backface-visibility: hidden;
  backface-visibility: hidden;
}

.flip-card-front {
  background-color: #bbb;
  color: black;
}

.flip-card-back {
  background-color: #2980b9;
  color: white;
  transform: rotateY(180deg);
}
</style>
<title>Return Book</title>
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
	  
	  <li class=""><a href="UserBookRequest.jsp">Request a Book</a></li>
	  
	  <li class="active"><a href="ReturnBook.jsp">Return Book</a></li>
	  
	  
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
<table>
    
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
try
{
	Class.forName("com.mysql.jdbc.Driver");
	System.out.println("Driver OK");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
    String sqlquery0 = "select user_id, email from library_user where username = ?";
    PreparedStatement pst =  con.prepareStatement(sqlquery0);
    pst.setString(1, username);
    ResultSet rs0 = pst.executeQuery();
    
    int userid=-1;
     String email = null;
    if(rs0.next())
    {
    	userid = rs0.getInt("user_id");
    	email = rs0.getString("email");
    }
    
    String sqlquery1 = "select book_id from book_requests where request_status = 'Approved' AND user_id="+userid;
    Statement st_count = con.createStatement();  
    ResultSet rs_count = st_count.executeQuery(sqlquery1);
    int i=0;
    //counting records
    ArrayList<Integer> bookIds = new ArrayList<Integer>();
    if(!rs_count.next())
    {
    	%>
    	<p>No more books to return</p>
    	<%
    }
    while(rs_count.next())
    {  
       bookIds.add(rs_count.getInt("book_id"));
       //this an arraylist function, go through result set to get the intergers, book id of every record.
       i++;
       
    }
   
    int count = i;
    System.out.println("count is :"+count); 

    String base64Image = "";
    int rows = 0;
    rows = (count%3==0)?(count/3):(count/3)+1;
    //check 3
    //? conditional
    //which of the 2 brackets in use
    System.out.println("No. of rows are :"+rows);
    i=0;
    if(rows==0)
    {   %>
    	<p>No more books to return</p>
      <%  }
    while(i<rows)
    { 	%>
   
    	<tr>
    <% 
    	int j =0;
         int k = (i*3)+j;
    	while((j<3)&&(k<count))
    	{  
    		String sqlquery = "select * from books where book_id=?";		     
             PreparedStatement pst2 = con.prepareStatement(sqlquery);
             System.out.println("The value of k is"+k);
			    //SETTING VALUES TO PARAMETER IN THE QUERY
				pst2.setInt(1, bookIds.get(k));
				//SEND QUERY AND RETRIEVE
				ResultSet rs2 = pst2.executeQuery();
    		rs2.next();
    	//System.out.println(rs.getBlob("photo"));
    	Blob blob = rs2.getBlob("photo");
    	
    	  if(blob!=null)
    	  {
        
        InputStream inputStream = blob.getBinaryStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
         
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
         
        byte[] imageBytes = outputStream.toByteArray();
         
        base64Image = Base64.getEncoder().encodeToString(imageBytes);
        //System.out.println(base64Image);
        inputStream.close();
        outputStream.close();
    	  }
    	  
    %> 
  <td>
 <div class="flip-card">
 <div class="flip-card-inner">
   <div class="flip-card-front">
     
      <div class="container1">
           <img src="data:image/jpg;base64, <%=base64Image %>" alt="Avatar" style="width:300px;height:300px;">
    	
       </div>
   </div>
   <div class="flip-card-back">
     <h4><b><%=rs2.getString("book_name") %></b></h4> 
         <p><%=rs2.getString("book_author") %></p> 
         <p><%=rs2.getString("book_status") %></p>
          <h4><b><a href= "ReturnBook?bookId=<%=rs2.getInt("book_id") %>&userId=<%=userid%>&email=<%=email%>&bookname=<%=rs2.getString("book_name")%>&username=<%=username%>">
          Return</a></b></h4>
   </div>
 </div>
</div>
    </td> 

    <%
    j++;
    k = (i*3)+j;
    }
    %>
    </tr>

    <% 
    i++;
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