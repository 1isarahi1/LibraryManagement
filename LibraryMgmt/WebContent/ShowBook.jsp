<%@ page import="java.sql.*"%>

<%@ page import="java.io.*"%>
<%@ page import="java.util.Base64"%>
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
<style>
body {
  font-family: Arial, Helvetica, sans-serif;
}

.flip-card { 
  background-color: transparent;
  width: 300px;
  height: 300px;
  perspective: 1000px
}

.flip-card-inner {
  position: relative;
  width: 100%;
  height: 100%;
  text-align: center;
  transition: transform 0.6s;
  transform-style: preserve-3d;
  /*box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);*/
}

.flip-card:hover .flip-card-inner {
  transform: rotateY(180deg);
}

.flip-card-front, .flip-card-back {
border-radius: 25px;
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
<title>Show Books</title>
</head>
<body>
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Library Management</a>
    </div>
    <ul class="nav navbar-nav">

      <li class=""><a href="HomePage.jsp">Home</a></li>
      
	  <li class="active"><a href="ShowBook.jsp">Show All Books</a></li>
	  
	  <li class=""><a href="UserBookRequest.jsp">Request a Book</a></li>
	  
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

<table class="table">
    
<%
try
{
	Class.forName("com.mysql.jdbc.Driver");
	System.out.println("Driver OK");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
    String searchedbook = request.getParameter("search");
    if(searchedbook==null)
    	searchedbook = "";
    String sqlquery1 = "select count(*) from books where book_name like '%"+searchedbook+"%'";
    Statement st_count = con.createStatement();  
    ResultSet rs_count = st_count.executeQuery(sqlquery1);
    rs_count.next();
    int count = rs_count.getInt("count(*)");
    System.out.println("count is :"+count);
    
    String sqlquery = "select * from books where book_name like '%"+searchedbook+"%'";		     
    Statement st = con.createStatement();  
    ResultSet rs = st.executeQuery(sqlquery);
     

    String base64Image = "";
    int rows = 0;
    rows = (count%3==0)?(count/3):(count/3)+1;
    //check 3
    //? conditional
    //which of the 2 brackets in use
    System.out.println("No. of rows are :"+rows);
    int i=0;
    while(i<rows)
    { 	%>
   
    	<tr>
    <% 
    	int j =0;
    	while((j<3)&&(((i*3)+j)<count))
    	{  rs.next();
    	//System.out.println(rs.getBlob("photo"));
    	Blob blob = rs.getBlob("photo");
    	
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
    <td></td>
  <td>
 <div class="flip-card">
 <div class="flip-card-inner">
   <div class="flip-card-front">
      <div class="container1">
         <img src="data:image/jpg;base64, <%=base64Image %>" alt="Avatar" style="width:300px;height:300px;border-radius:10%">        
       </div>
   </div>
   <div class="flip-card-back">
     <h1><%=rs.getString("book_name") %></h1> 
      <p><%=rs.getString("book_author") %></p> 
         <p><%=rs.getString("book_status") %></p>
     <p><%=rs.getString("genre") %></p>
   <p><%=rs.getInt("book_price") %></p> 
   </div>
 </div>
</div>
    </td> 
    <%
    j++;
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