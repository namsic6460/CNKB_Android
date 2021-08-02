package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.AdventureType;
import lkd.namsic.game.enums.AdventureWaitType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.exception.UnhandledEnumException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class AdventureManager {

    private static final AdventureManager instance = new AdventureManager();

    public static AdventureManager getInstance() {
        return instance;
    }

    public void tryAdventure(@NonNull Player self) {
        GameMap map = Config.getMapData(self.getLocation());

        MapType mapType = map.getMapType();
        if(MapType.cityList().contains(mapType)) {
            throw new WeirdCommandException("마을 지역에서는 모험이 불가합니다");
        } else if(Arrays.asList(MapType.HELL, MapType.HEAVEN, MapType.FAR_SIDE).contains(mapType)) {
            throw new WeirdCommandException("특수 지역에서는 모험이 불가합니다");
        }

        self.setDoing(Doing.ADVENTURE);
        self.addLog(LogData.ADVENTURE, 1);

        int adv = self.getAdv().get();
        int skipPercent = (int) (Math.random() * 10) + Math.min(adv * 4 / 10, 40);

        this.startAdventure(self, adv, skipPercent, mapType);
    }

    public void startAdventure(@NonNull Player self, int adv, int skipPercent, MapType mapType) {
        self.setVariable(Variable.ADVENTURE_WAIT_TYPE, AdventureWaitType.NONE);
        self.setVariable(Variable.ADVENTURE_FIGHT, false);
        self.replyPlayer("모험을 시작합니다\n모험 중 전투에 주의하세요!");

        boolean gotReward = false;
        StringBuilder innerMsg = new StringBuilder("---모험 종료---");

        Random random = new Random();
        AdventureType adventureType = AdventureType.NONE;
        boolean adventureFight;

        try {
            for (int i = 0; i < Config.ADVENTURE_COUNT; i++) {
                try {
                    Thread.sleep(Config.ADVENTURE_DELAY_TIME);
                } catch (InterruptedException e) {
                    Logger.e("Player.fightThread", e);
                    throw new RuntimeException(e.getMessage());
                }

                adventureFight = self.getObjectVariable(Variable.ADVENTURE_FIGHT, false);
                if(adventureFight) {
                    throw new RuntimeException("F");
                }

                Object[] randomAdventure = this.getRandomAdventure(adv, mapType);

                adventureType = (AdventureType) randomAdventure[0];
                String situation = (String) randomAdventure[1];
                String[] msgs = (String[]) randomAdventure[2];
                int[] percents = (int[]) randomAdventure[3];
                int[] rewardTier = (int[]) randomAdventure[4];

                StringBuilder adventureMsg = new StringBuilder(situation);
                adventureMsg.append("\n");

                for(int j = 0; j < 4; j++) {
                    adventureMsg.append("\n")
                            .append(j + 1)
                            .append(". ")
                            .append(msgs[j])
                            .append(" (확률: ")
                            .append(percents[j])
                            .append("%, 보상: ")
                            .append(rewardTier[j])
                            .append("등급)");
                }

                adventureMsg.append("\n(예시: ")
                        .append(Emoji.focus("n 모험 1"))
                        .append(")");
                self.replyPlayer(adventureMsg.toString());

                self.setVariable(Variable.ADVENTURE_WAIT_TYPE, AdventureWaitType.WAIT);

                synchronized (self) {
                    while(true) {
                        try {
                            self.wait(Config.ADVENTURE_WAIT_TIME);
                        } catch (InterruptedException e) {
                            Logger.e("Player.adventureThread", e);
                            throw new RuntimeException(e.getMessage());
                        }

                        self.notifyAll();

                        Boolean isFightResponse = self.getObjectVariable(Variable.IS_FIGHT_RESPONSE, true);
                        if(!isFightResponse) {
                            self.removeVariable(Variable.IS_FIGHT_RESPONSE);
                            break;
                        }
                    }
                }

                AdventureWaitType waitType = self.getObjectVariable(Variable.ADVENTURE_WAIT_TYPE, AdventureWaitType.WAIT);
                self.setVariable(Variable.ADVENTURE_WAIT_TYPE, AdventureWaitType.NONE);

                boolean canSkip;
                boolean success;
                int index;

                if(waitType.equals(AdventureWaitType.WAIT)) {
                    throw new RuntimeException("G");
                } else {
                    index = waitType.ordinal() - 1;
                    canSkip = percents[index] != 0;

                    success = random.nextInt(100) < percents[index];

                    if(canSkip && !success) {
                        success = random.nextInt(100) < skipPercent;
                    }
                }

                String msg;
                if(success) {
                    Map<Long, Integer> itemList = RandomList.ADVENTURE_LIST.get(rewardTier[index]);

                    Long itemId = null;
                    int weight;

                    int randomValue = random.nextInt(1000000);
                    int currentWeight = 0;
                    for (Map.Entry<Long, Integer> entry : itemList.entrySet()) {
                        itemId = entry.getKey();
                        weight = entry.getValue();

                        if (randomValue < currentWeight + weight) {
                            break;
                        } else {
                            currentWeight += weight;
                        }
                    }

                    if(itemId == null) {
                        throw new NullPointerException();
                    }

                    self.addItem(itemId, 1, false);
                    msg = adventureType.getSuccessMsg() + "\n난관을 성공적으로 해결하여 보상을 획득했습니다";

                    Item item = Config.getData(Id.ITEM, itemId);
                    innerMsg.append("\n")
                            .append(i + 1)
                            .append("번째 난관 보상: ")
                            .append(item.getName());

                    gotReward = true;
                } else {
                    if(canSkip && random.nextInt(100) < Config.EXPLORE_HARD_SUCCESS_PERCENT) {
                        msg = "보상을 획득하는 데에는 실패했으나, 난관에서 겨우 빠져나왔습니다";
                        innerMsg.append("\n")
                                .append(i + 1)
                                .append("번째 난관 보상: 없음");
                    } else {
                        throw new RuntimeException("G");
                    }
                }

                if(i != Config.ADVENTURE_COUNT - 1) {
                    msg += "\n모험을 이어나갑니다";
                }

                self.replyPlayer(msg);

                adventureFight = self.getObjectVariable(Variable.ADVENTURE_FIGHT, false);
                if(adventureFight) {
                    throw new RuntimeException("F");
                }
            }

            self.replyPlayer("모험을 완료했습니다!", innerMsg.toString());
        } catch (Exception e) {
            if(Objects.equals(e.getMessage(), "F")) {
                self.setVariable(Variable.ADVENTURE_FIGHT, false);

                String msg = "전투가 발생하여 모험을 종료합니다";

                if(gotReward) {
                    self.replyPlayer(msg, innerMsg.toString());
                } else {
                    self.replyPlayer(msg, innerMsg.toString() + "\n획득한 보상이 없습니다");
                }
            } else if(Objects.equals(e.getMessage(), "G")) {
                self.replyPlayer(adventureType.getFailMsg(),"난관에 막히며 모험에 실패했습니다...\n" +
                        "모든 모험 보상을 잃어버렸습니다");
            } else {
                throw e;
            }
        }

        self.removeVariable(Variable.ADVENTURE_WAIT_TYPE);
        if(self.getDoing().equals(Doing.ADVENTURE)) {
            self.setDoing(Doing.NONE);
        }
    }

    @NonNull
    private Object[] getRandomAdventure(int adv, @NonNull MapType mapType) {
        Object[] output = new Object[5];

        String situation;
        String[] msgs = new String[4];
        int[] percents = new int[4];
        int[] rewardTier = new int[4];

        List<AdventureType> types = new ArrayList<>(Arrays.asList(AdventureType.values()));
        types.remove(AdventureType.NONE);

        int ordinal;
        if(mapType.equals(MapType.SINKHOLE)) {
            ordinal = AdventureType.CLIFF.ordinal();

            types.add(ordinal, AdventureType.CLIFF);
            types.add(ordinal, AdventureType.CLIFF);
            types.add(ordinal, AdventureType.CLIFF);
        } else if(mapType.equals(MapType.MOUNTAIN)) {
            ordinal = AdventureType.SPIKE.ordinal();

            types.add(ordinal, AdventureType.SPIKE);
        } else if(MapType.caveList().contains(mapType)) {
            types.remove(AdventureType.SUN);
        }

        int maxValue = adv >= 150 ? 999 : adv / 15 + 5;
        int randomValue = new Random().nextInt(Math.min(types.size(), maxValue));
        AdventureType adventureType = types.get(randomValue);
        switch (adventureType) {
            case RIVER:
                situation = "도랑이 길을 가로막고 있습니다\n그냥 건너기에는 반대편 땅까지 거리가 꽤나 있어 보입니다";

                msgs[0] = "도랑의 끝을 찾아 지나간다";
                rewardTier[0] = 1;

                msgs[1] = "도랑 위로 받칠 판자같은 것을 찾아본다";
                rewardTier[1] = 2;

                msgs[2] = "바지를 걷고 도랑을 그대로 통과한다";
                rewardTier[2] = 4;

                msgs[3] = "나 자신을 믿고 그냥 뛴다";
                rewardTier[3] = 6;

                break;
            case SPIKE:
                situation = "가시 덤불로 길이 덮여있습니다\n길 안쪽은 살펴볼게 꽤나 많아 보입니다";

                msgs[0] = "덤불 길을 피해서 이동한다";
                rewardTier[0] = 1;

                msgs[1] = "덤불을 제거하면서 최대한 조심히 이동한다";
                rewardTier[1] = 3;

                msgs[2] = "덤불을 제거하면서 이동한다";
                rewardTier[2] = 5;

                msgs[3] = "덤불이 뭔데? 먹는건가? 그냥 돌진";
                rewardTier[3] = 8;

                break;
            case ROBBER:
                situation = "앞쪽에 사람들 무리가 있습니다\n이런! 자세히 보니 도적 무리였네요";

                msgs[0] = "눈에 띄지 않도록 피해서 지나간다";
                rewardTier[0] = 1;

                msgs[1] = "몰래 잠입해서 무리 내부를 살펴본다";
                rewardTier[1] = 5;

                msgs[2] = "정찰병 몇몇을 처리하고 내부를 살펴본다";
                rewardTier[2] = 7;

                msgs[3] = "쌍욕을 하면서 다가간다";
                rewardTier[3] = 9;

                break;
            case ANCIENT:
                situation = "오래된 유적같은 건물이 보입니다\n유적의 안쪽은 상당히 위험해 보입니다";

                msgs[0] = "무시하고 지나간다";
                rewardTier[0] = 1;

                msgs[1] = "유적의 벽면만 살펴본다";
                rewardTier[1] = 2;

                msgs[2] = "유적을 탐사한다";
                rewardTier[2] = 7;

                msgs[3] = "유적의 중심부를 향해 탐사한다";
                rewardTier[3] = 10;

                break;
            case RAIN:
                situation = "갑자기 하늘이 어두워고 천둥이 들리기 시작합니다\n보통 소나기가 아닌 것 같네요";

                msgs[0] = "소나기를 피해 숨을 장소를 찾는다";
                rewardTier[0] = 1;

                msgs[1] = "비가 약할때만 움직여본다";
                rewardTier[1] = 6;

                msgs[2] = "번개가 치기 전까지만 움직인다";
                rewardTier[2] = 10;

                msgs[3] = "소나기쯤은 문제없다! 그냥 이동";
                rewardTier[3] = 12;

                break;
            case CAT:
                situation = "칠흑같이 검은 고양이가 보입니다\n왜인지 모를 불안함이 몸을 감싸네요";

                msgs[0] = "고양이를 최대한 경계하면서 간다";
                rewardTier[0] = 1;

                msgs[1] = "고양이를 신경쓰지 않고 간다";
                rewardTier[1] = 5;

                msgs[2] = "고양이를 쓰다듬고 지나간다";
                rewardTier[2] = 12;

                msgs[3] = "고양이는 못참지, 바로 안아본다";
                rewardTier[3] = 14;

                break;
            case CLIFF:
                situation = "끝이 보이지 않을 정도로 깊은 절벽이 나타났습니다\n이런 안쪽에는 몬스터도 있는 것 같습니다";

                msgs[0] = "절벽의 끝을 찾아 이동한다";
                rewardTier[0] = 1;

                msgs[1] = "달려드는 몬스터만 죽이며 쉬운 길을 찾아본다";
                rewardTier[1] = 5;

                msgs[2] = "가장 앞쪽의 길을 향해 돌진한다";
                rewardTier[2] = 10;

                msgs[3] = "모든 몬스터를 죽이고 넘어간다";
                rewardTier[3] = 15;

                break;
            case SUN:
                situation = "갑자기 하늘에 구멍이 뚫린 것 처럼 밝아집니다\n근데 점점 더워지는 것 같은데요?";

                msgs[0] = "햇빛을 피할 장소를 찾는다";
                rewardTier[0] = 1;

                msgs[1] = "적당히 쉬며 걷는다";
                rewardTier[1] = 4;

                msgs[2] = "신경쓰지 않고 걷는다";
                rewardTier[2] = 12;

                msgs[3] = "일광욕을 하며 주변을 탐사한다";
                rewardTier[3] = 15;

                break;
            case GOD:
                situation = "갑자기 하늘에서 엄청나게 큰 소리가 들립니다";

                msgs[0] = "귀를 막고 최대한 빨리 지나간다";
                rewardTier[0] = 1;

                msgs[1] = "적당히 숨어서 소리가 없어질 때 까지 기다린다";
                rewardTier[1] = 8;

                msgs[2] = "노래를 부르며 지나간다";
                rewardTier[2] = 16;

                msgs[3] = "개소리좀 안나게 하라고 소리지른다";
                rewardTier[3] = 18;

                break;
            case CRACK:
                situation = "갑자기 시야가 이상해집니다\n아니 시야가 이상해진게 아니라 공간에 균열이 생긴거였네요..!";

                msgs[0] = "균열로부터 최대한 빨리 벗어난다";
                rewardTier[0] = 1;

                msgs[1] = "균열을 지나간다";
                rewardTier[1] = 10;

                msgs[2] = "균열을 살펴본다";
                rewardTier[2] = 12;

                msgs[3] = "균열로 들어가서 탐사한다";
                rewardTier[3] = 20;

                break;

            default:
                throw new UnhandledEnumException(adventureType);
        }

        int tier;
        int percentIncrease = Math.min((int) (adv / 1.1), 190);
        for(int i = 0; i < 4; i++) {
            tier = rewardTier[i];
            percents[i] = Math.min(100, Math.max(0, percentIncrease + (110 - tier * 10) - (tier < 9 ? 0 : tier == 9 ? 5 : 10)));
        }

        output[0] = adventureType;
        output[1] = situation;
        output[2] = msgs;
        output[3] = percents;
        output[4] = rewardTier;

        return output;
    }

    public void exploreCommand(@NonNull Player self, String command) {
        AdventureWaitType waitType = self.getObjectVariable(Variable.ADVENTURE_WAIT_TYPE, AdventureWaitType.WAIT);
        if(!waitType.equals(AdventureWaitType.WAIT)) {
            throw new WeirdCommandException("아직 난관이 나타나지 않았습니다");
        }
        
        AdventureWaitType response = AdventureWaitType.parseWaitType(command);

        self.setVariable(Variable.ADVENTURE_WAIT_TYPE, response);
        self.setVariable(Variable.IS_FIGHT_RESPONSE, false);

        synchronized (self) {
            self.notifyAll();
        }
    }

}
