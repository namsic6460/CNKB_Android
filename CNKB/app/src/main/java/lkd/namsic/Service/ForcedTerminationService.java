package lkd.namsic.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;

import lkd.namsic.Setting.Logger;
import lkd.namsic.MainActivity;

public class ForcedTerminationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        MainActivity.toast("Stopping...");
        Logger.saveLog(LocalDateTime.now().withDayOfMonth(Logger.lastLogDay));

        MainActivity.threadCleaner.cancel();
        for(Thread thread : MainActivity.chatThreads) {
            if (thread.isAlive()) {
                thread.interrupt();
                try {
                    thread.join(1000);
                } catch (InterruptedException ignored) {}
            }
        }

        MainActivity.chatThreads.clear();

        MainActivity.toast("Stopped");
        stopSelf();
    }
}
