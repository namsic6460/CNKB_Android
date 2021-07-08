package lkd.namsic.game.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.ObjectList;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object_list.ItemList;
import lkd.namsic.game.event.ItemEatEvent;
import lkd.namsic.game.event.ItemUseEvent;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.gameObject.Use;

public class ItemManager {

    private static final ItemManager instance = new ItemManager();

    public static ItemManager getInstance() {
        return instance;
    }

    public boolean tryUse(@NonNull Player self, @NonNull String itemName, int count) {
        Long itemId = ItemList.findByName(itemName);

        if(itemId == null) {
            throw new WeirdCommandException("알 수 없는 아이템입니다\n아이템의 이름을 다시 확인해주세요");
        }

        Item item = Config.getData(Id.ITEM, itemId);
        if(item.getUse() == null) {
            throw new WeirdCommandException("사용이 불가능한 아이템입니다");
        }

        if(self.getItem(itemId) == 0) {
            throw new WeirdCommandException("해당 아이템을 보유하고 있지 않습니다\n아이템을 획득한 후 다시 사용해주세요");
        }

        return use(self, itemId, count);
    }

    public boolean use(@NonNull Player self, long itemId, int count) {
        Config.checkId(Id.ITEM, itemId);

        boolean isCancelled;
        isCancelled = ItemUseEvent.handleEvent(self, self.getEvents().get(ItemUseEvent.getName()), itemId, count);

        if (!isCancelled) {
            self.addLog(LogData.TOTAL_ITEM_USE, 1);

            Item item = Config.getData(Id.ITEM, itemId);

            String msg = item.getName() + " " + count + "개를 사용했습니다";
            StringBuilder innerBuilder = new StringBuilder();

            String result = null;

            Use use = Objects.requireNonNull(item.getUse());
            for(int i = 1; i <= count; i++) {
                result = use.use(self, new ArrayList<>());

                if(result != null) {
                    innerBuilder.append(result)
                            .append("\n");
                }
            }
            
            self.addItem(itemId, count * -1);

            if(result == null) {
                self.replyPlayer(msg);
            } else {
                self.replyPlayer(msg, innerBuilder.toString());
            }
        }

        return !isCancelled;
    }

    public boolean tryEat(@NonNull Player self, @NonNull String itemName, int count) {
        Long itemId = ItemList.findByName(itemName);

        if(itemId == null) {
            throw new WeirdCommandException("알 수 없는 아이템입니다\n아이템의 이름을 다시 확인해주세요");
        }

        Item item = Config.getData(Id.ITEM, itemId);
        if(!item.isCanEat()) {
            if(Math.random() >= 0.001) {
                throw new WeirdCommandException("먹는것이 불가능한 아이템입니다");
            } else {
                throw new WeirdCommandException("먹는것이 불가능한 아이템입니다\n왜 그걸 먹으려고 하는거야...");
            }
        }

        if(self.getItem(itemId) == 0) {
            throw new WeirdCommandException("해당 아이템을 보유하고 있지 않습니다\n아이템을 획득한 후 다시 사용해주세요");
        }

        return eat(self, itemId, count);
    }

    public boolean eat(@NonNull Player self, long itemId, int count) {
        Config.checkId(Id.ITEM, itemId);

        boolean isCancelled;
        isCancelled = ItemEatEvent.handleEvent(self, self.getEvents().get(ItemEatEvent.getName()), itemId, count);

        if (!isCancelled) {
            self.addLog(LogData.TOTAL_ITEM_EAT, 1);

            Item item = Config.getData(Id.ITEM, itemId);

            StringBuilder innerBuilder = new StringBuilder("---획득한 버프---");

            Map<Long, HashMap<StatType, Integer>> eatBuff = item.getEatBuff();
            if(eatBuff.isEmpty()) {
                innerBuilder.append("\n획득한 버프가 없습니다");
            } else {
                long duration;
                StatType statType;
                int stat;

                long currentTime = System.currentTimeMillis();
                for(Map.Entry<Long, HashMap<StatType, Integer>> entry : eatBuff.entrySet()) {
                    duration = entry.getKey();

                    innerBuilder.append("\n[")
                            .append(duration / 1000D)
                            .append("초]");

                    duration += currentTime;
                    for(Map.Entry<StatType, Integer> statEntry : entry.getValue().entrySet()) {
                        statType = statEntry.getKey();
                        stat = statEntry.getValue() * count;

                        self.addBuff(duration, statType, stat);

                        innerBuilder.append("\n")
                                .append(statType.getDisplayName())
                                .append(": ")
                                .append(Config.getIncrease(stat));
                    }

                    innerBuilder.append("\n");
                }
            }

            self.addItem(itemId, count * -1);
            self.replyPlayer(item.getName() + " " + count + "개를 먹었습니다", innerBuilder.toString());
        }

        return !isCancelled;
    }

    public void craft(@NonNull Player self, @NonNull String itemName, int count, @Nullable Integer recipeIdx) {
        boolean isItem = true;
        Long itemId = ItemList.findByName(itemName);
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
        
        int recipeSize = item.getRecipe().size();
        if(recipeIdx != null && recipeIdx > recipeSize) {
            throw new WeirdCommandException("해당 아이템의 레시피는 " + recipeSize + "번 까지만 있습니다");
        }

        boolean flag = false;
        Map<Long, Integer> recipe = null;
        int resultCount = 0;

        int index = 1;
        for(Map<Long, Integer> recipeMap : item.getRecipe()) {
            if(recipeIdx != null) {
                if(recipeIdx != index++) {
                    continue;
                }
            }

            resultCount = recipeMap.getOrDefault(ItemList.NONE.getId(), 1);

            recipe = new HashMap<>(recipeMap);
            recipe.remove(ItemList.NONE.getId());

            for(Map.Entry<Long, Integer> entry : recipe.entrySet()) {
                recipe.put(entry.getKey(), entry.getValue() * count);
            }

            if(Config.compareMap(self.getInventory(), recipe, true)) {
                if(flag) {
                    self.replyPlayer("제작 가능한 레시피가 2개 이상입니다.\n레시피를 선택하여 제작해주세요");
                    return;
                }
                
                flag = true;
            }
        }

        if(!flag) {
            if(isItem) {
                throw new WeirdCommandException("해당 아이템을 제작하기 위한 재료가 충분하지 않습니다");
            } else {
                throw new WeirdCommandException("해당 장비를 제작하기 위한 재료가 충분하지 않습니다");
            }
        } else {
            resultCount *= count;
        }

        StringBuilder innerMsg = new StringBuilder();

        long removeItemId;
        int removeCount;
        for(Map.Entry<Long, Integer> entry : recipe.entrySet()) {
            removeItemId = entry.getKey();
            removeCount = entry.getValue() * count;

            self.addItem(removeItemId, removeCount * -1);

            innerMsg.append("[")
                    .append(ItemList.findById(removeItemId))
                    .append(" -")
                    .append(removeCount)
                    .append("]\n");
        }

        self.addItem(itemId, resultCount, false);
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