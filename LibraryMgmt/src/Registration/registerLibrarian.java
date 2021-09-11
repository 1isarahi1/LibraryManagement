package Registration;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.io.IOException;
import java.io.PrintWriter;

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
import java.sql.*;

/**
 * Servlet implementation class register
 */
@WebServlet("/registerLibrarian")
public class registerLibrarian extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public registerLibrarian()
    {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String email  = request.getParameter("email");
		String username  = request.getParameter("username");
		String password  = request.getParameter("password");
		String first_name  = request.getParameter("firstname");
		String last_name  = request.getParameter("lastname");
		try {
			Date dt = new Date();	
			//String date = new SimpleDateFormat("dd/MM/yyyy").format(dt);
			//java.util.Date dt1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			//java.sql.Date registerDate = new java.sql.Date(dt1.getTime());
			//System.out.println(dt1);
			Calendar c = Calendar.getInstance();
	        c.setTime(dt);
	        c.add(Calendar.YEAR, 3);
	        Date expire = c.getTime();
	        
	        java.sql.Date expireDate = new java.sql.Date(expire.getTime());
	        String status = "Pending Approval";
	        
            //String expire = 
			//SimpleDateFormat formatt= new SimpleDateFormat("dd/mm/yyyy");
			//Date dt;
			//dt = formatt.format(date);
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
			
		    String sqlquery = "insert into librarian(username,user_password,first_name,last_name,contract_expiration, status, email) values(?,?,?,?,?,?,?)";
		    PreparedStatement pst = con.prepareStatement(sqlquery);
		    pst.setString(1, username);
		    pst.setString(2, password);
		    pst.setString(3, first_name);
		    pst.setString(4, last_name);
		    pst.setDate(5, expireDate);
		    pst.setString(6, status);
		    pst.setString(7, email);
		    
		    int status1 = pst.executeUpdate();
		    if(status1>0)
		    {
		    System.out.println("Insertion done successfully");
		    
		 
		    String recipientadress = email;
		    String senderadress = "manageLibrary754@gmail.com";
		    String pass= "Sarahmalik123";
		    String subject = "account approved";
		    String text = "Account registration has been succesful for user: " + first_name + " " + last_name;
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
		    response.setContentType("text/html");
		    try
		    {
		    MimeMessage message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(senderadress));
		    message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipientadress));
		    message.setSubject(subject);
		    message.setText(text); 
		    Transport.send(message);
		    
		    RequestDispatcher rd = getServletContext().getRequestDispatcher("/LoginLibrarian.html");
	    	PrintWriter pw = response.getWriter();
	    	pw.println("<font color = green >You Registered Successfully and can now login from this page, wait for approval from Admin and check  for approval email. </font>");
	    	rd.include(request, response);	    
		    }
		    catch(MessagingException e)
		    {
		    System.out.println(e);
		    System.out.println("Message failed, email not sent");
		    System.out.println("Insertion Succesful");
	    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/LoginLibrarian.html");
	    	PrintWriter pw = response.getWriter();
	    	pw.println("<font color = green> Registration Succesfully, but email was not sent. </font>");
	    	rd.include(request, response);
		    }
		   
		    }
		    else
		    {
		    	System.out.println("Insertion unsuccessful");
		    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/registrationLibrarian.html");
		    	PrintWriter pw = response.getWriter();
		    	pw.println("<font color = red> Registration Unsuccessful. </font>");
		    	rd.include(request, response);
		    }
		    
		   
		    con.close();
		    
		} 
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
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
