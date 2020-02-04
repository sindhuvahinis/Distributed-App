package server.UDPServer;

import server.datastore.KeyValueStore;
import util.DataUtil;
import util.DateUtil;
import util.NumericConstants;
import util.StringConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UDP Handler, handles the client communication.
 */
public class UDPHandler {

    /** Key Value store */
    private final KeyValueStore keyValueStore = new KeyValueStore();

    /** UDP Handler Logger */
    private static final Logger LOGGER = Logger.getLogger(UDPHandler.class.getName());

    /**
     * This method listens to the client and communicate with it.
     *
     * @param socket - datagram socket
     * @throws IOException exception
     */
    public void listenToClient(DatagramSocket socket) throws IOException {
        while (true) {

            try {
                byte[] requestByte = new byte[NumericConstants.ARRAY_LIMIT];
                DatagramPacket receivePacket = new DatagramPacket(requestByte, requestByte.length);
                socket.receive(receivePacket);
                socket.setSoTimeout(NumericConstants.FIVE_SECONDS);

                String requestStr = DataUtil.convertByteArrayToString(requestByte);
                LOGGER.log(Level.INFO, DateUtil.getCurrentTimeInMilliSecondsFormatted()
                        + " Request is received from the " + "client " + requestStr);

                String[] requestArr = requestStr.split(DataUtil.SEPARATOR);
                String operationStr = requestArr[0].toLowerCase();

                if (isValidOperation(operationStr)) {
                    LOGGER.log(Level.SEVERE, StringConstants.INVALID_OPERATION_SENT_BY_CLIENT);
                    continue;
                }

                if (isValidRequestData(requestArr, operationStr)) {
                    LOGGER.log(Level.INFO, StringConstants.UDP_DATA_PACKET_CORRUPTED);
                    continue;
                }

                handleRequestOperations(socket, receivePacket, requestArr, operationStr);
            } catch (SocketTimeoutException socketTimeOutException) {
                LOGGER.log(Level.SEVERE, StringConstants.TIME_OUT_EXCEPTION);
            }
        }
    }

    /**
     * Handle requests the operations.
     *
     * @param socket Datagram socket
     * @param receivePacket received packet
     * @param requestArr array that has request key value store
     * @param operationStr operation name string
     * @throws IOException exception thrown
     */
    private void handleRequestOperations(DatagramSocket socket, DatagramPacket receivePacket,
                                         String[] requestArr, String operationStr) throws IOException {
        switch (operationStr) {
            case "put":
                handlePUTRequestAndResponse(socket, receivePacket, requestArr);
                break;
            case "get":
                handleGETRequestAndResponse(socket, receivePacket, requestArr);
                break;
            case "delete":
                handleDELETERequestAndResponse(socket, receivePacket, requestArr);
                break;
        }
    }

    /**
     * Handles PUT request and response.
     *
     * @param socket datagram socket
     * @param receivePacket received packet
     * @param requestArr array that has key and value
     * @throws IOException exception thrown
     */
    private void handlePUTRequestAndResponse(DatagramSocket socket, DatagramPacket receivePacket,
                                             String[] requestArr) throws IOException {
        keyValueStore.put(requestArr[1], requestArr[2]);
        LOGGER.log(Level.INFO, "Key-Value pair " + requestArr[1] + "-" + requestArr[2] +
                " has been updated in the key value store");

        String responseStr = "Key-Value Pair " + requestArr[1] + "-" + requestArr[2]
                + " has been updated successfully.";

        sendResponse(socket, receivePacket, responseStr);
    }


    /**
     * Handles GET request and response
     *
     * @param socket datagram socket
     * @param receivePacket received packet
     * @param requestArr array that has key and value
     * @throws IOException exception thrown
     */
    private void handleGETRequestAndResponse(DatagramSocket socket, DatagramPacket receivePacket,
                                             String[] requestArr) throws IOException {

        String value = keyValueStore.get(requestArr[1]);
        LOGGER.log(Level.INFO, "The get operation is done ");
        String responseStr = "The value for the key " + requestArr[1] + " is: " + value;
        sendResponse(socket, receivePacket, responseStr);
    }

    /**
     * Handles DELETE request and response
     *
     * @param socket datagram socket
     * @param receivePacket received packet
     * @param requestArr array that has key and value
     * @throws IOException exception thrown
     */
    private void handleDELETERequestAndResponse(DatagramSocket socket, DatagramPacket receivePacket,
                                                String[] requestArr) throws IOException {
        String value = keyValueStore.delete(requestArr[1]);

        String responseStr = "Key-Value Pair " + requestArr[1] + "-" + value
                + " has been deleted successfully.";
        sendResponse(socket, receivePacket, responseStr);
    }

    /**
     * Sends response to the client.
     *
     * @param socket datagram socket
     * @param receivePacket received packet
     * @param responseStr response string
     * @throws IOException exception thrown
     */
    private void sendResponse(DatagramSocket socket, DatagramPacket receivePacket,
                              String responseStr) throws IOException {
        byte[] responseBytes = responseStr.getBytes();
        DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length,
                receivePacket.getAddress(), receivePacket.getPort());
        socket.send(responsePacket);
        LOGGER.log(Level.INFO, "The following response is sent to the client " + responseStr);
    }

    /**
     * Validate whether it is valid operation
     *
     * @param operationStr operation string
     * @return whether operation is valid or not.
     */
    private boolean isValidOperation(String operationStr) {
        if (!DataUtil.operationPattern.matcher(operationStr).matches()) {
            return false;
        }

        return true;
    }

    /**
     * Checks the validity of request data.
     *
     * @param requestArr request array
     * @param operationStr operation string
     * @return whether request data is valid or not.
     */
    private boolean isValidRequestData(String[] requestArr, String operationStr) {
        switch (operationStr) {
            case "put": {
                if (requestArr.length < 3) {
                    return false;
                }
            }
            break;
            case "get":
            case "delete": {
                if (requestArr.length < 2) {
                    return false;
                }
            }
            break;
        }
        return true;
    }

}
