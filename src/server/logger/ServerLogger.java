package server.logger;

import util.DataUtil;
import util.DateUtil;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * ServerLogger - Logs the logger to file.
 * All the logger will be added to the file.
 */
public class ServerLogger {

    /** File handler to write to the file */
    private static FileHandler fileHandler;

    /**
     * Initialized the logger. Creates the file and adds the logger handler.
     * @param fileName name of the file.
     */
    public static void init(String fileName) {

        String fileNameWithDate = fileName + DateUtil.getCurrentTimeInMilliSecondsFormatted()
                + DataUtil.TEXT_EXTENSION;
        try {
            fileHandler = new FileHandler(fileNameWithDate, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger logger = Logger.getLogger("");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.CONFIG);
    }
}
