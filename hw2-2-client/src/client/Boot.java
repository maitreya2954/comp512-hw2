/**
 * @author Siddharth Rayabharam
 * @class COMP 512 homework 2
 * @instructor Dr. Truong Tran
 */

package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Boot {

	public static Logger LOGGER = Logger.getLogger(Boot.class.getName());
	
	// IP Address of the socket
	private static String IP_ADDRESS = "127.0.0.1";
	
	// Port of the socket
	private static int PORT = 6666;
	
	private static Client CLIENT;
	
	static {
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
		CLIENT = new Client();
		Runtime.getRuntime().addShutdownHook(new Thread() { // Runs this codeblock on shutdown ensuring that the socket is closed before exit
			@Override
			public void run() {
				super.run();
				LOGGER.log(Level.INFO, "Running client shutdown hook");
				try {
					CLIENT.stopConnection();
					LOGGER.log(Level.INFO, "Shutdown successfull");
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, "Could not close client socket properly");
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void main(String[] args) throws Exception {
		LOGGER.log(Level.INFO, "Welcome to the client application!!!");
		
		//Start connection
		CLIENT.startConnection(IP_ADDRESS, PORT);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter commands (Supports add, subtract, multiply, divide, change and exit)");
		while (CLIENT.checkConnection()) { // Check connection every time before sending a message
			System.out.print("> ");
			
			String command = br.readLine().trim();
			System.out.println(CLIENT.sendMessage(command)); // Send message
			if (command.equals("exit")) { // If command is exit, then stop the client progam
				break;
			}
		}
	}

}
