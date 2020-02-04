package client.UDPClient;

import client.logger.ClientLogger;
import util.DataUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class simulates the client which communicates with the server using UDP protocol.
 */
public class UDPClient {

    private DatagramSocket socket;
    InetAddress inetAddress;
    int portNumber;

    Scanner scanner = new Scanner(System.in);

    private static final Logger LOGGER = Logger.getLogger(UDPClient.class.getName());

    /**
     * This function, is the main heart of the client.
     * It connects to the server and send the requests and receive the responses.
     *
     * @param args arg[0] - IP address, arg[1] - port number
     */
    public static void main(String[] args) throws Exception {
        ClientLogger.init();

        if (args.length < 2) {
            System.out.println("Please enter in the following format. <IP Address>  <port number>");
            System.out.println("Example: 127.0.0.1 3000");
            return;
        }

        UDPClient UDPClient = new UDPClient();

        UDPClient.portNumber = Integer.parseInt(args[1]);
        UDPClient.inetAddress = InetAddress.getByName(args[0]);

        UDPClient.socket = new DatagramSocket();
        UDPClient.communicateWithServer();
    }

    /**
     * This function connects to the server endlessly.
     */
    private void communicateWithServer() {

        try {
            while (true) {
                handleInputFromUser();
            }
        } catch (Exception ex) {
            // Logger function
        }

    }

    private void handleInputFromUser() throws Exception {

        System.out.println("Enter the Operation: PUT/GET/DELETE:");
        String operation = "";

        while (!DataUtil.operationPattern.matcher(operation.toLowerCase()).matches()) {
            operation = scanner.next();

            switch (operation.toLowerCase()) {
                case "put": {
                    sendPutRequestToServer();
                }
                break;
                case "get": {
                    sendGetRequestToServer();
                }
                break;
                case "delete": {
                    sendDeleteRequestToServer();
                }
                break;
                default: {
                    System.out.println("Please enter a valid operation from the following: PUT/GET/DELETE");
                    LOGGER.log(Level.INFO, "User has entered an invalid operation " + operation);
                    LOGGER.log(Level.INFO, "User is prompted with following message : Please enter a valid operation from the following: PUT/GET/DELETE");
                }
            }
        }

    }

    private void sendPutRequestToServer() throws Exception {

        // SEND REQUEST
        System.out.println("Enter the key:");
        String key = scanner.next();

        System.out.println("Enter the value:");
        String value = scanner.next();

        String requestStr = "PUT" + DataUtil.SEPARATOR + key + DataUtil.SEPARATOR + value;
        sendRequest(requestStr);

        // RECEIVE RESPONSE
        byte[] responseBytes = new byte[10000];
        DatagramPacket response = new DatagramPacket(responseBytes, responseBytes.length);
        socket.receive(response);

        String responseStr = DataUtil.convertByteArrayToString(responseBytes);
        System.out.println(responseStr);
    }

    private void sendGetRequestToServer() throws Exception {
        System.out.println("Enter the key:");
        String key = scanner.next();

        String requestStr = "GET" + DataUtil.SEPARATOR + key;
        sendRequest(requestStr);

        // RECEIVE RESPONSE
        byte[] responseBytes = new byte[10000];
        DatagramPacket response = new DatagramPacket(responseBytes, responseBytes.length);
        socket.receive(response);

        String responseStr = DataUtil.convertByteArrayToString(responseBytes);
        System.out.println(responseStr);
    }

    private void sendDeleteRequestToServer() throws Exception {
        System.out.println("Enter the key:");
        String key = scanner.next();

        String requestStr = "DELETE" + DataUtil.SEPARATOR + key;
        sendRequest(requestStr);

        // RECEIVE RESPONSE
        byte[] responseBytes = new byte[10000];
        DatagramPacket response = new DatagramPacket(responseBytes, responseBytes.length);
        socket.receive(response);

        String responseStr = DataUtil.convertByteArrayToString(responseBytes);
        System.out.println(responseStr);
    }

    private void sendRequest(String requestStr) throws IOException {
        byte[] requestBytes = requestStr.getBytes();
        DatagramPacket request = new DatagramPacket(requestBytes, requestBytes.length, inetAddress, portNumber);
        socket.send(request);
    }

}
