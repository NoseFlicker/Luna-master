package space.coffos.lija.util.general;

import org.json.JSONException;
import org.json.JSONObject;
import space.coffos.lija.util.web.WebHelper;

import java.io.IOException;

/**
 * @author StackOverflow
 */

public class JSONParser {

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        String jsonText = WebHelper.getOptimized(url);
        return new JSONObject(jsonText);
    }

    public static String readJsonKey(String url, String key) throws IOException, JSONException {
        JSONObject jsonKey = readJsonFromUrl(url);
        return StringUtils.cleanString(String.valueOf(jsonKey.get(key)));
    }
}