package lkd.namsic.setting;

import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lkd.namsic.game.Config;
import lkd.namsic.MainActivity;

public class Logger {

    private Logger() {}

    private static final int SAVE_COUNT = 1000;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS", Locale.KOREA);
    static volatile int logCount = 0;
    static volatile String logs = "";

    public static synchronized void i(String title, String text) {
        log(title, text, "INFO");
        Log.i(title, text);
    }

    public static synchronized void w(String title, String text) {
        log(title, text, "WARN");
        Log.w(title, text);
    }

    public static synchronized void e(String title, Throwable e) {
        String errorString = Config.errorString(e);
        log(title, errorString, "ERR");
        Log.e(title, errorString);

        MainActivity.toast("ERROR!\n" + e.getMessage());
        FileManager.save("ERROR_" + getTimeFileName(), errorString);
    }

    private static synchronized void log(String title, String text, String prefix) {
        LocalDateTime time = LocalDateTime.now();
        String timeText = "\n[" + time.format(formatter) + "]\n";

        final String logText = timeText + "[" + prefix + "] : " + title + " - " + text + "\n";
        logs += logText;

        MainActivity.mainActivity.runOnUiThread(() -> MainActivity.textView.append(logText));

        logCount++;

        if(logCount == SAVE_COUNT) {
            saveLog(time);
        }
    }

    public static void saveLog(LocalDateTime date) {
        Thread thread = new Thread(() -> {
            if (!logs.equals("")) {
                String fileName = FileManager.LOG_PATH + "/Log - " + getTimeFileName();
                FileManager.save(fileName, logs);

                MainActivity.toast("로그 저장 완료 - " + fileName);
            }

            logCount = 0;
            logs = "";
        });

        MainActivity.startThread(thread);
    }

    private static String getTimeFileName() {
        return LocalDateTime.now().format(formatter) + ".txt";
    }

}
