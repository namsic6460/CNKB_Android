package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MagicType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Npc;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.object.Quest;

public class DisplayManager {

    private static final DisplayManager instance = new DisplayManager();

    public static DisplayManager getInstance() {
        return instance;
    }

    public void displayInfo(@NonNull Player self, @NonNull Player target) {
        StringBuilder innerMsg = new StringBuilder();

        if(self.equals(target)) {
            if (target.getDoing().equals(Doing.WAIT_RESPONSE)) {
                innerMsg.append(this.getDisplayResponse(target))
                        .append("\n\n");
            }
        }

        innerMsg.append(this.getDisplayStat(target))
                .append("\n\n")
                .append(this.getDisplayQuest(target))
                .append("\n\n")
                .append(this.getDisplayMagic(target))
                .append("\n\n달성한 업적 개수: ")
                .append(target.getAchieve().size())
                .append("\n연구 완료 개수: ")
                .append(target.getResearch().size());

        self.replyPlayer("===" + target.getNickName() + "님의 정보===\n" +
                        Emoji.GOLD + " 골드: " + target.getMoney() + "G\n" +
                        Emoji.HEART + " 체력: " + target.getDisplayHp() + "\n" +
                        Emoji.MANA + " 마나: " + target.getDisplayMn() + "\n" +
                        Emoji.WORLD + " 위치: " + Config.getMapData(target.getLocation()).getName() + "(" + target.getLocation().toString() + ")\n" +
                        Emoji.LV + " 레벨: " + target.getDisplayLv() + "\n" +
                        Emoji.SP + " 스텟 포인트: " + target.getSp().get() + "\n" +
                        Emoji.ADV + " 모험 포인트: " + target.getAdv().get() + "\n" +
                        Emoji.HOME + " 거점: " + Config.getMapData(target.getBaseLocation()).getName() + "(" + target.getBaseLocation().toString() + ")",
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
        self.revalidateBuff();

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

        List<Long> list = new ArrayList<>(self.getQuest().keySet());
        Collections.sort(list);
        for(long questId : list) {
            Quest quest = Config.getData(Id.QUEST, questId);

            builder.append("\n[")
                    .append(questId)
                    .append("] ")
                    .append(quest.getName());

            Long npcId = quest.getClearNpcId().get();
            if(!npcId.equals(0L)) {
                Npc npc = Config.getData(Id.NPC, npcId);

                builder.append(" (NPC: ")
                        .append(npc.getRealName())
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

    public void displayMapList(@NonNull Player self) {
        int movableDistance = self.getMovableDistance();

        StringBuilder msg = new StringBuilder("---이동 가능한 가까운 맵 목록---");
        StringBuilder innerMsg = new StringBuilder("---이동 가능한 먼 맵 목록---");

        boolean flag;
        StringBuilder builder = null;

        int currentX = self.getLocation().getX().get();
        int currentY = self.getLocation().getY().get();

        int distance;
        GameMap map;

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

                    if (movableDistance >= distance) {
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

    public void displayStatInfo(@NonNull Player self) {
        self.replyPlayer("스텟에 관한 정보는 전체보기로 확인해주세요",
                "---스텟 정보---\n" +
                        Emoji.LIST + " 최대 체력(MaxHp) : 대상이 가질 수 있는 최대량의 체력을 나타냅니다 (" +
                        StatType.MAXHP.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 체력(Hp) : 대상이 현재 가지고 있는 체력을 나타냅니다 (-)\n" +
                        Emoji.LIST + " 최대 마나(MaxMn) : 대상이 가질 수 있는 최대량의 마나를 나타냅니다 (" +
                        StatType.MAXMN.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 마나(Mn) : 대상이 현재 가지고 있는 마나를 나타냅니다 (-)\n" +
                        Emoji.LIST + " 공격력(Atk) : 대상의 물리 공격력을 나타냅니다 (" +
                        StatType.ATK.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 마법 공격력(MAtk) : 대상의 마법 공격력을 나타냅니다 (" +
                        StatType.MATK.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 민첩(Agi) : 치명타 확률을 나타냅니다 (스텟 1당 치명타 확률 " + (Config.CRIT_PER_AGI * 100) + "%) (" +
                        StatType.AGI.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 공격 속도(Ats) : 전투에서 턴을 가져올 상대적 우선권 나타냅니다 (" +
                        StatType.ATS.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 방어력(Def) : 물리 데미지를 방어하는 수치를 나타냅니다 (" +
                        StatType.DEF.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 마법 방어력(MDef) : 마법 데미지를 방어하는 수치를 나타냅니다 (" +
                        StatType.MDEF.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 방어 관통력(Bre) : 공격 시 대상의 방어력을 상쇄하는 수치를 나타냅니다 (" +
                        StatType.BRE.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 마법 관통력(MBre) : 공격 시 대상의 마법 방어력을 상쇄하는 수치를 나타냅니다 (" +
                        StatType.MBRE.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 흡수력(Dra) : 공격 시 최종 물리 데미지의 일부 %를 회복하는 수치를 나타냅니다 (최대: 100) (" +
                        StatType.DRA.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 마법 흡수력(MDra) : 공격 시 최종 마법 데미지의 일부 %를 회복하는 수치를 나타냅니다 (최대: 100) (" +
                        StatType.MDRA.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 회피(Eva) : 공격을 회피할 수 있는 수치를 나타냅니다 (회피 - 정확도 <= " + Config.MAX_EVADE + ") (" +
                        StatType.EVA.getUseSp() + "SP)\n" +
                        Emoji.LIST + " 정확도(Acc) : 공격 시 대상의 회피를 상쇄하는 수치를 나타냅니다 (" +
                        StatType.ACC.getUseSp() + "SP)"
        );
    }

    public void displayLvRanking(@NonNull Player self) {
        List<String> sortedList = new ArrayList<>(Config.PLAYER_LV_RANK.keySet());
        sortedList.sort((o1, o2) -> Integer.compare(Config.PLAYER_LV_RANK.get(o2), Config.PLAYER_LV_RANK.get(o1)));

        StringBuilder innerBuilder = new StringBuilder(Config.SPLIT_BAR);

        int rank = 1;
        for(String name : sortedList) {
            innerBuilder.append("\n")
                    .append(rank++)
                    .append(". ")
                    .append(name)
                    .append("\n")
                    .append(Config.SPLIT_BAR);
        }

        self.replyPlayer("레벨 랭킹은 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayQuestInfo(@NonNull Player self, long questId) {
        if(!(self.getQuest().containsKey(questId) || self.getClearedQuest().containsKey(questId))) {
            throw new WeirdCommandException("받아본 적 없는 퀘스트의 정보는 확인할 수 없습니다");
        }
        
        StringBuilder innerBuilder = new StringBuilder("퀘스트 이름: ");
        Quest quest = Config.getData(Id.QUEST, questId);
        
        innerBuilder.append(quest.getName())
                .append("\n\n")
                .append(Config.HARD_SPLIT_BAR)
                .append("\n\n요구 금액: ")
                .append(quest.getNeedMoney().get())
                .append("\n\n---요구 아이템---");
        
        if(quest.getNeedItem().isEmpty()) {
            innerBuilder.append("\n요구 아이템이 없습니다");
        } else {
            for (Map.Entry<Long, Integer> entry : quest.getNeedItem().entrySet()) {
                innerBuilder.append("\n")
                        .append(ItemList.findById(entry.getKey()))
                        .append(": ")
                        .append(entry.getValue())
                        .append("개");
            }
        }
        
        innerBuilder.append("\n\n---요구 스텟---");
        if(quest.getNeedStat().isEmpty()) {
            innerBuilder.append("\n요구 스텟이 없습니다");
        } else {
            for (Map.Entry<StatType, Integer> entry : quest.getNeedStat().entrySet()) {
                innerBuilder.append("\n")
                        .append(entry.getKey().getDisplayName())
                        .append(": ")
                        .append(entry.getValue());
            }
        }
        
        innerBuilder.append("\n\n---요구 친밀도---");
        if(quest.getNeedCloseRate().isEmpty()) {
            innerBuilder.append("\n요구 친밀도가 없습니다");
        } else {
            for(Map.Entry<Long, Integer> entry : quest.getNeedCloseRate().entrySet()) {
                innerBuilder.append("\n")
                        .append(NpcList.findById(entry.getKey()))
                        .append(": ")
                        .append(entry.getValue());
            }
        }
        
        innerBuilder.append("\n\n")
                .append(Config.HARD_SPLIT_BAR)
                .append("\n\n클리어 제한 레벨: ")
                .append(quest.getClearLimitLv().get())
                .append("\n\n")
                .append(Config.HARD_SPLIT_BAR)
                .append("\n\n보상 골드: ")
                .append(quest.getRewardMoney().get())
                .append("\n보상 경험치: ")
                .append(quest.getRewardExp().get())
                .append("\n보상 모험 스텟: ")
                .append(quest.getRewardAdv().get())
                .append("\n\n---보상 아이템---");
        
        if(quest.getRewardItem().isEmpty()) {
            innerBuilder.append("\n보상 아이템이 없습니다");
        } else {
            for(Map.Entry<Long, Integer> entry : quest.getRewardItem().entrySet()) {
                innerBuilder.append("\n")
                        .append(ItemList.findById(entry.getKey()))
                        .append(": ")
                        .append(entry.getValue())
                        .append("개");
            }
        }

        innerBuilder.append("\n\n---보상 스텟---");
        if(quest.getRewardStat().isEmpty()) {
            innerBuilder.append("\n보상 스텟이 없습니다");
        } else {
            for (Map.Entry<StatType, Integer> entry : quest.getRewardStat().entrySet()) {
                innerBuilder.append("\n")
                        .append(entry.getKey().getDisplayName())
                        .append(": ")
                        .append(entry.getValue());
            }
        }

        innerBuilder.append("\n\n---보상 친밀도---");
        if(quest.getRewardCloseRate().isEmpty()) {
            innerBuilder.append("\n보상 친밀도가 없습니다");
        } else {
            for(Map.Entry<Long, Integer> entry : quest.getRewardCloseRate().entrySet()) {
                innerBuilder.append("\n")
                        .append(NpcList.findById(entry.getKey()))
                        .append(": ")
                        .append(entry.getValue());
            }
        }

        innerBuilder.append("\n\n---보상 아이템 제작법---");
        if(quest.getRewardItemRecipe().isEmpty()) {
            innerBuilder.append("\n보상 아이템 제작법이 없습니다");
        } else {
            for(long itemId : quest.getRewardItemRecipe()) {
                innerBuilder.append("\n")
                        .append(Emoji.LIST)
                        .append(" ")
                        .append(ItemList.findById(itemId));
            }
        }

        innerBuilder.append("\n\n---보상 장비 제작법---");
        if(quest.getRewardEquipRecipe().isEmpty()) {
            innerBuilder.append("\n보상 장비 제작법이 없습니다");
        } else {
            for(long equipId : quest.getRewardEquipRecipe()) {
                innerBuilder.append("\n")
                        .append(Emoji.LIST)
                        .append(" ")
                        .append(EquipList.findById(equipId));
            }
        }

        innerBuilder.append("\n\n")
                .append(Config.HARD_SPLIT_BAR);
        
        self.replyPlayer("퀘스트 정보는 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayReinforceExplanation(@NonNull Player self) {
        self.replyPlayer("1. 장비는 최대 " + Config.MAX_REINFORCE_COUNT + "강 까지 강화할 수 있습니다\n" +
                "2. 강화에 성공할 시 모든 스텟이 균등하게 증가하지만 제한 레벨 또한 증가될 수 있습니다\n" +
                "3. 일정 횟수 이상 강화에 실패하면 무조건 강화에 성공하는 천장 시스템이 있습니다\n" +
                "4. 장비의 입수 난이도 및 다양한 조건에 따라 강화의 난이도 및 성장치가 변화합니다");
    }

}
