package ies.lab02;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class EmbeddedJettyExample {
 
    public static void main(String[] args) throws Exception {
        Server server = new Server(8680);
        try {

            ServletHandler servletHandler = new ServletHandler();
            server.setHandler(servletHandler);
                    
            servletHandler.addServletWithMapping(HelloServlet.class, "/");
            
            server.start();
            server.dumpStdErr();
            server.join();

        } catch (Exception e) {           
            e.printStackTrace();
        }  
    }
}