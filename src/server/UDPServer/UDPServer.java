package server.UDPServer;

import server.logger.ServerLogger;
import util.StringConstants;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class simulates the server.Server which connects to the client through UDP protocol.
 */
public class UDPServer {

    private static final Logger LOGGER = Logger.getLogger(UDPServer.class.getName());


    /**
     * This method provides the UDP connection between the server and client.
     * It accepts the request from the client and sends the response.
     *
     * @param args command line arguments. arg[0] - port number.
     * @throws SocketException exception is thrown when socket connection fails.
     */
    public static void main(String[] args) throws IOException {
        ServerLogger.init("UDPServerLog");

        int portNumber = Integer.parseInt(args[0]);
        DatagramSocket datagramSocket = new DatagramSocket(portNumber);
        LOGGER.log(Level.INFO, StringConstants.UDP_SERVER_CONNECTED_TO_CLIENT + " " + portNumber);
        UDPHandler udpHandler = new UDPHandler();
        udpHandler.listenToClient(datagramSocket);
    }


}
