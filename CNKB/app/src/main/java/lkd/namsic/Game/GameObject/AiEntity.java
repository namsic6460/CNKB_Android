package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Doing;
import lkd.namsic.Game.Enum.EquipType;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lkd.namsic.Game.Exception.UnhandledEnumException;

public class AiEntity extends Entity {

    @NonNull
    Map<EquipType, Double> dropPercent = new ConcurrentHashMap<>();

    public AiEntity(@NonNull String name) {
        super(name);
    }

    @Override
    public void onDeath() {
        this.endFight();

        MapClass map = null;

        try {
            map = Config.loadMap(this.location.getX().get(), this.location.getY().get());
            map.removeEntity(this);
        } finally {
            if(map != null) {
                Config.unloadMap(map);
            }
        }

        this.dropMoney(this.getMoney());

        Map<Long, Integer> itemCopy = new HashMap<>(this.inventory);
        for(Map.Entry<Long, Integer> entry : itemCopy.entrySet()) {
            this.dropItem(entry.getKey(), entry.getValue());
        }

        Set<Long> equipCopy = new HashSet<>(this.equipInventory);
        for(Long equipId : equipCopy) {
            this.dropEquip(equipId);
        }

        Config.deleteAiEntity(this);
    }

    @Override
    public void onKill(Entity entity) {
        long gap = this.lv.get() - entity.lv.get();

        if(gap <= 20) {
            this.lv.add(1);

            this.revalidateStat();

            this.setSkipRevalidate(true);

            this.setStat(StatType.HP, (int) (this.getStat(StatType.MAXHP) * 0.1));
            this.setStat(StatType.MN, (int) (this.getStat(StatType.MAXMN) * 0.5));

            for(StatType statType : StatType.values()) {
                try {
                    Config.checkStatType(statType);
                } catch (UnhandledEnumException ignore) {}

                this.setBasicStat(statType, (int) (this.getStat(statType) * 0.05));
            }

            this.setSkipRevalidate(false);
        }
    }

    @Override
    public boolean canFight(@NonNull Entity enemy) {
        List<Doing> doingList = Doing.fightList();

        if(doingList.contains(this.doing)) {
            return doingList.contains(enemy.getDoing());
        } else {
            return false;
        }
    }

    @NonNull
    @Override
    public EquipType equip(long equipId) {
        return this.equip(equipId, 1);
    }

    @NonNull
    public EquipType equip(long equipId, double dropPercent) {
        EquipType equipType = super.equip(equipId);
        this.setDropPercent(equipType, dropPercent);

        return equipType;
    }

    public void setDropPercent(EquipType equipType, double percent) {
        if(percent < 0 || percent > 1) {
            throw new NumberRangeException(percent, 0, 1);
        }

        this.dropPercent.put(equipType, percent);
    }

    public double getDropPercent(EquipType equipType) {
        Double value = this.dropPercent.get(equipType);

        if(value != null) {
            return value;
        } else {
            return 1;
        }
    }

}
