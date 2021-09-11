package LibrarianTasks;

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
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BookRequestDeny
 */
@WebServlet("/BookRequestDeny")
public class BookRequestDeny extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookRequestDeny() {
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
		String status = "Denied";
		try 
		{   
			 Date dt = new Date();	
				String date = new SimpleDateFormat("dd/MM/yyyy").format(dt);
				java.util.Date dt1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
				java.sql.Date currentDate = new java.sql.Date(dt1.getTime());			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
		    
		    String sqlquery = "update book_requests set issue_date=?, return_date=?, renewal_availability=?, request_status=? where request_id = ?";		    
		    
		    
		    PreparedStatement pst = con.prepareStatement(sqlquery);
		    pst.setDate(1, null);
		    pst.setDate(2,null);
		    pst.setInt(3, 0);
		    pst.setString(4,status);
		    pst.setInt(5, requestId);
		    
		   int updatestatus = pst.executeUpdate();
		    if(updatestatus>0)
		    {
		    	String sqlquery2= "update books set book_status= ? where book_id= ? ";
		    	PreparedStatement pst2 = con.prepareStatement(sqlquery2);
		    	
		    	pst2.setString(1, "Available");
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
			    	System.out.println("Request Denied");
			    	String notification = "Request for book: "+book_name+" is denied";
			    	String sqlquery4 = "insert into user_notifications(user_id, notification_string, notif_date) values(?,?, ?)";
			    	 PreparedStatement pst3 = con.prepareStatement(sqlquery4);
					    pst3.setInt(1, UserId);
					    pst3.setString(2, notification);
					    pst3.setDate(3, currentDate);
					    pst3.execute();
			    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/ShowBookRequests.jsp");
			    	PrintWriter pw = response.getWriter();
			    	pst3.close();
			    	pw.println("<font color = red> Request Denied </font>");
			    	rd.include(request, response);
		    	}
		    	else
		    	{   
		    		//response.getWriter().append("  Failed");
			    	System.out.println("Books table can't be updated");
			    	String notifyadmin = "Error updating books table. So Manually update status in books table for bookid="+bookID;
			    	String sqlquery5 = "insert into admin_notifications(notification_string) values(?)";
			    	 PreparedStatement pst3 = con.prepareStatement(sqlquery5);
					    pst3.setString(1, notifyadmin);
					    pst3.execute();
			    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/ShowBookRequests.jsp");
			    	PrintWriter pw = response.getWriter();
			    	pw.println("<font color = red> Something went wrong in updating books table, but user request is granted </font>");
			    	rd.include(request, response);
		    		
		    	}
		  
		    
		    }
		    else
		    {
	
		    	//response.getWriter().append("  Failed");
		    	System.out.println("Books Requests table can't be updated");
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
		} 
		catch (ParseException e) {
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
