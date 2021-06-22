package lkd.namsic.game.gameObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.base.LimitInteger;
import lkd.namsic.game.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.NumberRangeException;
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

    final LimitInteger handleLv = new LimitInteger(Config.MIN_HANDLE_LV, Config.MIN_HANDLE_LV, Config.MAX_HANDLE_LV);

    final Set<Map<Long, Integer>> recipe = new LinkedHashSet<>();

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
            return buff.getOrDefault(statType, 0);
        }
    }

    public void addEatBuff(long time, @NonNull StatType statType, int stat) {
        this.setEatBuff(time, statType, this.getEatBuff(time, statType) + stat);
    }

    public void addRecipe(@NonNull Map<Long, Integer> recipe) {
        this.addRecipe(recipe, false);
    }

    public void addRecipe(@NonNull Map<Long, Integer> recipe, boolean skip) {
        long key;
        int value;
        for(Map.Entry<Long, Integer> entry : recipe.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();

            if(!skip && key != 0L) {
                try {
                    Config.checkId(Id.ITEM, key);
                } catch (NumberRangeException e) {
                    Config.checkId(Id.EQUIPMENT, key);
                }
            }

            if(value < 1) {
                throw new NumberRangeException(value, 1, Integer.MAX_VALUE);
            }
        }

        this.recipe.add(recipe);
    }

}
