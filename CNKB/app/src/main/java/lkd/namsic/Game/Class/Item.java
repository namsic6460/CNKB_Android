package lkd.namsic.Game.Class;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    List<Map<Long, Integer>> recipe;

    protected Item() {}

    public Item(@NonNull String name) {
        new Item(name, "", Config.MIN_HANDLE_LV);
    }

    public Item(@NonNull String name, @NonNull String description, int handleLv) {
        new Item(name, description, handleLv, null, new ArrayList<Map<Long, Integer>>());
    }

    public Item(@NonNull String name, @NonNull String description, int handleLv,
                @Nullable Use use, @NonNull List<Map<Long, Integer>> recipe) {
        this.id.setId(Id.ITEM);

        this.name = name;
        this.description = description;
        this.handleLv.set(handleLv);
        this.use = use;
        this.addRecipe(recipe);
    }

    public void addRecipe(@NonNull List<Map<Long, Integer>> recipe) {
        List<Map<Long, Integer>> copy = new ArrayList<>(recipe);

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

        for(Map<Long, Integer> anotherRecipe : this.recipe) {
            if(anotherRecipe.equals(recipe)) {
                throw new ListAddFailedException("Recipe already exists - " + recipe.toString());
            }
        }

        this.recipe.add(recipe);
    }

}
