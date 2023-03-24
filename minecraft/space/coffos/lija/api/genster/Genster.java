package space.coffos.lija.api.genster;

import space.coffos.lija.util.encryption.Crypter;
import space.coffos.lija.util.general.JSONParser;
import space.coffos.lija.util.general.StringUtils;
import space.coffos.lija.util.general.core.API;
import space.coffos.lija.util.general.dynamic.DynamicVariables;
import space.coffos.lija.util.web.WebHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Genster {

    public static void initGenster() {

        try {
            String st = WebHelper.getWithCertificate("https://lunaclient.app/sopoorsoyouhavetograbthealts_.php").replace("]", "").replace("[", "");
            String out = StringUtils.cleanString(JSONParser.readJsonKey(API.api_data, "altToken"));
            System.setProperty("http.agent", Crypter.decrypt(out));
            URL url = new URL(Crypter.decrypt(st));
            System.setProperty("http.agent", Crypter.decrypt(out));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", Crypter.decrypt(out));
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("[\uFEFF-\uFFFF]", "").replaceAll("ï»¿", "");
                String[] split = line.split(":");
                DynamicVariables.initVar("GEN:name", split[0], true, true);
                DynamicVariables.initVar("GEN:pass", split[1], true, true);
            }
            reader.close();
        } catch (Exception ignored) {
        }
    }
}