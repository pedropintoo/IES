package ies.jakartawebstarter;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet 
{
    private String message;

    public void init() 
    {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        if (request.getParameter("msg") != null) 
        {
            this.message = request.getParameter("msg").replace("\"", "");
        }   
        out.printf("<h1 style='color: red'>Hello %s!</h1>\n",this.message);

        out.println("</body></html>");
    }

    public void destroy() {
    }
}