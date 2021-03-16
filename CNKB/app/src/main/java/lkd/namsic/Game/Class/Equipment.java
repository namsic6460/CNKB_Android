package lkd.namsic.Game.Class;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.EquipType;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Setting.FileManager;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Equipment extends Item {

    @Setter
    EquipType equipType;

    LimitInteger reinforceCount;
    LimitInteger limitLv;
    LimitInteger lvDown;

    Map<StatType, Integer> limitStat;
    Map<StatType, Integer> stat; //basic + reinforce
    Map<StatType, Integer> reinforceStat;

    public Equipment(EquipType equipType, String name) {
        new Equipment(equipType, name, "", 1);
    }

    public Equipment(EquipType equipType, String name, String description, int handleLv) {
        new Equipment(equipType, name, description, handleLv, null,
                new ArrayList<Map<Long, Integer>>());
    }

    public Equipment(EquipType equipType, String name, String description, int handleLv, Use use,
                List<Map<Long, Integer>> recipe) {
        new Equipment(equipType, name, description, handleLv, use, recipe, 0, 0, 0, 0,
                new HashMap<StatType, Integer>(), new HashMap<StatType, Integer>(),
                new HashMap<StatType, Integer>());
    }

    public Equipment(EquipType equipType, String name, String description, int handleLv, Use use,
                     List<Map<Long, Integer>> recipe, int reinforceCount, int maxReinforceCount,
                     int limitLv, int lvDown, Map<StatType, Integer> limitStat,
                     Map<StatType, Integer> stat, Map<StatType, Integer> reinforceStat) {
        super(name, description, handleLv, use, recipe);

        this.equipType = equipType;
        this.reinforceCount = new LimitInteger(reinforceCount, Config.MIN_REINFORCE_COUNT, maxReinforceCount);
    }

    @Override
    public String getPath() {
        return FileManager.DATA_PATH_MAP.get(Id.EQUIPMENT) + this.id + ".txt";
    }

}
