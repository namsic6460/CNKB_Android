package lkd.namsic.Game;

import lkd.namsic.Game.Enum.EquipType;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.GameObject.Equipment;
import lkd.namsic.Setting.Logger;

public class ObjectMaker {

    public static void start() {
        Logger.i("ObjectMaker", "Start!");

        try {
            Equipment newEquipment = new Equipment(EquipType.GEM, "테스트 장비", "테스트용 장비입니다!");
            newEquipment.getId().setObjectId(1L);
            Config.unloadObject(newEquipment);

            Config.ID_COUNT.put(Id.EQUIPMENT, 2L);

            Logger.i("ObjectMaker", "Done!");
        } catch (Exception e) {
            Logger.e("ObjectMaker", e);
        }
    }

}
