package UserTasks;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PayFine
 */
@WebServlet("/PayFine")
public class PayFine extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PayFine() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
				response.getWriter().append("Served at: ").append(request.getContextPath());
				
				int userID  = Integer.parseInt(request.getParameter("userID"));
				
				try 
				{  Date dt = new Date();	
				String date = new SimpleDateFormat("dd/MM/yyyy").format(dt);
				java.util.Date dt1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
				
				System.out.println(dt1);
				
					
					Class.forName("com.mysql.jdbc.Driver");
					System.out.println("Driver OK");
				    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
				    
				    //fix
				    String sqlquery = "select return_date from book_requests where userID = ?"; 
				    PreparedStatement pst1 = con.prepareStatement(sqlquery);	
					pst1.setInt(1, userID);
					ResultSet rs = pst1.executeQuery();
				    
				    //end fix
					
					int fineOwed = 0;
				    		
				    while(rs.next())
				    {
				    
				    	Date returnDate = rs.getDate("return_date");
				    	String datesql = new SimpleDateFormat("dd/MM/yyyy").format(returnDate);
				    	
				    	java.util.Date javareturndate = new SimpleDateFormat("dd/MM/yyyy").parse(datesql);
				    	
				    	long diffdays = javareturndate.getTime() - dt1.getTime();
				    	//subtract dates, if negative you owe money
				    	
				    	diffdays = (diffdays/1000/3600/24);
				    	//I want to round down if decimal 
				    	
				    	
				    	if(diffdays<0)
				    	{
				    		fineOwed += diffdays* 0.1;
				    		
				    		fineOwed = (int)(fineOwed);
				    	}	
				    	
				    	    	    	
				    }
				    
				    String sqlquery1 = "update library_user set fine_amount=? where user_id = ?";
				    PreparedStatement pst = con.prepareStatement(sqlquery);
				    pst.setInt(1, fineOwed);
				    pst.setInt(2, userID);
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
