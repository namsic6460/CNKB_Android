package lkd.namsic.game.object;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.UnhandledEnumException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class AiEntity extends Entity implements Cloneable {

    @Setter
    int maxIncrease = 0;

    @Getter(AccessLevel.NONE)
    private int currentIncrease = 0;

    final Map<Long, Double> itemDropPercent = new ConcurrentHashMap<>();
    final Map<Long, Integer> itemDropMinCount = new ConcurrentHashMap<>();
    final Map<EquipType, Double> equipDropPercent = new ConcurrentHashMap<>();

    public AiEntity(@NonNull String name) {
        super(name);
    }

    public void setItemDrop(long itemId, double percent, int minCount, int maxCount) {
        this.setItem(itemId, maxCount);
        this.setItemDropPercent(itemId, percent);

        if(minCount != 1) {
            this.setItemDropMinCount(itemId, minCount);
        }
    }

    public void setItemDropPercent(long itemId, double percent) {
        if(percent < 0 || percent > 1) {
            throw new NumberRangeException(percent, 0D, 1D);
        }

        this.itemDropPercent.put(itemId, percent);
    }

    public double getItemDropPercent(long itemId) {
        return this.itemDropPercent.getOrDefault(itemId, 0D);
    }

    public void setItemDropMinCount(long itemId, int minCount) {
        if(minCount < 1) {
            throw new NumberRangeException(minCount, 1);
        }

        this.itemDropMinCount.put(itemId, minCount);
    }

    public int getItemDropMinCount(long itemId) {
        return this.itemDropMinCount.getOrDefault(itemId, 1);
    }

    public void setEquipDropPercent(@NonNull EquipType equipType, double percent) {
        if(percent < 0 || percent > 1) {
            throw new NumberRangeException(percent, 0D, 1D);
        }

        this.equipDropPercent.put(equipType, percent);
    }

    public double getEquipDropPercent(@NonNull EquipType equipType) {
        return this.equipDropPercent.getOrDefault(equipType, 0D);
    }

    @Override
    public void onDeath() {
        super.onDeath();

        long money = this.getMoney();
        if(money > 0L) {
            this.dropMoney(money);
        }

        double dropPercent;
        Random random = new Random();

        double dropMultiplier = 1.0;

        assert this.getKiller() != null;
        Entity killer = Config.getData(this.getKiller().getId(), this.getKiller().getObjectId());

        long earringId = killer.getEquipped(EquipType.EARRINGS);
        if(earringId != EquipList.NONE.getId()) {
            Equipment earring = Config.getData(Id.EQUIPMENT, earringId);
            if(earring.getOriginalId() == EquipList.EMERALD_EARRING.getId()) {
                dropMultiplier = 1.15;
            }
        }

        long itemId;
        int minCount, count;
        for(Map.Entry<Long, Integer> entry : new HashMap<>(this.inventory).entrySet()) {
            itemId = entry.getKey();
            count = entry.getValue();

            dropPercent = this.getItemDropPercent(itemId) * dropMultiplier;

            if(random.nextDouble() < dropPercent) {
                minCount = this.getItemDropMinCount(itemId);
                this.addItem(itemId, minCount);
                this.dropItem(itemId, random.nextInt(count) + minCount);
            }
        }

        Equipment equip;
        for(Long equipId : new HashSet<>(this.equipInventory)) {
            equip = Config.getData(Id.EQUIPMENT, equipId);
            dropPercent = this.getEquipDropPercent(equip.getEquipType());

            if(random.nextDouble() < dropPercent) {
                Equipment newEquip = Config.newObject(Config.getData(Id.EQUIPMENT, equipId), true);
                long newEquipId = newEquip.getId().getObjectId();
                Config.unloadObject(newEquip);

                this.dropEquip(newEquipId);
            }
        }

        GameMap map = Config.loadMap(this.location);
        map.removeEntity(this);
        Config.unloadMap(map);

        Config.deleteAiEntity(this);
    }

    @Override
    public void onKill(@NonNull Entity entity) {
        long gap = this.lv - entity.lv;

        this.revalidateBuff();
        entity.revalidateBuff();

        if(gap <= 20 && currentIncrease <= maxIncrease) {
            this.lv += 1;
            currentIncrease++;

            this.addBasicStat(StatType.HP, (int) (this.getStat(StatType.MAXHP) * 0.1));
            this.addBasicStat(StatType.MN, (int) (this.getStat(StatType.MAXMN) * 0.5));

            for(StatType statType : StatType.values()) {
                try {
                    Config.checkStatType(statType);
                } catch (UnhandledEnumException e) {
                    continue;
                }

                this.addBasicStat(statType, (int) (this.getStat(statType) * 0.05));
            }

            this.setBasicStat(StatType.HP, this.getStat(StatType.MAXHP));
            this.setBasicStat(StatType.MN, this.getStat(StatType.MAXMN));
        }
    }

    @NonNull
    @Override
    public String getFightName() {
        return super.getName();
    }

}
