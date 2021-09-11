package login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class loginLibrarian
 */
@WebServlet("/loginLibrarian")
public class loginLibrarian extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginLibrarian() {
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
		String username  = request.getParameter("username");
		String password  = request.getParameter("librarianPassword");
		
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
		    
		    String sqlquery = "select * from librarian where username = ? and user_password = ? and UPPER(status)='APPROVED'";		    
		    
		    PreparedStatement pst = con.prepareStatement(sqlquery);
		    pst.setString(1, username);
		    pst.setString(2, password);
		    
		    ResultSet rs = pst.executeQuery();
		    if(rs.next())
		    {
		    	response.getWriter().append("    Login Succesful");
		    	System.out.println("Login succesful");
		    	Cookie loginLibrarianCookie = new Cookie("librarian", username);
		    	loginLibrarianCookie.setMaxAge(45*60);
		    	response.addCookie(loginLibrarianCookie);
		    	response.sendRedirect("HomePageLibrarian.jsp");
		    }
		    else
		    {
		    	response.getWriter().append("     User cannot be found");
		    	System.out.println("User cannot be found");
		    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/LoginLibrarian.html");
		    	PrintWriter pw = response.getWriter();
		    	pw.println("<font color = red> Either Username or Password is wrong, OR You are not approved by Admin. Try again! </font>");
		    	rd.include(request, response);
		    	response.sendRedirect("LoginLibrarian.jsp");
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
