package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Random;

import lkd.namsic.game.base.Int;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.FishWaitType;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.event.FishEvent;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class FishManager {

    private static final FishManager instance = new FishManager();

    public static FishManager getInstance() {
        return instance;
    }

    public void tryFish(@NonNull Player self, int fishLv) {
        GameMap map = Config.getMapData(self.getLocation());
        if(!MapType.waterList().contains(map.getMapType())) {
            throw new WeirdCommandException("강 또는 바다에서만 낚시가 가능합니다");
        }

        self.setDoing(Doing.FISH);
        self.addLog(LogData.FISH, 1);

        double randomPercent = Math.random() * 100;
        List<Integer> percents = RandomList.FISH_PERCENT.get(fishLv);

        Random random = new Random();

        int itemTier = 0;
        long itemId = 0;
        int commandCount = 0;
        double percent;

        //0 ~ 6
        for(; itemTier < percents.size(); itemTier++) {
            percent = percents.get(itemTier);

            if(randomPercent < percent) {
                if (randomPercent < percents.get(itemTier)) {
                    long startId = 0;
                    int itemCount = 0;

                    long[] itemList = new long[0];

                    switch (itemTier) {
                        case 0:
                            itemList = new long[]{ItemList.STONE.getId(), ItemList.BRANCH.getId(),
                                    ItemList.TRASH.getId(), ItemList.WATER_GRASS.getId()};
                            commandCount = random.nextInt(2);
                            break;
                        case 1:
                            startId = ItemList.COMMON_FISH1.getId();
                            itemCount = 5;
                            commandCount = random.nextInt(6);
                            break;
                        case 2:
                            startId = ItemList.RARE_FISH1.getId();
                            itemCount = 6;
                            commandCount = random.nextInt(8) + 1;
                            break;
                        case 3:
                            startId = ItemList.SPECIAL_FISH1.getId();
                            itemCount = 10;
                            commandCount = random.nextInt(8) + 3;
                            break;
                        case 4:
                            startId = ItemList.UNIQUE_FISH1.getId();
                            itemCount = 7;
                            commandCount = random.nextInt(10) + 6;
                            break;
                        case 5:
                            startId = ItemList.LEGENDARY_FISH1.getId();
                            itemCount = 4;
                            commandCount = random.nextInt(6) + 10;
                            break;
                        case 6:
                            startId = ItemList.MYSTIC_FISH1.getId();
                            itemCount = 2;
                            commandCount = random.nextInt(11) + 20;
                            break;
                        default:
                            throw new NumberRangeException(itemTier, 0, 6);
                    }

                    if(itemTier == 0) {
                        itemId = itemList[random.nextInt(itemList.length)];
                    } else {
                        itemId = random.nextInt(itemCount) + startId;
                    }
                }

                break;
            } else {
                randomPercent -= percent;
            }
        }

        this.startFish(self, itemId, itemTier, commandCount);
    }

    public void startFish(@NonNull Player self, long itemId, int itemTier, int commandCount) {
        Random random = new Random();

        self.setVariable(Variable.FISH_WAIT_TYPE, FishWaitType.NONE);
        self.replyPlayer("낚시를 시작합니다");

        FishWaitType lastWaitType = null;
        FishWaitType waitType;

        for (int i = commandCount; i >= 0; i--) {
            if (i == 0) {
                waitType = FishWaitType.CATCH;
            } else {
                int randomWaitTypeIdx;

                if (lastWaitType == null || lastWaitType.equals(FishWaitType.SHAKE) || lastWaitType.equals(FishWaitType.WAIT)) {
                    randomWaitTypeIdx = random.nextInt(4);
                    waitType = new FishWaitType[]{
                            FishWaitType.SHAKE, FishWaitType.WAIT, FishWaitType.PULL, FishWaitType.RESIST
                    }[randomWaitTypeIdx];
                } else {
                    randomWaitTypeIdx = random.nextInt(2);
                    waitType = new FishWaitType[]{
                            FishWaitType.PULL, FishWaitType.RESIST
                    }[randomWaitTypeIdx];
                }
            }

            lastWaitType = waitType;

            try {
                Thread.sleep(random.nextInt(Config.FISH_DELAY_TIME) + Config.FISH_DELAY_TIME_OFFSET);
            } catch (InterruptedException e) {
                Logger.e("FishManager", e);
                throw new RuntimeException(e.getMessage());
            }

            self.setVariable(Variable.FISH_WAIT_TYPE, waitType);

            String message;
            if (waitType.equals(FishWaitType.SHAKE)) {
                message = "아직 특별한 느낌이 없습니다\n낚싯대를 흔들어 물고기를 유혹해봅시다\n" +
                        Emoji.focus("(ㅜ/n) (낚시/fish) (흔들기/shake/s)");
            } else if (waitType.equals(FishWaitType.WAIT)) {
                message = "미세한 무언가가 느껴집니다...\n확실히 물릴때까지 기다려봅시다\n" +
                        Emoji.focus("(ㅜ/n) (낚시/fish) (기다리기/wait/w)");
            } else if (waitType.equals(FishWaitType.PULL)) {
                message = "걸린 것 같습니다!\n힘차게 당겨봅시다\n" +
                        Emoji.focus("(ㅜ/n) (낚시/fish) (당기기/pull/p)");
            } else if (waitType.equals(FishWaitType.RESIST)) {
                message = "이런! 잘못하면 낚싯대가 망가지겠네요\n최대한 버텨봅시다\n" +
                        Emoji.focus("(ㅜ/n) (낚시/fish) (버티기/resist/r)");
            } else {
                message = "지금입니다, 당기세요!\n" +
                        Emoji.focus("(ㅜ/n) (낚시/fish) (잡기/catch/c)");
            }

            self.replyPlayer(message);

            synchronized (self) {
                try {
                    self.wait(Config.FISH_WAIT_TIME);
                } catch (InterruptedException e) {
                    Logger.e("FishManager", e);
                    throw new RuntimeException(e.getMessage());
                }

                self.notifyAll();
            }

            FishWaitType response = self.getObjectVariable(Variable.FISH_WAIT_TYPE, FishWaitType.NONE);
            if (response.equals(FishWaitType.NONE)) {
                if (i == 0) {
                    Int itemCount = new Int(1);
                    String eventName = FishEvent.getName();
                    FishEvent.handleEvent(self, self.getEvent().get(eventName), self.getEquipEvents(eventName), itemId, itemCount);

                    self.addItem(itemId, itemCount.get(), false);
                    self.addExp(50L * (itemTier + 1) * itemCount.get());
                    self.setDoing(Doing.NONE);

                    StringBuilder innerBuilder = new StringBuilder();

                    if (itemTier != 0) {
                        int token = Config.randomToken(ItemList.LOW_FISH_TOKEN.getId(), RandomList.FISH_TOKEN, itemTier, self);
                        if(token >= 0) {
                            innerBuilder.append(Config.TIERS[token])
                                    .append("급 낚시꾼의 증표 1개를 획득하였습니다\n");
                        }

                        self.addVariable(Variable.FISH_SKILL, 1);

                        Map<Long, Integer> fishMap = self.getMapVariable(Variable.FISH_MAP);

                        int fishCount = fishMap.getOrDefault(itemId, 0) + 1;
                        fishMap.put(itemId, fishCount);

                        int skillIncrease;

                        if (fishCount == 1) {
                            skillIncrease = 2 * itemTier * itemTier;
                            innerBuilder.append("새로운 물고기를 낚았습니다!\n");

                            self.addVariable(Variable.FISH_SKILL, skillIncrease);
                        }

                        skillIncrease = 0;
                        if (fishCount == 10) {
                            skillIncrease = 5;
                        } else if (fishCount == 50) {
                            skillIncrease = 10;
                        } else if (fishCount == 100 || fishCount % 500 == 0) {
                            skillIncrease = 50;
                        }

                        if (skillIncrease != 0) {
                            self.addVariable(Variable.FISH_SKILL, skillIncrease);
                            innerBuilder.append("해당 물고기를 ")
                                    .append(fishCount)
                                    .append("회 낚아 낚시 숙련도가 상승했습니다\n");
                        }
                    }

                    int fishSkill = self.getVariable(Variable.FISH_SKILL);
                    self.removeVariable(Variable.FISH_WAIT_TYPE);

                    String msg = "낚시 성공!\n" +
                            "낚은 아이템: " + ItemList.findById(itemId) + "\n" +
                            "현재 보유 개수: " + self.getItem(itemId) + "개\n" +
                            "현재 낚시 숙련도: " + fishSkill;
                    if(innerBuilder.length() == 0) {
                        self.replyPlayer(msg);
                    } else {
                        self.replyPlayer(msg, innerBuilder.toString().trim());
                    }

                    int fishLv = self.getVariable(Variable.FISH);
                    if (checkFishLevel(self)) {
                        self.addVariable(Variable.FISH, 1);
                        self.setVariable(Variable.FISH_SKILL, 0);

                        self.replyPlayer("낚시 레벨이 올랐습니다!\n낚시 레벨: " + fishLv + " -> " + (fishLv + 1));
                    }

                    return;
                } else {
                    self.replyPlayer("성공적으로 물고기를 컨트롤했습니다!");
                }
            } else {
                int fishSkill = self.getVariable(Variable.FISH_SKILL);

                String failMessage = "이런... 물고기를 놓쳐버렸습니다";
                if (fishSkill > 0) {
                    if (random.nextInt(2) == 0) {
                        self.setVariable(Variable.FISH_SKILL, fishSkill - 1);
                        failMessage = failMessage + "\n낚시 숙련도 -1";
                    }
                }

                self.setDoing(Doing.NONE);

                self.replyPlayer(failMessage);
                return;
            }
        }

        self.removeVariable(Variable.FISH_WAIT_TYPE);
    }

    public void fishCommand(@NonNull Player self, @NonNull String command) {
        FishWaitType waitType = self.getObjectVariable(Variable.FISH_WAIT_TYPE, FishWaitType.NONE);
        FishWaitType response = FishWaitType.parseWaitType(command);

        if(waitType.equals(response)) {
            self.setVariable(Variable.FISH_WAIT_TYPE, FishWaitType.NONE);
        }

        synchronized (self) {
            self.notifyAll();
        }
    }

    public boolean checkFishLevel(@NonNull Player self) {
        int fishLv = self.getVariable(Variable.FISH);
        if(fishLv == Config.MAX_FISH_LV) {
            return false;
        }

        return self.getVariable(Variable.FISH_SKILL) >= RandomList.FISH_LV_REQUIREMENT.get(fishLv);
    }

}
