package lkd.namsic.game.manager;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import lkd.namsic.game.base.Int;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.RandomList;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Item;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class AppraiseManager {

    private final static AppraiseManager instance = new AppraiseManager();

    public static AppraiseManager getInstance() {
        return instance;
    }

    public void appraiseStone(@NonNull Player self, int count) {
        if(count > Config.APPRAISE_LIMIT) {
            throw new WeirdCommandException("한번에 최대 " + Config.APPRAISE_LIMIT + "개까지만 감정할 수 있습니다");
        }

        int currCount = self.getItem(ItemList.STONE.getId());
        if(currCount < count) {
            throw new WeirdCommandException("보유한 돌의 개수가 부족합니다\n현재 보유 돌 개수: " + currCount);
        }

        self.setDoing(Doing.APPRAISE);

        int totalTime = 6 * count;
        self.replyPlayer("감정을 시작합니다...\n소요 시간: " + (totalTime / 60) + "분 " + (totalTime % 60) + "초");

        boolean stopped = false;
        Int usedStone = new Int();
        Map<Long, Integer> successCount = new HashMap<>();

        Random random = new Random();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                usedStone.add(1);

                if(random.nextDouble() < 0.25) {
                    long itemId = RandomList.getRandomJewelry();
                    successCount.put(itemId, successCount.getOrDefault(itemId, 0) + 1);
                } else {
                    self.addItem(ItemList.COBBLE_STONE.getId(), 1, false);
                }

                if(usedStone.get() == count) {
                    synchronized (self) {
                        self.notifyAll();
                    }
                }
            }
        }, 0L, 6000L);

        synchronized (self) {
            try {
                self.wait(totalTime * 1000L);
            } catch (InterruptedException e) {
                Logger.e("Player.appraiseThread", e);
                throw new RuntimeException(e);
            }

            timer.cancel();
            self.notifyAll();
        }

        self.addItem(ItemList.STONE.getId(), -1 * count);

        boolean isAppraiseStopped = self.getObjectVariable(Variable.IS_APPRAISE_STOP, false);
        if(isAppraiseStopped) {
            self.removeVariable(Variable.IS_APPRAISE_STOP);
            stopped = true;
        }

        Item item;
        StringBuilder builder = new StringBuilder("---획득한 아이템---");

        if(successCount.isEmpty()) {
            builder.append("\n획득한 아이템이 없습니다");
        } else {
            long itemId;
            int itemCount;
            for (Map.Entry<Long, Integer> entry : successCount.entrySet()) {
                itemId = entry.getKey();
                itemCount = stopped ? (int) Math.ceil(entry.getValue() / 2D) : entry.getValue();

                item = Config.getData(Id.ITEM, itemId);

                self.addItem(itemId, itemCount, false);
                builder.append("\n")
                        .append(item.getName())
                        .append(": ")
                        .append(Config.getIncrease(itemCount));
            }
        }

        if(stopped) {
            self.replyPlayer("감정이 중지되었습니다\n사용한 돌 개수: " + usedStone, builder.toString());
        } else {
            self.replyPlayer("감정 완료!\n" +
                    "돌 " + count + "개를 사용한 감정이 모두 완료되었습니다", builder.toString());
        }

        self.setDoing(Doing.NONE);
    }

    public void stopAppraise(@NonNull Player self) {
        self.setVariable(Variable.IS_APPRAISE_STOP, true);

        synchronized (self) {
            self.notifyAll();
        }
    }

}
