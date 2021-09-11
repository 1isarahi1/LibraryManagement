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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class changePasswordAdministration
 */
@WebServlet("/changePasswordAdmin")
public class changePasswordAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public changePasswordAdmin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		String old =  request.getParameter("password");
		String newpass =  request.getParameter("newPassword");
		
		String username = null;
		Cookie[] cookies = request.getCookies();

		if(cookies != null)
		{
			for(Cookie c:cookies)
			{
				if(c.getName().equals("admin"))
				{
					username = c.getValue();
				}
			}
			
		}

		if(username==null)
		{
		response.sendRedirect("LoginAdmin.html");
		}
		
		
		try
		{
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Driver OK");
	    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
	    
	  
	    String sqlquery1 = "update library_admin set admin_password=? where username=? and admin_password=?";
	    PreparedStatement pst = con.prepareStatement(sqlquery1);
	    pst.setString(1, newpass);
	    pst.setString(2, username);
	    pst.setString(3, old);
	    
	    int status = pst.executeUpdate();
	    
	    if(status>0)
	    {
	    	System.out.println("Password Updated Succesfully");
	    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePageAdmin.jsp");
	    	PrintWriter pw = response.getWriter();
	    	pw.println("<font color = green> Password Updated Succesfully </font>");
	    	rd.include(request, response);
	    }
	    else
	    {   	System.out.println("Password Update Failed");
    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePageAdmin.jsp");
    	PrintWriter pw = response.getWriter();
    	pw.println("<font color = red> Password Update Failed. Either you entered wrong old password or user doesn't exist! </font>");
    	rd.include(request, response);
	    	
	    }
	    
	    
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	
	}

}
