<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.Base64"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="bootstrap.min.css">
  <link rel="stylesheet"  href="notifcss.css">
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
<title>Home Page</title>
</head>
<body onload="removeExtra()">
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Library Management</a>
    </div>
    <ul class="nav navbar-nav">

      <li class="active"><a href="HomePage.jsp">Home</a></li>
      
	  <li class=""><a href="ShowBook.jsp">Show All Books</a></li>
	  
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
<script>
function removeExtra()
{ var element = document.getElementById("panel0");
element.parentNode.removeChild(element);
var element1 = document.getElementById("panel1");
element1.parentNode.removeChild(element1);
var element2 = document.getElementById("panel2");
element2.style.top = "-380px";
element2.style.left = "800px";
element2.style.marginLeft = "80px";
var element3 = document.getElementById("datecontainer");
element3.style.marginRight = "-350px";
	}
</script>
<%--<h1>Library Home Page</h1> --%>

<%
String username = null;
Cookie[] cookies = request.getCookies();

//date obj
Date dt = new Date();	
//convert for display
String date = new SimpleDateFormat("dd/MM/yyyy").format(dt);
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

if(username==null)
{
response.sendRedirect("Login.html");
}
%>

<%-- <h2>Hello <%= username %>, Login Successful</h2>--%>

<div class="container contain-pos">
<div class="row">
<div class="col-sm-9">
<h3>Notifications</h3>
</div>
      <div id="datecontainer" class="col-md-2  toppad  pull-right col-md-offset-0 ">
           <A href="EditProfile.jsp" >Edit Profile</A>
<p class=" text-info"><%=date %></p>
      </div>
</div><!-- /row -->

<%
try
{
	Class.forName("com.mysql.jdbc.Driver");
	System.out.println("Driver OK");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
    int user_id = -1;
    System.out.println("Username is "+username);
	String sqlquery1 = "select user_id, first_name, last_name, gender, registration_date, expiration_date, fine_amount, email, profile_picture from library_user where username='"+username+"'";
	Statement st = con.createStatement();
	ResultSet rs = st.executeQuery(sqlquery1);
	String fname = null;
	String lname = null;
	String gender= null;
	Date regdate = null;
	Date expdate = null;
    float fineamount = 0;
    String email = null;
    String base64Image = "";
	
	if(rs.next())
	{
		 user_id = rs.getInt("user_id");
		 fname = rs.getString("first_name");
		 lname = rs.getString("last_name");
		 gender = rs.getString("gender");
		 regdate = rs.getDate("registration_date");
		 expdate = rs.getDate("expiration_date");
		 fineamount = rs.getFloat("fine_amount");
		 email = rs.getString("email");
		 Blob blob = rs.getBlob("profile_picture");
	    	
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
		 
	}
    //String sqlquery1 = "select * from library_user where user_name="+;
    String sqlquery2 = "select * from user_notifications where user_id="+user_id;		    
    System.out.println("Userid is "+user_id);
    Statement st1 = con.createStatement();  
    ResultSet rs1 = st1.executeQuery(sqlquery2);
   int count = 0;
   String id;
    while(rs1.next())
    { id="panel"+count;
    %>
    <div class="row">
<div class="col-sm-1">
<div class="thumbnail">
<img class="img-responsive user-photo" src="https://ssl.gstatic.com/accounts/ui/avatar_2x.png">
</div><!-- /thumbnail -->
</div><!-- /col-sm-1 -->

<div class="col-sm-7">
<div class="panel panel-default">
<div class="panel-heading">
<span class="text-muted"><%=rs1.getDate("notif_date") %></span>
</div>
<div class="panel-body">
<%=rs1.getString("notification_string") %>
</div><!-- /panel-body -->
</div><!-- /panel panel-default -->
</div><!-- /col-sm-5 -->
  <div id="<%=id %>" class="col-xs-12 col-sm-12 col-md-6 col-lg-6 col-xs-offset-0 col-sm-offset-0 col-md-offset-0 col-lg-offset-0 toppad" >
   
   
          <div class="panel panel-info">
            <div class="panel-heading">
              <h3 class="panel-title"> <%= username %></h3>
            </div>
            <div class="panel-body">
              <div class="row">
                <div class="col-md-3 col-lg-3 " align="center">
                <img src="data:image/jpg;base64, <%=base64Image %>" alt="User Pic" class="img-circle img-responsive">  
                </div>
                
                <!--<div class="col-xs-10 col-sm-10 hidden-md hidden-lg"> <br>
                  <dl>
                    <dt>DEPARTMENT:</dt>
                    <dd>Administrator</dd>
                    <dt>HIRE DATE</dt>
                    <dd>11/12/2013</dd>
                    <dt>DATE OF BIRTH</dt>
                       <dd>11/12/2013</dd>
                    <dt>GENDER</dt>
                    <dd>Male</dd>
                  </dl>
                </div>-->
                <div class=" col-md-9 col-lg-9 "> 
                  <table class="table table-user-information">
                    <tbody>
                      <tr>
                        <td>First Name:</td>
                        <td><%= fname %></td>
                      </tr>
                       <tr>
                        <td>Last Name:</td>
                        <td><%= lname %></td>
                      </tr>
                      <tr>
                        <td>Registration date:</td>
                        <td><%= regdate %></td>
                      </tr>
                      <tr>
                        <td>Expiration Date:</td>
                        <td><%= expdate %></td>
                      </tr>
                   
                         <tr>
                             <tr>
                        <td>Gender</td>
                        <td><%= gender %></td>
                      </tr>
                        <tr>
                        <td>Fine Amount</td>
                        <td><%= fineamount %></td>
                      </tr>
                      <tr>
                        <td>Email</td>
                        <td><a href="mailto:<%= email %>"><%= email %></a></td>
                     <!-- </tr>
                        <td>Phone Number</td>
                        <td>123-4567-890(Landline)<br><br>555-4567-890(Mobile)
                        </td> --> 
                           
                      </tr>
                     
                    </tbody>
                  </table>
                  
                  <a href="FineAmount.jsp" class="btn btn-primary">Pay Fine</a>
                  <a href="ChangePassword.html" class="btn btn-primary">Change Password</a>
                </div>
              </div>
            </div>         
          </div>
        </div>
</div><!-- row -->
   <% 
   count++;
    }
}
catch(SQLException e)
{
	out.println("Database Exception caught " + e.getMessage());
}
%>
   
</div><!-- /container -->
</body>
</html>