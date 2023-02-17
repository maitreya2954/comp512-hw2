/**
 * @author Siddharth Rayabharam
 * @class COMP 512 homework 2
 * @instructor Dr. Truong Tran
 */

package server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Boot {

	public static Logger LOGGER = Logger.getLogger(Boot.class.getName());
	
	private static int PORT = 6666;
	
	private static Server SERVER;
	
	static {
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
		SERVER = new Server();
		Runtime.getRuntime().addShutdownHook(new Thread() {// Runs this codeblock on shutdown ensuring that the socket is closed before exit
			@Override
			public void run() {
				super.run();
				LOGGER.log(Level.INFO, "Running server shutdown hook");
				try {
					SERVER.stop();
					LOGGER.log(Level.INFO, "Shutdown successfull");
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, "Could not close server socket properly");
					e.printStackTrace();
				}
			}
		});
	}

    public static void main(String[] args) throws Exception {
    	LOGGER.log(Level.INFO, "Welcome to server application!!!");
    	// Start the server and open a socket at given port
        SERVER.start(PORT);
    }

}
