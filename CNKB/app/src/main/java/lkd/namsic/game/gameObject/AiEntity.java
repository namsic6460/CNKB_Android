package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.Config;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.UnhandledEnumException;

public class AiEntity extends Entity {

    @NonNull
    final Map<EquipType, Double> dropPercent = new ConcurrentHashMap<>();

    @NonNull
    final Map<StatType, Integer> increaseStat = new ConcurrentHashMap<>();

    public AiEntity(@NonNull String name) {
        super(name);

        for(StatType statType : StatType.values()) {
            try {
                Config.checkStatType(statType);
            } catch (UnhandledEnumException e) {
                continue;
            }

            this.setIncreaseStat(statType, (int) (this.getIncreaseStat(statType) * Config.BASE_INCREASE_PERCENT));
        }
    }

    @Override
    public void onDeath() {
        this.endFight();

        MapClass map = null;

        try {
            map = Config.loadMap(this.location);
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

        this.revalidateBuff();
        entity.revalidateBuff();

        if(gap <= 20) {
            this.lv.add(1);

            this.setBasicStat(StatType.HP, (int) (this.getStat(StatType.MAXHP) * 0.1));
            this.setBasicStat(StatType.MN, (int) (this.getStat(StatType.MAXMN) * 0.5));

            for(StatType statType : StatType.values()) {
                try {
                    Config.checkStatType(statType);
                } catch (UnhandledEnumException ignore) {}

                this.setBasicStat(statType, (int) (this.getStat(statType) * 0.05));
            }
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

    public void addDropPercent(EquipType equipType, double percent) {
        this.setDropPercent(equipType, this.getDropPercent(equipType) + percent);
    }

    public void setIncreaseStat(StatType statType, int stat) {
        Config.checkStatType(statType);

        this.increaseStat.put(statType, stat);
    }

    public int getIncreaseStat(StatType statType) {
        Integer value = this.increaseStat.get(statType);

        if(value != null) {
            return value;
        } else {
            return 0;
        }
    }

    public void addIncreaseStat(StatType statType, int stat) {
        this.setIncreaseStat(statType, this.getIncreaseStat(statType) + stat);
    }

}
