package space.coffos.lija.util.general.dynamic;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class DynamicRegedit {

    private static String REGQUERY_UTIL = "reg query ";
    private static String REGSTR_TOKEN = "REG_SZ";

    private static String DISKID_CMD = REGQUERY_UTIL +
            "\"HKEY_LOCAL_MACHINE\\HARDWARE\\DESCRIPTION\\System\\MultifunctionAdapter\\0\\DiskController\\0\\DiskPeripheral\\0\""
            + " /v Identifier";

    public static String getDISKID() {
        try {
            Process process = Runtime.getRuntime().exec(DISKID_CMD);
            StreamReader reader = new StreamReader(process.getInputStream());

            reader.start();
            process.waitFor();
            reader.join();

            String result = reader.getResult();
            int p = result.indexOf(REGSTR_TOKEN);

            if (p == -1)
                return null;

            return result.substring(p + REGSTR_TOKEN.length()).trim();
        } catch (Exception e) {
            return null;
        }
    }

    static class StreamReader extends Thread {
        private InputStream is;
        private StringWriter sw;

        StreamReader(InputStream is) {
            this.is = is;
            sw = new StringWriter();
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            } catch (IOException ignored) {
            }
        }

        String getResult() {
            return sw.toString();
        }
    }
}