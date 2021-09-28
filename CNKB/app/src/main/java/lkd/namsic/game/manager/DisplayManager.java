package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import lkd.namsic.game.enums.object.MonsterList;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.enums.object.QuestList;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.exception.UnhandledEnumException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Boss;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.Monster;
import lkd.namsic.game.object.Npc;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.object.Quest;
import lkd.namsic.game.object.Skill;

public class DisplayManager {

    private static final DisplayManager instance = new DisplayManager();

    public static DisplayManager getInstance() {
        return instance;
    }

    public void displayInfo(@NonNull Player self, @NonNull Player target) {
        StringBuilder innerMsg = new StringBuilder();

        if (self.equals(target)) {
            if (target.getDoing().equals(Doing.WAIT_RESPONSE)) {
                innerMsg.append(this.getDisplayResponse(target))
                        .append("\n\n");
            }
        }

        innerMsg.append("광질 레벨: ")
                .append(target.getDisplayMineLv())
                .append("\n낚시 레벨: ")
                .append(target.getDisplayFishLv())
                .append("\n\n")
                .append(this.getDisplayStat(target))
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
                        Emoji.WORLD + " 위치: " + Config.getMapData(target.getLocation()).getLocationName(true) + "\n" +
                        Emoji.LV + " 레벨: " + target.getDisplayLv() + "\n" +
                        Emoji.SP + " 스텟 포인트: " + target.getSp() + "\n" +
                        Emoji.ADV + " 모험 포인트: " + target.getAdv() + "\n" +
                        Emoji.HOME + " 거점: " + Config.getMapData(target.getBaseLocation()).getLocationName(),
                innerMsg.toString());
    }

    public String getDisplayResponse(@NonNull Player self) {
        StringBuilder builder = new StringBuilder("---대기중인 대답---\n");

        for (WaitResponse waitResponse : self.getResponseChat().keySet()) {
            builder.append(waitResponse.getDisplay())
                    .append("\n");
        }

        if (!self.getAnyResponseChat().isEmpty()) {
            builder.append("\n(다른 메세지 목록)");

            String waitResponse;
            for (String response : self.getAnyResponseChat().keySet()) {
                if (response.startsWith("__")) {
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
        for (StatType statType : StatType.values()) {
            builder.append("\n")
                    .append(statType.getDisplayName())
                    .append(": ")
                    .append(self.getStat(statType));
        }

        return builder.toString();
    }

    public String getDisplayQuest(@NonNull Player self) {
        StringBuilder builder = new StringBuilder("---퀘스트 목록---");

        if (self.getQuest().isEmpty()) {
            return builder.toString() + "\n현재 진행중인 퀘스트 없음";
        }

        List<Long> list = new ArrayList<>(self.getQuest().keySet());
        Collections.sort(list);
        for (long questId : list) {
            Quest quest = Config.getData(Id.QUEST, questId);

            builder.append("\n[")
                    .append(questId)
                    .append("] ")
                    .append(quest.getName());

            Long npcId = quest.getClearNpcId();
            if (!npcId.equals(0L)) {
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

        for (MagicType magicType : MagicType.values()) {
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

        int currentX = self.getLocation().getX();
        int currentY = self.getLocation().getY();

        int distance;
        GameMap map;

        int minX = Math.max(0, currentX - movableDistance);
        int maxX = Math.min(Config.MAX_MAP_X, currentX + movableDistance);
        int minY = Math.max(0, currentY - movableDistance);
        int maxY = Math.min(Config.MAX_MAP_Y, currentY + movableDistance);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                flag = false;

                if (x == currentX && y == currentY) {
                    continue;
                }

                if (x >= currentX - 1 && x <= currentX + 1 && y >= currentY - 1 && y <= currentY + 1) {
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

                if (flag) {
                    map = Config.getMapData(x, y);

                    if(map.getName().equals(Config.INCOMPLETE)) {
                        continue;
                    }

                    builder.append("\n")
                            .append(map.getLocationName())
                            .append(" [Lv ")
                            .append(map.getRequireLv())
                            .append("] (거리: ")
                            .append(distance)
                            .append(")");
                }
            }
        }

        self.replyPlayer(msg.toString(), innerMsg.toString());
    }

    public void displayQuestInfo(@NonNull Player self, @NonNull String questName) {
        Long questId = QuestList.findByName(questName);
        if (questId == null) {
            throw new WeirdCommandException("알 수 없는 퀘스트입니다");
        }

        if (!(self.getQuest().containsKey(questId) || self.getClearedQuest().containsKey(questId))) {
            throw new WeirdCommandException("받아본 적 없는 퀘스트의 정보는 확인할 수 없습니다");
        }

        StringBuilder innerBuilder = new StringBuilder("퀘스트 이름: ");
        Quest quest = Config.getData(Id.QUEST, questId);

        innerBuilder.append(quest.getName())
                .append("\n\n")
                .append(Config.HARD_SPLIT_BAR)
                .append("\n\n요구 금액: ")
                .append(quest.getNeedMoney())
                .append("\n\n---요구 아이템---");

        if (quest.getNeedItem().isEmpty()) {
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
        if (quest.getNeedStat().isEmpty()) {
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
        if (quest.getNeedCloseRate().isEmpty()) {
            innerBuilder.append("\n요구 친밀도가 없습니다");
        } else {
            for (Map.Entry<Long, Integer> entry : quest.getNeedCloseRate().entrySet()) {
                innerBuilder.append("\n")
                        .append(NpcList.findById(entry.getKey()))
                        .append(": ")
                        .append(entry.getValue());
            }
        }

        innerBuilder.append("\n\n")
                .append(Config.HARD_SPLIT_BAR)
                .append("\n\n클리어 제한 레벨: ")
                .append(quest.getClearLimitLv())
                .append("\n\n")
                .append(Config.HARD_SPLIT_BAR)
                .append("\n\n보상 골드: ")
                .append(quest.getRewardMoney())
                .append("\n보상 경험치: ")
                .append(quest.getRewardExp())
                .append("\n보상 모험 스텟: ")
                .append(quest.getRewardAdv())
                .append("\n\n---보상 아이템---");

        if (quest.getRewardItem().isEmpty()) {
            innerBuilder.append("\n보상 아이템이 없습니다");
        } else {
            for (Map.Entry<Long, Integer> entry : quest.getRewardItem().entrySet()) {
                innerBuilder.append("\n")
                        .append(ItemList.findById(entry.getKey()))
                        .append(": ")
                        .append(entry.getValue())
                        .append("개");
            }
        }

        innerBuilder.append("\n\n---보상 스텟---");
        if (quest.getRewardStat().isEmpty()) {
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
        if (quest.getRewardCloseRate().isEmpty()) {
            innerBuilder.append("\n보상 친밀도가 없습니다");
        } else {
            for (Map.Entry<Long, Integer> entry : quest.getRewardCloseRate().entrySet()) {
                innerBuilder.append("\n")
                        .append(NpcList.findById(entry.getKey()))
                        .append(": ")
                        .append(entry.getValue());
            }
        }

        innerBuilder.append("\n\n---보상 아이템 제작법---");
        if (quest.getRewardItemRecipe().isEmpty()) {
            innerBuilder.append("\n보상 아이템 제작법이 없습니다");
        } else {
            for (long itemId : quest.getRewardItemRecipe()) {
                innerBuilder.append("\n")
                        .append(Emoji.LIST)
                        .append(" ")
                        .append(ItemList.findById(itemId));
            }
        }

        innerBuilder.append("\n\n---보상 장비 제작법---");
        if (quest.getRewardEquipRecipe().isEmpty()) {
            innerBuilder.append("\n보상 장비 제작법이 없습니다");
        } else {
            for (long equipId : quest.getRewardEquipRecipe()) {
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

    public void displaySkillInfo(@NonNull Player self, @NonNull String skillName) {
        Long skillId = SkillList.findByName(skillName);
        if (skillId == null) {
            throw new WeirdCommandException("알 수 없는 스킬입니다");
        }

        Skill skill = Config.getData(Id.SKILL, skillId);

        String innerMsg = Emoji.focus(skillName) + "의 정보\n\n[액티브] ";

        String description = skill.getActiveDes();
        if (description == null) {
            innerMsg += "\n액티브 효과가 없습니다";
        } else {
            innerMsg += description;
        }

        innerMsg += "\n\n[패시브]\n";

        description = skill.getPassiveDes();
        if (description == null) {
            innerMsg += "패시브 효과가 없습니다";
        } else {
            innerMsg += description;
        }

        self.replyPlayer("스킬 정보는 전체보기로 확인해주세요", innerMsg);
    }
    
    public void displayMonsterInfo(@NonNull Player self, @NonNull String monsterName) {
        Long monsterId = MonsterList.findByName(monsterName);
        if(monsterId == null) {
            throw new WeirdCommandException("알 수 없는 몬스터입니다");
        }

        Monster monster = Config.getData(Id.MONSTER, monsterId);
        GameMap map = Config.getMapData(GameMap.MONSTER_SPAWN_MAP.get(monsterId));

        StringBuilder innerBuilder = new StringBuilder("===")
                .append(monsterName)
                .append("의 정보===\n\n")
                .append("기준 레벨: ")
                .append(monster.getLv())
                .append("\n서식지: ")
                .append(map.getLocationName())
                .append("\n\n---기본 스텟---");

        for(StatType statType : StatType.values()) {
            try {
                Config.checkStatType(statType);
            } catch (UnhandledEnumException e) {
                continue;
            }

            innerBuilder.append("\n")
                    .append(statType.getDisplayName())
                    .append(": ")
                    .append(monster.getStat(statType));
        }

        innerBuilder.append("\n\n---드롭 아이템 목록---");

        long itemId;
        for(Map.Entry<Long, Double> entry : monster.getItemDropPercent().entrySet()) {
            itemId = entry.getKey();

            innerBuilder.append("\n")
                    .append(ItemList.findById(itemId))
                    .append("(")
                    .append(Config.getDisplayPercent(entry.getValue()))
                    .append(")(")
                    .append(monster.getItemDropMinCount(itemId))
                    .append(" ~ ")
                    .append(monster.getItem(itemId))
                    .append("개)");
        }

        innerBuilder.append("\n\n--사용 스킬 목록---");

        for(Map.Entry<Long, Double> entry : monster.getSkillPercent().entrySet()) {
            innerBuilder.append("\n")
                    .append(SkillList.findById(entry.getKey()))
                    .append("(")
                    .append(Config.getDisplayPercent(entry.getValue(), 0))
                    .append(")");
        }
        
        self.replyPlayer("몬스터 정보는 전체보기로 확인해주세요", innerBuilder.toString());
    }
    
    public void displayMap(@NonNull Player self) {
        GameMap map = Config.getMapData(self.getLocation());

        StringBuilder innerBuilder = new StringBuilder("---플레이어 목록---");

        Set<Long> playerSet = map.getEntity(Id.PLAYER);
        if(playerSet.isEmpty()) {
            innerBuilder.append("\n플레이어 없음");
        } else {
            Player player;

            for (long playerId : new HashSet<>(playerSet)) {
                player = Config.getData(Id.PLAYER, playerId);

                if(player.getLv() < Config.MIN_RANK_LV && !player.equals(self)) {
                    continue;
                }

                innerBuilder.append("\n[");

                if(FightManager.getInstance().fightId.containsKey(player.getId())) {
                    innerBuilder.append("F] [");
                }

                innerBuilder.append(player.getLocation().toFieldString())
                        .append("] ")
                        .append(player.getName());
            }
        }

        innerBuilder.append("\n\n---NPC 목록---");

        Set<Long> npcSet = map.getEntity(Id.NPC);
        npcSet.remove(NpcList.SECRET.getId());
        npcSet.remove(NpcList.ABEL.getId());

        if(npcSet.isEmpty()) {
            innerBuilder.append("\nNPC 없음");
        } else {
            Npc npc;

            for(long npcId : new HashSet<>(npcSet)) {
                npc = Config.getData(Id.NPC, npcId);
                innerBuilder.append("\n[")
                        .append(npc.getLocation().toFieldString())
                        .append("] ")
                        .append(npc.getName());
            }
        }

        innerBuilder.append("\n\n---떨어진 골드---");

        if(map.getMoney().isEmpty()) {
            innerBuilder.append("\n떨어진 골드 없음");
        } else {
            for (Map.Entry<Location, Long> entry : new HashMap<>(map.getMoney()).entrySet()) {
                innerBuilder.append("\n[")
                        .append(entry.getKey().toFieldString())
                        .append("] ")
                        .append(entry.getValue())
                        .append("G");
            }
        }

        int index = 1;

        innerBuilder.append("\n\n---몬스터 목록---");

        List<Long> monsterList = new ArrayList<>(map.getEntity(Id.MONSTER));
        Collections.sort(monsterList);

        if(monsterList.isEmpty()) {
            innerBuilder.append("\n몬스터 없음");
        } else {
            Monster monster;
            Set<Long> removeSet = new HashSet<>();

            for(long monsterId : monsterList) {
                try {
                    monster = Config.getData(Id.MONSTER, monsterId);
                } catch (ObjectNotFoundException e) {
                    removeSet.add(monsterId);
                    continue;
                }

                innerBuilder.append("\n")
                        .append(index++)
                        .append(". [");

                if(FightManager.getInstance().fightId.containsKey(monster.getId())) {
                    innerBuilder.append("F] [");
                }

                innerBuilder.append(monster.getLocation().toFieldString())
                        .append("] ")
                        .append(monster.getName());
            }

            map.getEntity(Id.MONSTER).removeAll(removeSet);
        }

        innerBuilder.append("\n\n---보스 목록---");

        Set<Long> bossSet = map.getEntity(Id.BOSS);
        if(bossSet.isEmpty()) {
            innerBuilder.append("\n보스 없음");
        } else {
            Boss boss;

            for(long bossId : new HashSet<>(bossSet)) {
                boss = Config.getData(Id.BOSS, bossId);
                innerBuilder.append("\n")
                        .append(index++)
                        .append(". [");

                if(FightManager.getInstance().fightId.containsKey(boss.getId())) {
                    innerBuilder.append("F] [");
                }

                innerBuilder.append(boss.getLocation().toFieldString())
                        .append("] ")
                        .append(boss.getName());
            }
        }

        innerBuilder.append("\n\n---떨어진 아이템---");

        if(map.getItem().isEmpty()) {
            innerBuilder.append("\n떨어진 아이템 없음");
        } else {
            Item item;
            String locationStr;
            for (Map.Entry<Location, Map<Long, Integer>> entry : new HashMap<>(map.getItem()).entrySet()) {
                locationStr = "\n[" + entry.getKey().toFieldString() + "] ";

                for(Map.Entry<Long, Integer> itemEntry : entry.getValue().entrySet()) {
                    item = Config.getData(Id.ITEM, itemEntry.getKey());
                    innerBuilder.append(locationStr)
                            .append(item.getName())
                            .append(" ")
                            .append(itemEntry.getValue())
                            .append("개");
                }
            }
        }

        innerBuilder.append("\n\n---떨어진 장비---");

        if(map.getEquip().isEmpty()) {
            innerBuilder.append("\n떨어진 장비 없음");
        } else {
            Equipment equipment;
            String locationStr;
            for (Map.Entry<Location, Set<Long>> entry : new HashMap<>(map.getEquip()).entrySet()) {
                locationStr = "\n[" + entry.getKey().toFieldString() + "] ";

                for(long equipId : entry.getValue()) {
                    equipment = Config.getData(Id.EQUIPMENT, equipId);
                    innerBuilder.append(locationStr)
                            .append(equipment.getName());
                }
            }
        }

        self.replyPlayer(map.getName() + "(요구 레벨: " + map.getRequireLv() + ") [" + map.getMapType().getMapName() + "]\n" +
                Emoji.WORLD + ": " + map.getLocation().toMapString() + "\n" +
                Emoji.MONSTER + ": " + map.getEntity(Id.MONSTER).size() + ", " +
                Emoji.BOSS + ": " + map.getEntity(Id.BOSS).size(),
                innerBuilder.toString());
    }

}
