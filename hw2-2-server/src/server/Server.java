/**
 * @author Siddharth Rayabharam
 * @class COMP 512 homework 2
 * @instructor Dr. Truong Tran
 */

package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

	private static Logger LOGGER = Logger.getLogger(Server.class.getSimpleName());

	private ServerSocket serverSocket;

	private Socket clientSocket;

	private PrintWriter out;

	private BufferedReader in;
	
	private int x = 0;
	
	private int y = 0;
	
	private String prevOperation = null;

	/**
	 * Start a server and socket at given port
	 * @param port Integer representing port number at which socket should start
	 * @throws Exception Throws exception  if there is an error in connecting or parsing the command
	 */
	public void start(int port) throws Exception {
		LOGGER.log(Level.INFO, "Server socket intialized in port: {0}", port);
		// Start a server socket at the given port
		serverSocket = new ServerSocket(port);
		
		// Accept the client connection
		clientSocket = serverSocket.accept();
		
		// Output stream of the server
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		// Input stream of the server
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		
		String command;
		while ((command = in.readLine()) != null) {
			LOGGER.log(Level.INFO, "Received message: {0}", command);
			// EXIT COMMAND
			if (command.equals("exit")) {
				out.println("good bye");
				break;
			} else if (command.contains("change")) { // CHANGE command
				if (prevOperation == null || prevOperation.isEmpty()) {
					out.println("No previous operation found. 'change' operator cannot be used.");
					continue;
				}
				String[] commandSplit = command.split(" ");
				int value = Integer.parseInt(commandSplit[3]);
				if (commandSplit[1].equals("x")) {
					x = value;
				} else {
					y = value;
				}
				command = prevOperation;
			} else if (command.contains("add") || command.contains("subtract") || command.contains("multiply") || command.contains("divide")) { // Other commands
				String[] commandCommaSplit = command.split(",");
				String[] commandSpaceSplit = commandCommaSplit[1].trim().split(" ");
				x = Integer.parseInt(commandCommaSplit[0]);
				y = Integer.parseInt(commandSpaceSplit[0]);
			} else {
				out.println("Unsupported operation.");
			} 
			if (command.contains("add")) {
				out.println(add());
				prevOperation = "add";
			} else if (command.contains("subtract")) {
				out.println(subtract());
				prevOperation = "subtract";
			} else if (command.contains("multiply")) {
				out.println(multiply());
				prevOperation = "multiply";
			} else if (command.contains("divide")) {
				if (y != 0) {
					out.println(divide());
					prevOperation = "divide";
				} else {
					out.println("Cannot divide by zero.");
				}
			}
		}
		
	}
	
	/**
	 * Add x and y
	 * @return Integer resulting from adding x and y
	 */
	public int add() {
		return x + y;
	}
	
	/**
	 * Subtract y from x
	 * @return Integer resulting from subtracting y from x
	 */
	public int subtract() {
		return x - y;
	}
	
	/**
	 * Multiply x and y
	 * @return Integer resulting from multiplying x and y
	 */
	public int multiply() {
		return x * y;
	}
	
	/**
	 * Divide x with y
	 * @return Double resulting from dividing x with y
	 */
	public double divide() {
		return ((double)x) / y;
	}
	
	
	/**
	 * Stop the socket
	 * @throws Exception Throws Exception if there is an error closing the socket
	 */
	public void stop() throws Exception {
		LOGGER.log(Level.INFO, "Closing socket");
		in.close();
		out.close();
		clientSocket.close();
		serverSocket.close();
	}
}
