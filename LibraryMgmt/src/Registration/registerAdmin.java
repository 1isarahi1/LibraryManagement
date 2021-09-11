package Registration;


import java.util.Date;
import java.io.IOException;
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
@WebServlet("/registerAdmin")
public class registerAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public registerAdmin()
    {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String username  = request.getParameter("username");
		String password  = request.getParameter("admin_password");
		String first_name  = request.getParameter("firstname");
		String last_name  = request.getParameter("lastname");
		String gender = request.getParameter("gender");
		try {
			Date dt = new Date();	
			String date = new SimpleDateFormat("dd/MM/yyyy").format(dt);
			java.util.Date dt1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			java.sql.Date startDate = new java.sql.Date(dt1.getTime());
			System.out.println(dt1);
			
			Class.forName("com.mysql.jdbc.Driver");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
			
		    String sqlquery = "insert into library_admin(username,admin_password,first_name,last_name,gender,start_date) values(?,?,?,?,?,?)";
		    PreparedStatement pst = con.prepareStatement(sqlquery);
		    pst.setString(1, username);
		    pst.setString(2, password);
		    pst.setString(3, first_name);
		    pst.setString(4, last_name);
		    pst.setString(5, gender);
		    pst.setDate(6, startDate);
		    pst.execute();
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

