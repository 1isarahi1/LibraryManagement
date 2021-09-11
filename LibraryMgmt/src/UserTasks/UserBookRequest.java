package UserTasks;

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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserBookRequest
 */
@WebServlet("/UserBookRequest")
public class UserBookRequest extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserBookRequest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String username  = "'"+request.getParameter("username")+"'";
		String book_id  = request.getParameter("BookId");
		System.out.println("Username is"+username);
		
		System.out.println("Book Id is"+book_id);
		int userid= -1;
		try {
			Date dt = new Date();	
			String date = new SimpleDateFormat("dd/MM/yyyy").format(dt);
			java.util.Date dt1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			java.sql.Date currentDate = new java.sql.Date(dt1.getTime());
			System.out.println(dt1);
			////Calendar c = Calendar.getInstance();
//	        c.setTime(dt);
//	        c.add(Calendar.YEAR, 4);
//	        Date expire = c.getTime();
//	        
//	        java.sql.Date expireDate = new java.sql.Date(expire.getTime());
//            //String expire = 
			//SimpleDateFormat formatt= new SimpleDateFormat("dd/mm/yyyy");
			//Date dt;
			//dt = formatt.format(date);
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
		    String sqlquery1 = "select user_id from library_user  where username="+username;	 
		    Statement st = con.createStatement();
		    ResultSet rs = st.executeQuery(sqlquery1);
		    while(rs.next())
		    {
		    userid = rs.getInt("user_id");
		    }
		    rs.close();
		    st.close();
		    
		    String sqlquery2 = "insert into book_requests(book_id,user_id,issue_date,return_date,renewal_availability,request_status) values(?,?,?,?,?,?)";
		    PreparedStatement pst2 = con.prepareStatement(sqlquery2);
		    pst2.setInt(1, Integer.parseInt(book_id));
		    pst2.setInt(2, userid);
		    pst2.setString(3, null);
		    pst2.setString(4, null);
		    pst2.setString(5, null);
		    pst2.setString(6, "Pending Approval");
		    pst2.execute();
		    System.out.println("Insertion done successfully");
		    pst2.close();
		    String book_name = null;
    		String sqlquery6 = "select book_name from books where book_id="+book_id;
    		Statement st6 = con.createStatement();
    		ResultSet rs6 = st6.executeQuery(sqlquery6);
    		if(rs6.next())
    		{
    			 book_name = rs6.getString("book_name");
    		}
		    String notification = "You have made a request for book: "+book_name+". You will receive an email and a notification on the portal when"
		    		+ "an avtion is taken on this request by the librarian ";
	    	String sqlquery7 = "insert into user_notifications(user_id, notification_string, notif_date) values(?, ?, ?)";
	    	 PreparedStatement pst7 = con.prepareStatement(sqlquery7);
			    pst7.setInt(1, userid);
			    pst7.setString(2, notification);
			    pst7.setDate(3, currentDate);
			    pst7.execute();
			    
			    System.out.println("Insertion done successfully");
		    String sqlquery3 = "update books set book_status=? where book_id = ?";
		    		 

				    PreparedStatement pst3 = con.prepareStatement(sqlquery3);
				    pst3.setString(1, "Request Pending");
				    pst3.setInt(2, Integer.parseInt(book_id));
				    
				    int updatestatus = pst3.executeUpdate();
				    if(updatestatus>0)
				    {
				    	
				  
				    	//response.getWriter().append("     User cannot be found");
				    	System.out.println("Book status updated");
				    	
				    	String notifylibrarian = "A new boook request is pending. Go to BookRequests menu to Approve/Deny.";
				    	String sqlquery5 = "insert into librarian_notifications(notification_string) values(?)";
				    	 PreparedStatement pst4 = con.prepareStatement(sqlquery5);
						    pst4.setString(1, notifylibrarian);
						    pst4.execute();			    	
				    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePage.jsp");
				    	PrintWriter pw = response.getWriter();
				    	pst4.close();
				    	pw.println("<font color = green> Request Submitted Successfully </font>");
				    	rd.include(request, response);
				    }
				    else
				    {
				    	//response.getWriter().append("  Failed");
				    	System.out.println("Book status update failed but request submitted sucessfully");
				    	String notifylibrarian = "A new boook request is pending. Go to BookRequests menu to Approve/Deny.";
				    	String sqlquery5 = "insert into librarian_notifications(notification_string) values(?)";
				    	 PreparedStatement pst4 = con.prepareStatement(sqlquery5);
						    pst4.setString(1, notifylibrarian);
						    pst4.execute();		
						    String notifyadmin = "Error updating books table. So Manually update status in books table for bookid="+book_id;
					    	String sqlqueryadmin = "insert into admin_notifications( notification_string) values( ?)";
					    	 PreparedStatement pst5 = con.prepareStatement(sqlqueryadmin);
							    pst5.setString(1,notifyadmin);
							    pst5.execute();
				    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePage.jsp");
				    	PrintWriter pw = response.getWriter();
				    	pw.println("<font color = red>Book status update failed but request submitted sucessfully</font>");
				    	rd.include(request, response);
				    	
				    }
		    
				    pst3.close();
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
