package ui.util;

public class Util {

    public static Integer tryParseToInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

}
