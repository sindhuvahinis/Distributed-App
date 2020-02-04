package server.TCPServer;

import server.logger.ServerLogger;
import util.DateUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TCP Server represents the Server in the application.
 * It communicates with the client endlessly.
 */
public class TCPServer {

    /** TCP Server Logger */
    private static final Logger LOGGER = Logger.getLogger(TCPServer.class.getName());

    /**
     * Main method of TCP Server where the connection starts.
     * @param args -  port number is sent in the 0th index.
     */
    public static void main(String[] args) {

        ServerLogger.init("TCPServerLog");
        if (args.length < 1) {
            LOGGER.log(Level.INFO, DateUtil.getCurrentTimeInMilliSecondsFormatted() + "User has " +
                    "not given the port number");
            System.out.println("Please provide a port number");
            return;
        }

        int portNumber = Integer.parseInt(args[0]);

        try {

            ServerSocket serverSocket = new ServerSocket(portNumber);
            LOGGER.log(Level.INFO, "Waiting for client...");
            TCPHandler tcpHandler = new TCPHandler();

            while (true) {
                /* Request from client is accepted in the above port number */
                Socket socket = serverSocket.accept();
                LOGGER.log(Level.INFO, DateUtil.getCurrentTimeInMilliSecondsFormatted() +
                        "Accepted a request from the client with inet address "
                        + socket.getInetAddress() + " in the port number " + socket.getPort());

                LOGGER.log(Level.INFO, DateUtil.getCurrentTimeInMilliSecondsFormatted() + "Client" +
                        " connection is accepted...");

                tcpHandler.listenToClient(socket);
            }

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, DateUtil.getCurrentTimeInMilliSecondsFormatted() +
                    "Exception occurred while establishing connection in socket.");
        }
    }
}
