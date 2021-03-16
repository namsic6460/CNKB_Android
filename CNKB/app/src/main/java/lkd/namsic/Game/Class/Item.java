package lkd.namsic.Game.Class;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lkd.namsic.Game.Base.LimitInteger;
import lkd.namsic.Game.Config;
import lkd.namsic.Game.Enum.Id;
import lkd.namsic.Game.Enum.StatType;
import lkd.namsic.Game.Exception.ListAddFailedException;
import lkd.namsic.Setting.FileManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item implements GameClass {

    String name;
    String description;
    Use use;

    @Setter(AccessLevel.PRIVATE)
    LimitInteger handleLv;

    @Setter(AccessLevel.PRIVATE)
    List<Map<Long, Integer>> recipe = new ArrayList<>();

    protected Item() {}

    public Item(String name) {
        new Item(name, "", 1);
    }

    public Item(String name, String description, int handleLv) {
        new Item(name, description, handleLv, null, new ArrayList<Map<Long, Integer>>());
    }

    public Item(String name, String description, int handleLv, Use use, List<Map<Long, Integer>> recipe) {
        this.name = name;
        this.description = description;
        this.handleLv = new LimitInteger(handleLv, Config.MIN_HANDLE_LV, Config.MAX_HANDLE_LV);
        this.use = use;
        this.recipe = recipe;
    }

    public void addRecipe(Map<Long, Integer> recipe) {
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
    public String getPath() {
        return FileManager.DATA_PATH_MAP.get(Id.ITEM) + this.id + ".txt";
    }

}
