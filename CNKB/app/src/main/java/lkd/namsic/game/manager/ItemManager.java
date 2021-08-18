package lkd.namsic.game.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lkd.namsic.game.base.Use;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.event.ItemEatEvent;
import lkd.namsic.game.event.ItemUseEvent;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Entity;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.Player;

public class ItemManager {

    private static final ItemManager instance = new ItemManager();

    public static ItemManager getInstance() {
        return instance;
    }

    public long checkUse(@NonNull Player self, @NonNull String itemName, @Nullable String other, int count) {
        Long itemId = ItemList.findByName(itemName);

        if(itemId == null) {
            throw new WeirdCommandException("알 수 없는 아이템입니다\n아이템의 이름을 다시 확인해주세요");
        }

        int currentCount = self.getItem(itemId);
        if(currentCount < count) {
            if(currentCount == 0 && count == 1) {
                throw new WeirdCommandException("해당 아이템을 보유하고 있지 않습니다");
            } else {
                throw new WeirdCommandException("아이템이 " + (count - currentCount) + "개 부족합니다\n아이템을 획득한 후 다시 사용해주세요");
            }
        }

        Item item = Config.getData(Id.ITEM, itemId);
        Use use = item.getUse();
        if(use == null) {
            throw new WeirdCommandException("사용이 불가능한 아이템입니다");
        }

        use.checkUse(self, other);
        return itemId;
    }

    public void tryUse(@NonNull Player self, @NonNull String itemName, @Nullable String other, int count) {
        long itemId = checkUse(self, itemName, other, count);
        this.use(self, itemId, other, count);
    }

    public void use(@NonNull Entity self, long itemId, @Nullable String other, int count) {
        String eventName = ItemUseEvent.getName();
        ItemUseEvent.handleEvent(self, self.getEvent().get(eventName), self.getEquipEvents(eventName), itemId, other, count);

        Item item = Config.getData(Id.ITEM, itemId);

        String msg = item.getName() + " " + count + "개를 사용했습니다";
        StringBuilder innerBuilder = new StringBuilder();

        Use use = Objects.requireNonNull(item.getUse());
        for(int i = 1; i <= count; i++) {
            innerBuilder.append(use.use(self, other))
                    .append("\n\n");
        }

        self.addItem(itemId, count * -1);

        if(self.getId().getId().equals(Id.PLAYER)) {
            Player player = (Player) self;

            player.replyPlayer(msg, innerBuilder.toString());
            player.addLog(LogData.TOTAL_ITEM_USE, 1);
        }
    }

    public void tryEat(@NonNull Player self, @NonNull String itemName, int count) {
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

        int currentCount = self.getItem(itemId);
        if(currentCount < count) {
            throw new WeirdCommandException("아이템이 " + (count - currentCount) + "개 부족합니다\n아이템을 획득한 후 다시 사용해주세요");
        }

        eat(self, itemId, count);
    }

    public void eat(@NonNull Player self, long itemId, int count) {
        String eventName = ItemEatEvent.getName();
        ItemEatEvent.handleEvent(self, self.getEvent().get(eventName), self.getEquipEvents(eventName), itemId, count);

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

                if(duration != -1) {
                    innerBuilder.append("\n[")
                            .append(duration / 1000D)
                            .append("초]");

                    duration += currentTime;
                } else {
                    innerBuilder.append("\n[영구 지속]");
                }

                for(Map.Entry<StatType, Integer> statEntry : entry.getValue().entrySet()) {
                    statType = statEntry.getKey();
                    stat = statEntry.getValue() * count;

                    if(duration != -1) {
                        self.addBuff(duration, statType, stat);
                    } else {
                        self.addBasicStat(statType, stat);
                    }

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

    public void craft(@NonNull Player self, @NonNull String itemName, int count, @Nullable Integer recipeIdx) {
        boolean isItem = true;
        Long itemId = ItemList.findByName(itemName);
        Set<Long> recipeSet;

        if(itemId == null) {
            isItem = false;
            itemId = EquipList.findByName(itemName);
            recipeSet = self.getEquipRecipe();

            if(itemId == null) {
                throw new WeirdCommandException("해당 아이템 또는 장비를 찾을 수 없습니다\n" +
                        "띄어쓰기나 괄호 등 정확한 이름을 입력해주세요\n입력값: " + Emoji.focus(itemName));
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
        Map<Long, Integer> recipe;
        Map<Long, Integer> craftRecipe = null;
        int tempCount, resultCount = 0;

        int index = 1;
        for(Map<Long, Integer> recipeMap : item.getRecipe()) {
            if(recipeIdx != null) {
                if(recipeIdx != index++) {
                    continue;
                }
            }

            tempCount = recipeMap.getOrDefault(ItemList.NONE.getId(), 1);

            recipe = new HashMap<>(recipeMap);
            recipe.remove(ItemList.NONE.getId());

            for(Map.Entry<Long, Integer> entry : recipe.entrySet()) {
                recipe.put(entry.getKey(), entry.getValue() * count);
            }

            if(Config.compareMap(self.getInventory(), recipe, true, false, 0)) {
                if(flag) {
                    self.replyPlayer("제작 가능한 레시피가 2개 이상입니다.\n레시피를 선택하여 제작해주세요");
                    return;
                }

                flag = true;
                craftRecipe = recipe;
                resultCount = tempCount;
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
        for(Map.Entry<Long, Integer> entry : craftRecipe.entrySet()) {
            removeItemId = entry.getKey();
            removeCount = entry.getValue();

            self.addItem(removeItemId, removeCount * -1);

            innerMsg.append("[")
                    .append(ItemList.findById(removeItemId))
                    .append(" -")
                    .append(removeCount)
                    .append("]\n");
        }

        if(isItem) {
            self.addItem(itemId, resultCount, false);
        } else {
            for(int i = 0; i < count; i++) {
                self.addEquip(itemId);
            }
        }

        self.replyPlayer("제작이 완료되었습니다\n[" + itemName + " +" + resultCount + "]", innerMsg.toString());
    }

}
