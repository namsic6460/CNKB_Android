package lkd.namsic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lkd.namsic.game.Config;
import lkd.namsic.game.ObjectMaker;
import lkd.namsic.game.base.ConcurrentArrayList;
import lkd.namsic.setting.FileManager;
import lkd.namsic.service.ForcedTerminationService;

public class MainActivity extends AppCompatActivity {

    public static final List<Thread> threads = new ConcurrentArrayList<>();
    public static Timer threadCleaner;

    @SuppressLint("StaticFieldLeak")
    public static MainActivity mainActivity;

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public static Boolean isOn = false;

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
                                 Manifest.permission.READ_EXTERNAL_STORAGE };

        List<String> requirePermissions = new ArrayList<>();
        for(String permission : permissions) {
            if(!hasPermission(this, permission)) {
                requirePermissions.add(permission);
            }
        }

        ActivityCompat.requestPermissions(this, requirePermissions.toArray(new String[3]), 1);
        if(!hasPermissions(permissions)) {
            ActivityCompat.finishAffinity(this);
        } else {
            String notiPermission = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
            if(notiPermission == null || !notiPermission.contains(this.getPackageName())) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
                ActivityCompat.finishAffinity(this);
            }
        }

        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        textView.setMovementMethod(new ScrollingMovementMethod());

        startService(new Intent(this, ForcedTerminationService.class));

        threadCount = findViewById(R.id.thread_count);

        context = this;
        switchBtn = findViewById(R.id.switchBtn);

        Config.init();
        FileManager.initDir();
        Config.loadPlayers();

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
        ObjectMaker.start();
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

    private boolean hasPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasPermissions(String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(this, permission)) {
                return false;
            }
        }

        return true;
    }

}