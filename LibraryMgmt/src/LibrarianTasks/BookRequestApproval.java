package LibrarianTasks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BookRequestApproval
 */
@WebServlet("/BookRequestApproval")
public class BookRequestApproval extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookRequestApproval() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		int requestId  = Integer.parseInt(request.getParameter("requestId"));
		int bookID  = Integer.parseInt(request.getParameter("bookID"));
		int UserId  = Integer.parseInt(request.getParameter("UserId"));
		String status = "Approved";
		try 
		{    Date dt = new Date();	
			String date = new SimpleDateFormat("dd/MM/yyyy").format(dt);
			java.util.Date dt1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			java.sql.Date registerDate = new java.sql.Date(dt1.getTime());
			System.out.println(dt1);
			Calendar c = Calendar.getInstance();
	        c.setTime(dt);
	        c.add(Calendar.MONTH, 1);
	        Date expire = c.getTime();
	        
	        java.sql.Date expireDate = new java.sql.Date(expire.getTime());
            //String expire = 
			//SimpleDateFormat formatt= new SimpleDateFormat("dd/mm/yyyy");
			//Date dt;
			//dt = formatt.format(date);
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
		    
		    String sqlquery = "update book_requests set issue_date=?, return_date=?, renewal_availability=?, request_status=? where request_id = ?";		    
		    
		    
		    PreparedStatement pst = con.prepareStatement(sqlquery);
		    pst.setDate(1, registerDate);
		    pst.setDate(2,expireDate);
		    pst.setInt(3, 1);
		    pst.setString(4,status);
		    pst.setInt(5, requestId);
		    
		   int updatestatus = pst.executeUpdate();
		    if(updatestatus>0)
		    {
		    	String sqlquery2= "update books set book_status= ? where book_id= ? ";
		    	PreparedStatement pst2 = con.prepareStatement(sqlquery2);
		    	
		    	pst2.setString(1, "Unavailable");
		    	pst2.setInt(2, bookID);
		    	
		    	int updatestatus2 = pst2.executeUpdate();
		    	
		    	if(updatestatus2>0)
		    	{    
		    		String book_name = null;
		    		String sqlquery3 = "select book_name from books where book_id="+bookID;
		    		Statement st = con.createStatement();
		    		ResultSet rs = st.executeQuery(sqlquery3);
		    		if(rs.next())
		    		{
		    			 book_name = rs.getString("book_name");
		    		}
		    	   	//response.getWriter().append("     User cannot be found");
			    	System.out.println("Approval Succesful");
			    	String notification = "Request for book: "+book_name+" is approved";
			    	String sqlquery4 = "insert into user_notifications(user_id, notification_string, notif_date) values(?, ?, ?)";
			    	 PreparedStatement pst3 = con.prepareStatement(sqlquery4);
					    pst3.setInt(1, UserId);
					    pst3.setString(2, notification);
					    pst3.setDate(3, registerDate);
					    pst3.execute();
					    
					    System.out.println("Insertion done successfully");
					    String email = null;
			    		String sqlquery5 = "select email from library_user where user_id="+ UserId;
			    		Statement st2 = con.createStatement();
			    		ResultSet rs2 = st2.executeQuery(sqlquery5);
			    		if(rs2.next())
			    		{
			    			 email = rs2.getString("email");
			    		}

					    String recipientadress = email;
					    String senderadress = "manageLibrary754@gmail.com";
					    String pass= "Sarahmalik123";
					    String subject = "Book Request approved";
					    String text = "Book Request Approved for book: " + book_name;
					    String host = "smtp.gmail.com";
					    
					    Properties properties = new Properties();
					    properties.put("mail.smtp.auth", true);
					    properties.put("mail.smtp.starttls.enable", true);
					    properties.put("mail.smtp.host", host);
					    properties.put("mail.smtp.port", "587");
					    //setting up the mail server
					    //comment out jdk.tls in the java.security file (jdk 16)
					    //when using email notifications
					    Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
					    	protected PasswordAuthentication getPasswordAuthentication() {
					    		return new PasswordAuthentication(senderadress,pass);
					    		}
					    });
					    try
					    {
					    MimeMessage message = new MimeMessage(session);
					    message.setFrom(new InternetAddress(senderadress));
					    message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipientadress));
					    message.setSubject(subject);
					    message.setText(text); 
					    Transport.send(message);
					    
			    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/ShowBookRequests.jsp");
			    	PrintWriter pw = response.getWriter();
			    	pst3.close();
			    	pw.println("<font color = green> Approval Succesful </font>");
			    	rd.include(request, response);
					    }
					    catch(MessagingException e)
					    {
					    System.out.println(e);
					    System.out.println("Message failed, email not sent");
					    System.out.println("Insertion Succesful");
				    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/LoginLibrarian.html");
				    	PrintWriter pw = response.getWriter();
				    	pw.println("<font color = green> Approval Succesfull, but email was not sent. </font>");
				    	rd.include(request, response);
				    	response.sendRedirect("LoginLibrarian.html");
					    }
			}
		    	else
		    	{   
		    		//response.getWriter().append("  Failed");
			    	System.out.println("Books table cant be updated");
			    	String notifyadmin = "Error updating books table. So Manually update status in books table for bookid="+bookID;
			    	String sqlquery5 = "insert into admin_notifications( notification_string) values( ?)";
			    	 PreparedStatement pst3 = con.prepareStatement(sqlquery5);
					    pst3.setString(1, notifyadmin);
					    pst3.execute();
			    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/ShowBookRequestsLibrarian.jsp");
			    	PrintWriter pw = response.getWriter();
			    	pw.println("<font color = red> Something went wrong in updating books table, but user request is granted </font>");
			    	rd.include(request, response);
			    	response.sendRedirect("ShowBookRequestsLibrarian.jsp");
		    		
		    	}
		  
		    
		    }
		    else
		    {
	
		    	//response.getWriter().append("  Failed");
		    	System.out.println("Books Requests table cant be updated");
		    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/ShowBookRequests.jsp");
		    	PrintWriter pw = response.getWriter();
		    	pw.println("<font color = red> Something went wrong, Try again </font>");
		    	rd.include(request, response);
		    	
		    }
		    	
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
