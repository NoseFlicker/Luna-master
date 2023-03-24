package space.coffos.lija.util.web;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * @author iHaq
 */

public class WebHelper {

    private static String inpStringNC;

    public static void trustCertificates() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException ignored) {
        }
    }

    public static String getWithCertificate(String url) {
        try {
            trustCertificates();
            URL urls = new URL(url);
            ArrayList<Object> lines = new ArrayList<>();
            URLConnection connection = urls.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
                inpStringNC = String.valueOf(lines);
            }
        } catch (Exception ignored) {
        }
        return inpStringNC;
    }

    public static String cleanString(String input) {
        return input.replace("]", "[");
    }

    public static String getOptimized(String url) throws IOException {

        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Lynx/2.8.8dev.12");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = in.lines().map(inputLine -> inputLine + "\n").collect(Collectors.joining());

        in.close();

        return response;
    }

    public static String post(String url, Map<String, String> requestMap, String body) throws IOException {

        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        if (requestMap != null)
            requestMap.forEach(con::setRequestProperty);

        con.setDoOutput(true);
        con.setDoInput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = in.lines().map(inputLine -> inputLine + "\n").collect(Collectors.joining());

        in.close();
        return response;
    }
}