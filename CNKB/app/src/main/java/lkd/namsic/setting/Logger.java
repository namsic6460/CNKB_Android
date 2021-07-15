package lkd.namsic.setting;

import android.util.Log;
import android.view.View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lkd.namsic.game.config.Config;
import lkd.namsic.MainActivity;

public class Logger {

    private Logger() {}

    private static final int SAVE_COUNT = 1000;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm.ss.SSS", Locale.KOREA);
    public static volatile int logCount = 0;
    public static volatile String logs = "";

    public static synchronized void i(String title, String text) {
        if(title.equals("FileManager")) {
            if (Config.IGNORE_FILE_LOG) {
                return;
            }

            text = text.replaceFirst(FileManager.PATH, "");
        }

        log(title, text, "INFO");
        Log.i(title, text);
    }

    public static synchronized void w(String title, Throwable t) {
        w(title, Config.errorString(t));
    }

    public static synchronized void w(String title, String text) {
        log(title, text, "WARN");
        Log.w(title, text);
    }

    public static synchronized void e(String title, Throwable t) {
        e(title, Config.errorString(t));
        MainActivity.toast("ERROR!\n" + t.getMessage());
    }

    public static synchronized void e(String title, String errorString) {
        try {
            log(title, errorString, "ERR");
            Log.e(title, errorString);

            if(title.startsWith("FileManager(path : ")) {
                throw new RuntimeException(errorString);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }

        FileManager.save(FileManager.LOG_PATH + "/ERROR_" + getTimeFileName(), errorString);
    }

    private static synchronized void log(String title, String text, String prefix) {
        LocalDateTime time = LocalDateTime.now();
        String timeText = "\n[" + time.format(formatter) + "]\n";

        final String logText = timeText + "[" + prefix + "] : " + title + " - " + text + "\n";
        logs += logText;

        MainActivity.mainActivity.runOnUiThread(() -> {
            MainActivity.textView.append(logText);
            MainActivity.scrollView.fullScroll(View.FOCUS_DOWN);
        });

        logCount++;

        if(logCount == SAVE_COUNT) {
            saveLog();
        }
    }

    public static void saveLog() {
        if (logCount != 0) {
            Thread thread = new Thread(() -> {
                String fileName = FileManager.LOG_PATH + "/Log - " + getTimeFileName();
                FileManager.save(fileName, logs);

                MainActivity.toast("로그 저장 완료 - " + fileName);

                logCount = 0;
                logs = "";
            });

            MainActivity.startThread(thread);
        }
    }

    private static String getTimeFileName() {
        return LocalDateTime.now().format(formatter) + ".txt";
    }

}
