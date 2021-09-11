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
import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 * Servlet implementation class register
 */
@WebServlet("/register")
public class register extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public register()
    {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String email  = request.getParameter("email");
		String username  = request.getParameter("username");
		String password  = request.getParameter("password");
		String first_name  = request.getParameter("firstname");
		String last_name  = request.getParameter("lastname");
		String gender = request.getParameter("gender");
		try {
			Date dt = new Date();	
			String date = new SimpleDateFormat("dd/MM/yyyy").format(dt);
			java.util.Date dt1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			java.sql.Date registerDate = new java.sql.Date(dt1.getTime());
			System.out.println(dt1);
			Calendar c = Calendar.getInstance();
	        c.setTime(dt);
	        c.add(Calendar.YEAR, 4);
	        Date expire = c.getTime();
	        
	        java.sql.Date expireDate = new java.sql.Date(expire.getTime());
            //String expire = 
			//SimpleDateFormat formatt= new SimpleDateFormat("dd/mm/yyyy");
			//Date dt;
			//dt = formatt.format(date);
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
			
		    String sqlquery = "insert into library_user(username,user_password,first_name,last_name,gender,registration_date,expiration_date,fine_amount,email) values(?,?,?,?,?,?,?,?,?)";
		    PreparedStatement pst = con.prepareStatement(sqlquery);
		    pst.setString(1, username);
		    pst.setString(2, password);
		    pst.setString(3, first_name);
		    pst.setString(4, last_name);
		    pst.setString(5, gender);
		    pst.setDate(6, registerDate);
		    pst.setDate(7, expireDate);
		    pst.setString(8, null);
		    pst.setString(9, email);
		    int status = pst.executeUpdate();
		    if(status>0)
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
		    try
		    {
		    MimeMessage message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(senderadress));
		    message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipientadress));
		    message.setSubject(subject);
		    message.setText(text); 
		    Transport.send(message);
		    
		    RequestDispatcher rd = getServletContext().getRequestDispatcher("/Login.html");
	    	PrintWriter pw = response.getWriter();
	    	pw.println("<font color = green>You Registered Successfully and can now login from this page. </font>");
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
		    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/registration.html");
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
