package lkd.namsic.game.object;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import lkd.namsic.game.base.EquipUse;
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

    @Nullable
    final String activeDes;

    @Nullable
    final String passiveDes;

    @Setter
    @NonNull
    EquipType equipType;

    @Setter
    int handleLv = 1;

    @Setter
    int reinforceCount = 0;

    @Setter
    double reinforceFloor1 = 0;

    @Setter
    int reinforceFloor2 = 0;

    @Setter
    int limitLv = 1;

    @Setter
    int lvDown = 0;

    final Map<StatType, Integer> limitStat = new HashMap<>();
    final Map<StatType, Integer> basicStat = new HashMap<>();
    final Map<StatType, Integer> reinforceStat = new HashMap<>();

    public Equipment(@NonNull EquipType equipType, @NonNull EquipList equipData, @Nullable String activeDes, @Nullable String passiveDes) {
        super(equipData.getDisplayName());

        this.id.setId(Id.EQUIPMENT);

        this.id.setObjectId(equipData.getId());
        this.originalId = equipData.getId();

        this.activeDes = activeDes;
        this.passiveDes = passiveDes;
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

        this.name = equipment.getRealName();
        this.handleLv = equipment.handleLv;
        this.limitLv = equipment.limitLv;

        double totalIncrease = 0;
        double statIncrease = Config.REINFORCE_EFFICIENCY + Config.REINFORCE_EFFICIENCY_PER_HANDLE_LV * this.handleLv;
        double newStatIncrease = statIncrease;
        for(int i = 0; i < this.reinforceCount; i++) {
            this.limitLv += RandomList.REINFORCE_LV_INCREASE[this.handleLv - 1][i];

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
        if(this.reinforceCount == Config.MAX_REINFORCE_COUNT) {
            return 0;
        }

        double basicPercent = RandomList.REINFORCE_PERCENT[this.handleLv - 1][this.reinforceCount];
        int floor2 = this.reinforceFloor2;

        if(this.reinforceFloor1 >= 1 || floor2 >= 200) {
            return 1;
        }

        return Math.min(1, basicPercent * multiplier * (1 + this.reinforceFloor1 + 0.1 * Math.min(10, floor2)));
    }

    public long getReinforceItem() {
        return ItemList.LOW_REINFORCE_STONE.getId() + this.reinforceCount / 5;
    }

    public long getReinforceCost() {
        return this.getReinforceCost(this.reinforceCount);
    }

    public long getReinforceCost(int reinforceCount) {
        return (long) ((Config.REINFORCE_BASE_COST + Config.REINFORCE_COST_PER_HANDLE_LV * this.handleLv) *
                (1 + Config.REINFORCE_COST_MULTIPLIER * reinforceCount));
    }

    public int getTotalLimitLv() {
        return Math.min(Math.max(this.limitLv - this.lvDown, 1), Config.MAX_LV);
    }

    @Deprecated
    @Nullable
    @Override
    public Use getUse() {
        throw new RuntimeException("Cannot get use in Equipment");
    }

    @Nullable
    public EquipUse getEquipUse() {
        return EquipUses.MAP.get(this.originalId);
    }

    @Nullable
    public Map<String, Event> getEvent() {
        return EquipEvents.MAP.get(this.originalId);
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
        this.reinforceFloor1 = 0;
        this.reinforceFloor2 = 0;
        this.reinforceCount += 1;

        this.revalidateStat(false, null);
    }

    public void failReinforce(double percent) {
        this.reinforceFloor1 += percent / 10;
        this.reinforceFloor2 += 1;
    }

    @NonNull
    @Override
    public String getName() {
        return super.getName() + " (+" + this.reinforceCount + ")";
    }

    @NonNull
    public String getRealName() {
        return super.getName();
    }

    @Deprecated
    @Override
    public void setDescription(@NonNull String description) {
        throw new RuntimeException("Deprecated");
    }

    @Deprecated
    @NonNull
    @Override
    public String getDescription() {
        throw new RuntimeException("Deprecated");
    }
}
