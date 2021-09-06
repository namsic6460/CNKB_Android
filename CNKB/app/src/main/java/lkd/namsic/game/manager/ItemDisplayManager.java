package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.Player;

public class ItemDisplayManager {

    private static final ItemDisplayManager instance = new ItemDisplayManager();

    public static ItemDisplayManager getInstance() {
        return instance;
    }

    public void displayInventory(@NonNull Player self, int page) {
        if(self.getInventory().isEmpty()) {
            self.replyPlayer("\n인벤토리 (1페이지 / 1페이지)\n인벤토리가 비어있습니다");
            return;
        }

        int maxPage = Config.getMaxPage(self.getInventory().size());
        if(page < 1 || page > maxPage) {
            throw new WeirdCommandException("1 ~ " + maxPage + " 페이지 범위 내의 숫자를 입력해주세요");
        }

        List<Long> highPriorityItems = self.getListVariable(Variable.HIGH_PRIORITY_ITEM);

        StringBuilder msg = new StringBuilder("\n인벤토리 (")
                .append(page)
                .append("페이지 / ")
                .append(maxPage)
                .append("페이지)");

        if(highPriorityItems.isEmpty()) {
            msg.append("\n우선 표시 설정된 아이템이 없습니다");
        } else {
            for(long itemId : highPriorityItems) {
                msg.append("\n")
                        .append(ItemList.findById(itemId))
                        .append(": ")
                        .append(self.getItem(itemId))
                        .append("개");
            }
        }

        StringBuilder innerMsg = new StringBuilder();

        List<Long> sortedList = new ArrayList<>(self.getInventory().keySet());
        Collections.sort(sortedList);

        long itemId;
        int startIndex = Config.MAX_COUNT_PER_PAGE * (page - 1);
        for(int i = 0; i < Config.MAX_COUNT_PER_PAGE; i++) {
            try {
                itemId = sortedList.get(startIndex + i);
            } catch (IndexOutOfBoundsException e) {
                break;
            }

            if(highPriorityItems.contains(itemId)) {
                continue;
            }

            innerMsg.append(ItemList.findById(itemId))
                    .append(": ")
                    .append(self.getItem(itemId))
                    .append("개\n");
        }

        self.replyPlayer(msg.toString(), innerMsg.toString());
    }

    public void displayRecipes(@NonNull Player self, boolean isItem) {
        StringBuilder msg;

        List<Long> sortedList;

        if(isItem) {
            sortedList = new ArrayList<>(self.getItemRecipe());
            Collections.sort(sortedList);

            msg = new StringBuilder("---아이템 목록---");
        } else {
            sortedList = new ArrayList<>(self.getEquipRecipe());
            Collections.sort(sortedList);

            msg = new StringBuilder("---장비 목록---");
        }

        if(sortedList.isEmpty()) {
            msg.append("\n제작법을 아는 장비가 없습니다");
            self.replyPlayer(msg.toString());
            return;
        }

        StringBuilder innerMsg = new StringBuilder();

        boolean flag = false;
        if(isItem) {
            List<Long> highPriorityItems = self.getListVariable(Variable.HIGH_PRIORITY_ITEM);

            if (highPriorityItems.isEmpty()) {
                msg.append("\n우선 표시 설정된 아이템이 없습니다");
            } else {
                for (long itemId : highPriorityItems) {
                    if (self.getItemRecipe().contains(itemId)) {
                        flag = true;
                        msg.append("\n")
                                .append(ItemList.findById(itemId));
                    }
                }
            }

            if(!flag) {
                msg.append("\n우선 표시 설정된 제작법을 아는 아이템이 없습니다");
            }

            for(long itemId : sortedList) {
                if(highPriorityItems.contains(itemId)) {
                    continue;
                }

                innerMsg.append(ItemList.findById(itemId)).append("\n");
            }
        } else {
            for(long equipId : sortedList) {
                innerMsg.append(EquipList.findById(equipId)).append("\n");
            }
        }

        self.replyPlayer(msg.toString(), innerMsg.toString());
    }

    public void displayRecipe(@NonNull Player self, @NonNull String itemName) {
        boolean isItem = true;
        Long itemId = ItemList.findByName(itemName);
        Set<Long> recipeSet;

        if(itemId == null) {
            isItem = false;
            itemId = EquipList.findByName(itemName);
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

        StringBuilder msg = new StringBuilder("---")
                .append(itemName)
                .append("의 제작법---");

        int index = 1;
        Item item = Config.getData(isItem ? Id.ITEM : Id.EQUIPMENT, itemId);
        for(Map<Long, Integer> recipe : item.getRecipe()) {
            int size = recipe.size();
            int count = 0;

            long tempItemId;
            Item tempItem;
            int resultCount = 1;

            msg.append("\n")
                    .append(index++)
                    .append(". ");

            for(Map.Entry<Long, Integer> entry : recipe.entrySet()) {
                count++;

                tempItemId = entry.getKey();
                if(tempItemId == ItemList.NONE.getId()) {
                    resultCount = entry.getValue();
                    continue;
                }

                tempItem = Config.getData(Id.ITEM, tempItemId);

                msg.append(tempItem.getName())
                        .append(" ")
                        .append(entry.getValue())
                        .append("개");

                if(size != count) {
                    msg.append(" + ");
                }
            }

            msg.append(" => ")
                    .append(resultCount)
                    .append("개");
        }

        self.replyPlayer(msg.toString());
    }

    public void displayItemInfo(@NonNull Player self, @NonNull String itemName) {
        Long itemId = ItemList.findByName(itemName);

        if(itemId == null) {
            throw new WeirdCommandException("알 수 없는 아이템입니다");
        }

        Item item = Config.getData(Id.ITEM, itemId);
        self.replyPlayer(Emoji.focus(item.getName()) + "\n" + item.getDescription());
    }

    public void displayEquipInfo(@NonNull Player self, @NonNull String equipName) {
        Long equipId = EquipList.findByName(equipName);
        if(equipId == null) {
            throw new WeirdCommandException("알 수 없는 장비입니다");
        }

        displayEquipInfo(self, equipId);
    }

    public void displayEquipInfo(@NonNull Player self, int index) {
        long equipId = self.getEquipIdByIndex(index);
        displayEquipInfo(self, equipId);
    }

    public void displayEquipInfo(@NonNull Player self, long equipId) {
        Equipment equipment = Config.getData(Id.EQUIPMENT, equipId);

        StringBuilder innerBuilder = new StringBuilder(equipment.getName() + " 의 정보\n\n장비 종류: ")
                .append(equipment.getEquipType().getDisplayNames().get(0))
                .append("\n장비 등급: ")
                .append(equipment.getHandleLv())
                .append("\n\n[액티브] ");

        Equipment originalEquip = Config.getData(Id.EQUIPMENT, equipment.getOriginalId());
        String description = originalEquip.getActiveDes();
        if(description == null) {
            innerBuilder.append("\n액티브 효과가 없습니다");
        } else {
            innerBuilder.append(description);
        }

        innerBuilder.append("\n\n[패시브]\n");

        description = originalEquip.getPassiveDes();
        if(description == null) {
            innerBuilder.append("패시브 효과가 없습니다");
        } else {
            innerBuilder.append(description);
        }

        innerBuilder.append("\n\n장착 제한 레벨: ")
                .append(equipment.getTotalLimitLv())
                .append("\n강화 성공 확률: ")
                .append(Config.getDisplayPercent(equipment.getReinforcePercent(self.getReinforceMultiplier())))
                .append("(x")
                .append(self.getReinforceMultiplier())
                .append(")\n강화 필요 아이템: ")
                .append(ItemList.findById(equipment.getReinforceItem()))
                .append("\n강화 비용: ")
                .append(equipment.getReinforceCost())
                .append("G\n\n---스텟 현황---");

        if(equipment.getBasicStat().isEmpty()) {
            innerBuilder.append("\n장착 스텟이 없습니다");
        } else {
            StatType statType;
            int stat;
            for (Map.Entry<StatType, Integer> entry : equipment.getBasicStat().entrySet()) {
                statType = entry.getKey();

                innerBuilder.append("\n")
                        .append(statType.getDisplayName())
                        .append(": ")
                        .append(equipment.getStat(statType))
                        .append("(")
                        .append(entry.getValue());

                stat = equipment.getReinforceStat(statType);
                if (stat >= 0) {
                    innerBuilder.append(" + ")
                            .append(stat);
                } else {
                    innerBuilder.append(" - ")
                            .append(stat);
                }

                innerBuilder.append(")");
            }
        }

        innerBuilder.append("\n\n---장착 제한 스텟---");

        if(equipment.getLimitStat().isEmpty()) {
            innerBuilder.append("\n장착 제한 스텟이 없습니다");
        } else {
            for(Map.Entry<StatType, Integer> entry : equipment.getLimitStat().entrySet()) {
                innerBuilder.append("\n")
                        .append(entry.getKey().getDisplayName())
                        .append(": ")
                        .append(entry.getValue());
            }
        }

        self.replyPlayer("장비 정보는 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayEquippedInfo(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("---착용하고 있는 장비---");

        Equipment equipment;
        String equipName;
        for(EquipType equipType : EquipType.values()) {
            long equipId = self.getEquipped(equipType);

            if(equipId == EquipList.NONE.getId()) {
                equipName = "미착용";
            } else {
                equipment = Config.getData(Id.EQUIPMENT, equipId);
                equipName = equipment.getName();
            }

            innerBuilder.append("\n")
                    .append(equipType.getDisplayNames().get(0))
                    .append(": ")
                    .append(equipName);
        }

        innerBuilder.append("\n\n---장비 스텟 현황---");
        if(self.getEquipStat().isEmpty()) {
            innerBuilder.append("\n장비로 얻은 스텟이 없습니다");
        } else {
            for (Map.Entry<StatType, Integer> entry : self.getEquipStat().entrySet()) {
                innerBuilder.append("\n")
                        .append(entry.getKey().getDisplayName())
                        .append(": ")
                        .append(Config.getIncrease(entry.getValue()));
            }
        }

        self.replyPlayer("장비 현황은 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayEquipInventory(@NonNull Player self, int page) {
        List<Long> list = new ArrayList<>(self.getEquipInventory());
        if(list.isEmpty()) {
            self.replyPlayer("\n---장비 인벤토리---\n인벤토리가 비어있습니다...");
            return;
        }

        int maxPage = Config.getMaxPage(self.getEquipInventory().size());
        if(page < 1 || page > maxPage) {
            throw new WeirdCommandException("1 ~ " + maxPage + " 페이지 범위 내의 숫자를 입력해주세요");
        }

        Collections.sort(list);
        Set<Long> equippedSet = new HashSet<>(self.getEquipped().values());

        StringBuilder innerBuilder = new StringBuilder(Config.SPLIT_BAR);

        long equipId;
        Equipment equipment;
        int startIndex = (page - 1) * Config.MAX_COUNT_PER_PAGE;
        for(int i = 0; i < 30; i++) {
            try {
                equipId = list.get(startIndex + i);
            } catch (IndexOutOfBoundsException e) {
                break;
            }

            equipment = Config.getData(Id.EQUIPMENT, equipId);

            innerBuilder.append("\n")
                    .append(startIndex + i + 1)
                    .append(". ");

            if(equippedSet.contains(equipId)) {
                innerBuilder.append("[E] ");
            }

            innerBuilder.append(equipment.getName())
                    .append("\n")
                    .append(Config.SPLIT_BAR);
        }

        self.replyPlayer("\n---장비 인벤토리---\n[" + page + "페이지 / " + maxPage + "페이지]", innerBuilder.toString());
    }

    public void displayReinforceInfo(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder();

        double multiplier = self.getReinforceMultiplier();

        long equipId;
        Equipment equipment;
        for(EquipType equipType : EquipType.values()) {
            innerBuilder.append(equipType.getDisplayNames().get(0))
                    .append(": ");

            equipId = self.getEquipped(equipType);

            if(equipId == EquipList.NONE.getId()) {
                innerBuilder.append("미장착");
            } else {
                equipment = Config.getData(Id.EQUIPMENT, equipId);

                innerBuilder.append(equipment.getName());

                if(equipment.getReinforceCount() != Config.MAX_REINFORCE_COUNT) {
                    innerBuilder.append("\n강화 성공 확률: ")
                            .append(Config.getDisplayPercent(equipment.getReinforcePercent(multiplier)))
                            .append("(x")
                            .append(multiplier)
                            .append(")\n현재 강화 실패 횟수: ")
                            .append(equipment.getReinforceFloor2())
                            .append("회\n강화 재료: ")
                            .append(ItemList.findById(equipment.getReinforceItem()))
                            .append("\n강화 비용: ")
                            .append(equipment.getReinforceCost());
                }
            }

            innerBuilder.append("\n\n");
        }

        self.replyPlayer("장비 강화 현황은 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayLowRecipe(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("---아이템 제작법 목록---");

        for(long itemId : RandomList.lowRecipeItems) {
            innerBuilder.append("\n- ")
                    .append(ItemList.findById(itemId));
        }

        innerBuilder.append("\n\n---장비 제작법 목록---");

        for(long equipId : RandomList.lowRecipeEquips) {
            innerBuilder.append("\n- ")
                    .append(EquipList.findById(equipId));
        }

        self.replyPlayer("하급 제작법으로 획득 가능한 제작법은 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayMiddleRecipe(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("---아이템 제작법 목록---");

        for(long itemId : RandomList.middleRecipeItems) {
            innerBuilder.append("\n- ")
                    .append(ItemList.findById(itemId));
        }

        innerBuilder.append("\n\n---장비 제작법 목록---");

        for(long equipId : RandomList.middleRecipeEquips) {
            innerBuilder.append("\n- ")
                    .append(EquipList.findById(equipId));
        }

        self.replyPlayer("중급 제작법으로 획득 가능한 제작법은 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayHighRecipe(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("---아이템 제작법 목록---");

        for(long itemId : RandomList.highRecipeItems) {
            innerBuilder.append("\n- ")
                    .append(ItemList.findById(itemId));
        }

        innerBuilder.append("\n\n---장비 제작법 목록---");

        for(long equipId : RandomList.highRecipeEquips) {
            innerBuilder.append("\n- ")
                    .append(EquipList.findById(equipId));
        }

        self.replyPlayer("상급 제작법으로 획득 가능한 제작법은 전체보기로 확인해주세요", innerBuilder.toString());
    }

}
