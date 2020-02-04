package client.logger;

import util.DataUtil;
import util.DateUtil;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientLogger {
    private static FileHandler fileHandler;

    public static void init() {

        String fileName = "ClientLog" + DateUtil.getCurrentTimeInMilliSecondsFormatted()
                + DataUtil.TEXT_EXTENSION;
        try {
            fileHandler = new FileHandler(fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger logger = Logger.getLogger("");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.CONFIG);
    }
}
