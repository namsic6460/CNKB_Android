package lkd.namsic.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import lkd.namsic.MainActivity;

public class ForcedTerminationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }
    
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        MainActivity.endProgram();
        this.stopSelf();
    }

}
