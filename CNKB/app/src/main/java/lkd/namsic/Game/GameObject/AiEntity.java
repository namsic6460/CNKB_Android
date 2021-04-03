package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Doing;

public class AiEntity extends Entity {

    public AiEntity(@NonNull String name) {
        super(name);
    }

    @Override
    public void death() {
        super.death();

        MapClass map = Config.loadMap(this.location.getX().get(), this.location.getY().get());
        map.removeEntity(this.id.getId(), this.id.getObjectId());
        Config.unloadMap(map);

        this.dropMoney(this.getMoney());

        Map<Long, Integer> itemCopy = new HashMap<>(this.inventory);
        for(Map.Entry<Long, Integer> entry : itemCopy.entrySet()) {
            this.dropItem(entry.getKey(), entry.getValue());
        }

        Set<Long> equipCopy = new HashSet<>(this.equipInventory);
        for(Long equipId : equipCopy) {
            this.dropEquip(equipId);
        }
    }

    @Override
    public boolean canFight(@NonNull Entity enemy) {
        boolean flag = super.canFight(enemy);

        if(flag) {
            List<Doing> doingList;

            if(enemy instanceof Player) {
                doingList = Doing.playerNonFightList();
            } else {
                doingList = Doing.nonFightList();
            }

            return !doingList.contains(enemy.getDoing());
        } else {
            return false;
        }
    }

}
