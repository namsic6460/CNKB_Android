package lkd.namsic.game.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.game.base.EquipUse;
import lkd.namsic.game.base.LimitDouble;
import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.base.Use;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.event.Event;
import lkd.namsic.game.manager.EquipManager;
import lkd.namsic.game.object.implement.EquipEvents;
import lkd.namsic.game.object.implement.EquipUses;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Equipment extends Item implements Cloneable {

    final long originalId;

    @Setter
    @NonNull
    EquipType equipType;

    final LimitInteger handleLv = new LimitInteger(Config.MIN_HANDLE_LV, Config.MIN_HANDLE_LV, Config.MAX_HANDLE_LV);

    final LimitInteger reinforceCount = new LimitInteger(0, 0, Config.MAX_REINFORCE_COUNT);
    final LimitDouble reinforceFloor1 = new LimitDouble(0, 0D, null);
    final LimitInteger reinforceFloor2 = new LimitInteger(0, 0, null);
    final LimitInteger limitLv = new LimitInteger(Config.MIN_LV, Config.MIN_LV, null);

    @Setter
    int lvDown = 0;

    final Map<StatType, Integer> limitStat = new HashMap<>();
    final Map<StatType, Integer> basicStat = new HashMap<>();
    final Map<StatType, Integer> reinforceStat = new HashMap<>();

    public Equipment(@NonNull EquipType equipType, @NonNull EquipList equipData, @NonNull String description) {
        super(equipData.getDisplayName());
        this.description = description;

        this.id.setId(Id.EQUIPMENT);

        this.id.setObjectId(equipData.getId());
        this.originalId = equipData.getId();

        this.description = description;
        this.equipType = equipType;
    }

    public boolean revalidateStat(boolean checkBasic, @Nullable Entity entity) {
        boolean available = true;

        Equipment equipment = Config.loadObject(Id.EQUIPMENT, this.originalId);

        if(checkBasic) {
            assert entity != null;

            this.limitStat.clear();
            this.limitStat.putAll(equipment.limitStat);

            if(entity.getId().getId().equals(Id.PLAYER) && entity.isEquipped(equipType, this.id.getObjectId())) {
                available = EquipManager.getInstance().canEquip((Player) entity, this.id.getObjectId());
            }

            this.basicStat.clear();
            this.basicStat.putAll(equipment.basicStat);
        }

        this.handleLv.set(equipment.handleLv.get());
        this.limitLv.set(equipment.limitLv.get());

        double totalIncrease = 0;
        double statIncrease = Config.REINFORCE_EFFICIENCY + Config.REINFORCE_EFFICIENCY_PER_HANDLE_LV * this.handleLv.get();
        double newStatIncrease = statIncrease;
        for(int i = 0; i < this.reinforceCount.get(); i++) {
            this.limitLv.add(RandomList.REINFORCE_LV_INCREASE[this.handleLv.get() - 1][i]);

            totalIncrease += newStatIncrease;
            newStatIncrease *= 1 + statIncrease;
        }

        int stat;
        for(Map.Entry<StatType, Integer> entry : this.basicStat.entrySet()) {
            stat = entry.getValue();

            if(stat > 0) {
                this.setReinforceStat(entry.getKey(), (int) (stat * totalIncrease));
            }
        }

        Config.unloadObject(equipment);
        return available;
    }

    public double getReinforcePercent(double multiplier) {
        if(this.reinforceCount.get() == Config.MAX_REINFORCE_COUNT) {
            return 0;
        }

        double basicPercent = RandomList.REINFORCE_PERCENT[this.handleLv.get() - 1][this.reinforceCount.get()];
        double floor1 = this.reinforceFloor1.get();
        int floor2 = this.reinforceFloor2.get();

        if(floor1 >= 1 || floor2 >= 200) {
            return 1;
        }

        return Math.min(1, basicPercent * multiplier * (1 + floor1 + 0.1 * Math.min(10, floor2)));
    }

    public long getReinforceItem() {
        return ItemList.LOW_REINFORCE_STONE.getId() + this.reinforceCount.get() / 5;
    }

    public long getReinforceCost() {
        return (long) ((Config.REINFORCE_BASE_COST + Config.REINFORCE_COST_PER_HANDLE_LV * this.handleLv.get()) *
                        (1 + Config.REINFORCE_COST_MULTIPLIER * this.reinforceCount.get()));
    }

    public int getTotalLimitLv() {
        return Math.min(Math.max(this.limitLv.get() - this.lvDown, Config.MIN_LV), Config.MAX_LV);
    }

    @Deprecated
    @Nullable
    @Override
    public Use getUse() {
        throw new RuntimeException("Cannot get use in Equipment");
    }

    @Nullable
    public EquipUse getEquipUse() {
        return EquipUses.EQUIP_USE_MAP.get(this.originalId);
    }

    @Nullable
    public Map<String, Event> getEvent() {
        return EquipEvents.EVENT_MAP.get(this.originalId);
    }

    public void addLvDown(int lvDown) {
        this.setLvDown(this.getLvDown() + lvDown);
    }

    public Equipment setBasicStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat == 0) {
            this.basicStat.remove(statType);
        } else {
            this.basicStat.put(statType, stat);
        }

        return this;
    }

    public int getBasicStat(@NonNull StatType statType) {
        Config.checkStatType(statType);
        return this.basicStat.getOrDefault(statType, 0);
    }

    public Equipment addBasicStat(@NonNull StatType statType, int stat) {
        return this.setBasicStat(statType, this.getBasicStat(statType) + stat);
    }

    public Equipment setReinforceStat(@NonNull StatType statType, int stat) {
        Config.checkStatType(statType);

        if(stat == 0) {
            this.reinforceStat.remove(statType);
        } else {
            this.reinforceStat.put(statType, stat);
        }

        return this;
    }

    public int getReinforceStat(@NonNull StatType statType) {
        Config.checkStatType(statType);
        return this.reinforceStat.getOrDefault(statType, 0);
    }

    public int getStat(@NonNull StatType statType) {
        Config.checkStatType(statType);
        return this.getBasicStat(statType) + this.getReinforceStat(statType);
    }

    public void successReinforce() {
        this.reinforceFloor1.set(0D);
        this.reinforceFloor2.set(0);
        this.reinforceCount.add(1);

        this.revalidateStat(false, null);
    }

    public void failReinforce(double percent) {
        this.reinforceFloor1.add(percent / 10);
        this.reinforceFloor2.add(1);
    }

    @NonNull
    @Override
    public String getName() {
        return super.getName() + " (+" + this.reinforceCount.get() + ")";
    }

}
