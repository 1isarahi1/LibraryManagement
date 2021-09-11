package AdminTasks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Deny
 */
@WebServlet("/Deny")
public class Deny extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Deny() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		int librarianId  = Integer.parseInt(request.getParameter("librarianId"));
		String status = "Denied";
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
		    
		    String sqlquery = "update librarian set status=? where librarian_id = ?";		    
		    
		    PreparedStatement pst = con.prepareStatement(sqlquery);
		    pst.setString(1, status);
		    pst.setInt(2, librarianId);
		   int updatestatus = pst.executeUpdate();
		    if(updatestatus>0)
		    {
		    	
		  
		    	//response.getWriter().append("     User cannot be found");
		    	System.out.println("Denied Succesfully");
		    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/librarianRequest.jsp");
		    	PrintWriter pw = response.getWriter();
		    	pw.println("<font color = green> Denial Succesful </font>");
		    	rd.include(request, response);
		    }
		    else
		    {
		    	//response.getWriter().append("  Failed");
		    	System.out.println("User could not be denied");
		    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/librarianRequest.jsp");
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
