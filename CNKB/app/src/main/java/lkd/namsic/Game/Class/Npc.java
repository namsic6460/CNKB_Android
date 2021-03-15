package lkd.namsic.Game.Class;

import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Setting.FileManager;

public class Npc implements Entity {

    @Override
    public String getPath() {
        return FileManager.DATA_PATH_MAP.get(Id.NPC) + this.id + ".txt";
    }

}
