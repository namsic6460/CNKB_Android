package lkd.namsic.Game.GameObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Item extends NamedObject {

    @Setter
    @NonNull
    String description;

    @Setter
    @Nullable
    Use use = null;

    @Setter
    private boolean isFood = false;

    private final Map<Long, HashMap<StatType, Integer>> eatBuff = new HashMap<>();

    LimitInteger handleLv = new LimitInteger(Config.MIN_HANDLE_LV, Config.MIN_HANDLE_LV, Config.MAX_HANDLE_LV);

    Set<Map<Long, Integer>> recipe = new HashSet<>();

    public Item(@NonNull String name, @NonNull String description) {
        super(name);
        this.id.setId(Id.ITEM);

        this.description = description;
    }

    public void setEatBuff(long time, @NonNull StatType statType, int stat) {
        Config.checkStatType(statType);
        if(time < 1) {
            throw new NumberRangeException(time, 1, Long.MAX_VALUE);
        }

        HashMap<StatType, Integer> buff = this.eatBuff.get(time);

        if(buff == null) {
            if(stat == 0) {
                return;
            }

            buff = new HashMap<>();
            buff.put(statType, stat);
            this.eatBuff.put(time, buff);
        } else {
            if(stat == 0) {
                if(buff.size() == 1) {
                    this.eatBuff.remove(time);
                } else {
                    buff.remove(statType);
                }
            } else {
                buff.put(statType, stat);
            }
        }
    }

    public int getEatBuff(long time, @NonNull StatType statType) {
        Map<StatType, Integer> buff = this.eatBuff.get(time);

        if(buff == null) {
            return 0;
        } else {
            Integer value = buff.get(statType);

            if(value != null) {
                return value;
            } else {
                return 0;
            }
        }
    }

    public void addEatBuff(long time, @NonNull StatType statType, int stat) {
        this.setEatBuff(time, statType, this.getEatBuff(time, statType) + stat);
    }

    public void addRecipe(@NonNull Map<Long, Integer> recipe) {
        long key;
        int value;
        for(Map.Entry<Long, Integer> entry : recipe.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();

            try {
                Config.checkId(Id.ITEM, key);
            } catch (NumberRangeException e) {
                Config.checkId(Id.EQUIPMENT, key);
            }

            if(value < 1) {
                throw new NumberRangeException(value, 1, Integer.MAX_VALUE);
            }
        }

        this.recipe.add(recipe);
    }

}