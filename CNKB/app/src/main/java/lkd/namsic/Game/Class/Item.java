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
import lkd.namsic.Setting.FileManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item implements GameClass {

    @NonNull
    String name;

    @NonNull
    String description = "";

    @Nullable
    Use use = null;

    @Setter(AccessLevel.PRIVATE)
    LimitInteger handleLv = new LimitInteger(Config.MIN_HANDLE_LV, Config.MIN_HANDLE_LV, Config.MAX_HANDLE_LV);

    @Setter(AccessLevel.PRIVATE)
    List<Map<Long, Integer>> recipe = new ArrayList<>();

    protected Item() {}

    public Item(@NonNull String name) {
        new Item(name, "", Config.MIN_HANDLE_LV);
    }

    public Item(@NonNull String name, @NonNull String description, int handleLv) {
        new Item(name, description, handleLv, null, new ArrayList<Map<Long, Integer>>());
    }

    public Item(@NonNull String name, @NonNull String description, int handleLv,
                @Nullable Use use, @NonNull List<Map<Long, Integer>> recipe) {
        this.name = name;
        this.description = description;
        this.handleLv.set(handleLv);
        this.use = use;
        this.recipe = recipe;
    }

    public void addRecipe(@NonNull Map<Long, Integer> recipe) {
        Integer value;
        for(Map.Entry<Long, Integer> entry : recipe.entrySet()) {
            value = entry.getValue();

            if(value == null || value < 1) {
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

    @Override
    @NonNull
    public String getPath() {
        return FileManager.DATA_PATH_MAP.get(Id.ITEM) + this.id + ".txt";
    }

}
