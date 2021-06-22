package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.LogData;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.exception.InvalidNumberException;
import lkd.namsic.game.exception.NumberRangeException;
import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.setting.Logger;

public class MineManager {

    private static final MineManager instance = new MineManager();

    public static MineManager getInstance() {
        return instance;
    }

    public boolean canMine(@NonNull Player self) {
        return Config.getMapData(self.getLocation()).getMapType().equals(MapType.COUNTRY);
    }

    public void mine(@NonNull Player self) {
        self.setDoing(Doing.MINE);
        self.addLog(LogData.MINED, 1);

        int mineLv = self.getVariable(Variable.MINE);

        double randomPercent = Math.random() * 100;

        List<Double> percents = new ArrayList<>();
        for(List<Double> minePercent : RandomList.MINE_PERCENT) {
            percents.add(minePercent.get(mineLv));
        }

        List<Long> output = Arrays.asList(20L, 21L, 0L, 0L, 0L, 31L, 33L, 0L, 0L, 0L, 43L, 0L);

        double percent;
        long itemId = 0;
        for(int itemTier = 0; itemTier < percents.size(); itemTier++) {
            percent = percents.get(itemTier);

            if(randomPercent < percent) {
                itemId = output.get(itemTier);

                if(itemId == 0) {
                    long[] itemList;

                    switch(itemTier) {
                        case 2:
                            itemList = new long[]{23L, 24L, 25L, 26L};
                            break;
                        case 3:
                            itemList = new long[]{27L, 28L};
                            break;
                        case 4:
                            itemList = new long[]{29L, 30L};
                            break;
                        case 7:
                            itemList = new long[]{34L, 35L};
                            break;
                        case 8:
                            itemList = new long[]{36L, 37L};
                            break;
                        case 9:
                            itemList = new long[]{39L, 40L};
                            break;
                        case 11:
                            itemList = new long[]{44L, 45L, 46L};
                            break;
                        default:
                            throw new InvalidNumberException(itemTier);
                    }

                    itemId = itemList[new Random().nextInt(itemList.length)];
                }

                break;
            } else {
                randomPercent -= percent;
            }
        }

        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            Logger.e("player.mineThread", e);
            return;
        }

        int count = self.getItem(itemId);
        self.addItem(itemId, 1);

        Item item = Config.getData(Id.ITEM, itemId);
        self.replyPlayer(item.getName() + "을 캤습니다!\n아이템 개수 : " + count + " -> " + (count + 1));

        if(this.checkMineLevel(self)) {
            self.addVariable(Variable.MINE, 1);
            self.replyPlayer("광업 레벨이 올랐습니다!\n광업 레벨: " + mineLv + " -> " + (mineLv + 1));
        }

        self.setDoing(Doing.NONE);
    }

    public boolean checkMineLevel(@NonNull Player self) {
        int mineLv = self.getVariable(Variable.MINE);
        long mined = self.getLog(LogData.MINED);
        int requireCount;

        if(mineLv == 8) {
            return false;
        }

        switch (mineLv) {
            case 0:
                requireCount = 30;
                break;
            case 1:
                requireCount = 200;
                break;
            case 2:
                requireCount = 1000;
                break;
            case 3:
                requireCount = 5000;
                break;
            case 4:
                requireCount = 20_000;
                break;
            case 5:
                requireCount = 100_000;
                break;
            case 6:
                requireCount = 1_000_000;
                break;
            case 7:
                requireCount = 10_000_000;
                break;
            default:
                throw new NumberRangeException(mineLv, 0, 7);
        }

        return mined >= requireCount;
    }

}
