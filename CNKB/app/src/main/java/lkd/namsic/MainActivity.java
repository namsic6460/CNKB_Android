package lkd.namsic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.creator.ObjectCreator;
import lkd.namsic.game.base.ConcurrentArrayList;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.GameObject;
import lkd.namsic.service.ForcedTerminationService;
import lkd.namsic.setting.FileManager;
import lkd.namsic.setting.Logger;

public class MainActivity extends AppCompatActivity {

    public static final List<Thread> threads = new ConcurrentArrayList<>();
    public static Timer threadCleaner;

    @SuppressLint("StaticFieldLeak")
    public static MainActivity mainActivity;

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public static Boolean isOn = false;

    @SuppressLint("StaticFieldLeak")
    public static ScrollView scrollView;

    @SuppressLint("StaticFieldLeak")
    public static TextView textView;

    @SuppressLint("StaticFieldLeak")
    private static TextView threadCount;

    private SwitchCompat switchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                 Manifest.permission.READ_EXTERNAL_STORAGE,
                                 Manifest.permission.ACCESS_COARSE_LOCATION,
                                 Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                 Manifest.permission.ACCESS_FINE_LOCATION,
                                 Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE };
        ActivityCompat.requestPermissions(this, permissions, 1);

        if(Logger.logCount != 0) {
            Logger.saveLog();
        }

        setContentView(R.layout.activity_main);

        scrollView = findViewById(R.id.scroll);

        textView = findViewById(R.id.text);
        textView.setText(null);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        startService(new Intent(this, ForcedTerminationService.class));

        threadCount = findViewById(R.id.thread_count);

        context = this;
        switchBtn = findViewById(R.id.switchBtn);
        switchBtn.setChecked(isOn);

        Config.init();
        FileManager.initDir();
        Config.loadPlayers();

        KakaoTalk.registerCommands();

        threadCleaner = new Timer();
        threadCleaner.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int size = threads.size();

                if(size == 0) {
                    return;
                }

                List<Thread> deadThreads = new ArrayList<>();
                for(Thread thread : threads) {
                    if(thread.isInterrupted() || !thread.isAlive()) {
                        deadThreads.add(thread);
                    }
                }

                if(!deadThreads.isEmpty()) {
                    threads.removeAll(deadThreads);
                    mainActivity.runOnUiThread(() -> threadCount.setText(String.valueOf(size - deadThreads.size())));
                }
            }
        }, 0, 1000);

        Logger.logCount = 0;
        Logger.logs = "";
    }

    @Override
    public void onBackPressed() {
        endProgram();
        this.finish();
    }

    public static void endProgram() {
        MainActivity.toast("Stopping...");
        Logger.saveLog();

        MainActivity.threadCleaner.cancel();

        Config.IGNORE_FILE_LOG = true;
        Config.save();
        Config.IGNORE_FILE_LOG = false;

        GameObject object;
        for(Map.Entry<Id, ConcurrentHashMap<Long, Long>> entry : Config.OBJECT_COUNT.entrySet()) {
            for(Map.Entry<Long, Long> entry_ : entry.getValue().entrySet()) {
                object = Config.getData(entry.getKey(), entry_.getKey());

                for(long i = 0; i < entry_.getValue(); i++) {
                    Config.unloadObject(object);
                }
            }
        }

        GameMap map;
        for(Map.Entry<String, Long> entry : Config.MAP_COUNT.entrySet()) {
            map = Config.MAP.get(entry.getKey());

            for(long i = 0; i < entry.getValue(); i++) {
                Config.unloadMap(map);
            }
        }

        for(Thread thread : MainActivity.threads) {
            if (thread.isAlive()) {
                thread.interrupt();

                try {
                    thread.join(1000);
                } catch (InterruptedException ignored) {}
            }
        }

        MainActivity.threads.clear();
        MainActivity.threadCleaner.cancel();

        MainActivity.toast("Stopped");
    }

    public void onSwitchClick(View view) {
        isOn = switchBtn.isChecked();

        if(isOn) {
            toast("스위치가 켜졌습니다");
        } else {
            toast("스위치가 꺼졌습니다");
        }
    }

    public void onInitClick(View view) {
        ObjectCreator.start((Button) view);
    }

    public static void toast(final String msg) {
        if(mainActivity != null) {
            mainActivity.runOnUiThread(() -> {
                Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.show();
            });
        }
    }

    public static void startThread(Thread thread) {
        threads.add(thread);
        mainActivity.runOnUiThread(() -> threadCount.setText(String.valueOf(threads.size())));
        thread.start();
    }

}