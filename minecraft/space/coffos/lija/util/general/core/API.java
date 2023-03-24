package space.coffos.lija.util.general.core;

import org.json.JSONException;
import space.coffos.lija.util.general.JSONParser;
import space.coffos.lija.util.general.dynamic.DynamicVariables;

import java.io.IOException;

public class API {

    /**
     * API URL
     */
    public static final String api_data = "https://coffos.space/luna/loader.json";

    /*
     * The rest of the variables is stored via DynamicVariables.*;
     */

    /**
     * Refresh the api cache and update it.
     */
    public void refreshAPI() {
        try {
            DynamicVariables.initVar("S:Temperance", Boolean.parseBoolean((String) retrieveJSONKey("Temperance")), true, false);
            DynamicVariables.initVar("S:Luna", Boolean.parseBoolean((String) retrieveJSONKey("Luna")), true, false);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve a json key from the api, returned as an object.
     */
    private Object retrieveJSONKey(String key) throws IOException, JSONException {
        return JSONParser.readJsonKey(api_data, key);
    }
}