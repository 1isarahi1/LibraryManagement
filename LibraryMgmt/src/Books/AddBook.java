package Books;

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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


/**
 * Servlet implementation class AddBook
 */
@WebServlet("/AddBook")
@MultipartConfig(maxFileSize = 16177215)
public class AddBook extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddBook() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
		int bookID  = Integer.parseInt(request.getParameter("bookID"));
		String bookName  = request.getParameter("bookName");
		String bookAuthor  = request.getParameter("bookAuthor");
		String bookPrice  = request.getParameter("bookPrice");
		String Genre = request.getParameter("Genre");
		
		
		
		InputStream is = null;
		Part filePart = request.getPart("Photo");
		if(filePart !=null)
		{
			System.out.println("Name of file is:" + filePart.getName());
			System.out.println("File size is:" + filePart.getSize());
			System.out.println("Content type is:" + filePart.getContentType());
			
			is = filePart.getInputStream();
		}
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_library?autoReconnect=true&useSSL=false","root","Tsunayoshi27");
			
		    String sqlquery = "insert into books(book_id,book_name,book_author,book_price,genre,book_status, photo ) values(?,?,?,?,?,?,?)";
		    PreparedStatement pst = con.prepareStatement(sqlquery);
		    pst.setInt(1, bookID);
		    pst.setString(2, bookName);
		    pst.setString(3, bookAuthor);
		    pst.setString(4, bookPrice);
		    pst.setString(5, Genre);
		    pst.setString(6, "Available");
		    if(is!=null)
		    {
		    	pst.setBlob(7, is);
		    }
		    pst.execute();
		    System.out.println("Insertion done successfully");
		    con.close();
		    RequestDispatcher rd = getServletContext().getRequestDispatcher("/HomePageAdmin.jsp");
	    	PrintWriter pw = response.getWriter();
	    	pw.println("<font color = green>Book Inserted Successfully </font>");
	    	rd.include(request, response);
	    	
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
