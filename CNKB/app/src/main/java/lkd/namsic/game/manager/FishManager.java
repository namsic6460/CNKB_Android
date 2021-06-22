package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lkd.namsic.game.Config;
import lkd.namsic.game.Emoji;
import lkd.namsic.game.ObjectList;
import lkd.namsic.game.RandomList;
import lkd.namsic.game.Variable;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.FishWaitType;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.MapClass;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.setting.Logger;

public class FishManager {

    private static final FishManager instance = new FishManager();

    public static FishManager getInstance() {
        return instance;
    }

    public void tryFish(@NonNull Player self) {
        MapClass map = Config.getMapData(self.getLocation());
        if(MapType.waterList().contains(map.getMapType())) {
            throw new WeirdCommandException("강 또는 바다에서만 낚시가 가능합니다");
        }

        self.setDoing(Doing.FISH);
        self.addLog(LogData.FISH, 1);

        int fishLv = self.getVariable(Variable.FISH);

        double randomPercent = Math.random() * 100;

        List<Double> percents = new ArrayList<>();
        for(List<Double> fishPercent : RandomList.FISH_PERCENT) {
            percents.add(fishPercent.get(fishLv));
        }

        Random random = new Random();

        int itemTier = 0;
        long itemId = 0;
        int commandCount = 0;
        double percent;
        for(; itemTier < percents.size(); itemTier++) {
            percent = percents.get(itemTier);

            if(randomPercent < percent) {
                if (randomPercent < percents.get(itemTier)) {
                    long[] itemList;

                    switch (itemTier) {
                        case 0:
                            itemList = new long[]{1L, 2L, 58L, 59L};
                            commandCount = random.nextInt(2);
                            break;
                        case 1:
                            itemList = new long[]{60L, 61L, 62L, 63L, 64L};
                            commandCount = random.nextInt(6);
                            break;
                        case 2:
                            itemList = new long[]{65L, 66L, 67L, 68L, 69L, 70L};
                            commandCount = random.nextInt(8) + 1;
                            break;
                        case 3:
                            itemList = new long[]{71L, 72L, 73L, 74L, 75L, 76L, 77L, 78L, 79L, 80L};
                            commandCount = random.nextInt(8) + 3;
                            break;
                        case 4:
                            itemList = new long[]{81L, 82L, 83L, 84L, 85L, 86L, 87L};
                            commandCount = random.nextInt(10) + 6;
                            break;
                        case 5:
                            itemList = new long[]{88L, 89L, 90L, 91L};
                            commandCount = random.nextInt(6) + 10;
                            break;
                        case 6:
                            itemList = new long[]{92L, 93L};
                            commandCount = random.nextInt(11) + 20;
                            break;
                        default:
                            throw new NumberRangeException(itemTier, 0, 6);
                    }

                    itemId = itemList[random.nextInt(itemList.length)];
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
                Logger.e("Player.fishThread", e);
                throw new RuntimeException(e.getMessage());
            }

            self.setVariable(Variable.FISH_WAIT_TYPE, waitType);

            String message;
            if (waitType.equals(FishWaitType.SHAKE)) {
                message = "아직 특별한 느낌이 없습니다\n낚싯대를 흔들어 물고기를 유혹해봅시다\n" +
                        Emoji.focus("(ㅜ/n) (낚시/fish) (흔들기/shake)");
            } else if (waitType.equals(FishWaitType.WAIT)) {
                message = "미세한 무언가가 느껴집니다...\n확실히 물릴때까지 기다려봅시다\n" +
                        Emoji.focus("(ㅜ/n) (낚시/fish) (기다리기/wait)");
            } else if (waitType.equals(FishWaitType.PULL)) {
                message = "걸린 것 같습니다!\n힘차게 당겨봅시다\n" +
                        Emoji.focus("(ㅜ/n) (낚시/fish) (당기기/pull)");
            } else if (waitType.equals(FishWaitType.RESIST)) {
                message = "이런! 잘못하면 낚싯대가 망가지겠네요\n최대한 버텨봅시다\n" +
                        Emoji.focus("(ㅜ/n) (낚시/fish) (버티기/resist)");
            } else {
                message = "지금입니다, 당기세요!\n" +
                        Emoji.focus("(ㅜ/n) (낚시/fish) (잡기/catch)");
            }

            self.replyPlayer(message);

            synchronized (self) {
                try {
                    self.wait(Config.FISH_WAIT_TIME);
                } catch (InterruptedException e) {
                    Logger.e("Player.fishThread - wait", e);
                    throw new RuntimeException(e.getMessage());
                }

                self.notifyAll();
            }

            FishWaitType response = self.getObjectVariable(Variable.FISH_WAIT_TYPE, FishWaitType.NONE);
            if (response.equals(FishWaitType.NONE)) {
                if (i == 0) {
                    self.addItem(itemId, 1);
                    self.setDoing(Doing.NONE);

                    if (itemTier != 0) {
                        self.addVariable(Variable.FISH_SKILL, 1);

                        Map<Long, Integer> fishMap = self.getMapVariable(Variable.FISH_MAP);

                        int fishCount = fishMap.getOrDefault(itemId, 0) + 1;
                        fishMap.put(itemId, fishCount);

                        if (fishCount == 1) {
                            int skillIncrease = 2 * itemTier * itemTier;
                            self.replyPlayer("새로운 물고기를 낚았습니다!\n낚시 숙련도 + " + skillIncrease);

                            self.addVariable(Variable.FISH_SKILL, skillIncrease);
                        }

                        int skillIncrease = 0;
                        if (fishCount == 10) {
                            skillIncrease = 5;
                        } else if (fishCount == 50) {
                            skillIncrease = 10;
                        } else if (fishCount == 100 || fishCount % 500 == 0) {
                            skillIncrease = 50;
                        }

                        if (skillIncrease != 0) {
                            self.replyPlayer("해당 물고기를 " + fishCount + "회 낚았습니다!\n낚시 숙련도 + " + skillIncrease);
                            self.addVariable(Variable.FISH_SKILL, skillIncrease);
                        }
                    }

                    int fishLv = self.getVariable(Variable.FISH);
                    if (checkFishLevel(self)) {
                        self.addVariable(Variable.FISH, 1);
                        self.setVariable(Variable.FISH_SKILL, 0);
                        self.replyPlayer("낚시 레벨이 올랐습니다!\n낚시 레벨: " + fishLv + " -> " + (fishLv + 1));
                    }

                    int fishSkill = self.getVariable(Variable.FISH_SKILL);

                    self.getVariable().remove(Variable.FISH_WAIT_TYPE);
                    self.replyPlayer("낚시 성공!\n" +
                            "낚은 아이템: " + ObjectList.itemList.inverse().get(itemId) + "\n" +
                            "현재 보유 개수: " + self.getItem(itemId) + "개\n" +
                            "현재 낚시 숙련도: " + fishSkill);

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
        int fishSkill = self.getVariable(Variable.FISH_SKILL);
        int requireSkill;

        if(fishLv == 8) {
            return false;
        }

        switch (fishLv) {
            case 0:
                requireSkill = 20;
                break;
            case 1:
                requireSkill = 50;
                break;
            case 2:
                requireSkill = 100;
                break;
            case 3:
                requireSkill = 500;
                break;
            case 4:
                requireSkill = 1000;
                break;
            case 5:
                requireSkill = 5000;
                break;
            case 6:
                requireSkill = 10000;
                break;
            case 7:
                requireSkill = 20000;
                break;
            default:
                throw new NumberRangeException(fishLv, 0, 7);
        }

        return fishSkill >= requireSkill;
    }

}
