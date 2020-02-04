package client.TCPClient;

import client.logger.ClientLogger;
import util.DataUtil;
import util.NumericConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient {
    InetAddress inetAddress;
    int portNumber;
    Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;


    Scanner scanner = new Scanner(System.in);

    private static final Logger LOGGER = Logger.getLogger(TCPClient.class.getName());

    public static void main(String[] args) {
        ClientLogger.init();

        if (args.length < 2) {
            System.out.println("Please enter in the following format. <IP Address>  <port number>");
            System.out.println("Example: 127.0.0.1 3000");
            return;
        }

        /* Socket connection is established to talk to server*/
        TCPClient tcpClient = new TCPClient();

        tcpClient.portNumber = Integer.parseInt(args[1]);
        try {
            tcpClient.inetAddress = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, "Exception in getting INET Address");
            return;
        }

        while (true) {
            tcpClient.communicateWithServer();
            tcpClient.closeConnections();
        }
    }

    private boolean initializeSocketInputAndOutputStream() {
        try {
            socket = new Socket("localhost", portNumber);
            socket.setSoTimeout(NumericConstants.FIVE_SECONDS);

        } catch (SocketTimeoutException ex) {
            LOGGER.log(Level.SEVERE, "Time out exception has occurred.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while initializing socket");
            return true;
        }

        try {
            socket.getInputStream().read();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Time out exception has occurred " + e.getMessage());
            return true;
        }

        /* Created inputstream object to read data written by the server  */
        try {
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (SocketTimeoutException ex) {
            LOGGER.log(Level.SEVERE, "Time out exception has occurred.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while initializing input stream " + e.getMessage());
            return true;
        }

        /* Created outputstream object to write the data in the stream */
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while initializing output stream");
            return true;
        }
        return false;
    }

    /**
     * This function connects to the server endlessly.
     */
    private void communicateWithServer() {

        try {
            handleInputFromUser();
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
                    LOGGER.log(Level.INFO, "User is prompted with following message : "
                            + "Please enter a valid operation from the following: PUT/GET/DELETE");
                }
            }
        }
    }

    private void sendPutRequestToServer() {

        if (initializeSocketInputAndOutputStream())
            return;

        // SEND REQUEST
        System.out.println("Enter the key:");
        String key = scanner.next();

        System.out.println("Enter the value:");
        String value = scanner.next();

        String requestStr = "PUT" + DataUtil.SEPARATOR + key + DataUtil.SEPARATOR + value;
        sendRequest(requestStr);

        // RECEIVE RESPONSE
        receiveResponseFromServer();
    }

    private void sendGetRequestToServer() throws Exception {

        if (initializeSocketInputAndOutputStream())
            return;

        System.out.println("Enter the key:");
        String key = scanner.next();

        String requestStr = "GET" + DataUtil.SEPARATOR + key;
        sendRequest(requestStr);

        // RECEIVE RESPONSE
        receiveResponseFromServer();
    }

    private void sendDeleteRequestToServer() {
        if (initializeSocketInputAndOutputStream())
            return;

        System.out.println("Enter the key:");
        String key = scanner.next();

        String requestStr = "DELETE" + DataUtil.SEPARATOR + key;
        sendRequest(requestStr);

        // RECEIVE RESPONSE
        receiveResponseFromServer();
    }

    private void sendRequest(String requestStr) {
        try {
            outputStream.writeUTF(requestStr);
            LOGGER.log(Level.INFO, "Request has been sent to the server.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "ExceptionOccurred while sending request to the server" + e.getMessage());
        }
    }

    private void receiveResponseFromServer() {
        String responseStr = null;
        try {
            responseStr = inputStream.readUTF();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while receiving response from the " +
                    "server" + e.getMessage());
            return;
        }

        LOGGER.log(Level.INFO,
                "Response string received from the server. Response string is " + responseStr);
    }

    private void closeConnections() {
        try {
            if (inputStream != null)
                inputStream.close();

            if (outputStream != null)
                outputStream.close();
            socket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while closing the connections.");
        }
    }
}

