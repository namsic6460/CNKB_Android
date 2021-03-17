package lkd.namsic.Setting;

import android.annotation.SuppressLint;
import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lkd.namsic.Game.Config;
import lkd.namsic.MainActivity;

public class Logger {

    private Logger() {}

    public static Integer lastLogDay = null;

    private static final int SAVE_COUNT = 100;
    static int logCount = 0;
    static String logs = "";

    @SuppressLint("StaticFieldLeak")
    public static MainActivity activity;

    public static void i(String title, String text) {
        log(title, text, "INFO");
        Log.i(title, text);
    }

    public static void w(String title, String text) {
        log(title, text, "WARN");
        Log.w(title, text);
    }

    public static void e(String title, Throwable e) {
        String errorString = Config.errorString(e);
        log(title, errorString, "ERR");
        Log.e(title, errorString);

        MainActivity.toast("ERROR!\n" + e.getMessage());
    }

    private static void log(String title, String text, String prefix) {
        LocalDateTime time = LocalDateTime.now();
        if(lastLogDay != null) {
            if(lastLogDay != time.getDayOfMonth()) {
                saveLog(time.plusDays(lastLogDay - time.getDayOfMonth()));
            }
        }

        String timeText = "\n[" + time.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")) + "]\n";

        final String logText = timeText + "[" + prefix + "] : " + title + " - " + text + "\n";
        logs += logText;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.textView.append(logText);
            }
        });

        logCount++;
        lastLogDay = time.getDayOfMonth();

        if(logCount == SAVE_COUNT) {
            saveLog(time);
        }
    }

    public static void saveLog(LocalDateTime date) {
        if(!logs.equals("")) {
            String fileName = FileManager.LOG_PATH + "/Log - " + date.format(DateTimeFormatter.BASIC_ISO_DATE) + ".txt";
            FileManager.append(fileName, logs);

            MainActivity.toast("로그 저장 완료 - " + fileName);
        }

        logCount = 0;
        logs = "";
    }

}
