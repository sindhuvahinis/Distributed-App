package server.TCPServer;

import server.datastore.KeyValueStore;
import util.DataUtil;
import util.DateUtil;
import util.StringConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TCP Handler that listens to the client,
 * accept requests and send appropriate response.
 */
public class TCPHandler {

    /** Key Value storage instance. */
    private final KeyValueStore keyValueStore = new KeyValueStore();

    /** Logger of the TCP Handler class */
    private static final Logger LOGGER = Logger.getLogger(TCPHandler.class.getName());

    /**
     * Accepts the request from the client and send the response.
     * @param socket - socket connection to communicate with client.
     */
    public void listenToClient(Socket socket) {
        /* Created inputstream object to read data written by the client  */
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, DateUtil.getCurrentTimeInMilliSecondsFormatted() +
                    "Exception occurred while initializing input stream");
            return;
        }

        /* Created outputstream object to write the data in the client */
        DataOutputStream outputStream;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, DateUtil.getCurrentTimeInMilliSecondsFormatted() +
                    "Exception occurred while initializing output stream");
            return;
        }

        String requestStr;
        try {
            requestStr = inputStream.readUTF();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, DateUtil.getCurrentTimeInMilliSecondsFormatted() +
                    "Exception occurred while reading input from the client");
            return;
        }
        LOGGER.log(Level.INFO, DateUtil.getCurrentTimeInMilliSecondsFormatted()
                + " Request is received from the client " + requestStr);

        String[] requestArr = requestStr.split(DataUtil.SEPARATOR);

        String operationStr = requestArr[0].toLowerCase();
        switch (operationStr) {
            case "put": {
                if (requestArr.length < 3) {
                    // data is corrupted.
                    LOGGER.log(Level.INFO,  DateUtil.getCurrentTimeInMilliSecondsFormatted() +
                            "Data packet is corrupted. request "
                            + "misses some elements. \nRequest string is :" + requestStr);
                    break;
                }

                handlePUTRequestAndResponse(requestArr, outputStream);
            }
            break;
            case "get": {
                if (requestArr.length < 2) {
                    // data is corrupted.
                    LOGGER.log(Level.INFO,  DateUtil.getCurrentTimeInMilliSecondsFormatted() +
                            "Data packet is corrupted. request "
                            + "misses some elements. \nRequest string is :" + requestStr);
                    break;
                }
                handleGETRequestAndResponse(requestArr, outputStream);
            }
            break;
            case "delete": {
                if (requestArr.length < 2) {
                    // data is corrupted.
                    LOGGER.log(Level.INFO, DateUtil.getCurrentTimeInMilliSecondsFormatted() +
                            "Data packet is corrupted. " +
                            "Request misses some elements. \nRequest string is :" + requestStr);
                    break;
                }
                handleDELETERequestAndResponse(requestArr, outputStream);
            }
            break;
            default: {
                LOGGER.log(Level.INFO, StringConstants.INVALID_OPERATION_SENT_BY_CLIENT + operationStr);
            }

        }
    }

    /*********************** PRIVATE METHODS  ***************************************/

    /**
     * Handles the GET request and response.
     * @param requestArr - request array contains key
     * @param dataOutputStream output stream to write to the stream.
     */
    private void handleGETRequestAndResponse(String[] requestArr, DataOutputStream dataOutputStream) {
        String value = keyValueStore.get(requestArr[1]);
        String responseStr;
        if (value == null) {
            responseStr = "Key is not found.";
        } else {
            responseStr = "The value for the key " + requestArr[1] + " is: " + value;
        }
        LOGGER.log(Level.INFO, DateUtil.getCurrentTimeInMilliSecondsFormatted() + "The get " +
                "operation is done ");
        sendResponse(responseStr, dataOutputStream);
    }

    /**
     * Handles the DELETE request and response.
     *
     * @param requestArr - request array contains key
     * @param dataOutputStream output stream to write to the stream.
     */
    private void handleDELETERequestAndResponse(String[] requestArr, DataOutputStream dataOutputStream) {
        String value = keyValueStore.delete(requestArr[1]);

        String responseStr;
        if (value == null) {
            responseStr = "Key not found";
        } else {
            responseStr = "Key: " + requestArr[1] + " Value: " + value
                    + " has been deleted successfully.";
        }
        sendResponse(responseStr, dataOutputStream);
    }

    /**
     * Handles the PUT request and response.
     *
     * @param requestArr - request array contains key
     * @param dataOutputStream output stream to write to the stream.
     */
    private void handlePUTRequestAndResponse(String[] requestArr,
                                             DataOutputStream dataOutputStream) {
        keyValueStore.put(requestArr[1], requestArr[2]);
        LOGGER.log(Level.INFO, "Key-Value pair " + requestArr[1] + "-" + requestArr[2] +
                " has been updated in the key value store");

        String responseStr = "Key-Value Pair " + requestArr[1] + "-" + requestArr[2]
                + " has been updated successfully.";

        sendResponse(responseStr, dataOutputStream);
    }

    /**
     * Handles sending response to the client.
     *
     * @param responseStr - response string to be sent to the client.
     * @param dataOutputStream output stream to write to the stream.
     */
    private void sendResponse(String responseStr, DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF(responseStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
