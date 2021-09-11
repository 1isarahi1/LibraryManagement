package LibrarianTasks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Statement;

/**
 * Servlet implementation class CalculateFines
 */
@WebServlet("/CalculateFines")
public class CalculateFines extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalculateFines() 
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		try 
		{   Date dt = new Date();	
			String date = new SimpleDateFormat("dd/MM/yyyy").format(dt);
			java.util.Date dt1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			
			System.out.println(dt1);
			
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
		    
		    //fix
		    String sqlquery = "select user_id, book_id, return_date from book_requests where request_status = 'Approved'"; 
		    Statement st1 = con.createStatement();	
			ResultSet rs = st1.executeQuery(sqlquery);
			while(rs.next())
			{
			   float fineOwed = 0;

		    	Date returnDate = rs.getDate("return_date");
		    	String datesql = new SimpleDateFormat("dd/MM/yyyy").format(returnDate);
		    	
		    	java.util.Date javareturndate = new SimpleDateFormat("dd/MM/yyyy").parse(datesql);
		    	
		    	long diffdays = javareturndate.getTime() - dt1.getTime();
		    	//subtract dates, if negative you owe money
		    	
		    	diffdays = (diffdays/1000/3600/24);
		    	//I want to round down if decimal 
		    	
		    	int userID = rs.getInt("user_id");
		    	System.out.println("The difference of days for userid "+ userID +" is "+diffdays);
		    	
		    	if(diffdays<0)
		    	{
		    		diffdays = diffdays*(-1);
		    		fineOwed += diffdays*0.1;	 
		    		
		    		String sqlquery2 = "update library_user set fine_amount=? where user_id =?";
		    		PreparedStatement pst2 = con.prepareStatement(sqlquery2);
		    		pst2.setFloat(1, fineOwed);
		    		pst2.setInt(2, userID);
		    		
		    		 int updatestatus = pst2.executeUpdate();
		    		 if(updatestatus>0)
		 		    {
		 		    	
		 		  
		 		    	//response.getWriter().append("     User cannot be found");
		 		    	System.out.println("Update Succesful");
		 		    
		 		    }
		 		    else
		 		    {
		 		    	//response.getWriter().append("  Failed");
		 		    	System.out.println("Update not Succesful");
		 		    	
		 		    	
		 		    }
		    		
		    	}	
		    	
		    	
				
			}
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePageLibrarian.jsp");
		    	PrintWriter pw = response.getWriter();
		    	pw.println("<font color = green> Fine Calculation Succesful </font>");
		    	rd.include(request, response);
		    	response.sendRedirect("HomePageLibrarian.jsp");
		   
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
