package plusm.vilonix.gapi.utils.core;

import java.text.SimpleDateFormat;
import java.util.Date;

//todo переписать
public class RestartServer extends Thread {

    private final String time;

    public RestartServer(String restart) {
        start();
        this.time = restart;
    }

    private String getCurrentTimeStamp() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public void run() {
        while (true) {
            if ((time + ":00").contains(getCurrentTimeStamp())) {
                System.exit(0);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
