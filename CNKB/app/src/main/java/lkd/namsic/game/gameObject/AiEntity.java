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
import lombok.Getter;

@Getter
public class AiEntity extends Entity {

    @NonNull
    final Map<EquipType, Double> dropPercent = new ConcurrentHashMap<>();

    public AiEntity(@NonNull String name) {
        super(name);
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
    public void onKill(@NonNull Entity entity) {
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
            throw new NumberRangeException(percent, 0D, 1D);
        }

        this.dropPercent.put(equipType, percent);
    }

    public double getDropPercent(EquipType equipType) {
        return this.dropPercent.getOrDefault(equipType, 0D);
    }

    public void addDropPercent(EquipType equipType, double percent) {
        this.setDropPercent(equipType, this.getDropPercent(equipType) + percent);
    }

}
