package util;

import java.util.regex.Pattern;

/**
 * Helper classes for such as data, extensions.
 */
public class DataUtil {

    /** String separator */
    public static final String SEPARATOR = "~~";

    /** Pattern for operations */
    public static final Pattern operationPattern = Pattern.compile("put|get|delete");

    /** Extension for the text. */
    public static final String TEXT_EXTENSION = ".txt";

    /**
     * Convert byte array to string.
     *
     * @param byteArray byte array to be converted
     * @return converted string.
     */
    public static String convertByteArrayToString(byte[] byteArray) {
        if (byteArray == null)
            return null;

        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (byteArray[i] != 0) {
            stringBuilder.append((char)byteArray[i++]);
        }

        return stringBuilder.toString();
    }
}
