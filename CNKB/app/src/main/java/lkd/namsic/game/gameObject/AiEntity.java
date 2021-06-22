package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.UnhandledEnumException;
import lombok.Getter;

public abstract class AiEntity extends Entity {

    @Getter
    @NonNull
    final LimitInteger maxIncrease = new LimitInteger(Config.MIN_AI_INCREASE, Config.MIN_AI_INCREASE, null);

    private int currentIncrease = 0;

    @Getter
    @NonNull
    final Map<EquipType, Double> dropPercent = new ConcurrentHashMap<>();

    public AiEntity(@NonNull String name) {
        super(name);
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

    @Override
    public void onDeath() {
        MapClass map = null;

        try {
            map = Config.loadMap(this.location);
            map.removeEntity(this);
        } finally {
            if(map != null) {
                Config.unloadMap(map);
            }
        }

        if(this.getMoney() > 0L) {
            this.dropMoney(this.getMoney());
        }

        Map<Long, Integer> itemCopy = new HashMap<>(this.inventory);
        for(Map.Entry<Long, Integer> entry : itemCopy.entrySet()) {
            this.dropItem(entry.getKey(), entry.getValue());
        }

        Equipment equip;
        Random random = new Random();
        Set<Long> equipCopy = new HashSet<>(this.equipInventory);
        for(Long equipId : equipCopy) {
            equip = Config.getData(Id.EQUIPMENT, equipId);
            double dropPercent = this.getDropPercent(equip.getEquipType());

            if(random.nextDouble() < dropPercent) {
                this.dropEquip(equipId);
            }
        }

        Config.deleteAiEntity(this);
    }

    @Override
    public void onKill(@NonNull Entity entity) {
        long gap = this.lv.get() - entity.lv.get();

        this.revalidateBuff();
        entity.revalidateBuff();

        if(gap <= 20 && currentIncrease <= maxIncrease.get()) {
            this.lv.add(1);
            currentIncrease++;

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

}
