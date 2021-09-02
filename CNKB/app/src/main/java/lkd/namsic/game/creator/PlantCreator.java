package lkd.namsic.game.creator;

import java.util.HashMap;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.object.Farm;
import lkd.namsic.setting.Logger;
import lombok.NonNull;

public class PlantCreator implements Creatable {

    public void createPlant(@NonNull ItemList itemData, int plantLv, long growMinute, @NonNull HashMap<Long, Integer> rewardItem) {
        Farm.Plant plant = new Farm.Plant(itemData, plantLv, growMinute, rewardItem);
        Config.unloadObject(plant);
    }

    @Override
    public void start() {
        createPlant(ItemList.SMALL_GOLD_SEED, 1, 120, new HashMap<Long, Integer>() {{
            put(ItemList.GOLD_FRUIT.getId(), 1);
        }});
        createPlant(ItemList.GOLD_SEED, 2, 90, new HashMap<Long, Integer>() {{
            put(ItemList.GOLD_FRUIT.getId(), 2);
        }});
        createPlant(ItemList.BIG_GOLD_SEED, 3, 60, new HashMap<Long, Integer>() {{
            put(ItemList.GOLD_FRUIT.getId(), 3);
        }});

        createPlant(ItemList.SMALL_EXP_SEED, 1, 120, new HashMap<Long, Integer>() {{
            put(ItemList.EXP_FRUIT.getId(), 1);
        }});
        createPlant(ItemList.EXP_SEED, 2, 90, new HashMap<Long, Integer>() {{
            put(ItemList.EXP_FRUIT.getId(), 2);
        }});
        createPlant(ItemList.BIG_EXP_SEED, 3, 60, new HashMap<Long, Integer>() {{
            put(ItemList.EXP_FRUIT.getId(), 3);
        }});

        createPlant(ItemList.SMALL_PURIFYING_SEED, 1, 20, new HashMap<Long, Integer>() {{
            put(ItemList.PURIFYING_FRUIT.getId(), 1);
        }});
        createPlant(ItemList.PURIFYING_SEED, 2, 10, new HashMap<Long, Integer>() {{
            put(ItemList.PURIFYING_FRUIT.getId(), 1);
        }});
        createPlant(ItemList.BIG_PURIFYING_SEED, 4, 8, new HashMap<Long, Integer>() {{
            put(ItemList.PURIFYING_FRUIT.getId(), 2);
        }});

        Logger.i("ObjectMaker", "Plant making is done!");
    }

}
