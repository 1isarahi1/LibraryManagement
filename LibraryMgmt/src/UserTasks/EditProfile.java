package UserTasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class EditProfile
 */
@WebServlet("/EditProfile")
@MultipartConfig(maxFileSize = 16177215)
public class EditProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProfile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String Fname =  request.getParameter("Fname");
		String Lname =  request.getParameter("Lname");
		String gender =  request.getParameter("gender");
		String email =  request.getParameter("email");
		

		InputStream is = null;
		Part filePart = request.getPart("profilepic");
		if(filePart !=null)
		{
			System.out.println("Name of file is:" + filePart.getName());
			System.out.println("File size is:" + filePart.getSize());
			System.out.println("Content type is:" + filePart.getContentType());
			
			is = filePart.getInputStream();
		}
		
		
		String username = null;
		Cookie[] cookies = request.getCookies();

		if(cookies != null)
		{
			for(Cookie c:cookies)
			{
				if(c.getName().equals("user"))
				{
					username = c.getValue();
				}
			}
			
		}

		if(username==null)
		{
		response.sendRedirect("Login.html");
		}
		
		
		try
		{
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Driver OK");
	    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
	    
	  
	    String sqlquery1 = "update library_user set first_name=?, last_name=?,gender=?, email=?, profile_picture=? where username=?";
	    PreparedStatement pst = con.prepareStatement(sqlquery1);
	    pst.setString(1, Fname);
	    pst.setString(2, Lname);
	    pst.setString(3, gender);
	    pst.setString(4, email);    
	    if(is!=null)
	    {
	    	pst.setBlob(5, is);
	    }
	    
	    pst.setString(6, username);
	    
	    int status = pst.executeUpdate();
	    
	    if(status>0)
	    {
	    	con.close();
	    	System.out.println("Profile Updated Succesfully");
	    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePage.jsp");
	    	PrintWriter pw = response.getWriter();
	    	pw.println("<font color = green> Profile Updated Succesfully </font>");
	    	rd.include(request, response);
	    	response.sendRedirect("HomePage.jsp");
	    }
	    else
	    {   	
	    	con.close();
	    	System.out.println("Profile Update Failed");
    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePage.jsp");
    	PrintWriter pw = response.getWriter();
    	pw.println("<font color = red> Profile Update Failed! </font>");
    	rd.include(request, response);
    	response.sendRedirect("HomePage.jsp");
	    	
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


