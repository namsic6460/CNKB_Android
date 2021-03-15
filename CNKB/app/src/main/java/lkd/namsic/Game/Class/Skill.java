package lkd.namsic.Game.Class;

import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Setting.FileManager;

public class Skill implements GameClass {

    @Override
    public String getPath() {
        return FileManager.DATA_PATH_MAP.get(Id.SKILL) + this.id + ".txt";
    }

}
