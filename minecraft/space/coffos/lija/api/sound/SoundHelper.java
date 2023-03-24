package space.coffos.lija.api.sound;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import space.coffos.lija.util.entity.PlayerUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

/**
 * @author Zhn17
 * <-> 2018-05-04 <-> 23:55
 **/
public class SoundHelper {

    public static Thread music;
    public static boolean started = false;

    public static void createThread(final String url) {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException ignored) {
        }
        try {
            music = new Thread(() -> {
                try {
                    Player player = new Player(new URL(url).openStream());
                    player.play();
                } catch (MalformedURLException e) {
                    PlayerUtils.tellPlayer("§4Error: URLCOR; Radio failed to load music from URL.", false);
                } catch (JavaLayerException e) {
                    PlayerUtils.tellPlayer("§4Error: JLAYER; Radio failed to load, try again.", false);
                } catch (IOException e) {
                    PlayerUtils.tellPlayer("§4Error: IOEXC; Radio failed to load (Lag, radio down?)", false);
                }
            });
        } catch (Exception ignored) {
        }
    }
}