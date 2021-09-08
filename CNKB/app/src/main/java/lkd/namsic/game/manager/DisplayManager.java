package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lkd.namsic.game.base.Location;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.config.RandomList;
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
import lkd.namsic.game.object.Farm;
import lkd.namsic.game.object.GameMap;
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
                        Emoji.WORLD + " 위치: " + Config.getMapData(target.getLocation()).getName() +
                        "(" + target.getLocation().toString() + ")\n" +
                        Emoji.LV + " 레벨: " + target.getDisplayLv() + "\n" +
                        Emoji.SP + " 스텟 포인트: " + target.getSp() + "\n" +
                        Emoji.ADV + " 모험 포인트: " + target.getAdv() + "\n" +
                        Emoji.HOME + " 거점: " + Config.getMapData(target.getBaseLocation()).getName() +
                        "(" + target.getBaseLocation().toString() + ")",
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
                            .append(map.getName())
                            .append(" (")
                            .append(map.getLocation().toMapString())
                            .append(") [Lv ")
                            .append(map.getRequireLv())
                            .append("] (거리: ")
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
                        StatType.MAXHP.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 체력(Hp) : 대상이 현재 가지고 있는 체력을 나타냅니다 (-)\n\n" +
                        Emoji.LIST + " 최대 마나(MaxMn) : 대상이 가질 수 있는 최대량의 마나를 나타냅니다 (" +
                        StatType.MAXMN.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 마나(Mn) : 대상이 현재 가지고 있는 마나를 나타냅니다 (-)\n\n" +
                        Emoji.LIST + " 공격력(Atk) : 대상의 물리 공격력을 나타냅니다 (" +
                        StatType.ATK.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 마법 공격력(MAtk) : 대상의 마법 공격력을 나타냅니다 (" +
                        StatType.MATK.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 민첩(Agi) : 치명타 확률을 나타냅니다 (스텟 1당 치명타 확률 " + (Config.CRIT_PER_AGI * 100) + "%) (" +
                        StatType.AGI.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 공격 속도(Ats) : 전투에서 턴을 가져올 상대적 우선권 나타냅니다 (" +
                        StatType.ATS.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 방어력(Def) : 물리 데미지를 방어하는 수치를 나타냅니다 (" +
                        StatType.DEF.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 마법 방어력(MDef) : 마법 데미지를 방어하는 수치를 나타냅니다 (" +
                        StatType.MDEF.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 방어 관통력(Bre) : 공격 시 대상의 방어력을 상쇄하는 수치를 나타냅니다 (" +
                        StatType.BRE.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 마법 관통력(MBre) : 공격 시 대상의 마법 방어력을 상쇄하는 수치를 나타냅니다 (" +
                        StatType.MBRE.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 흡수력(Dra) : 공격 시 최종 물리 데미지의 일부 %를 회복하는 수치를 나타냅니다 (최대: 100) (" +
                        StatType.DRA.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 마법 흡수력(MDra) : 공격 시 최종 마법 데미지의 일부 %를 회복하는 수치를 나타냅니다 (최대: 100) (" +
                        StatType.MDRA.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 회피(Eva) : 공격을 회피할 수 있는 수치를 나타냅니다 (회피 - 정확도 <= " + Config.MAX_EVADE + ") (" +
                        StatType.EVA.getUseSp() + "SP)\n\n" +
                        Emoji.LIST + " 정확도(Acc) : 공격 시 대상의 회피를 상쇄하는 수치를 나타냅니다 (" +
                        StatType.ACC.getUseSp() + "SP)"
        );
    }

    public void displayLvRanking(@NonNull Player self) {
        List<String> sortedList = new ArrayList<>(Config.PLAYER_LV_RANK.keySet());
        sortedList.sort((o1, o2) -> Integer.compare(Config.PLAYER_LV_RANK.get(o2), Config.PLAYER_LV_RANK.get(o1)));

        StringBuilder innerBuilder = new StringBuilder(Config.SPLIT_BAR);

        int rank = 1;
        for (String name : sortedList) {
            innerBuilder.append("\n")
                    .append(rank++)
                    .append(". ")
                    .append(name)
                    .append("\n")
                    .append(Config.SPLIT_BAR);
        }

        self.replyPlayer("레벨 랭킹은 전체보기로 확인해주세요", innerBuilder.toString());
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

    public void displayReinforceExplanation(@NonNull Player self) {
        self.replyPlayer(
                "1. 장비는 최대 " + Config.MAX_REINFORCE_COUNT + "강 까지 강화할 수 있습니다\n" +
                        "2. 강화에 성공할 시 모든 스텟이 균등하게 증가하지만 제한 레벨 또한 증가될 수 있습니다\n" +
                        "3. 일정 횟수 이상 강화에 실패하면 무조건 강화에 성공하는 천장 시스템이 있습니다\n" +
                        "4. 장비의 입수 난이도 및 다양한 조건에 따라 강화의 난이도 및 성장치가 변화합니다"
        );
    }

    public void displayShopHelp(@NonNull Player self) {
        self.replyPlayer("---상점 도움말---\n" +
                Emoji.LIST + " (도움말/명령어/?/help/h) - 상점 도움말을 표시합니다\n" +
                Emoji.LIST + " (종료/end/e) - 상점 이용을 종료합니다\n" +
                Emoji.LIST + " (변경/change/c) - 구매/판매 모드를 전환합니다\n" +
                Emoji.LIST + " ({페이지}) - 해당 페이지의 상점 물품을 표시합니다\n" +
                Emoji.LIST + " (다음/next/n) - 다음 페이지의 상점 물품을 표시합니다\n" +
                Emoji.LIST + " (이전/prev/p) - 이전 페이지의 상점 물품을 표시합니다\n" +
                Emoji.LIST + " (구매/buy/b) ({아이템 이름}) [{아이템 개수}] - 아이템을 구매합니다\n" +
                Emoji.LIST + " (판매/sell/s) ({아이템 이름}) [{아이템 개수}] - 아이템을 판매합니다\n\n" +
                "예시: " + Emoji.focus("n 상점 구매 하급 체력 포션 1"));
    }

    public void displayShopList(@NonNull Player self) {
        GameMap map = Config.getMapData(self.getLocation());

        Set<Long> npcSet = map.getEntity(Id.NPC);

        StringBuilder innerBuilder = new StringBuilder("---상점 목록---");

        boolean exist = false;
        for (long npcId : npcSet) {
            try {
                Config.getData(Id.SHOP, npcId);
            } catch (ObjectNotFoundException e) {
                continue;
            }

            exist = true;
            innerBuilder.append("\n")
                    .append(Objects.requireNonNull(NpcList.findById(npcId)));
        }

        if (!exist) {
            innerBuilder.append("\n현재 맵에서 이용 가능한 상점이 없습니다");
        }

        self.replyPlayer("상점 목록은 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayFarmHelp(@NonNull Player self) {
        self.replyPlayer(
                "1. 농장은 인당 최대 1개 이고 " + Config.FARM_PRICE + "G 에 구매할 수 있습니다\n" +
                        "2. 농장에 식물을 심으면 제거하지 않는 이상 영구 지속됩니다\n" +
                        "3. 농장의 작물은 최대 5일 분량까지 수확할 수 있습니다\n" +
                        "4. 씨앗은 NPC 에게서 구매 가능합니다(시작의 마을의 경우 NPC " +
                        Emoji.focus(NpcList.EL.getDisplayName()) + " 에게서 구매 가능)\n" +
                        "5. 농장 레벨을 업그레이드하면 더 많은 씨앗을 심을 수 있습니다"
        );
    }

    public void displayFarm(@NonNull Player self) {
        Farm farm = Config.loadObject(Id.FARM, self.getId().getObjectId());

        StringBuilder innerBuilder = new StringBuilder("---")
                .append(self.getNickName())
                .append(" 님의 농장 정보---\n농장 레벨: ")
                .append(farm.getLv())
                .append("\n작물 개수: ")
                .append(farm.getPlanted().size())
                .append("/")
                .append(farm.getMaxPlantCount())
                .append("\n\n---작물 현황---");

        if (farm.getPlanted().isEmpty()) {
            innerBuilder.append("\n작물이 심어져있지 않습니다");
        } else {
            long currentTime = System.currentTimeMillis();
            long diff;

            int index = 1;
            long day, hour, minute;
            for (Farm.Plant plant : farm.getPlanted()) {
                diff = (currentTime - plant.getLastHarvestTime()) / 1000;

                day = diff / 86400;
                diff = diff % 86400;
                hour = diff / 3600;
                minute = (diff % 3600) / 60;

                innerBuilder.append("\n")
                        .append(Config.SPLIT_BAR)
                        .append("\n")
                        .append(index++)
                        .append(". ")
                        .append(ItemList.findById(plant.getId().getObjectId()))
                        .append("\n마지막 수확: ")
                        .append(day)
                        .append("일 ")
                        .append(hour)
                        .append("시간 ")
                        .append(minute)
                        .append("분 전");
            }

            innerBuilder.append("\n")
                    .append(Config.SPLIT_BAR);

            Config.unloadObject(farm);
        }

        self.replyPlayer("농장 정보는 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displaySkillHelp(@NonNull Player self) {
        self.replyPlayer(
                "1. 스킬은 전투중에만 사용이 가능합니다\n" +
                        "2. 대부분의 스킬의 액티브는 대상이 필요합니다\n" +
                        "3. 스킬은 NPC 상점에서 스킬 북을 구매하여 사용하면 배울 수 있습니다(시작의 마을의 경우 " +
                        Emoji.focus(NpcList.SELINA.getDisplayName()) + ")\n"
        );
    }

    public void displaySkillList(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("---보유 스킬 목록---");

        if(self.getSkill().isEmpty()) {
            self.replyPlayer("보유중인 스킬이 없습니다");
            return;
        }

        for(long skillId : self.getSkill()) {
            innerBuilder.append("\n- ")
                    .append(SkillList.findById(skillId));
        }
        
        self.replyPlayer("스킬 목록은 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayMinePercent(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("===광질 확률표===");

        for(int mineLv = 0; mineLv <= Config.MAX_MINE_LV; mineLv++) {
            innerBuilder.append("\n\n---")
                    .append(mineLv)
                    .append(" 레벨 확률표---");

            List<Double> minePercent = RandomList.MINE_PERCENT.get(mineLv);

            double percent;
            StringBuilder tempBuilder;
            for(int index = 0; index < minePercent.size(); index++) {
                percent = minePercent.get(index);

                if(percent == 0) {
                    continue;
                }

                innerBuilder.append("\n[");

                tempBuilder = new StringBuilder();
                for(long itemId : RandomList.MINE_OUTPUT.get(index)) {
                    tempBuilder.append(ItemList.findById(itemId))
                            .append(", ");
                }

                innerBuilder.append(tempBuilder.substring(0, tempBuilder.length() - 2))
                        .append("]: ")
                        .append(Config.getDisplayPercent(percent / 100, 4));
            }
        }

        self.replyPlayer("광질 레벨: " + self.getDisplayMineLv() +
                "\n\n광질 확률표는 전체보기로 확인해주세요", innerBuilder.toString());
    }

    public void displayFishPercent(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("===낚시 확률표===");

        for(int fishLv = 0; fishLv <= Config.MAX_FISH_LV; fishLv++) {
            innerBuilder.append("\n\n---")
                    .append(fishLv)
                    .append(" 레벨 확률---");

            List<Integer> fishPercent = RandomList.FISH_PERCENT.get(fishLv);
            List<String> outputStr = Arrays.asList(
                    "돌/나뭇가지/쓰레기/물풀",
                    "일반 물고기",
                    "희귀 물고기",
                    "특별 물고기",
                    "유일 물고기",
                    "전설 물고기",
                    "신화 물고기"
            );

            int percent;
            for(int index = 0; index < fishPercent.size(); index++) {
                percent = fishPercent.get(index);

                if(percent == 0) {
                    continue;
                }

                innerBuilder.append("\n")
                        .append(outputStr.get(index))
                        .append(": ")
                        .append(percent)
                        .append("%");
            }
        }

        self.replyPlayer("낚시 레벨: " + self.getDisplayFishLv() +
                        "\n\n낚시 확률표는 전체보기로 확인해주세요", innerBuilder.toString());
    }
    
    public void displayAdvPercent(@NonNull Player self) {
        StringBuilder innerBuilder = new StringBuilder("===모험 보상 확률표===");

        for(Map.Entry<Integer, Map<Long, Integer>> entry : RandomList.ADVENTURE_LIST.entrySet()) {
            innerBuilder.append("\n\n---")
                    .append(entry.getKey())
                    .append(" 등급 보상---");

            for(Map.Entry<Long, Integer> percentEntry : entry.getValue().entrySet()) {
                innerBuilder.append("\n")
                        .append(ItemList.findById(percentEntry.getKey()))
                        .append(": ")
                        .append(Config.getDisplayPercent(percentEntry.getValue() / 1_000_000D, 4));
            }
        }

        self.replyPlayer("모험 스텟: " + self.getAdv() +
                "\n\n모험 보상 확률표는 전체보기로 확인해주세요", innerBuilder.toString());
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
                .append(map.getName())
                .append("(")
                .append(map.getLocation().toMapString())
                .append(")")
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

}
