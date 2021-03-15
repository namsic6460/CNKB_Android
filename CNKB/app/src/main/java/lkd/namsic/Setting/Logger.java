package lkd.namsic.Setting;

import android.annotation.SuppressLint;
import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lkd.namsic.MainActivity;

public class Logger {

    private Logger() {}

    public static Integer lastLogDay = null;

    private static final int SAVE_COUNT = 100;
    private static int logCount = 0;
    private static String logs = "";

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
        StringBuilder builder = new StringBuilder(e.getClass().getName());
        builder.append(": ");
        builder.append(e.getMessage());
        builder.append("\n");
        for(StackTraceElement element : e.getStackTrace()) {
           builder.append("\tat");
           builder.append(element.toString());
        }

        log(title, builder.toString(), "ERR");
        Log.e(title, builder.toString());
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
        String fileName = FileManager.LOG_PATH + "/Log - " + date.format(DateTimeFormatter.BASIC_ISO_DATE) + ".txt";

        FileManager.append(fileName, logs);
        logCount = 0;
        logs = "";

        MainActivity.toast("로그 저장 완료 - " + fileName);
    }

}
