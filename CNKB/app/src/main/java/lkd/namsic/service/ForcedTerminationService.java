package lkd.namsic.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import lkd.namsic.game.Config;
import lkd.namsic.setting.Logger;
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
        Logger.saveLog();

        MainActivity.threadCleaner.cancel();
        Config.onTerminate();
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
        stopSelf();
    }
}
