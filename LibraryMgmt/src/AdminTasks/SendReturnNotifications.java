package AdminTasks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * Servlet implementation class SendReturnNotifications
 */
@WebServlet("/SendReturnNotifications")
public class SendReturnNotifications extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendReturnNotifications() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		try 
		{   Date dt = new Date();	
			String date = new SimpleDateFormat("dd/MM/yyyy").format(dt);
			java.util.Date dt1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			System.out.println(dt1);
			java.sql.Date currentDate = new java.sql.Date(dt1.getTime());
			/*Calendar c = Calendar.getInstance();
	        c.setTime(dt);
	        c.add(Calendar.MONTH, 1);
	        Date expire = c.getTime();
	        
	        java.sql.Date expireDate = new java.sql.Date(expire.getTime());*/
	        // see later till this
            //String expire = 
			//SimpleDateFormat formatt= new SimpleDateFormat("dd/mm/yyyy");
			//Date dt;
			//dt = formatt.format(date);
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
		    
		    String sqlquery = "select * from book_requests where request_status = 'Approved'";	
		    
		 
		    Statement st = con.createStatement();  
		    ResultSet rs = st.executeQuery(sqlquery);
		    
		    while(rs.next())
		    {
		    	int bookID = rs.getInt("book_id");
		    	int userID = rs.getInt("user_id");
		    	Date returnDate = rs.getDate("return_date");
		    	int renewal = rs.getInt("renewal_availability");
		    	String datesql = new SimpleDateFormat("dd/MM/yyyy").format(returnDate);
				java.util.Date javareturndate = new SimpleDateFormat("dd/MM/yyyy").parse(datesql);
				long diffdays = javareturndate.getTime() - dt1.getTime();
				
				//number of difference
				diffdays = (diffdays/1000/3600/24);
				System.out.println("The date difference between return date and current date for userId"+userID+"is :"+diffdays);
				//this case we send notification
				if(diffdays<=7)
				{
					String sqlquery1 = "select username, email from library_user where user_id = ? AND expiration_date >= CURDATE()";
					
					//QUERY
					PreparedStatement pst1 = con.prepareStatement(sqlquery1);
					
					//SETTING VALUES TO PARAMETER IN THE QUERY
					pst1.setInt(1, userID);
					
					//SEND QUERY AND RETRIEVE
					ResultSet rs1 = pst1.executeQuery();
					
					if(rs1.next())
					{
						String username = rs1.getString("username");
						String email =  rs1.getString("email");
						String sqlquery2 = "select book_name from books where book_id = ?";
						//QUERY
						PreparedStatement pst2 = con.prepareStatement(sqlquery2);
					    //SETTING VALUES TO PARAMETER IN THE QUERY
						pst2.setInt(1, bookID);
						//SEND QUERY AND RETRIEVE
						ResultSet rs2 = pst2.executeQuery();
						String bookname;
						if(rs2.next()) 
						{
							bookname = rs2.getString("book_name");
							
						}
						else
						{
							bookname = "invalid book name";
						}
						String returnNotification = "Hi,"+username+" The return date of your book : "+bookname+" is approaching. Please return the "
								+ "book by : "+returnDate;
						String sqlquery4 = "insert into user_notifications(user_id, notification_string, notif_date) values(?, ?, ?)";
				    	 PreparedStatement pst3 = con.prepareStatement(sqlquery4);
						    pst3.setInt(1, userID);
						    pst3.setString(2, returnNotification);
						    pst3.setDate(3, currentDate);
						    pst3.execute();
						    
						    System.out.println("Insertion done successfully");
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
					    try
					    {
					    MimeMessage message = new MimeMessage(session);
					    message.setFrom(new InternetAddress(senderadress));
					    message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipientadress));
					    message.setSubject(subject);
					    message.setText(text); 
					    Transport.send(message);
					 //We will start from here   
			    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePageAdmin.jsp");
			    	PrintWriter pw = response.getWriter();
			    	pw.println("<font color = green> Return Notification sent successfully </font>");
			    	rd.include(request, response);
					    }
					    catch(MessagingException e)
					    {
					    System.out.println(e);
					    System.out.println("Message failed, email not sent");
				    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePageAdmin.jsp");
				    	PrintWriter pw = response.getWriter();
				    	pw.println("<font color = red> Return Notification sending error! </font>");
				    	rd.include(request, response);
					    }
					    finally
					    {
					    	pst2.close();
					    	pst1.close();
					    }
						
					}
										
				}
				
				
		    	
		    	
		    	
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
