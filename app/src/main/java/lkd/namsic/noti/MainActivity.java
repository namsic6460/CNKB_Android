package lkd.namsic.noti;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import java.util.concurrent.atomic.AtomicBoolean;

import lkd.namsic.noti.service.ForcedTerminationService;
import lkd.namsic.noti.service.NotificationListener;

public class MainActivity extends AppCompatActivity {

    public static final AtomicBoolean isOn = new AtomicBoolean(true);

    @SuppressLint("StaticFieldLeak")
    public static MainActivity mainActivity;

    public TextView textView;

    private SwitchCompat switchBtn;
    private Intent forceTerminateIntent;
    private Intent notificationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE,
        }, 1);

        this.setContentView(R.layout.activity_main);

        this.textView = this.findViewById(R.id.text);
        this.textView.setText(null);
        this.textView.setMovementMethod(new ScrollingMovementMethod());
        this.textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        this.forceTerminateIntent = new Intent(this, ForcedTerminationService.class);
        this.startService(this.forceTerminateIntent);

        this.notificationIntent = new Intent(this, NotificationListener.class);
        this.startService(this.notificationIntent);

        this.switchBtn = this.findViewById(R.id.switchBtn);
        this.switchBtn.setChecked(isOn.get());

        this.getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                endProgram();
            }
        });
    }

    public static void endProgram() {
        toast("Stopping...");

        mainActivity.stopService(mainActivity.forceTerminateIntent);
        mainActivity.stopService(mainActivity.notificationIntent);
        mainActivity.forceTerminateIntent = null;
        mainActivity.notificationIntent = null;

        toast("Stopped");
    }

    public void onSwitchClick(View view) {
        isOn.set(this.switchBtn.isChecked());

        if(isOn.get()) {
            toast("스위치가 켜졌습니다");
        } else {
            toast("스위치가 꺼졌습니다");
        }
    }

    public void onRefreshClick(View view) {
        toast("Refresh");

        if (mainActivity.forceTerminateIntent != null) {
            this.stopService(mainActivity.forceTerminateIntent);
        }

        if (mainActivity.notificationIntent == null) {
            this.stopService(mainActivity.forceTerminateIntent);
        }

        this.forceTerminateIntent = new Intent(this, ForcedTerminationService.class);
        this.notificationIntent = new Intent(this, NotificationListener.class);
        this.startService(this.forceTerminateIntent);
        this.startService(this.notificationIntent);
    }

    public static void toast(final String msg) {
        if(mainActivity != null) {
            mainActivity.runOnUiThread(() -> {
                Toast toast = Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT);
                toast.show();
            });
        }
    }
}