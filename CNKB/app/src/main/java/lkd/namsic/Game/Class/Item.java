package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Exception.ListAddFailedException;
import lkd.namsic.Game.Exception.NumberRangeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Item implements GameObject {

    @Setter
    @NonNull
    String name;

    @Setter
    @NonNull
    String description;

    @Setter
    @Nullable
    Use use;

    LimitInteger handleLv = new LimitInteger(Config.MIN_HANDLE_LV, Config.MIN_HANDLE_LV, Config.MAX_HANDLE_LV);

    Set<Map<Long, Integer>> recipe;

    protected Item() {}

    public Item(@NonNull String name, @NonNull String description, int handleLv,
                @Nullable Use use, @NonNull Set<Map<Long, Integer>> recipe) {
        this.id.setId(Id.ITEM);

        this.name = name;
        this.description = description;
        this.handleLv.set(handleLv);
        this.use = use;
        this.addRecipe(recipe);
    }

    public void addRecipe(@NonNull Set<Map<Long, Integer>> recipe) {
        Set<Map<Long, Integer>> copy = new HashSet<>(this.recipe);

        try {
            for (Map<Long, Integer> map : recipe) {
                this.addRecipe(map);
            }
        } catch (Exception e) {
            this.recipe = copy;
            throw e;
        }
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
                throw new ListAddFailedException("Recipe count cannot lower than 1 - " + entry.getKey() + ", " + value);
            }
        }

        this.recipe.add(recipe);
    }

}
