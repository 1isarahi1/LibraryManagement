package Logout;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LogoutLibrarian
 */
@WebServlet("/LogoutLibrarian")
public class LogoutLibrarian extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutLibrarian() {
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
		Cookie login = null;
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null)
		{
			for(Cookie c: cookies)
			{
				if(c.getName().equals("librarian"))
				{
					login = c;
					break;
				}
			}
			
		}
		if(login != null)
		{
			login.setMaxAge(0);
			response.addCookie(login);
		}
	response.sendRedirect("LoginLibrarian.html");
	}
	
	}


