package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import com.google.common.collect.BiMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lkd.namsic.game.Config;
import lkd.namsic.game.ObjectList;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.event.ItemUseEvent;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.GameObject;
import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.Player;

public class ItemManager {

    private static final ItemManager instance = new ItemManager();

    public static ItemManager getInstance() {
        return instance;
    }

    public boolean canUse(@NonNull Player self, long itemId) {
        if(self.getItem(itemId) > 0) {
            Item item = Config.getData(Id.ITEM, itemId);
            return item.getUse() != null;
        } else {
            return false;
        }
    }

    public boolean use(@NonNull Player self, long itemId, @NonNull List<GameObject> other) {
        Config.checkId(Id.ITEM, itemId);

        boolean isCancelled;
        Item item = Config.getData(Id.ITEM, itemId);
        isCancelled = ItemUseEvent.handleEvent(self.getEvents().get(ItemUseEvent.getName()), new Object[]{item});

        if (!isCancelled) {
            self.addLog(LogData.TOTAL_ITEM_USED, 1);
            Objects.requireNonNull(item.getUse()).use(self, other);
            self.addItem(itemId, -1);
        }

        return isCancelled;
    }

    public boolean canEat(@NonNull Player self, long itemId) {
        if(self.getItem(itemId) > 0) {
            Item item = Config.getData(Id.ITEM, itemId);
            return item.isFood();
        } else {
            return false;
        }
    }

    public void eat(@NonNull Player self, long itemId) {
        Item item = Config.getData(Id.ITEM, itemId);

        self.addItem(itemId, -1);
        self.addLog(LogData.EAT, 1);

        for(Map.Entry<Long, HashMap<StatType, Integer>> entry : item.getEatBuff().entrySet()) {
            for(Map.Entry<StatType, Integer> statEntry : entry.getValue().entrySet()) {
                self.addBuff(entry.getKey(), statEntry.getKey(), statEntry.getValue());
            }
        }

        self.revalidateBuff();

        self.replyPlayer(item.getName() + "을 먹었습니다\n남은 개수 : " + self.getItem(itemId));
    }

    public void craft(@NonNull Player self, @NonNull String itemName) {
        boolean isItem = true;
        Long itemId = ObjectList.itemList.get(itemName);
        Set<Long> recipeSet;

        if(itemId == null) {
            isItem = false;
            itemId = ObjectList.equipList.get(itemName);
            recipeSet = self.getEquipRecipe();

            if(itemId == null) {
                throw new WeirdCommandException("해당 아이템 또는 장비를 찾을 수 없습니다\n" +
                        "띄어쓰기나 괄호 등 정확한 이름을 입력해주세요");
            }
        } else {
            recipeSet = self.getItemRecipe();
        }

        if(!recipeSet.contains(itemId)) {
            if(isItem) {
                throw new WeirdCommandException("해당 아이템의 제작법을 알고 있지 않습니다");
            } else {
                throw new WeirdCommandException("해당 장비의 제작법을 알고 있지 않습니다");
            }
        }

        Item item = Config.getData(isItem ? Id.ITEM : Id.EQUIPMENT, itemId);

        boolean flag = false;
        Map<Long, Integer> recipe = null;
        int resultCount = 0;

        for(Map<Long, Integer> recipeMap : item.getRecipe()) {
            resultCount = recipeMap.getOrDefault(0L, 1);

            recipe = new HashMap<>(recipeMap);
            recipe.remove(0L);

            if(Config.compareMap(self.getInventory(), recipe, true)) {
                flag = true;
                break;
            }
        }

        if(!flag) {
            if(isItem) {
                throw new WeirdCommandException("해당 아이템을 제작하기 위한 재료가 충분하지 않습니다");
            } else {
                throw new WeirdCommandException("해당 장비를 제작하기 위한 재료가 충분하지 않습니다");
            }
        }

        StringBuilder innerMsg = new StringBuilder();
        BiMap<Long, String> inverseMap = ObjectList.itemList.inverse();

        long removeItemId;
        int removeCount;
        for(Map.Entry<Long, Integer> entry : recipe.entrySet()) {
            removeItemId = entry.getKey();
            removeCount = entry.getValue();

            self.addItem(removeItemId, -1 * removeCount);

            innerMsg.append("[")
                    .append(inverseMap.get(removeItemId))
                    .append(" -")
                    .append(removeCount)
                    .append("]\n");
        }

        self.addItem(itemId, resultCount);
        self.replyPlayer("제작이 완료되었습니다\n[" + itemName + " +" + resultCount + "]", innerMsg.toString());
    }

    //TODO
    public void giveLowRecipe(@NonNull Player self) {

    }

    public void giveMiddleRecipe(@NonNull Player self) {

    }

    public void giveHighRecipe(@NonNull Player self) {

    }

}
