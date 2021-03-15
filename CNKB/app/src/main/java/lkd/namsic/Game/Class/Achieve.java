package lkd.namsic.Game.Class;

import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Setting.FileManager;

public class Achieve extends Item {

    @Override
    public String getPath() {
        return FileManager.DATA_PATH_MAP.get(Id.ACHIEVE) + this.id + ".txt";
    }

}
