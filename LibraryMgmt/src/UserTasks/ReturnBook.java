package UserTasks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * Servlet implementation class ReturnBook
 */
@WebServlet("/ReturnBook")
public class ReturnBook extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReturnBook() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		int userId  = Integer.parseInt(request.getParameter("userId"));
		int bookId = Integer.parseInt(request.getParameter("bookId"));
		
		String username =  request.getParameter("username"); 
		String bookname = request.getParameter("bookname"); 
		String email = request.getParameter("email");
		String status = "Available";
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
		    
		    String requestStatus = "Book Returned";  
		   
		    String sqlquery1 = "select fine_amount from library_user where user_id = ?";
		    
		    //pst
		    
		    PreparedStatement pst1 = con.prepareStatement(sqlquery1);
		    
		    //setting the arguments
		    
		    pst1.setInt(1,userId);
		    
		    //execute query
		    
		    ResultSet rs1 = pst1.executeQuery();
		    int fineamount=0;
		    PrintWriter pw = response.getWriter();
		    if(rs1.next())
		    {
		    	fineamount = rs1.getInt("fine_amount");
		    	if(fineamount > 0)
		    	{
		    		System.out.println("Pay Fine");
		    		pw.println("Pay Fine "+fineamount);
		    		
		    	}
		    	else
		    	{
		    		System.out.println("No Outstanding Fine");
		    		pw.println("No Outstanding Fine");
		    		
		    	}
		    	
		    }
		    
		    String sqlquery0 = "update book_requests set request_status=?, renewal_availability=? where user_id = ? AND book_id = ?";
		    
		    PreparedStatement pst0 = con.prepareStatement(sqlquery0);
		    pst0.setString(1, requestStatus);
		    pst0.setInt(2, 0);
		    pst0.setInt(3, userId);
		    pst0.setInt(4, bookId);
		    
		    int updatestatus0 = pst0.executeUpdate();
		    
		    if(updatestatus0>0)
		    {
		    	
		  
		    	//response.getWriter().append("     User cannot be found");
		    	System.out.println("Book Request table Update Succesful");
		    
		    	
		    }
		    
		    else
		    {
		    	
		  
		    	//response.getWriter().append("     User cannot be found");
		    	System.out.println("Book Request table Update failed");
		    
		    	
		    }
		    
		    
		    
		    
		    String sqlquery = "update books set book_status=? where book_id = ?";	
		    
		    PreparedStatement pst = con.prepareStatement(sqlquery);
		    pst.setString(1, status);
		    pst.setInt(2, bookId);
		    
		    int updatestatus = pst.executeUpdate();
		    if(updatestatus>0)
		    {
		    	
		  
		    	//response.getWriter().append("     User cannot be found");
		 	System.out.println("Book Return Succesful");
		 	
		 		
			String returnNotification = "Hello," + username + " You succesfully returned book:" + bookname;
			String recipientadress = email;
		    String senderadress = "managelibrary754@gmail.com";
		    String pass= "Sarahmalik123";
		    String subject = "Book Return notification";
		    String text = returnNotification;
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
		   
		    
		    MimeMessage message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(senderadress));
		    message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipientadress));
		    message.setSubject(subject);
		    message.setText(text); 
		    Transport.send(message);
		 		
		    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePage.jsp");
		    	PrintWriter pw1 = response.getWriter();
		    	pw1.println("<font color = green> Book Return Succesful </font>");
		    	
		    	rd.include(request, response);
		
		    	
		    	
		    }
		    
		    else
		    {
		    	//response.getWriter().append("  Failed");
		    	System.out.println("Return did not work");
		    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePage.jsp");
		    	PrintWriter pw1 = response.getWriter();
		    	pw1.println("<font color = red> Something went wrong, Try again </font>");
		    	rd.include(request, response);
		    	
		    }
		    	
		}
		 catch(MessagingException e)
	    {
	    System.out.println(e);
	    System.out.println("Message failed, email not sent");
    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePage.jsp");
    	PrintWriter pw = response.getWriter();
    	pw.println("<font color = green> Book Return Succesful but email not sent </font>");
    	rd.include(request, response);
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
