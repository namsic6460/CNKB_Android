package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import com.google.common.collect.BiMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lkd.namsic.game.Config;
import lkd.namsic.game.Emoji;
import lkd.namsic.game.ObjectList;
import lkd.namsic.game.Variable;
import lkd.namsic.game.base.Location;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MagicType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.MapClass;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.gameObject.Quest;

public class DisplayManager {

    private static final DisplayManager instance = new DisplayManager();

    public static DisplayManager getInstance() {
        return instance;
    }

    public void displayInfo(@NonNull Player self) {
        StringBuilder innerMsg = new StringBuilder();

        if(self.getDoing().equals(Doing.WAIT_RESPONSE)) {
            innerMsg.append(this.getDisplayResponse(self))
                    .append("\n\n");
        }

        innerMsg.append(this.getDisplayStat(self))
                .append("\n\n")
                .append(this.getDisplayQuest(self))
                .append("\n\n")
                .append(this.getDisplayMagic(self))
                .append("\n\n달성한 업적 개수: ")
                .append(self.getAchieve().size())
                .append("\n연구 완료 개수: ")
                .append(self.getResearch().size());

        self.replyPlayer("===내 정보===\n" +
                        Emoji.GOLD + " 골드: " + self.getMoney() + "G\n" +
                        Emoji.HEART + " 체력: " + self.getDisplayHp() + "\n" +
                        Emoji.MANA + " 마나: " + self.getStat(StatType.MN) + "/" + self.getStat(StatType.MAXMN) + "\n" +
                        Emoji.WORLD + " 위치: " + Config.getMapData(self.getLocation()).getName() + "(" + self.getLocation().toString() + ")\n" +
                        Emoji.LV + " 레벨: " + self.getLv().get() + "Lv (" + self.getExp().get() + "/" + self.getNeedExp() + ")\n" +
                        Emoji.SP + " 스텟 포인트: " + self.getSp().get() + "\n" +
                        Emoji.ADV + " 모험 포인트: " + self.getAdv().get() + "\n" +
                        Emoji.HOME + " 거점: " + Config.getMapData(self.getBaseLocation()).getName() + "(" + self.getBaseLocation().toString() + ")",
                innerMsg.toString());
    }

    public String getDisplayResponse(@NonNull Player self) {
        StringBuilder builder = new StringBuilder("---대기중인 대답---\n");

        for(WaitResponse waitResponse : self.getResponseChat().keySet()) {
            builder.append(waitResponse.getDisplay())
                    .append("\n");
        }

        if(!self.getAnyResponseChat().isEmpty()) {
            builder.append("\n(다른 메세지 목록)");

            String waitResponse;
            for(String response : self.getAnyResponseChat().keySet()) {
                if(response.startsWith("__")) {
                    waitResponse = response.replace("__", "(ㅜ/n) ");
                } else {
                    waitResponse = response;
                }

                builder.append("\n")
                        .append(waitResponse);
            }
        }

        return builder.toString();
    }

    public String getDisplayStat(@NonNull Player self) {
        StringBuilder builder = new StringBuilder("---스텟---");
        for(StatType statType : StatType.values()) {
            builder.append("\n")
                    .append(statType.getDisplayName())
                    .append(": ")
                    .append(self.getStat(statType));
        }

        return builder.toString();
    }

    public String getDisplayQuest(@NonNull Player self) {
        StringBuilder builder = new StringBuilder("---퀘스트 목록---");

        if(self.getQuest().isEmpty()) {
            return builder.toString() + "\n현재 진행중인 퀘스트 없음";
        }

        for(long questId : self.getQuest().keySet()) {
            Quest quest = Config.getData(Id.QUEST, questId);

            builder.append("\n[")
                    .append(questId)
                    .append("] ")
                    .append(quest.getName());

            Long npcId = quest.getNpcId().get();
            if(!npcId.equals(0L)) {
                Npc npc = Config.getData(Id.NPC, npcId);

                builder.append(" (NPC: ")
                        .append(npc.getName())
                        .append(")");
            }
        }

        return builder.toString();
    }

    public String getDisplayMagic(@NonNull Player self) {
        StringBuilder builder = new StringBuilder("---마법 정보---");

        for(MagicType magicType : MagicType.values()) {
            builder.append("\n")
                    .append(magicType.toString())
                    .append(": ")
                    .append(self.getMagic(magicType))
                    .append("Lv, 저항: ")
                    .append(self.getResist(magicType))
                    .append("Lv");
        }

        return builder.toString();
    }

    public void displayInventory(@NonNull Player self, int page) {
        if(self.getInventory().isEmpty()) {
            self.replyPlayer("---인벤토리---\n인벤토리가 비어있습니다...");
            return;
        }

        int maxPage = (int) Math.ceil(self.getInventory().size() / 30D);

        if(page < 1 || page > maxPage) {
            throw new WeirdCommandException(page + "페이지 부터 " + maxPage + "페이지 범위 내의 숫자를 입력해주세요");
        }

        List<Long> highPriorityItems = self.getListVariable(Variable.HIGH_PRIORITY_ITEM);
        BiMap<Long, String> inverseMap = ObjectList.itemList.inverse();

        StringBuilder msg = new StringBuilder("---인벤토리---\n[")
                .append(page)
                .append("페이지 /")
                .append(maxPage)
                .append("페이지]");

        if(highPriorityItems.isEmpty()) {
            msg.append("\n우선 표시 설정된 아이템이 없습니다");
        } else {
            for(long itemId : highPriorityItems) {
                msg.append("\n")
                        .append(inverseMap.get(itemId))
                        .append(": ")
                        .append(self.getItem(itemId))
                        .append("개");
            }
        }

        StringBuilder innerMsg = new StringBuilder();

        int count = 0;
        List<Long> sortedList = new ArrayList<>(self.getInventory().keySet());
        Collections.sort(sortedList);
        for(long itemId : sortedList) {
            if(highPriorityItems.contains(itemId)) {
                continue;
            }

            innerMsg.append(inverseMap.get(itemId))
                    .append(": ")
                    .append(self.getItem(itemId))
                    .append("개\n");
            count++;

            if(count == 30) {
                break;
            }
        }

        self.replyPlayer(msg.toString(), innerMsg.toString());
    }

    public void displayMapList(@NonNull Player self) {
        int movableDistance = self.getMovableDistance();

        StringBuilder msg = new StringBuilder("---이동 가능한 가까운 맵 목록---");
        StringBuilder innerMsg = new StringBuilder("---이동 가능한 먼 맵 목록---");

        boolean flag;
        StringBuilder builder = null;

        int currentX = self.getLocation().getX().get();
        int currentY = self.getLocation().getY().get();

        int distance;
        MapClass map;

        int minX = Math.max(Config.MIN_MAP_X, currentX - movableDistance);
        int maxX = Math.min(Config.MAX_MAP_X, currentX + movableDistance);
        int minY = Math.max(Config.MIN_MAP_Y, currentY - movableDistance);
        int maxY = Math.min(Config.MAX_MAP_Y, currentY + movableDistance);

        for(int x = minX; x <= maxX; x++) {
            for(int y = minY; y <= maxY; y++) {
                flag = false;

                if(x == currentX && y == currentY) {
                    continue;
                }

                if(x >= currentX - 1 && x <= currentX + 1 && y >= currentY - 1 && y <= currentY + 1) {
                    builder = msg;
                    distance = 1;
                    flag = true;
                } else {
                    distance = self.getMapDistance(new Location(x, y));

                    if (movableDistance <= distance) {
                        builder = innerMsg;
                        flag = true;
                    }
                }

                if(flag) {
                    map = Config.getMapData(x, y);

                    builder.append("\n")
                            .append(map.getName())
                            .append("(")
                            .append(map.getLocation().toMapString())
                            .append(") (거리: ")
                            .append(distance)
                            .append(")");
                }
            }
        }

        self.replyPlayer(msg.toString(), innerMsg.toString());
    }

    public void displayRecipes(@NonNull Player self, boolean isItem) {
        StringBuilder msg;

        BiMap<Long, String> inverseMap;
        List<Long> sortedList;

        if(isItem) {
            inverseMap = ObjectList.itemList.inverse();

            sortedList = new ArrayList<>(self.getItemRecipe());
            Collections.sort(sortedList);

            msg = new StringBuilder("---아이템 목록---");
        } else {
            inverseMap = ObjectList.equipList.inverse();

            sortedList = new ArrayList<>(self.getEquipRecipe());
            Collections.sort(sortedList);

            msg = new StringBuilder("---장비 목록---");
        }

        if(sortedList.isEmpty()) {
            //아이템의 경우, 기본으로 제공되는 제작법이 존재한다
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
                        msg.append("\n").append(inverseMap.get(itemId));
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

                innerMsg.append(inverseMap.get(itemId)).append("\n");
            }
        } else {
            for(long equipId : sortedList) {
                innerMsg.append(inverseMap.get(equipId)).append("\n");
            }
        }

        self.replyPlayer(msg.toString(), innerMsg.toString());
    }

    public void displayRecipe(@NonNull Player self, @NonNull String itemName) {
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

        StringBuilder msg = new StringBuilder("---")
                .append(itemName)
                .append("의 제작법---");

        Item item = Config.getData(isItem ? Id.ITEM : Id.EQUIPMENT, itemId);
        for(Map<Long, Integer> recipe : item.getRecipe()) {
            int size = recipe.size();
            int count = 0;

            long tempItemId;
            Item tempItem;
            int resultCount = 1;

            msg.append("\n")
                    .append(Emoji.LIST)
                    .append(" ");

            for(Map.Entry<Long, Integer> entry : recipe.entrySet()) {
                count++;

                tempItemId = entry.getKey();
                if(tempItemId == 0L) {
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

}
