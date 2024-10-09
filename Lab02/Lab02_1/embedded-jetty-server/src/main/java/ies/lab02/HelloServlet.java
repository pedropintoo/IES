package ies.lab02;
 
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet 
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>New Hello Simple Servlet</h1>"); 

        String message = request.getParameter("msg");
        if (message != null) {
            response.getWriter().printf("<h1 style='color:red'>%s!</h1>\n", message.replace("\"", ""));
        } else {
            response.getWriter().println("<h1 style='color:red'>Hello World!</h1>");
        }

    } 
}


