package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;

import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Setting.FileManager;

public class Achieve implements GameClass {

    @Override
    @NonNull
    public String getPath() {
        return FileManager.DATA_PATH_MAP.get(Id.ACHIEVE) + this.id + ".txt";
    }

}
