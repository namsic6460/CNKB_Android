package lkd.namsic.Game.Class;

import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Setting.FileManager;

public class Player implements Entity {

    @Override
    public String getPath() {
        return FileManager.DATA_PATH_MAP.get(Id.PLAYER) + this.id + ".txt";
    }

}
