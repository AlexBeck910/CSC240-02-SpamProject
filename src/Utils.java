import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Utility class.
 * Contains methods that would have to be reimplemented in various classes.
 */
public class Utils {
    /**
     * Counts the number of times a given word appears in a string.
     * @param body String of text
     * @return Hash map containing the frequency of each word
     */
    public static HashMap<String, Integer> countFrequencies(String body) {
        HashMap<String, Integer> result = new HashMap<>();

        var data = body.split(" ");
        for(String s : data) {
            if(result.containsKey(s)) {
                result.put(s, result.get(s) + 1);
            } else {
                result.put(s, 1);
            }
        }
        return result;
    }

    /**
     * Converts the string to resemble the format used by the dataset, stripping out
     * numbers, URLs, and non-alphanumeric characters.
     * @param text Text to strip
     * @return Stripped text
     */
    public static String stripText(String text) {
        return Arrays.stream(text.split(" "))

                .map(Utils::conformToDataset)
                .collect(Collectors.joining(" "));
    }

    private static String conformToDataset(String token) {

        if(isNumeric(token)) {
            return "NUMBER";
        } else if(isValidURL(token)) {
            return "URL";
        }
        else {
            return token.toLowerCase().replaceAll( "[^a-zA-Z0-9]", "");
        }
    }

    // credit to baeldung.com
    // Both of these methods should be built into Java, but aren't
    // Too bad!
    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
             Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
