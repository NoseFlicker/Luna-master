package space.coffos.lija.util.general;

/**
 * @author Jay, 2018
 */
public class StringUtils {

    public static String cleanString(String input) {
        return input.replace("]", "[");
    }
}