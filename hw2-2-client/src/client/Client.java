/**
 * @author Siddharth Rayabharam
 * @class COMP 512 homework 2
 * @instructor Dr. Truong Tran
 */

package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
	
	private static Logger LOGGER = Logger.getLogger(Client.class.getSimpleName());
	// Socket
	private Socket clientSocket;
	// Output Stream of the client
    private PrintWriter out;
    // Input stream of the client
    private BufferedReader in;

    /**
     * Start a connection with server at given IP address and port
     * @param ip Address to which the client has to connect
     * @param port Port to which the client has to connect
     * @throws Exception Throws an exception if there is an exception in starting a connection with the server
     */
    public void startConnection(String ip, int port) throws Exception {
    	LOGGER.log(Level.INFO, "Intializing client socket with address: {0} port: {1}", new Object[] {ip, port});
    	while (clientSocket == null) {
    		try {
    			clientSocket = new Socket(ip, port); // Create a new socket
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Could not connect to server. Please start server. Retrying after 3 secs.");
				Thread.sleep(3000L);
			}
    	}
        
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /**
     * Send message and receive a response from the server
     * @param msg Message to be sent to the server
     * @return Response from the server as a String
     * @throws Exception Throws exception if there is an error sending message
     */
    public String sendMessage(String msg) throws Exception {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }
    
    /**
     * Stop client connection with the server
     * @throws Exception Throws Exception if there is an error in stopping connection
     */
    public void stopConnection() throws Exception {
        in.close();
        out.close();
        clientSocket.close();
    }
    
    /**
     * Check if client is connected to the server
     * @return Boolean indicating if the connection is alive or not
     * @throws Exception Throws Exception if there is an exception in checking the status of the connection
     */ 
    public boolean checkConnection() throws Exception {
    	return clientSocket.isConnected();
    }
}
