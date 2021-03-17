package lkd.namsic.Game.Exception;

import androidx.annotation.NonNull;

import java.util.Map;

import lkd.namsic.Game.Config;

public class MapSetterException extends RuntimeException {

    public MapSetterException(@NonNull Map<?, ?> originalMap, @NonNull Map<?, ?> settingMap, @NonNull Throwable throwable) {
        super(Config.mapsToString(originalMap, settingMap) + "\n" + Config.errorString(throwable));
    }

}
